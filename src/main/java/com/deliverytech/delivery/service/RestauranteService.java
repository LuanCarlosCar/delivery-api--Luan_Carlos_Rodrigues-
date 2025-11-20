package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.RestauranteDTO;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import com.deliverytech.delivery.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

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

    public List<ProdutoDTO> findProdutosByRestaurante(Long restauranteId, Boolean disponivel) {
        Optional<Restaurante> restaurante = restauranteRepository.findById(restauranteId);
        if (restaurante.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }

        List<Produto> produtos = produtoRepository.findByRestauranteIdAndDisponivel(restauranteId, disponivel);
        return produtos.stream()
            .map(this::convertProdutoToDTO)
            .collect(Collectors.toList());
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

    private ProdutoDTO convertProdutoToDTO(Produto produto) {
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getCategoria(),
            produto.getDisponivel(),
            produto.getRestaurante().getId(),
            produto.getRestaurante().getNome()
        );
    }

    public boolean isOwner(Long restauranteId) {
        return SecurityUtils.isRestauranteOwnerOrAdmin(restauranteId);
    }

    public boolean isProdutoOwner(Long produtoId) {
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        if (produto.isEmpty()) {
            return false;
        }
        return SecurityUtils.isRestauranteOwnerOrAdmin(produto.get().getRestaurante().getId());
    }

    public RestauranteDTO criarRestaurante(RestauranteDTO restauranteDTO) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setCategoria(restauranteDTO.getCategoria());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setTelefone(restauranteDTO.getTelefone());
        restaurante.setTaxaEntrega(restauranteDTO.getTaxaEntrega());
        restaurante.setTempoEntregaMin(restauranteDTO.getTempoEntregaMin());
        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());
        
        Restaurante novoRestaurante = restauranteRepository.save(restaurante);
        return convertToDTO(novoRestaurante);
    }

    public RestauranteDTO atualizarRestaurante(Long id, RestauranteDTO restauranteDTO) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(id);
        if (restauranteOpt.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }
        
        Restaurante restaurante = restauranteOpt.get();
        restaurante.setNome(restauranteDTO.getNome());
        restaurante.setCategoria(restauranteDTO.getCategoria());
        restaurante.setEndereco(restauranteDTO.getEndereco());
        restaurante.setTelefone(restauranteDTO.getTelefone());
        restaurante.setTaxaEntrega(restauranteDTO.getTaxaEntrega());
        restaurante.setTempoEntregaMin(restauranteDTO.getTempoEntregaMin());
        restaurante.setAtivo(restauranteDTO.getAtivo());
        
        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        return convertToDTO(restauranteAtualizado);
    }

    public void deletarRestaurante(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new RuntimeException("Restaurante não encontrado");
        }
        restauranteRepository.deleteById(id);
    }

    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) {
        Long restauranteId = produtoDTO.getRestauranteId();
        
        // Se não é admin, só pode criar produtos para seu próprio restaurante
        if (!SecurityUtils.isAdmin() && !SecurityUtils.getCurrentUserRestauranteId().equals(restauranteId)) {
            throw new RuntimeException("Acesso negado - você só pode criar produtos para seu restaurante");
        }
        
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(restauranteId);
        if (restauranteOpt.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }
        
        Produto produto = new Produto();
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setCategoria(produtoDTO.getCategoria());
        produto.setDisponivel(true);
        produto.setRestaurante(restauranteOpt.get());
        
        Produto novoProduto = produtoRepository.save(produto);
        return convertProdutoToDTO(novoProduto);
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isEmpty()) {
            throw new RuntimeException("Produto não encontrado");
        }
        
        Produto produto = produtoOpt.get();
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setCategoria(produtoDTO.getCategoria());
        produto.setDisponivel(produtoDTO.getDisponivel());
        
        Produto produtoAtualizado = produtoRepository.save(produto);
        return convertProdutoToDTO(produtoAtualizado);
    }

    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }
}