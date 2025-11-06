package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "endereco_entrega")
    private String enderecoEntrega;
    
    private String observacoes;
    
    @PrePersist
    protected void onCreate() {
        dataPedido = LocalDateTime.now();
        if (status == null) {
            status = StatusPedido.PENDENTE;
        }
    }
    
    public Pedido() {}
    
    public Pedido(Cliente cliente, Restaurante restaurante, BigDecimal valorTotal, String enderecoEntrega) {
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.valorTotal = valorTotal;
        this.enderecoEntrega = enderecoEntrega;
        this.status = StatusPedido.PENDENTE;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Restaurante getRestaurante() {
        return restaurante;
    }
    
    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
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
}