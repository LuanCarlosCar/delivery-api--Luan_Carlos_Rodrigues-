package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.projection.ClienteReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> findByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Cliente c WHERE c.ativo = true")
    List<Cliente> findByAtivoTrue();
    
    @Query("SELECT c FROM Cliente c WHERE UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email")
    boolean existsByEmail(@Param("email") String email);
    
    @Query("SELECT c FROM Cliente c WHERE c.ativo = true AND c.dataCadastro >= CURRENT_DATE")
    List<Cliente> findClientesCadastradosHoje();
    
    @Query("SELECT c.id as id, c.nome as nome, c.email as email, c.ativo as ativo, " +
           "c.dataCadastro as dataCadastro, COUNT(p.id) as totalPedidos " +
           "FROM Cliente c LEFT JOIN Pedido p ON c.id = p.cliente.id " +
           "GROUP BY c.id, c.nome, c.email, c.ativo, c.dataCadastro")
    List<ClienteReportProjection> findClientesComTotalPedidos();
}