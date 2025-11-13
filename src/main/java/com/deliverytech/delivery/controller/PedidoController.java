package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO request) {
        try {
            PedidoResponseDTO pedido = pedidoService.criarPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}