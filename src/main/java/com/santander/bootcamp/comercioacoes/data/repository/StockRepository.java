package com.santander.bootcamp.comercioacoes.data.repository;

import com.santander.bootcamp.comercioacoes.data.model.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByNameAndDate(String name, LocalDate date);
}
