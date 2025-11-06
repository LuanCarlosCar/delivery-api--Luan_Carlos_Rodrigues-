package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.projection.RestauranteReportProjection;
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
    
    @Query("SELECT r.id as id, r.nome as nome, r.categoria as categoria, r.taxaEntrega as taxaEntrega, " +
           "r.tempoEntregaMin as tempoEntregaMin, r.ativo as ativo, r.dataCadastro as dataCadastro, " +
           "COUNT(p.id) as totalPedidos, COALESCE(SUM(p.valorTotal), 0) as receitaTotal " +
           "FROM Restaurante r LEFT JOIN Pedido p ON r.id = p.restaurante.id " +
           "GROUP BY r.id, r.nome, r.categoria, r.taxaEntrega, r.tempoEntregaMin, r.ativo, r.dataCadastro")
    List<RestauranteReportProjection> findRestaurantesComEstatisticas();
}