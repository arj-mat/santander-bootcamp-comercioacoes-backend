package com.santander.bootcamp.comercioacoes.data.model.mapper;

import com.santander.bootcamp.comercioacoes.data.model.dto.StockDTO;
import com.santander.bootcamp.comercioacoes.data.model.entity.StockEntity;

import java.time.LocalDate;

public abstract class StockMapper {
    public static StockDTO EntityToDTO(StockEntity entity) {
        return new StockDTO().mapper( entity )
                             .map( "name", (String name) -> "<B>" + name + "</B>" )
                             .map( "date", (LocalDate date) -> date.plusYears( 2 ) )
                             .copyRemaining();
    }

    public static StockEntity DTOToEntity(StockDTO dto) {
        return new StockEntity().mapper( dto )
                                .map( "name", (String name) -> name.replaceAll( "\\<\\/?B\\>", "" ) )
                                .map( "date", (LocalDate date) -> date.minusYears( 2 ) )
                                .copyRemaining();
    }
}
