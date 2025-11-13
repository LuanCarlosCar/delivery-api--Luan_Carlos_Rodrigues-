package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @GetMapping("/restaurantes")
    public ResponseEntity<Page<RestauranteDTO>> getRestaurantes(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RestauranteDTO> restaurantes = restauranteService.findRestaurantes(categoria, ativo, pageable);
        
        return ResponseEntity.ok(restaurantes);
    }
}