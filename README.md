# Delivery Tech API 🚀

Sistema completo de delivery de comida desenvolvido com Spring Boot e Java 21, oferecendo uma API REST robusta para gerenciamento de restaurantes, produtos, pedidos e relatórios de vendas.

## 📋 Sobre o Projeto

O **Delivery Tech API** é uma aplicação backend moderna que simula um sistema real de delivery, com funcionalidades completas para:

- **Gestão de Restaurantes**: Cadastro, consulta com filtros e paginação
- **Catálogo de Produtos**: Produtos organizados por restaurante com controle de disponibilidade
- **Sistema de Pedidos**: Criação e gerenciamento de pedidos com validações de negócio
- **Relatórios de Vendas**: Análises detalhadas de vendas por restaurante com métricas de performance
- **Documentação Interativa**: Interface Swagger para testes e documentação da API
- **Testes Automatizados**: Suite completa com 60+ testes e cobertura 80%+

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 21 LTS** - Última versão LTS com recursos modernos
- **Spring Boot 3.5.7** - Framework principal
- **Spring Web** - API REST
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM com otimizações de performance
- **Maven** - Gerenciamento de dependências

### Database & Tools
- **H2 Database** - Banco em memória para desenvolvimento
- **SpringDoc OpenAPI 3** - Documentação Swagger automatizada
- **Spring DevTools** - Hot reload para desenvolvimento

### Testing & Quality
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Mocking para testes isolados
- **Spring Boot Test** - Testes de integração
- **JaCoCo** - Relatórios de cobertura de código
- **MockMvc** - Testes de endpoints HTTP

## ⚡ Recursos Modernos do Java 21

- **Records** - DTOs imutáveis e limpos
- **Text Blocks** - Queries SQL mais legíveis
- **Pattern Matching** - Lógica condicional moderna
- **Virtual Threads** - Performance otimizada
- **Switch Expressions** - Código mais conciso

## 🏗️ Arquitetura

```
src/main/java/com/deliverytech/delivery/
├── config/          # Configurações (Swagger, Performance, DataLoader)
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── model/          # Entidades JPA
├── projection/     # Interfaces para consultas otimizadas
├── repository/     # Repositórios JPA
└── service/        # Lógica de negócio
```

## 🚀 Como Executar

### Pré-requisitos
- **JDK 21** instalado
- **Maven 3.6+** (ou usar o wrapper incluído)

### Execução
```bash
# Clone o repositório
git clone <repository-url>
cd delivery-api

# Execute a aplicação
./mvnw spring-boot:run

```

