package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.enums.Role;

import java.time.LocalDateTime;

public class LoginResponse {
    
    private String token;
    private String tipo = "Bearer";
    private LocalDateTime expiracao;
    private UserResponse usuario;

    public LoginResponse() {}

    public LoginResponse(String token, LocalDateTime expiracao, UserResponse usuario) {
        this.token = token;
        this.expiracao = expiracao;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getExpiracao() {
        return expiracao;
    }

    public void setExpiracao(LocalDateTime expiracao) {
        this.expiracao = expiracao;
    }

    public UserResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UserResponse usuario) {
        this.usuario = usuario;
    }
}