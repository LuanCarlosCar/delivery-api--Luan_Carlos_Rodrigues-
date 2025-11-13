package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private EntityManager entityManager;

    public Page<RestauranteDTO> findRestaurantes(String categoria, Boolean ativo, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT r FROM Restaurante r WHERE 1=1");
        
        if (categoria != null && !categoria.isEmpty()) {
            jpql.append(" AND r.categoria = :categoria");
        }
        
        if (ativo != null) {
            jpql.append(" AND r.ativo = :ativo");
        }
        
        jpql.append(" ORDER BY r.nome ASC");

        TypedQuery<Restaurante> query = entityManager.createQuery(jpql.toString(), Restaurante.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(
            jpql.toString().replace("SELECT r", "SELECT COUNT(r)"), Long.class);

        if (categoria != null && !categoria.isEmpty()) {
            query.setParameter("categoria", categoria);
            countQuery.setParameter("categoria", categoria);
        }
        
        if (ativo != null) {
            query.setParameter("ativo", ativo);
            countQuery.setParameter("ativo", ativo);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Restaurante> restaurantes = query.getResultList();
        Long total = countQuery.getSingleResult();

        List<RestauranteDTO> restaurantesDTO = restaurantes.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

        return new PageImpl<>(restaurantesDTO, pageable, total);
    }

    private RestauranteDTO convertToDTO(Restaurante restaurante) {
        return new RestauranteDTO(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getCategoria(),
            restaurante.getEndereco(),
            restaurante.getTelefone(),
            restaurante.getTaxaEntrega(),
            restaurante.getTempoEntregaMin(),
            restaurante.getAtivo(),
            restaurante.getDataCadastro()
        );
    }
}