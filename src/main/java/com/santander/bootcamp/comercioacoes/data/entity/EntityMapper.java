package com.santander.bootcamp.comercioacoes.data.entity;

import com.santander.bootcamp.comercioacoes.data.dto.DTOBase;
import com.santander.bootcamp.comercioacoes.exceptions.DataMapperException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EntityMapper<T extends EntityBase> {
    private EntityBase<T> entity;
    private DTOBase sourceDTO;

    private HashMap<String, Field> dtoFields = new HashMap<>();
    private HashMap<String, Field> entityFields = new HashMap<>();

    private Boolean hasMappedAny = false;

    private Map<String, Boolean> fieldsRemainingToBeMapped = new HashMap<>();

    public EntityMapper(DTOBase sourceDTO, EntityBase<T> entity) {
        this.sourceDTO = sourceDTO;
        this.entity = entity;

        this.processObjectsFields();
    }

    private void processObjectsFields() {
        for ( Field field : this.sourceDTO.getClass().getDeclaredFields() ) {
            dtoFields.put( field.getName(), field );
        }

        for ( Field field : this.entity.getClass().getDeclaredFields() ) {
            entityFields.put( field.getName(), field );
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

    public <K> EntityMapper<T> map(String field, Function<K, ?> mapFunction) {
        if ( this.entityFields.containsKey( field ) ) {
            try {
                Object mapFunctionResult = mapFunction.apply(
                        (K) this.dtoFields.get( field ).get( this.sourceDTO )
                );

                if ( !this.entityFields.get( field ).getType().isAssignableFrom( mapFunctionResult.getClass() ) ) {
                    throw new DataMapperException( String.format(
                            "Erro ao mapear o campo \"%s\" da Entity %s. A Entity espera que este campo seja do tipo %s " +
                                    "mas a função de mapeamento retornou um tipo %s.",
                            field,
                            this.entity.getClass().getName(),
                            this.entityFields.get( field ).getType().getName(),
                            mapFunctionResult.getClass().getName()
                    ) );
                }

                this.entityFields.get( field ).set( this.entity, mapFunctionResult );

                this.hasMappedAny = true;
                fieldsRemainingToBeMapped.put( field, false );
            } catch ( IllegalAccessException e ) {
                throw new DataMapperException( String.format(
                        "Erro ao mapear o campo \"%s\" da Entity %s. IllegalAccessException: %s",
                        field,
                        this.entity.getClass().getName(),
                        e.getMessage()
                ) );
            }
        } else {
            throw new DataMapperException( String.format(
                    "Erro ao mapear o campo \"%s\" porque ele não existe na Entity %s.",
                    field,
                    this.entity.getClass().getName()
            ) );
        }

        return this;
    }

    private EntityMapper<T> mapAll(List<String> targetFields) {
        if ( targetFields == null ) {
            targetFields = new ArrayList<>( this.dtoFields.keySet() );
        }

        for ( String dtoFieldName : targetFields ) {
            if ( !this.entityFields.containsKey( dtoFieldName ) ) {
                continue;
            }

            Field dtoField = this.dtoFields.get( dtoFieldName );

            Object dtoFieldValue = null;

            try {
                if ( !this.entityFields.get( dtoFieldName ).getType().isAssignableFrom( dtoField.getType() ) ) {
                    throw new DataMapperException( String.format(
                            "Erro ao copiar para a Entity %s valores do DTO %s. O tipo do campo \"%s\" na Entity é %s mas no DTO é %s.",
                            this.entity.getClass().getName(),
                            this.sourceDTO.getClass().getName(),
                            dtoFieldName,
                            this.entityFields.get( dtoFieldName ).getType().getName(),
                            dtoField.getType().getName()
                    ) );
                }

                dtoFieldValue = dtoField.get( this.sourceDTO );

                if ( dtoFieldValue != null ) {
                    this.entityFields.get( dtoFieldName ).set( this.entity, dtoFieldValue );
                }

                this.hasMappedAny = true;
                fieldsRemainingToBeMapped.put( dtoFieldName, false );
            } catch ( IllegalAccessException e ) {
                throw new DataMapperException( String.format(
                        "Erro ao copiar para a Entity %s valores do DTO %s. %s",
                        this.entity.getClass().getName(),
                        this.sourceDTO.getClass().getName(),
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
        return (T) this.entity;
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

        return (T) this.entity;
    }

    public T returnEntity() {
        if ( this.hasMappedAny == false ) {
            Logger.getLogger( this.getClass().getName() ).log( Level.WARNING, String.format(
                    "Nenhum campo foi mapeado da Entity %s usando o DTO %s. Certifique-se de usar alguma função de mapeamento do " +
                            "EntityMapper depois " +
                            "de chamar fromDTO (como copyAll, por exemplo).",
                    this.entity.getClass().getName(),
                    this.sourceDTO.getClass().getName()
            ) );
        }

        return (T) this.entity;
    }
}
