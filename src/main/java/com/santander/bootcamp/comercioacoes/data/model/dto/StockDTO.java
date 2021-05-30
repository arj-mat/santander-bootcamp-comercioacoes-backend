package com.santander.bootcamp.comercioacoes.data.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class StockDTO extends DTOBase<StockDTO> {
    public Long id;

    @NotEmpty
    @NotNull
    public String name;

    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 6, fraction = 2) // Até 999999.99
    public Double price;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public LocalDate date;

    @NotNull
    @Digits(integer = 3, fraction = 2) // Até 999.99
    public Double variation;
}
