package com.deliverytech.delivery;

import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.model.Pedido;
import com.deliverytech.delivery.model.Produto;
import com.deliverytech.delivery.model.Restaurante;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CenariosObrigatoriosTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    public void cenario1_BuscaClientePorEmail() {
        System.out.println("\n=== CENÁRIO 1: Busca de Cliente por Email ===");
        
        Optional<Cliente> clienteOpt = clienteRepository.findByEmail("joao@email.com");
        
        System.out.println("Executando: clienteRepository.findByEmail(\"joao@email.com\")");
        
        assertTrue(clienteOpt.isPresent(), "Cliente deve ser encontrado");
        
        Cliente cliente = clienteOpt.get();
        assertEquals("joao@email.com", cliente.getEmail());
        assertNotNull(cliente.getNome());
        assertTrue(cliente.getAtivo());
        
        System.out.println("✅ Cliente encontrado: " + cliente.getNome());
        System.out.println("   Email: " + cliente.getEmail());
        System.out.println("   Ativo: " + cliente.getAtivo());
        System.out.println("   Data Cadastro: " + cliente.getDataCadastro());
    }

    @Test
    public void cenario2_ProdutosPorRestaurante() {
        System.out.println("\n=== CENÁRIO 2: Produtos por Restaurante ===");
        
        List<Produto> produtos = produtoRepository.findByRestauranteId(1L);
        
        System.out.println("Executando: produtoRepository.findByRestauranteId(1L)");
        
        assertFalse(produtos.isEmpty(), "Lista de produtos não deve estar vazia");
        
        System.out.println("✅ Produtos encontrados: " + produtos.size());
        
        for (Produto produto : produtos) {
            assertEquals(1L, produto.getRestaurante().getId());
            System.out.println("   - " + produto.getNome() + " (R$ " + produto.getPreco() + ")");
            System.out.println("     Categoria: " + produto.getCategoria());
            System.out.println("     Disponível: " + produto.getDisponivel());
        }
    }

    @Test
    public void cenario3_PedidosRecentes() {
        System.out.println("\n=== CENÁRIO 3: Pedidos Recentes ===");
        
        List<Pedido> pedidos = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        
        System.out.println("Executando: pedidoRepository.findTop10ByOrderByDataPedidoDesc()");
        
        assertNotNull(pedidos, "Lista de pedidos não deve ser null");
        assertTrue(pedidos.size() <= 10, "Deve retornar no máximo 10 pedidos");
        
        System.out.println("✅ Pedidos encontrados: " + pedidos.size());
        
        // Verificar se estão ordenados por data decrescente
        for (int i = 0; i < pedidos.size() - 1; i++) {
            assertTrue(
                pedidos.get(i).getDataPedido().isAfter(pedidos.get(i + 1).getDataPedido()) ||
                pedidos.get(i).getDataPedido().isEqual(pedidos.get(i + 1).getDataPedido()),
                "Pedidos devem estar ordenados por data decrescente"
            );
        }
        
        for (Pedido pedido : pedidos) {
            System.out.println("   - Pedido #" + pedido.getId());
            System.out.println("     Cliente: " + pedido.getCliente().getNome());
            System.out.println("     Restaurante: " + pedido.getRestaurante().getNome());
            System.out.println("     Data: " + pedido.getDataPedido());
            System.out.println("     Valor: R$ " + pedido.getValorTotal());
            System.out.println("     Status: " + pedido.getStatus());
        }
    }

    @Test
    public void cenario4_RestaurantesPorTaxa() {
        System.out.println("\n=== CENÁRIO 4: Restaurantes por Taxa ===");
        
        List<Restaurante> restaurantes = restauranteRepository
            .findByTaxaEntregaLessThanEqual(new BigDecimal("5.00"));
        
        System.out.println("Executando: restauranteRepository.findByTaxaEntregaLessThanEqual(BigDecimal(\"5.00\"))");
        
        assertNotNull(restaurantes, "Lista de restaurantes não deve ser null");
        
        System.out.println("✅ Restaurantes encontrados: " + restaurantes.size());
        
        for (Restaurante restaurante : restaurantes) {
            assertTrue(
                restaurante.getTaxaEntrega().compareTo(new BigDecimal("5.00")) <= 0,
                "Taxa de entrega deve ser menor ou igual a R$ 5,00"
            );
            
            System.out.println("   - " + restaurante.getNome());
            System.out.println("     Categoria: " + restaurante.getCategoria());
            System.out.println("     Taxa de Entrega: R$ " + restaurante.getTaxaEntrega());
            System.out.println("     Tempo de Entrega: " + restaurante.getTempoEntregaMin() + " min");
            System.out.println("     Ativo: " + restaurante.getAtivo());
        }
    }

    @Test
    public void todosOsCenarios_TesteCompleto() {
        System.out.println("\n=== EXECUÇÃO COMPLETA DE TODOS OS CENÁRIOS ===");
        
        // Cenário 1
        cenario1_BuscaClientePorEmail();
        
        // Cenário 2  
        cenario2_ProdutosPorRestaurante();
        
        // Cenário 3
        cenario3_PedidosRecentes();
        
        // Cenário 4
        cenario4_RestaurantesPorTaxa();
        
        System.out.println("\n🎉 TODOS OS CENÁRIOS EXECUTADOS COM SUCESSO!");
    }
}