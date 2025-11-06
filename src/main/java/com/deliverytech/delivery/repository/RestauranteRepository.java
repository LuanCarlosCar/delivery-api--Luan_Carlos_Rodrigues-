package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    List<Restaurante> findByCategoria(String categoria);
    
    List<Restaurante> findByAtivoTrue();
    
    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);
    
    List<Restaurante> findTop5ByOrderByNomeAsc();
}