package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Long restauranteId;
    private String restauranteNome;
    private StatusPedido status;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private String enderecoEntrega;
    private String observacoes;
    private List<ItemPedidoDTO> itens;

    public PedidoResponseDTO() {}

    public PedidoResponseDTO(Long id, Long clienteId, String clienteNome, Long restauranteId, 
                            String restauranteNome, StatusPedido status, LocalDateTime dataPedido, 
                            BigDecimal valorTotal, String enderecoEntrega, String observacoes,
                            List<ItemPedidoDTO> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.restauranteId = restauranteId;
        this.restauranteNome = restauranteNome;
        this.status = status;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.enderecoEntrega = enderecoEntrega;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public String getRestauranteNome() {
        return restauranteNome;
    }

    public void setRestauranteNome(String restauranteNome) {
        this.restauranteNome = restauranteNome;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}