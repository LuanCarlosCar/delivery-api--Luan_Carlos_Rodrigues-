package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/restaurantes/{id}/produtos")
    public ResponseEntity<List<ProdutoDTO>> getProdutosByRestaurante(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean disponivel) {
        
        List<ProdutoDTO> produtos = restauranteService.findProdutosByRestaurante(id, disponivel);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping("/restaurantes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestauranteDTO> criarRestaurante(@RequestBody RestauranteDTO restauranteDTO) {
        try {
            RestauranteDTO novoRestaurante = restauranteService.criarRestaurante(restauranteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoRestaurante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/restaurantes/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))")
    public ResponseEntity<RestauranteDTO> atualizarRestaurante(@PathVariable Long id, @RequestBody RestauranteDTO restauranteDTO) {
        try {
            RestauranteDTO restauranteAtualizado = restauranteService.atualizarRestaurante(id, restauranteDTO);
            return ResponseEntity.ok(restauranteAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/restaurantes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        try {
            restauranteService.deletarRestaurante(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/produtos")
    @PreAuthorize("hasRole('RESTAURANTE') or hasRole('ADMIN')")
    public ResponseEntity<ProdutoDTO> criarProduto(@RequestBody ProdutoDTO produtoDTO) {
        try {
            ProdutoDTO novoProduto = restauranteService.criarProduto(produtoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/produtos/{id}")
    @PreAuthorize("hasRole('ADMIN') or @restauranteService.isProdutoOwner(#id)")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO) {
        try {
            ProdutoDTO produtoAtualizado = restauranteService.atualizarProduto(id, produtoDTO);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/produtos/{id}")
    @PreAuthorize("hasRole('ADMIN') or @restauranteService.isProdutoOwner(#id)")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        try {
            restauranteService.deletarProduto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}