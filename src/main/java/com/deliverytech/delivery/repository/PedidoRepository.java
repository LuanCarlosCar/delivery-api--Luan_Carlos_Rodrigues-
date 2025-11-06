package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByStatus(StatusPedido status);
    
    List<Pedido> findTop10ByOrderByDataPedidoDesc();
    
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
}