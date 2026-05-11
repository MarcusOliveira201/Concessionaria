package com.entrevista.api_concessionaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entrevista.api_concessionaria.dto.ConsumidorDto;
import com.entrevista.api_concessionaria.service.ConsumidorService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
@Tag(name = "Consumidor", description = "Endpoint Criado para facilitar na hora de fazer o teste da aplicação, pois requer um consumidor.")
public class ConsumidorController {
    
    @Autowired
    private ConsumidorService service;
    
    @PostMapping("consumidores")
    public ResponseEntity<Void> salvarNovoConsumidor(@Valid @RequestBody ConsumidorDto dto) {
        service.salvarNovoConsumidor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
