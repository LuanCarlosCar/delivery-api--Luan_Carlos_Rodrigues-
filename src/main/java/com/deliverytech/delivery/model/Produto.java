package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    private String descricao;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;
    
    private String categoria;
    
    @Column(nullable = false)
    private Boolean disponivel = true;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    public Produto() {}
    
    public Produto(String nome, String descricao, BigDecimal preco, String categoria, Restaurante restaurante) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.restaurante = restaurante;
        this.disponivel = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Boolean getDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public Restaurante getRestaurante() {
        return restaurante;
    }
    
    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }
}