package com.santander.bootcamp.comercioacoes.data.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_stock")
public class StockEntity extends EntityBase<StockEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public Double price;

    public LocalDate date;

    public Double variation;
}
