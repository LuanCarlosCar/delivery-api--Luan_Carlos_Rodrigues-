package com.deliverytech.delivery.dto;

public class ItemPedidoRequestDTO {
    private Long produtoId;
    private Integer quantidade;

    public ItemPedidoRequestDTO() {}

    public ItemPedidoRequestDTO(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}