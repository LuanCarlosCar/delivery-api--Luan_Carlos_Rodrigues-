package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria")
    List<Restaurante> findByCategoria(@Param("categoria") String categoria);
    
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true")
    List<Restaurante> findByAtivoTrue();
    
    @Query("SELECT r FROM Restaurante r WHERE r.taxaEntrega <= :taxa")
    List<Restaurante> findByTaxaEntregaLessThanEqual(@Param("taxa") BigDecimal taxa);
    
    @Query("SELECT r FROM Restaurante r ORDER BY r.nome ASC LIMIT 5")
    List<Restaurante> findTop5ByOrderByNomeAsc();
    
    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true AND r.taxaEntrega <= :taxaMaxima ORDER BY r.taxaEntrega ASC")
    List<Restaurante> findRestaurantesComTaxaBaixa(@Param("taxaMaxima") BigDecimal taxaMaxima);
}