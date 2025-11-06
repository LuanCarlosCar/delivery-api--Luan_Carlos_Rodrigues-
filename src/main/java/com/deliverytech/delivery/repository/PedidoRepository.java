package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT p FROM Pedido p WHERE p.status = :status")
    List<Pedido> findByStatus(@Param("status") StatusPedido status);
    
    @Query("SELECT p FROM Pedido p ORDER BY p.dataPedido DESC LIMIT 10")
    List<Pedido> findTop10ByOrderByDataPedidoDesc();
    
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim")
    List<Pedido> findByDataPedidoBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT p FROM Pedido p JOIN p.cliente c WHERE c.email = :email ORDER BY p.dataPedido DESC")
    List<Pedido> findPedidosPorEmailCliente(@Param("email") String email);
    
    @Query("SELECT p FROM Pedido p WHERE p.valorTotal >= :valorMinimo ORDER BY p.valorTotal DESC")
    List<Pedido> findPedidosComValorMinimo(@Param("valorMinimo") BigDecimal valorMinimo);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = :status")
    Long countByStatus(@Param("status") StatusPedido status);
}