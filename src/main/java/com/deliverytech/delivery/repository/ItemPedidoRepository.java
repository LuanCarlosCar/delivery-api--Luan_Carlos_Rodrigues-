package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    List<ItemPedido> findByPedidoId(Long pedidoId);
}