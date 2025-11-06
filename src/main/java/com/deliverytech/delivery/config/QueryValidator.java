package com.deliverytech.delivery.config;

import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(2)
public class QueryValidator implements CommandLineRunner {

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
        System.out.println("\n=== VALIDAÇÃO DAS CONSULTAS SPRING DATA JPA ===");
        
        testarConsultasCliente();
        testarConsultasRestaurante();
        testarConsultasProduto();
        testarConsultasPedido();
        verificarRelacionamentos();
        
        System.out.println("=== VALIDAÇÃO CONCLUÍDA ===\n");
    }
    
    private void testarConsultasCliente() {
        System.out.println("\n--- CONSULTAS CLIENTE ---");
        
        // findByEmail
        var clientePorEmail = clienteRepository.findByEmail("joao@email.com");
        System.out.println("findByEmail('joao@email.com'): " + 
            (clientePorEmail.isPresent() ? clientePorEmail.get().getNome() : "Não encontrado"));
        
        // findByAtivoTrue
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("findByAtivoTrue(): " + clientesAtivos.size() + " clientes ativos");
        
        // findByNomeContainingIgnoreCase
        List<Cliente> clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("silva");
        System.out.println("findByNomeContainingIgnoreCase('silva'): " + clientesPorNome.size() + " clientes");
        
        // existsByEmail
        boolean emailExiste = clienteRepository.existsByEmail("maria@email.com");
        System.out.println("existsByEmail('maria@email.com'): " + emailExiste);
    }
    
    private void testarConsultasRestaurante() {
        System.out.println("\n--- CONSULTAS RESTAURANTE ---");
        
        // findByCategoria
        List<Restaurante> restaurantesItaliano = restauranteRepository.findByCategoria("Italiana");
        System.out.println("findByCategoria('Italiana'): " + restaurantesItaliano.size() + " restaurantes");
        
        // findByAtivoTrue
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        System.out.println("findByAtivoTrue(): " + restaurantesAtivos.size() + " restaurantes ativos");
        
        // findByTaxaEntregaLessThanEqual
        List<Restaurante> restaurantesTaxa = restauranteRepository.findByTaxaEntregaLessThanEqual(new BigDecimal("4.00"));
        System.out.println("findByTaxaEntregaLessThanEqual(4.00): " + restaurantesTaxa.size() + " restaurantes");
        
        // findTop5ByOrderByNomeAsc
        List<Restaurante> top5Restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        System.out.println("findTop5ByOrderByNomeAsc(): " + top5Restaurantes.size() + " restaurantes");
        top5Restaurantes.forEach(r -> System.out.println("  - " + r.getNome()));
    }
    
    private void testarConsultasProduto() {
        System.out.println("\n--- CONSULTAS PRODUTO ---");
        
        // findByRestauranteId
        List<Produto> produtosPorRestaurante = produtoRepository.findByRestauranteId(1L);
        System.out.println("findByRestauranteId(1): " + produtosPorRestaurante.size() + " produtos");
        
        // findByDisponivelTrue
        List<Produto> produtosDisponiveis = produtoRepository.findByDisponivelTrue();
        System.out.println("findByDisponivelTrue(): " + produtosDisponiveis.size() + " produtos disponíveis");
        
        // findByCategoria
        List<Produto> produtosPizza = produtoRepository.findByCategoria("Pizza");
        System.out.println("findByCategoria('Pizza'): " + produtosPizza.size() + " pizzas");
        
        // findByPrecoLessThanEqual
        List<Produto> produtosBaratos = produtoRepository.findByPrecoLessThanEqual(new BigDecimal("30.00"));
        System.out.println("findByPrecoLessThanEqual(30.00): " + produtosBaratos.size() + " produtos");
        produtosBaratos.forEach(p -> System.out.println("  - " + p.getNome() + ": R$ " + p.getPreco()));
    }
    
    private void testarConsultasPedido() {
        System.out.println("\n--- CONSULTAS PEDIDO ---");
        
        // findByClienteId
        List<Pedido> pedidosPorCliente = pedidoRepository.findByClienteId(1L);
        System.out.println("findByClienteId(1): " + pedidosPorCliente.size() + " pedidos");
        
        // findByStatus
        List<Pedido> pedidosPendentes = pedidoRepository.findByStatus(StatusPedido.PENDENTE);
        System.out.println("findByStatus(PENDENTE): " + pedidosPendentes.size() + " pedidos");
        
        List<Pedido> pedidosConfirmados = pedidoRepository.findByStatus(StatusPedido.CONFIRMADO);
        System.out.println("findByStatus(CONFIRMADO): " + pedidosConfirmados.size() + " pedidos");
        
        // findTop10ByOrderByDataPedidoDesc
        List<Pedido> ultimosPedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        System.out.println("findTop10ByOrderByDataPedidoDesc(): " + ultimosPedidos.size() + " pedidos");
        
        // findByDataPedidoBetween
        LocalDateTime hoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime amanha = hoje.plusDays(1);
        List<Pedido> pedidosHoje = pedidoRepository.findByDataPedidoBetween(hoje, amanha);
        System.out.println("findByDataPedidoBetween(hoje): " + pedidosHoje.size() + " pedidos hoje");
    }
    
    private void verificarRelacionamentos() {
        System.out.println("\n--- VERIFICAÇÃO DE RELACIONAMENTOS ---");
        
        // Verificar relacionamento Cliente -> Pedido
        List<Cliente> clientes = clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            List<Pedido> pedidosCliente = pedidoRepository.findByClienteId(cliente.getId());
            System.out.println("Cliente " + cliente.getNome() + " tem " + pedidosCliente.size() + " pedido(s)");
        }
        
        // Verificar relacionamento Restaurante -> Produto
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        for (Restaurante restaurante : restaurantes) {
            List<Produto> produtosRestaurante = produtoRepository.findByRestauranteId(restaurante.getId());
            System.out.println("Restaurante " + restaurante.getNome() + " tem " + produtosRestaurante.size() + " produto(s)");
        }
        
        // Verificar relacionamento Pedido -> ItemPedido
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido pedido : pedidos) {
            List<ItemPedido> itensPedido = itemPedidoRepository.findByPedidoId(pedido.getId());
            System.out.println("Pedido #" + pedido.getId() + " tem " + itensPedido.size() + " item(ns)");
            for (ItemPedido item : itensPedido) {
                System.out.println("  - " + item.getProduto().getNome() + " (Qtd: " + item.getQuantidade() + ")");
            }
        }
        
        // Verificar totais
        System.out.println("\n--- TOTAIS PERSISTIDOS ---");
        System.out.println("Total de clientes: " + clienteRepository.count());
        System.out.println("Total de restaurantes: " + restauranteRepository.count());
        System.out.println("Total de produtos: " + produtoRepository.count());
        System.out.println("Total de pedidos: " + pedidoRepository.count());
        System.out.println("Total de itens de pedido: " + itemPedidoRepository.count());
    }
}