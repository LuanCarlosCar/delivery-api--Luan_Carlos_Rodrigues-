package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByEmail(String email);
    
    List<Cliente> findByAtivoTrue();
    
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByEmail(String email);
}