package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(name = "preco_unitario", precision = 10, scale = 2)
    private BigDecimal precoUnitario;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    public ItemPedido() {}
    
    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Pedido getPedido() {
        return pedido;
    }
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        if (precoUnitario != null) {
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
    
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        if (quantidade != null) {
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}