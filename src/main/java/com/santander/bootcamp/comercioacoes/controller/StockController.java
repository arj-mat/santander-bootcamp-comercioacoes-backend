package com.santander.bootcamp.comercioacoes.controller;

import com.santander.bootcamp.comercioacoes.data.model.dto.StockDTO;
import com.santander.bootcamp.comercioacoes.data.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("ALL")
@RestController
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    private StockService service;

    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockDTO>> getIndex() {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( this.service.getAll() );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {

        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( this.service.getByID( id ) );
    }

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> postStock(@Valid @RequestBody StockDTO dto) {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( this.service.save( dto ) );
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> deleteStock(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( this.service.deleteByID( id ) );
    }
}
