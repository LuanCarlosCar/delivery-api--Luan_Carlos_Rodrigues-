package com.deliverytech.delivery.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RestauranteReportProjection {
    Long getId();
    String getNome();
    String getCategoria();
    BigDecimal getTaxaEntrega();
    Integer getTempoEntregaMin();
    Boolean getAtivo();
    LocalDateTime getDataCadastro();
    Long getTotalPedidos();
    BigDecimal getReceitaTotal();
}