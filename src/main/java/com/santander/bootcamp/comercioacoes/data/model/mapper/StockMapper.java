package com.santander.bootcamp.comercioacoes.data.model.mapper;

import com.santander.bootcamp.comercioacoes.data.model.dto.StockDTO;
import com.santander.bootcamp.comercioacoes.data.model.entity.StockEntity;

public abstract class StockMapper {
    public static StockDTO FromEntityToDTO(StockEntity entity) {
        return new StockDTO().mapper( entity )
                             .map( "tags", (String tags) -> tags.split( ";" ) )
                             .copyRemaining();
    }

    public static StockEntity FromDTOToEntity(StockDTO dto) {
        return new StockEntity().mapper( dto )
                                .map( "tags", (String[] tags) -> String.join( ";", tags ) )
                                .copyRemaining();
    }
}
