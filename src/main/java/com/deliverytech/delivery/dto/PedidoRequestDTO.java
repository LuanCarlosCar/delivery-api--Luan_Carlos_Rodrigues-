package com.deliverytech.delivery.dto;

import java.util.List;

public class PedidoRequestDTO {
    private Long clienteId;
    private Long restauranteId;
    private String enderecoEntrega;
    private String observacoes;
    private List<ItemPedidoRequestDTO> itens;

    public PedidoRequestDTO() {}

    public PedidoRequestDTO(Long clienteId, Long restauranteId, String enderecoEntrega, 
                           String observacoes, List<ItemPedidoRequestDTO> itens) {
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.observacoes = observacoes;
        this.itens = itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
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

    public List<ItemPedidoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens) {
        this.itens = itens;
    }
}