package com.deliverytech.delivery.projection;

import java.time.LocalDateTime;

public interface ClienteReportProjection {
    Long getId();
    String getNome();
    String getEmail();
    Boolean getAtivo();
    LocalDateTime getDataCadastro();
    Long getTotalPedidos();
}