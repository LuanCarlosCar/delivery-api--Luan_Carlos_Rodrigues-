package com.deliverytech.delivery.config;

import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Order(1)
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (clienteRepository.count() == 0) {
            carregarDadosTeste();
        }
    }
    
    private void carregarDadosTeste() {
        System.out.println("=== Carregando dados de teste ===");
        
        // 3 Clientes
        Cliente cliente1 = new Cliente("João Silva", "joao@email.com", "(11) 99999-1111", "Rua A, 123");
        Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "(11) 99999-2222", "Rua B, 456");
        Cliente cliente3 = new Cliente("Pedro Costa", "pedro@email.com", "(11) 99999-3333", "Rua C, 789");
        
        cliente1 = clienteRepository.save(cliente1);
        cliente2 = clienteRepository.save(cliente2);
        cliente3 = clienteRepository.save(cliente3);
        
        System.out.println("✓ Clientes criados: " + clienteRepository.count());
        
        // 2 Restaurantes
        Restaurante restaurante1 = new Restaurante(
            "Pizza Express", 
            "Italiana", 
            "Av. Principal, 100", 
            "(11) 3333-1111", 
            new BigDecimal("5.00"), 
            30
        );
        
        Restaurante restaurante2 = new Restaurante(
            "Burger House", 
            "Hamburguer", 
            "Rua das Flores, 200", 
            "(11) 3333-2222", 
            new BigDecimal("3.50"), 
            25
        );
        
        restaurante1 = restauranteRepository.save(restaurante1);
        restaurante2 = restauranteRepository.save(restaurante2);
        
        System.out.println("✓ Restaurantes criados: " + restauranteRepository.count());
        
        // 5 Produtos
        Produto produto1 = new Produto("Pizza Margherita", "Pizza tradicional com molho de tomate, mussarela e manjericão", new BigDecimal("35.90"), "Pizza", restaurante1);
        Produto produto2 = new Produto("Pizza Pepperoni", "Pizza com molho de tomate, mussarela e pepperoni", new BigDecimal("42.90"), "Pizza", restaurante1);
        Produto produto3 = new Produto("Refrigerante 350ml", "Coca-Cola 350ml", new BigDecimal("4.50"), "Bebida", restaurante1);
        
        Produto produto4 = new Produto("Hamburger Classic", "Hamburger com carne, alface, tomate e maionese", new BigDecimal("28.90"), "Hamburger", restaurante2);
        Produto produto5 = new Produto("Batata Frita", "Porção de batata frita crocante", new BigDecimal("12.90"), "Acompanhamento", restaurante2);
        
        produto1 = produtoRepository.save(produto1);
        produto2 = produtoRepository.save(produto2);
        produto3 = produtoRepository.save(produto3);
        produto4 = produtoRepository.save(produto4);
        produto5 = produtoRepository.save(produto5);
        
        System.out.println("✓ Produtos criados: " + produtoRepository.count());
        
        // 2 Pedidos com itens
        Pedido pedido1 = new Pedido(cliente1, restaurante1, new BigDecimal("75.30"), "Rua A, 123");
        pedido1.setObservacoes("Sem cebola na pizza");
        pedido1 = pedidoRepository.save(pedido1);
        
        // Itens do pedido 1
        ItemPedido item1 = new ItemPedido(pedido1, produto1, 1, produto1.getPreco());
        ItemPedido item2 = new ItemPedido(pedido1, produto2, 1, produto2.getPreco());
        ItemPedido item3 = new ItemPedido(pedido1, produto3, 2, produto3.getPreco());
        
        itemPedidoRepository.save(item1);
        itemPedidoRepository.save(item2);
        itemPedidoRepository.save(item3);
        
        Pedido pedido2 = new Pedido(cliente2, restaurante2, new BigDecimal("41.80"), "Rua B, 456");
        pedido2.setStatus(StatusPedido.CONFIRMADO);
        pedido2 = pedidoRepository.save(pedido2);
        
        // Itens do pedido 2
        ItemPedido item4 = new ItemPedido(pedido2, produto4, 1, produto4.getPreco());
        ItemPedido item5 = new ItemPedido(pedido2, produto5, 1, produto5.getPreco());
        
        itemPedidoRepository.save(item4);
        itemPedidoRepository.save(item5);
        
        System.out.println("✓ Pedidos criados: " + pedidoRepository.count());
        System.out.println("✓ Itens de pedido criados: " + itemPedidoRepository.count());
        System.out.println("=== Dados de teste carregados com sucesso ===\n");
    }
}