package com.santander.bootcamp.comercioacoes.data.model.dto;

public class ExceptionDTO {
    public String message;

    public ExceptionDTO(Exception exception) {
        this.message = exception.getMessage();
    }
}
