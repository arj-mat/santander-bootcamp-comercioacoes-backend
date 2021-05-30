package com.santander.bootcamp.comercioacoes.exceptions;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String resource) {
        super( "[404 Not Found]: " + resource );
    }
}
