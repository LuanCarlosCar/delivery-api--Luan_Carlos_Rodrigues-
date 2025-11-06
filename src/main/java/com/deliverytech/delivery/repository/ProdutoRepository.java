package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    List<Produto> findByRestauranteId(Long restauranteId);
    
    List<Produto> findByDisponivelTrue();
    
    List<Produto> findByCategoria(String categoria);
    
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);
}