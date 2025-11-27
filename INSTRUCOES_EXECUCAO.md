# 🎯 Instruções de Execução - Testes Delivery API

## 📋 Resumo da Implementação

### ✅ O que foi implementado:

1. **Configuração de Testes** (`pom.xml`)
   - Plugin JaCoCo para cobertura de código
   - Meta de cobertura mínima de 80%
   - Configurações de relatórios

2. **Arquivo de Configuração de Teste** (`src/test/resources/application-test.properties`)
   - Banco H2 em memória para testes
   - Configurações otimizadas para ambiente de teste
   - Logs reduzidos para melhor performance

3. **ClienteService** (`src/main/java/com/deliverytech/delivery/service/ClienteService.java`)
   - Service completo para operações CRUD de Cliente
   - Validações de negócio (email duplicado, etc.)
   - Métodos para busca, listagem e ativação/desativação

4. **ClienteController** (`src/main/java/com/deliverytech/delivery/controller/ClienteController.java`)
   - REST Controller com todas as operações
   - Documentação Swagger/OpenAPI
   - Tratamento de exceções e códigos HTTP apropriados
   - Controle de acesso por roles

5. **Testes Unitários**:
   - **ClienteServiceTest**: 15+ cenários de teste cobrindo CRUD completo
   - **PedidoServiceTest**: 12+ cenários testando criação de pedidos e validações

6. **Testes de Integração**:
   - **ClienteControllerIT**: 20+ cenários testando endpoints HTTP
   - **PedidoControllerIT**: 15+ cenários testando API de pedidos

7. **Documentação Completa**:
   - `README_TESTES.md`: Documentação detalhada da estratégia de testes
   - Exemplos de uso, boas práticas e comandos

## 🚀 Como Executar os Testes

### Pré-requisitos
```bash
# Verificar se Java está instalado
java -version

# Verificar se Maven está instalado
mvn -version
```

### Comandos de Execução

#### 1. Executar Todos os Testes
```bash
# Executar suite completa de testes
mvn clean test

# Executar com perfil de teste específico
mvn clean test -Dspring.profiles.active=test
```

#### 2. Executar Testes Específicos
```bash
# Apenas testes unitários
mvn test -Dtest=ClienteServiceTest
mvn test -Dtest=PedidoServiceTest

# Apenas testes de integração
mvn test -Dtest=ClienteControllerIT
mvn test -Dtest=PedidoControllerIT

# Teste específico
mvn test -Dtest=ClienteServiceTest#deveSalvarClienteComDadosValidos
```

#### 3. Gerar Relatório de Cobertura
```bash
# Executar testes e gerar relatório JaCoCo
mvn clean test jacoco:report

# Visualizar relatório
# Linux/Mac: open target/site/jacoco/index.html
# Windows: start target/site/jacoco/index.html
```

## 📊 Resultados Esperados

### Estrutura de Testes Implementada

```
src/test/java/
├── com/deliverytech/delivery/
│   ├── service/
│   │   ├── ClienteServiceTest.java      (15+ testes)
│   │   └── PedidoServiceTest.java       (12+ testes)
│   └── controller/
│       ├── ClienteControllerIT.java     (20+ testes)
│       └── PedidoControllerIT.java      (15+ testes)
└── resources/
    └── application-test.properties
```

### Cobertura de Teste

| Componente | Cenários Testados | Cobertura Esperada |
|------------|-------------------|-------------------|
| ClienteService | CRUD completo + validações | > 90% |
| PedidoService | Criação + validações + cálculos | > 85% |
| ClienteController | Todos endpoints + HTTP status | > 90% |
| PedidoController | Endpoints + autenticação/autorização | > 85% |

### Cenários Críticos Cobertos

#### ✅ ClienteService
- Salvar cliente com dados válidos
- Validação de email duplicado
- Busca por ID (existente/inexistente)
- Listagem paginada
- Atualização com validações
- Ativação/Desativação
- Busca por nome

