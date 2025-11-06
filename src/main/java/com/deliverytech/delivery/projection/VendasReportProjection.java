package com.deliverytech.delivery.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface VendasReportProjection {
    LocalDateTime getDataPedido();
    Long getTotalPedidos();
    BigDecimal getReceitaTotal();
    BigDecimal getTicketMedio();
    String getRestauranteMaisVendido();
    String getCategoriaMaisVendida();
}