# 🧪 Documentação de Testes - Delivery API

## 📋 Índice
- [Visão Geral](#visão-geral)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Tipos de Testes](#tipos-de-testes)
- [Estrutura dos Testes](#estrutura-dos-testes)
- [Executando os Testes](#executando-os-testes)
- [Cobertura de Código](#cobertura-de-código)
- [Estratégias de Teste](#estratégias-de-teste)
- [Boas Práticas](#boas-práticas)

## 🎯 Visão Geral

Este projeto implementa uma suíte completa de testes automatizados para garantir a qualidade e confiabilidade da API de delivery. A estratégia de testes inclui:

- **Testes Unitários**: Validam componentes isolados (Services)
- **Testes de Integração**: Validam a integração entre camadas (Controllers + Services + Repository)
- **Cobertura de Código**: Meta de 80% de cobertura mínima

### 📊 Estatísticas de Teste

| Categoria | Quantidade | Cobertura |
|-----------|------------|-----------|
| Testes Unitários | 25+ | Services |
| Testes de Integração | 20+ | Controllers |
| Cenários de Erro | 15+ | Exception Handling |

## ⚙️ Configuração do Ambiente

### Dependências de Teste

As seguintes dependências estão configuradas no `pom.xml`:

```xml
<!-- Já incluído no spring-boot-starter-test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- JaCoCo para cobertura de código -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
</plugin>
```

### Configuração de Perfil de Teste

Arquivo `application-test.properties`:
```properties
# Banco de dados H2 em memória
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# Configurações JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# Desabilitar recursos desnecessários
spring.h2.console.enabled=false
springdoc.api-docs.enabled=false
```

## 🧪 Tipos de Testes

### 1. Testes Unitários

#### ClienteServiceTest
**Localização**: `src/test/java/com/deliverytech/delivery/service/ClienteServiceTest.java`

**Cobertura**:
- ✅ Salvar cliente com dados válidos
- ✅ Validação de email duplicado
- ✅ Buscar cliente por ID (existente/inexistente)
- ✅ Listar clientes com paginação
- ✅ Buscar por nome
- ✅ Atualizar cliente
- ✅ Ativar/Desativar cliente
- ✅ Verificar existência de email

**Técnicas Utilizadas**:
- `@ExtendWith(MockitoExtension.class)`
- `@Mock` para dependências
- `@InjectMocks` para classe testada
- `verify()` para validar interações
- Nested classes para organização

#### PedidoServiceTest
**Localização**: `src/test/java/com/deliverytech/delivery/service/PedidoServiceTest.java`

**Cobertura**:
- ✅ Criar pedido com dados válidos
- ✅ Validações de cliente/restaurante/produto
- ✅ Cálculo de valor total
- ✅ Tratamento de produtos indisponíveis
- ✅ Validação de restaurante inativo
- ✅ Listagem de pedidos por perfil
- ✅ Relatórios de vendas

### 2. Testes de Integração

#### ClienteControllerIT
**Localização**: `src/test/java/com/deliverytech/delivery/controller/ClienteControllerIT.java`

**Cobertura**:
- ✅ POST /api/clientes (criação)
- ✅ GET /api/clientes/{id} (busca)
- ✅ GET /api/clientes (listagem paginada)
- ✅ GET /api/clientes/ativos (listagem filtrada)
- ✅ PUT /api/clientes/{id} (atualização)
- ✅ PUT /api/clientes/{id}/desativar (desativação)
- ✅ PUT /api/clientes/{id}/ativar (ativação)
- ✅ Códigos de status HTTP (200, 201, 400, 404, 409)
- ✅ Validação de JSON responses
- ✅ Autenticação e autorização

#### PedidoControllerIT
**Localização**: `src/test/java/com/deliverytech/delivery/controller/PedidoControllerIT.java`

**Cobertura**:
- ✅ POST /api/pedidos (criação de pedido)
- ✅ GET /api/pedidos (listagem admin)
- ✅ GET /api/pedidos/meus (pedidos do cliente)
- ✅ GET /api/pedidos/restaurante (pedidos do restaurante)
- ✅ GET /api/relatorios/vendas-por-restaurante (relatórios)
- ✅ Validações de segurança por role
- ✅ Cenários de erro complexos
- ✅ Validação de cálculos de valor

## 🚀 Executando os Testes

### Comandos Básicos

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=ClienteServiceTest
mvn test -Dtest=PedidoServiceTest
mvn test -Dtest=ClienteControllerIT

# Executar com perfil de teste
mvn test -Dspring.profiles.active=test

# Executar testes e gerar relatório de cobertura
mvn clean test jacoco:report
```

### Testes por Categoria

```bash
# Apenas testes unitários
mvn test -Dtest="**/*Test"

# Apenas testes de integração  
mvn test -Dtest="**/*IT"

# Testes de um pacote específico
mvn test -Dtest="com.deliverytech.delivery.service.*"
mvn test -Dtest="com.deliverytech.delivery.controller.*"
```

### Debug de Testes

```bash
# Executar com logs detalhados
mvn test -Dtest=ClienteServiceTest -Dlogging.level.com.deliverytech=DEBUG

# Executar um teste específico
mvn test -Dtest=ClienteServiceTest#deveSalvarClienteComDadosValidos
```

## 📊 Cobertura de Código

### Configuração JaCoCo

O plugin JaCoCo está configurado para:
- **Cobertura mínima**: 80%
- **Relatórios**: HTML e XML
- **Localização**: `target/site/jacoco/`

### Visualizando Relatórios

```bash
# Gerar relatório
mvn clean test jacoco:report

# Abrir relatório (Linux/Mac)
open target/site/jacoco/index.html

# Abrir relatório (Windows)
start target/site/jacoco/index.html
```

### Interpretando Métricas

| Métrica | Descrição | Meta |
|---------|-----------|------|
| **Instruction Coverage** | Linhas de código executadas | ≥ 80% |
| **Branch Coverage** | Ramificações (if/else) testadas | ≥ 70% |
| **Method Coverage** | Métodos executados | ≥ 90% |
| **Class Coverage** | Classes testadas | ≥ 85% |

## 🎯 Estratégias de Teste

### 1. Pyramid Testing Strategy

```
     🔺 E2E (Poucos)
    🔶🔶 Integration (Médio)
   🟦🟦🟦 Unit Tests (Muitos)
```

### 2. Cenários de Teste

#### ✅ Happy Path (Caminho Feliz)
- Dados válidos
- Operações bem-sucedidas
- Fluxo normal do sistema

#### ⚠️ Edge Cases (Casos Extremos)
- Validações de entrada
- Limites de dados
- Condições especiais

#### ❌ Error Cases (Cenários de Erro)
- Dados inválidos
- Recursos não encontrados
- Violações de regras de negócio
- Falhas de autenticação/autorização

### 3. Test Data Management

#### Dados de Teste Isolados
```java
@BeforeEach
void setUp() {
    // Limpar dados antes de cada teste
    clienteRepository.deleteAll();
    
    // Criar dados específicos para o teste
    cliente = new Cliente("João", "joao@test.com", ...);
}
```

#### Factories e Builders
```java
public class ClienteTestFactory {
    public static Cliente createValidCliente() {
        return new Cliente("João Silva", "joao@email.com", 
                          "11999999999", "Rua Teste, 123");
    }
}
```

## 📏 Boas Práticas

### 1. Nomenclatura de Testes

```java
@Test
@DisplayName("Deve criar cliente com dados válidos")
void deveCriarClienteComDadosValidos() {
    // Given / When / Then
}
```

**Padrão**:
- **deve** + **ação** + **condição**
- Descritivo e em português
- Uso de `@DisplayName` para clareza

### 2. Estrutura AAA (Arrange-Act-Assert)

```java
@Test
void deveCalcularValorTotalCorretamente() {
    // Arrange - Preparar dados
    PedidoRequestDTO request = createValidRequest();
    when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
    
    // Act - Executar ação
    PedidoResponseDTO resultado = pedidoService.criarPedido(request);
    
    // Assert - Verificar resultado
    assertEquals(new BigDecimal("51.80"), resultado.getValorTotal());
    verify(pedidoRepository).save(any(Pedido.class));
}
```

### 3. Isolamento de Testes

```java
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClienteControllerIT {
    // Cada teste terá contexto limpo
}
```

### 4. Mocking Estratégico

```java
// ✅ Mock de dependências externas
@Mock
private ClienteRepository clienteRepository;

// ✅ Verificação de interações
verify(clienteRepository).save(any(Cliente.class));
verify(clienteRepository, never()).delete(any());

// ✅ Configuração de comportamento
when(clienteRepository.existsByEmail("test@email.com")).thenReturn(false);
```

### 5. Assertions Expressivas

```java
// ✅ Assertions claras e específicas
assertThat(resultado.getNome()).isEqualTo("João Silva");
assertThat(resultado.getId()).isNotNull();
assertThat(resultado.getAtivo()).isTrue();

// ✅ Verificações de coleções
assertThat(clientes).hasSize(2);
assertThat(clientes).extracting("email")
    .containsExactly("joao@email.com", "maria@email.com");
```

### 6. Testes de Exceções

```java
@Test
void deveLancarExcecaoParaEmailDuplicado() {
    // Arrange
    when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);
    
    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> clienteService.salvarCliente(clienteDTO));
    
    assertEquals("Email já está em uso", exception.getMessage());
}
```

## 🔍 Debugging de Testes

### 1. Logs de Depuração

```properties
# application-test.properties
logging.level.com.deliverytech.delivery=DEBUG
logging.level.org.springframework.test=INFO
```

### 2. Prints Temporários

```java
@Test
void debugTest() {
    System.out.println("Cliente: " + cliente);
    System.out.println("Request: " + objectMapper.writeValueAsString(request));
    // Remover após debug
}
```

### 3. Breakpoints em IDE

- Configurar breakpoints nos testes
- Executar em modo debug
- Inspecionar variáveis e estado

## 📈 Métricas de Qualidade

### Critérios de Aceitação

#### ✅ Cobertura
- Instrução: ≥ 80%
- Branch: ≥ 70%
- Método: ≥ 90%

#### ✅ Performance
- Testes unitários: < 100ms cada
- Testes integração: < 5s cada
- Suite completa: < 2 minutos

#### ✅ Manutenibilidade
- Nomes descritivos
- Setup/teardown adequado
- Dados isolados
- Assertions claras

## 🚀 Próximos Passos

### 1. Expansão de Testes
- [ ] Testes de performance/carga
- [ ] Testes de segurança
- [ ] Testes de API com TestContainers
- [ ] Testes de mutação

### 2. Automação
- [ ] Pipeline CI/CD com testes
- [ ] Relatórios automáticos
- [ ] Quality gates
- [ ] Notificações de falhas

### 3. Ferramentas Adicionais
- [ ] ArchUnit para testes arquiteturais
- [ ] WireMock para mocks de API
- [ ] TestContainers para testes reais
- [ ] Mutation testing

---

## 📞 Suporte

Para dúvidas sobre os testes:
1. Consulte esta documentação
2. Verifique os exemplos nos arquivos de teste
3. Execute `mvn test -Dtest=NomeDoTeste -X` para debug
4. Analise os logs de execução

**Lembre-se**: Testes são investimento na qualidade e confiabilidade do sistema! 🎯