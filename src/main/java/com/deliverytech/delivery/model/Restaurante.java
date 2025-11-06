package com.deliverytech.delivery.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurante")
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String categoria;
    
    private String endereco;
    
    private String telefone;
    
    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega;
    
    @Column(name = "tempo_entrega_min")
    private Integer tempoEntregaMin;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
    
    public Restaurante() {}
    
    public Restaurante(String nome, String categoria, String endereco, String telefone, BigDecimal taxaEntrega, Integer tempoEntregaMin) {
        this.nome = nome;
        this.categoria = categoria;
        this.endereco = endereco;
        this.telefone = telefone;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMin = tempoEntregaMin;
        this.ativo = true;
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
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }
    
    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }
    
    public Integer getTempoEntregaMin() {
        return tempoEntregaMin;
    }
    
    public void setTempoEntregaMin(Integer tempoEntregaMin) {
        this.tempoEntregaMin = tempoEntregaMin;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}