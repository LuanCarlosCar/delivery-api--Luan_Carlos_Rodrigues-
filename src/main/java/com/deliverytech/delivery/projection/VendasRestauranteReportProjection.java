package com.deliverytech.delivery.projection;

import java.math.BigDecimal;

public interface VendasRestauranteReportProjection {
    Long getRestauranteId();
    String getRestauranteNome();
    String getRestauranteCategoria();
    Long getTotalPedidos();
    BigDecimal getReceitaTotal();
    BigDecimal getTicketMedio();
}