package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
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
}