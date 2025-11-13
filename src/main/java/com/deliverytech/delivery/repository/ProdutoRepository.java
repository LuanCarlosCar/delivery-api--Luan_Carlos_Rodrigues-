package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId")
    List<Produto> findByRestauranteId(@Param("restauranteId") Long restauranteId);
    
    @Query("SELECT p FROM Produto p WHERE p.disponivel = true")
    List<Produto> findByDisponivelTrue();
    
    @Query("SELECT p FROM Produto p WHERE p.categoria = :categoria")
    List<Produto> findByCategoria(@Param("categoria") String categoria);
    
    @Query("SELECT p FROM Produto p WHERE p.preco <= :preco")
    List<Produto> findByPrecoLessThanEqual(@Param("preco") BigDecimal preco);
    
    @Query("SELECT p FROM Produto p WHERE p.disponivel = true AND p.restaurante.ativo = true ORDER BY p.preco ASC")
    List<Produto> findProdutosDisponiveisOrdenadosPorPreco();
    
    @Query("SELECT p FROM Produto p JOIN p.restaurante r WHERE r.categoria = :categoria AND p.disponivel = true")
    List<Produto> findProdutosPorCategoriaRestaurante(@Param("categoria") String categoria);
    
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND (:disponivel IS NULL OR p.disponivel = :disponivel)")
    List<Produto> findByRestauranteIdAndDisponivel(@Param("restauranteId") Long restauranteId, @Param("disponivel") Boolean disponivel);
}