### Acesso
- **API Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:deliverydb`
  - User: `sa`
  - Password: (vazio)

## 📋 Endpoints Principais

### Restaurantes
```http
GET /api/restaurantes                          # Lista com filtros e paginação
GET /api/restaurantes/{id}/produtos            # Produtos do restaurante
```

### Pedidos
```http
POST /api/pedidos                              # Criar novo pedido
```

### Relatórios
```http
GET /api/relatorios/vendas-por-restaurante     # Relatório de vendas por período
```

### Utilitários
```http
GET /health                                    # Status da aplicação
GET /h2-console                               # Interface do banco H2
GET /swagger-ui.html                          # Documentação interativa
```

## 🔧 Configurações

### Banco de Dados
- **Tipo**: H2 (em memória)
- **URL**: `jdbc:h2:mem:deliverydb`
- **Inicialização**: Automática com dados de exemplo
- **Console Web**: Habilitado para desenvolvimento

### Performance
- **Logs SQL**: Habilitados com formatação
- **Estatísticas Hibernate**: Ativas para monitoramento
- **Connection Pool**: Configurado para desenvolvimento

### Desenvolvimento
- **Hot Reload**: Habilitado via DevTools
- **Porta**: 8080
- **Profile**: Development

## 📊 Funcionalidades Detalhadas

### 🍽️ Gestão de Restaurantes
- Listagem com filtros por categoria e status
- Paginação configurável
- Dados completos incluindo taxa de entrega e tempo estimado

### 🛒 Sistema de Pedidos
- Validações de negócio completas
- Cálculo automático de valores
- Controle de disponibilidade de produtos
- Histórico completo de pedidos

### 📈 Relatórios de Vendas
- Vendas agrupadas por restaurante
- Métricas: total de pedidos, receita total, ticket médio
- Filtros por período personalizado
- Ordenação por performance

### 📚 Documentação Swagger
- Interface interativa para todos os endpoints
- Exemplos de requisições e respostas
- Validações e tipos de dados documentados

## 🗄️ Modelo de Dados

### Entidades Principais
- **Cliente**: Dados pessoais e histórico
- **Restaurante**: Informações, categoria e produtos
- **Produto**: Catálogo com preços e disponibilidade
- **Pedido**: Transações com itens e valores
- **ItemPedido**: Detalhes de cada produto no pedido

### Relacionamentos
- Cliente → Pedidos (1:N)
- Restaurante → Produtos (1:N)
- Restaurante → Pedidos (1:N)
- Pedido → ItemsPedido (1:N)
- Produto → ItemsPedido (1:N)

## 📈 Métricas e Monitoramento

- **Logs SQL**: Queries executadas com parâmetros
- **Estatísticas Hibernate**: Cache hits, queries executadas
- **Performance**: Tempo de resposta dos endpoints
- **Health Check**: Status da aplicação e dependências

## 🧪 Dados de Exemplo

A aplicação inicializa automaticamente com:
- **3 Restaurantes** (Pizza Express, Burger House, Sushi Zen)
- **12 Produtos** distribuídos entre os restaurantes
- **2 Clientes** para testes
- **Pedidos de exemplo** para demonstrar relatórios

## 🧪 Testes Automatizados

### 🎯 **Suíte de Testes Completa**
- ✅ **60+ testes implementados** cobrindo cenários críticos
- ✅ **Cobertura 80%+** com relatórios JaCoCo
- ✅ **Testes unitários** para lógica de negócio  
- ✅ **Testes de integração** para endpoints HTTP
- ✅ **Autenticação JWT** testada
- ✅ **Cenários de erro** e validações

### ⚡ **Execução Rápida**

```bash
# Executar todos os testes
mvn clean test

# Executar com relatório de cobertura
mvn test jacoco:report

# Ver relatório no navegador
open target/site/jacoco/index.html
```

### 📊 **Comandos Específicos**

```bash
# Testes unitários (Services)
mvn test -Dtest="**/*Test"

# Testes de integração (Controllers)
mvn test -Dtest="**/*IT"

# Teste específico
mvn test -Dtest=ClienteServiceTest

# Debug com logs detalhados
mvn test -Dtest=PedidoServiceTest -Dlogging.level.com.deliverytech=DEBUG
```

### 📋 **Estrutura de Testes**

```
src/test/java/
├── service/
│   ├── ClienteServiceTest.java     (15+ testes unitários)
│   └── PedidoServiceTest.java      (12+ testes unitários)
└── controller/
    ├── ClienteControllerIT.java    (20+ testes integração)
    └── PedidoControllerIT.java     (15+ testes integração)
```

### 🎯 **Cobertura de Testes**

| Componente | Cenários | Cobertura |
|------------|----------|-----------|
| **ClienteService** | CRUD + Validações | 90%+ |
| **PedidoService** | Criação + Cálculos | 85%+ |
| **Controllers** | HTTP + Auth | 85%+ |
| **Security** | JWT + Roles | 80%+ |

**📖 Documentação Completa**: Veja [README_TESTES.md](README_TESTES.md)

## 🚦 Status de Desenvolvimento

- ✅ **CRUD Restaurantes** - Completo com filtros
- ✅ **Catálogo de Produtos** - Por restaurante com filtros
- ✅ **Sistema de Pedidos** - Criação com validações
- ✅ **Relatórios de Vendas** - Por restaurante e período
- ✅ **Documentação Swagger** - Interface completa
- ✅ **Testes Automatizados** - 60+ testes com 80%+ cobertura
- ✅ **Testes de Performance** - Configurações otimizadas

## 👨‍💻 Desenvolvedor

**Luan Carlos Rodrigues Da Costa**  
📧 Email: [seu-email]  
🎓 Universidade: UNA  
🔧 Tecnologias: Java 21, Spring Boot 3.5.7, Maven

---

> 💡 **Dica**: Use a interface Swagger em `/swagger-ui.html` para explorar e testar todos os endpoints de forma interativa!