#### ✅ PedidoService  
- Criação de pedido válido
- Validação de cliente inexistente
- Validação de restaurante inexistente/inativo
- Validação de produto inexistente/indisponível
- Cálculo correto de valor total
- Listagem por perfil de usuário
- Relatórios de vendas

#### ✅ ClienteController
- POST /api/clientes (201, 400, 409)
- GET /api/clientes/{id} (200, 404)
- GET /api/clientes (200 com paginação)
- PUT /api/clientes/{id} (200, 404, 409)
- PUT /api/clientes/{id}/desativar (200, 404)
- Autenticação/Autorização (401, 403)

#### ✅ PedidoController
- POST /api/pedidos (201, 400, 401, 403)
- GET /api/pedidos/* (200, 403)
- Validações de segurança por role
- Relatórios com parâmetros

## 🔧 Solução de Problemas

### Erros Comuns

#### 1. Falha de Autenticação nos Testes
```
Erro: 401 Unauthorized
Solução: Verificar se o token JWT está sendo gerado corretamente
```

#### 2. Banco H2 não Configurado
```
Erro: Could not create connection to database
Solução: Verificar application-test.properties
```

#### 3. Testes Intermitentes
```
Erro: Testes passam/falham aleatoriamente
Solução: Verificar @DirtiesContext e isolamento de dados
```

### Debug de Testes

#### Habilitar Logs Detalhados
```bash
# Executar com logs debug
mvn test -Dtest=ClienteServiceTest -Dlogging.level.com.deliverytech=DEBUG

# Ver SQL gerado pelo Hibernate
mvn test -Dlogging.level.org.hibernate.SQL=DEBUG
```

#### Executar Teste Individual
```bash
# Para debuggar um teste específico
mvn test -Dtest=ClienteServiceTest#deveSalvarClienteComDadosValidos
```

## 📈 Métricas de Qualidade

### Critérios de Sucesso
- [ ] Todos os testes passam (0 failures)
- [ ] Cobertura de código ≥ 80%
- [ ] Tempo de execução < 2 minutos
- [ ] 0 warnings de segurança
- [ ] Todas as validações de negócio testadas

### Relatórios Gerados
- `target/surefire-reports/`: Relatórios XML dos testes
- `target/site/jacoco/`: Relatório HTML de cobertura
- Console: Sumário de execução e falhas

## 🎯 Validação dos Requisitos

### ✅ Atividade 1: Testes Unitários
- [x] Dependências JUnit 5, Mockito configuradas
- [x] Perfil de teste criado
- [x] ClienteServiceTest implementado com cenários completos
- [x] PedidoServiceTest implementado com validações
- [x] Uso correto de @Mock, @InjectMocks, verify()
- [x] Cenários positivos e negativos
- [x] Tratamento de exceções

### ✅ Atividade 2: Testes de Integração
- [x] @SpringBootTest e @AutoConfigureMockMvc configurados
- [x] Banco H2 em memória configurado
- [x] ClienteControllerIT implementado
- [x] PedidoControllerIT implementado
- [x] Validação de códigos HTTP (200, 201, 400, 404, 409)
- [x] Validação de estrutura JSON
- [x] Isolamento adequado entre testes
- [x] Autenticação/Autorização testada

### ✅ Entregáveis
- [x] Código fonte completo dos testes
- [x] Configurações de teste
- [x] README_TESTES.md detalhado
- [x] Instruções de execução

## 📞 Próximos Passos

1. **Executar os Testes**:
   ```bash
   mvn clean test
   ```

2. **Verificar Cobertura**:
   ```bash
   mvn test jacoco:report
   ```

3. **Analisar Resultados**:
   - Abrir relatório JaCoCo
   - Verificar falhas se houver
   - Validar métricas de cobertura

4. **Melhorias Contínuas**:
   - Adicionar mais cenários se necessário
   - Otimizar performance dos testes
   - Implementar testes E2E com TestContainers

---

**Nota**: Esta implementação segue as melhores práticas de testing com Spring Boot, garantindo qualidade, confiabilidade e manutenibilidade do código. Os testes cobrem cenários críticos de negócio e casos extremos, proporcionando confiança para refatorações e novas funcionalidades.