package com.santander.bootcamp.comercioacoes.data.model.dto;

import com.santander.bootcamp.comercioacoes.data.model.entity.EntityBase;
import com.santander.bootcamp.comercioacoes.data.mapper.DTOMapper;

public class DTOBase<T extends DTOBase> {
    /**
     * Cria um Mapper que irá servir para alterar os campos do DTO atual utilizando uma Entity como fonte.
     *
     * @param sourceEntity A Entity contendo os valores a serem mapeados ou copiados.
     * @return Retorna um DTOMapper.
     */
    public DTOMapper<T> mapper(EntityBase sourceEntity) {
        return new DTOMapper<T>( sourceEntity, this );
    }
}
