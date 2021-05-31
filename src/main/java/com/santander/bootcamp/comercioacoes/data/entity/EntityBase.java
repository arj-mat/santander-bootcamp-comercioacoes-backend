package com.santander.bootcamp.comercioacoes.data.entity;

import com.santander.bootcamp.comercioacoes.data.dto.DTOBase;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityBase<T extends EntityBase> {

    /**
     * Cria um Mapper que irá servir para alterar os campos do DTO atual utilizando uma Entity como fonte.
     *
     * @param sourceDTO A Entity contendo os valores a serem mapeados ou copiados.
     * @return Retorna um DTOMapper.
     */
    public EntityMapper<T> mapper(DTOBase sourceDTO) {
        return new EntityMapper<T>( sourceDTO, this );
    }

    /**
     * Obtém o nome da tabela declarado na anotação @Table da Entity.
     *
     * @return String ou null caso não haja tal anotação.
     */
    public String getTableName() {
        if ( this.getClass().isAnnotationPresent( Table.class ) ) {
            return this.getClass().getAnnotation( Table.class ).name();
        } else {
            return null;
        }
    }

    /**
     * Obtém o nome da coluna primária declarado na anotação @Id da Entity.
     *
     * @return String ou null caso não haja tal anotação.
     */
    public String getPrimaryKeyName() {
        for ( Field field : this.getClass().getDeclaredFields() ) {
            if ( field.isAnnotationPresent( Id.class ) ) {
                if ( field.isAnnotationPresent( Column.class ) ) {
                    return field.getAnnotation( Column.class ).name();
                } else {
                    return field.getName();
                }
            }
        }

        return null;
    }

    /**
     * Retorna os nomes das colunas da Entity. As colunas que possuírem a anotação @Column(name="valor") terão esse valor considerado.
     *
     * @return Lista de Strings
     */
    public List<String> getColumnNames() {
        List<String> result = new ArrayList<>();

        for ( Field field : this.getClass().getFields() ) {
            String fieldName;

            if ( field.isAnnotationPresent( Column.class ) ) {
                fieldName = field.getAnnotation( Column.class ).name();
            } else {
                fieldName = field.getName();
            }

            result.add( fieldName );
        }

        return result;
    }

    public String concatColumnNames(Function<String, String> fieldNameFormatter, String delimiter) {
        return this.getColumnNames().stream().map( fieldNameFormatter ).collect( Collectors.joining( delimiter ) );
    }

    public String concatColumnNames() {
        return this.concatColumnNames( s -> s, ", " );
    }

    public String concatColumnNamesAs(String replaceNamesWith) {
        return this.concatColumnNames( s -> replaceNamesWith, ", " );
    }

    public String concatColumnNamesAs(String replaceNamesWith, String delimiter) {
        return this.concatColumnNames( s -> replaceNamesWith, delimiter );
    }
}
