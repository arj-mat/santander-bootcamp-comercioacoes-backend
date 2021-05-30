package com.santander.bootcamp.comercioacoes.exceptions;

import com.santander.bootcamp.comercioacoes.data.model.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionDTO> businessException(BusinessException e) {
        return ResponseEntity.status( HttpStatus.UNPROCESSABLE_ENTITY )
                             .contentType( MediaType.APPLICATION_JSON )
                             .body( new ExceptionDTO( e ) );
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity notFoundException(NotFoundException e) {
        Logger.getLogger( this.getClass().getName() ).log( Level.WARNING, e.getMessage());

        return ResponseEntity.status( HttpStatus.NOT_FOUND )
                             .build();
    }
}
