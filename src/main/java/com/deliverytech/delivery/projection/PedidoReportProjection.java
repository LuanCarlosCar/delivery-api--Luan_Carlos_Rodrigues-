package com.deliverytech.delivery.projection;

import com.deliverytech.delivery.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PedidoReportProjection {
    Long getId();
    String getClienteNome();
    String getRestauranteNome();
    StatusPedido getStatus();
    LocalDateTime getDataPedido();
    BigDecimal getValorTotal();
    String getEnderecoEntrega();
}