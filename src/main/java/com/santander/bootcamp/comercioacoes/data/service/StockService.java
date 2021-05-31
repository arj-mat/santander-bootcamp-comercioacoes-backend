package com.santander.bootcamp.comercioacoes.data.service;

import com.santander.bootcamp.comercioacoes.AppMessages;
import com.santander.bootcamp.comercioacoes.data.model.dto.StockDTO;
import com.santander.bootcamp.comercioacoes.data.model.entity.StockEntity;
import com.santander.bootcamp.comercioacoes.data.model.mapper.StockMapper;
import com.santander.bootcamp.comercioacoes.data.repository.StockRepository;
import com.santander.bootcamp.comercioacoes.exceptions.BusinessException;
import com.santander.bootcamp.comercioacoes.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {
    @Autowired
    private StockRepository repository;

    @Transactional
    public StockDTO save(StockDTO dto) {
        Optional<StockEntity> existingStock = repository.findByNameAndDate( dto.name, dto.date );

        if ( existingStock.isPresent() ) {
            throw new BusinessException( AppMessages.BUSINESS_EXCEPTION_STOCK_ALREADY_EXISTS );
        }

        StockEntity entity = StockMapper.FromDTOToEntity( dto );

        repository.save( entity );

        return StockMapper.FromEntityToDTO( entity );
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getAll() {
        return repository.findAll()
                         .stream()
                         .map( stockEntity -> StockMapper.FromEntityToDTO( stockEntity ) )
                         .collect( Collectors.toList() );
    }

    @Transactional(readOnly = true)
    public StockDTO getByID(Long id) {
        StockEntity entity = repository.findById( id )
                                       .orElseThrow( () -> new NotFoundException( String.format(
                                               "GET /stock/get/%d",
                                               id
                                       ) ) );

        return StockMapper.FromEntityToDTO( entity );
    }

    @Transactional
    public StockDTO deleteByID(Long id) {
        StockEntity entity = repository.findById( id )
                                       .orElseThrow( () -> new NotFoundException( String.format(
                                               "DELETE /stock/delete/%d",
                                               id
                                       ) ) );

        repository.deleteById( id );

        return StockMapper.FromEntityToDTO( entity );
    }
}
