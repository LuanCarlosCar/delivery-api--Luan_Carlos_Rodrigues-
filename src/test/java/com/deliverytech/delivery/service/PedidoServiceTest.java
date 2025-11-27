package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
import com.deliverytech.delivery.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PedidoService Tests")
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;
    private Pedido pedido;
    private PedidoRequestDTO pedidoRequest;
    private ItemPedidoRequestDTO itemRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("João Silva", "joao@email.com", "11999999999", "Rua das Flores, 123");
        cliente.setId(1L);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setAtivo(true);

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Hambúrguer");
        produto.setPreco(new BigDecimal("25.90"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);

        pedido = new Pedido(cliente, restaurante, BigDecimal.ZERO, "Rua de Entrega, 456");
        pedido.setId(1L);

        itemRequest = new ItemPedidoRequestDTO();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(2);

        pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setClienteId(1L);
        pedidoRequest.setRestauranteId(1L);
        pedidoRequest.setEnderecoEntrega("Rua de Entrega, 456");
        pedidoRequest.setObservacoes("Sem cebola");
        pedidoRequest.setItens(Arrays.asList(itemRequest));
    }

    @Nested
    @DisplayName("Criar Pedido")
    class CriarPedido {

        @Test
        @DisplayName("Deve criar pedido com dados válidos")
        void deveCriarPedidoComDadosValidos() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(itemPedidoRepository.saveAll(any())).thenReturn(Arrays.asList());

            PedidoResponseDTO resultado = pedidoService.criarPedido(pedidoRequest);

            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            assertEquals("João Silva", resultado.getClienteNome());
            assertEquals("Restaurante Teste", resultado.getRestauranteNome());
            assertEquals("Rua de Entrega, 456", resultado.getEnderecoEntrega());

            verify(clienteRepository).findById(1L);
            verify(restauranteRepository).findById(1L);
            verify(produtoRepository).findById(1L);
            verify(pedidoRepository, times(2)).save(any(Pedido.class));
            verify(itemPedidoRepository).saveAll(any());
        }

        @Test
        @DisplayName("Deve lançar exceção para cliente não encontrado")
        void deveLancarExcecaoParaClienteNaoEncontrado() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertEquals("Cliente não encontrado", exception.getMessage());
            verify(clienteRepository).findById(1L);
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção para restaurante não encontrado")
        void deveLancarExcecaoParaRestauranteNaoEncontrado() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertEquals("Restaurante não encontrado", exception.getMessage());
            verify(restauranteRepository).findById(1L);
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção para restaurante inativo")
        void deveLancarExcecaoParaRestauranteInativo() {
            restaurante.setAtivo(false);
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertEquals("Restaurante não está ativo", exception.getMessage());
            verify(pedidoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção para produto não encontrado")
        void deveLancarExcecaoParaProdutoNaoEncontrado() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
            when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertTrue(exception.getMessage().contains("Produto não encontrado"));
            verify(produtoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção para produto indisponível")
        void deveLancarExcecaoParaProdutoIndisponivel() {
            produto.setDisponivel(false);
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertTrue(exception.getMessage().contains("Produto não disponível"));
            verify(produtoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção para produto de restaurante diferente")
        void deveLancarExcecaoParaProdutoDeRestauranteDiferente() {
            Restaurante outroRestaurante = new Restaurante();
            outroRestaurante.setId(2L);
            produto.setRestaurante(outroRestaurante);

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoService.criarPedido(pedidoRequest));

            assertTrue(exception.getMessage().contains("Produto não pertence ao restaurante selecionado"));
            verify(produtoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve calcular valor total corretamente")
        void deveCalcularValorTotalCorretamente() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            
            // Mock para retornar o próprio argumento quando save for chamado
            when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
                Pedido pedidoArgumento = invocation.getArgument(0);
                if (pedidoArgumento.getId() == null) {
                    pedidoArgumento.setId(1L); // Simular que o banco gerou o ID
                }
                return pedidoArgumento;
            });
            
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
            when(itemPedidoRepository.saveAll(any())).thenReturn(Arrays.asList());

            PedidoResponseDTO resultado = pedidoService.criarPedido(pedidoRequest);

            // Verificar que save foi chamado duas vezes
            verify(pedidoRepository, times(2)).save(any(Pedido.class));
            
            // Verificar que o resultado não é null e tem o valor total correto
            assertNotNull(resultado);
            BigDecimal valorEsperado = produto.getPreco().multiply(new BigDecimal(itemRequest.getQuantidade()));
            assertEquals(valorEsperado, resultado.getValorTotal());
            
            verify(produtoRepository).findById(1L);
            verify(itemPedidoRepository).saveAll(any());
        }
    }

    @Nested
    @DisplayName("Listar Pedidos")
    class ListarPedidos {

        @Test
        @DisplayName("Deve listar todos os pedidos")
        void deveListarTodosOsPedidos() {
            when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido));
            when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(Collections.emptyList());

            List<PedidoResponseDTO> resultado = pedidoService.listarTodosPedidos();

            assertEquals(1, resultado.size());
            assertEquals(1L, resultado.get(0).getId());
            verify(pedidoRepository).findAll();
        }

        @Test
        @DisplayName("Deve listar pedidos do cliente logado")
        void deveListarPedidosDoClienteLogado() {
            try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
                securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
                when(pedidoRepository.findByClienteId(1L)).thenReturn(Arrays.asList(pedido));
                when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(Collections.emptyList());

                List<PedidoResponseDTO> resultado = pedidoService.listarPedidosDoCliente();

                assertEquals(1, resultado.size());
                verify(pedidoRepository).findByClienteId(1L);
            }
        }

        @Test
        @DisplayName("Deve listar pedidos do restaurante logado")
        void deveListarPedidosDoRestauranteLogado() {
            try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
                securityUtilsMock.when(SecurityUtils::getCurrentUserRestauranteId).thenReturn(1L);
                when(pedidoRepository.findByRestauranteId(1L)).thenReturn(Arrays.asList(pedido));
                when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(Collections.emptyList());

                List<PedidoResponseDTO> resultado = pedidoService.listarPedidosDoRestaurante();

                assertEquals(1, resultado.size());
                verify(pedidoRepository).findByRestauranteId(1L);
            }
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não está associado a restaurante")
        void deveLancarExcecaoQuandoUsuarioNaoEstaAssociadoARestaurante() {
            try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
                securityUtilsMock.when(SecurityUtils::getCurrentUserRestauranteId).thenReturn(null);

                RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> pedidoService.listarPedidosDoRestaurante());

                assertEquals("Usuário não está associado a um restaurante", exception.getMessage());
                verify(pedidoRepository, never()).findByRestauranteId(any());
            }
        }
    }

    @Nested
    @DisplayName("Relatórios")
    class Relatorios {

        @Test
        @DisplayName("Deve gerar relatório de vendas por restaurante")
        void deveGerarRelatorioVendasPorRestaurante() {
            when(pedidoRepository.findVendasPorRestaurante(any(), any())).thenReturn(Collections.emptyList());

            var resultado = pedidoService.relatorioVendasPorRestaurante(
                java.time.LocalDate.now(),
                java.time.LocalDate.now()
            );

            assertNotNull(resultado);
            verify(pedidoRepository).findVendasPorRestaurante(any(), any());
        }
    }
}