package com.santander.bootcamp.comercioacoes.data.dto;

import com.santander.bootcamp.comercioacoes.data.entity.EntityBase;
import com.santander.bootcamp.comercioacoes.exceptions.DataMapperException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DTOMapper<T extends DTOBase> {
    private EntityBase sourceEntity;
    private DTOBase<T> dto;

    private HashMap<String, Field> entityFields = new HashMap<>();
    private HashMap<String, Field> dtoFields = new HashMap<>();

    private Boolean hasMappedAny = false;

    private Map<String, Boolean> fieldsRemainingToBeMapped = new HashMap<>();

    public DTOMapper(EntityBase sourceEntity, DTOBase<T> dto) {
        this.sourceEntity = sourceEntity;
        this.dto = dto;

        this.processObjectsFields();
    }

    public DTOMapper(EntityBase sourceEntity, Supplier<? extends DTOBase> dtoConstructor) {
        // https://mkyong.com/java8/java-8-supplier-examples/

        this.sourceEntity = sourceEntity;
        this.dto = dtoConstructor.get();

        this.processObjectsFields();
    }

    private void processObjectsFields() {
        for ( Field field : this.sourceEntity.getClass().getDeclaredFields() ) {
            entityFields.put( field.getName(), field );
        }

        for ( Field field : this.dto.getClass().getDeclaredFields() ) {
            dtoFields.put( field.getName(), field );
            fieldsRemainingToBeMapped.put( field.getName(), true );
        }
    }

    /**
     * Mapeia o campo especificado usando uma função para atribuir seu valor no DTO.
     *
     * @param field       O nome do campo.
     * @param mapFunction Uma função que recebe como argumento o valor do campo na Entity e retorna como ele deve ser no DTO.
     *                    <p>
     *                    A ausência do campo especificado na Entity ou no DTO, além de diferença entre o tipo retornado pela função o tipo
     *                    esperado pelo DTO, causarão uma DataMapperException.
     * @return Retorna o Mapper.
     */

    public <K> DTOMapper<T> map(String field, Function<K, ?> mapFunction) {
        if ( this.dtoFields.containsKey( field ) && this.entityFields.containsKey( field ) ) {
            try {
                Object mapFunctionResult = mapFunction.apply(
                        (K) this.entityFields.get( field ).get( this.sourceEntity )
                );

                if ( !this.dtoFields.get( field ).getType().isAssignableFrom( mapFunctionResult.getClass() ) ) {
                    throw new DataMapperException( String.format(
                            "Erro ao mapear o campo \"%s\" do DTO %s. O DTO espera que este campo seja do tipo %s " +
                                    "mas a função de mapeamento retornou um tipo %s.",
                            field,
                            this.dto.getClass().getName(),
                            this.dtoFields.get( field ).getType().getName(),
                            mapFunctionResult.getClass().getName()
                    ) );
                }

                this.dtoFields.get( field ).set( this.dto, mapFunctionResult );

                this.hasMappedAny = true;
                fieldsRemainingToBeMapped.put( field, false );
            } catch ( IllegalAccessException e ) {
                throw new DataMapperException( String.format(
                        "Erro ao mapear o campo \"%s\" do DTO %s. IllegalAccessException: %s",
                        field,
                        this.dto.getClass().getName(),
                        e.getMessage()
                ) );
            }
        } else {
            throw new DataMapperException( String.format(
                    "Erro ao mapear o campo \"%s\" porque ele não existe no DTO %s.",
                    field,
                    this.dto.getClass().getName()
            ) );
        }

        return this;
    }

    private DTOMapper<T> mapAll(List<String> targetFields) {
        if ( targetFields == null ) {
            targetFields = new ArrayList<>( this.entityFields.keySet() );
        }

        for ( String entityFieldName : targetFields ) {
            if ( !this.dtoFields.containsKey( entityFieldName ) ) {
                continue;
            }

            Field entityField = this.entityFields.get( entityFieldName );

            Object entityFieldValue = null;

            try {
                if ( !this.dtoFields.get( entityFieldName ).getType().isAssignableFrom( entityField.getType() ) ) {
                    throw new DataMapperException( String.format(
                            "Erro ao copiar para o DTO %s valores da Entity %s. O tipo do campo \"%s\" no DTO é %s mas na Entity é %s.",
                            this.dto.getClass().getName(),
                            this.sourceEntity.getClass().getName(),
                            entityFieldName,
                            this.dtoFields.get( entityFieldName ).getType().getName(),
                            entityField.getType().getName()
                    ) );
                }

                entityFieldValue = entityField.get( this.sourceEntity );

                if ( entityFieldValue != null ) {
                    this.dtoFields.get( entityFieldName ).set( this.dto, entityFieldValue );
                }

                this.hasMappedAny = true;
                fieldsRemainingToBeMapped.put( entityFieldName, false );
            } catch ( IllegalAccessException e ) {
                throw new DataMapperException( String.format(
                        "Erro copiar para o DTO %s todos os valores da Entity %s. %s",
                        this.dto.getClass().getName(),
                        this.sourceEntity.getClass().getName(),
                        e.getMessage()
                ) );
            }
        }

        return this;
    }

    /**
     * Copia todos os campos da Entity para o DTO.
     * Isso irá substituir qualquer valor que já tenha sido atribuído aos campos do DTO.
     * Campos da Entity ausentes no DTO ou contrário, além de tipos diferentes, causarão uma DataMapperException.
     *
     * @return Retorna o DTO.
     */
    public T copyAll() {
        this.mapAll( null );
        return (T) this.dto;
    }

    /**
     * Copia da Entity todos os campos do DTO que ainda não tenham sido atribuídos com sucesso pelas funções map ou copyAll.
     * Campos da Entity ausentes no DTO ou tipos diferentes causarão uma DataMapperException.
     *
     * @return Retorna o DTO.
     */
    public T copyRemaining() {
        this.mapAll(
                this.fieldsRemainingToBeMapped
                        .keySet()
                        .stream()
                        .filter( s -> {
                            return this.fieldsRemainingToBeMapped.get( s );
                        } ).collect( Collectors.toList() )
        );

        return (T) this.dto;
    }

    public T returnDTO() {
        if ( this.hasMappedAny == false ) {
            Logger.getLogger( this.getClass().getName() ).log( Level.WARNING, String.format(
                    "Nenhum campo foi mapeado no DTO %s usando a Entity %s. Certifique-se de usar alguma função de mapeamento do " +
                            "DTOMapper depois " +
                            "de chamar fromEntity (como copyAll, por exemplo).",
                    this.dto.getClass().getName(),
                    this.sourceEntity.getClass().getName()
            ) );
        }

        return (T) this.dto;
    }
}