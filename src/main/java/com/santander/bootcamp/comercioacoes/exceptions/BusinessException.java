package com.santander.bootcamp.comercioacoes.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super( message );
    }
}
