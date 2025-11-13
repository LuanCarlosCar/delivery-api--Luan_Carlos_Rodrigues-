package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.projection.VendasRestauranteReportProjection;
import com.deliverytech.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
        Optional<Cliente> cliente = clienteRepository.findById(request.getClienteId());
        if (cliente.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        Optional<Restaurante> restaurante = restauranteRepository.findById(request.getRestauranteId());
        if (restaurante.isEmpty()) {
            throw new RuntimeException("Restaurante não encontrado");
        }

        if (!restaurante.get().getAtivo()) {
            throw new RuntimeException("Restaurante não está ativo");
        }

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        Pedido pedido = new Pedido(cliente.get(), restaurante.get(), BigDecimal.ZERO, request.getEnderecoEntrega());
        pedido.setObservacoes(request.getObservacoes());
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        for (ItemPedidoRequestDTO itemRequest : request.getItens()) {
            Optional<Produto> produto = produtoRepository.findById(itemRequest.getProdutoId());
            if (produto.isEmpty()) {
                throw new RuntimeException("Produto não encontrado: " + itemRequest.getProdutoId());
            }

            if (!produto.get().getDisponivel()) {
                throw new RuntimeException("Produto não disponível: " + produto.get().getNome());
            }

            if (!produto.get().getRestaurante().getId().equals(request.getRestauranteId())) {
                throw new RuntimeException("Produto não pertence ao restaurante selecionado: " + produto.get().getNome());
            }

            ItemPedido item = new ItemPedido(pedidoSalvo, produto.get(), itemRequest.getQuantidade(), produto.get().getPreco());
            itens.add(item);
            valorTotal = valorTotal.add(item.getSubtotal());
        }

        pedidoSalvo.setValorTotal(valorTotal);
        pedidoRepository.save(pedidoSalvo);

        itemPedidoRepository.saveAll(itens);

        return convertToResponseDTO(pedidoSalvo, itens);
    }

    private PedidoResponseDTO convertToResponseDTO(Pedido pedido, List<ItemPedido> itens) {
        List<ItemPedidoDTO> itensDTO = itens.stream()
            .map(this::convertItemToDTO)
            .collect(Collectors.toList());

        return new PedidoResponseDTO(
            pedido.getId(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNome(),
            pedido.getRestaurante().getId(),
            pedido.getRestaurante().getNome(),
            pedido.getStatus(),
            pedido.getDataPedido(),
            pedido.getValorTotal(),
            pedido.getEnderecoEntrega(),
            pedido.getObservacoes(),
            itensDTO
        );
    }

    private ItemPedidoDTO convertItemToDTO(ItemPedido item) {
        return new ItemPedidoDTO(
            item.getId(),
            item.getProduto().getId(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal()
        );
    }

    public List<VendasRestauranteReportProjection> relatorioVendasPorRestaurante(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);
        
        return pedidoRepository.findVendasPorRestaurante(inicio, fim);
    }
}