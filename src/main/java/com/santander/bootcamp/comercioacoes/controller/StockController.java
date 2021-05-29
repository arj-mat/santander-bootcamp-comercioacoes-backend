package com.santander.bootcamp.comercioacoes.controller;

import com.santander.bootcamp.comercioacoes.data.dto.StockDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/stock")
public class StockController {
    @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getIndex() {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( "" );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStock(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( "" );
    }

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postStock(@Valid StockDTO dto) {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( "" );
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteStock(@PathVariable Integer id) {
        return ResponseEntity
                .ok()
                .contentType( MediaType.APPLICATION_JSON )
                .body( "" );
    }
}
