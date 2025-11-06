# Documentação de Consultas SQL - Entrega 2

## Análise das Consultas Executadas

### 1. Formatação e Log de Queries

O sistema agora está configurado para exibir:
- **SQL formatado** com indentação e quebras de linha
- **Parâmetros das consultas** com valores específicos
- **Métricas de performance** detalhadas
- **Comentários SQL** identificando origem das queries

### 2. Consultas Identificadas no Log

#### 2.1 Consulta de Clientes
```sql
/* SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId */
select p1_0.id, p1_0.cliente_id, p1_0.data_pedido, p1_0.endereco_entrega, 
       p1_0.observacoes, p1_0.restaurante_id, p1_0.status, p1_0.valor_total
from pedido p1_0
where p1_0.cliente_id=?
```
**Parâmetro:** `binding parameter (1:BIGINT) <- [1]`

#### 2.2 Consulta de Restaurantes
```sql
/* <criteria> */
select r1_0.id, r1_0.ativo, r1_0.categoria, r1_0.data_cadastro, 
       r1_0.endereco, r1_0.nome, r1_0.taxa_entrega, r1_0.telefone, 
       r1_0.tempo_entrega_min
from restaurante r1_0
```

#### 2.3 Consulta de Produtos por Restaurante
```sql
/* SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId */
select p1_0.id, p1_0.categoria, p1_0.descricao, p1_0.disponivel, 
       p1_0.nome, p1_0.preco, p1_0.restaurante_id
from produto p1_0
where p1_0.restaurante_id=?
```
**Parâmetros observados:** `[1]`, `[2]`

#### 2.4 Consulta de Itens de Pedido
```sql
/* <criteria> */
select ip1_0.id, ip1_0.pedido_id, ip1_0.preco_unitario, ip1_0.produto_id, 
       ip1_0.quantidade, ip1_0.subtotal
from item_pedido ip1_0
left join pedido p1_0 on p1_0.id=ip1_0.pedido_id
where p1_0.id=?
```

### 3. Métricas de Performance

#### 3.1 Exemplo de Métricas Capturadas
```
Session Metrics {
    31500 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    91400 nanoseconds spent preparing 1 JDBC statements;
    274200 nanoseconds spent executing 1 JDBC statements;
    0 nanoseconds spent executing 0 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    0 nanoseconds spent performing 0 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
}
```

#### 3.2 Análise de Performance por Query
- **HQL Criteria queries**: ~1-2ms de execução
- **Queries com JOIN**: ~9-22ms de execução 
- **Queries simples (COUNT)**: ~0ms de execução

### 4. Problemas de Performance Identificados

#### 4.1 Problema N+1
**Observado em:** Carregamento de produtos por restaurante
- Query principal busca restaurantes
- Para cada restaurante, executa query separada para produtos
- **Impacto:** 2 restaurantes = 3 queries (1 + 2)

#### 4.2 Lazy Loading Excessivo
**Observado em:** 
```sql
select r1_0.id, r1_0.ativo, r1_0.categoria, ...
from restaurante r1_0
where r1_0.id=?
```
Múltiplas queries para buscar o mesmo restaurante quando já foi carregado.

#### 4.3 Queries com Múltiplos JOINs
```sql
select p1_0.id, p1_0.cliente_id, c1_0.id, c1_0.ativo, ..., 
       r1_0.id, r1_0.ativo, r1_0.categoria, ...
from pedido p1_0
join cliente c1_0 on c1_0.id=p1_0.cliente_id
join restaurante r1_0 on r1_0.id=p1_0.restaurante_id
where p1_0.id=?
```

### 5. Configurações Implementadas

#### 5.1 Logging Configuration
```properties
# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true
spring.jpa.properties.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Logging levels
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.org.hibernate.orm.jdbc.bind=TRACE
logging.level.org.hibernate.stat=DEBUG

# Performance monitoring
spring.jpa.properties.hibernate.generate_statistics=true
```

### 6. DTOs e Projections Criadas

#### 6.1 DTOs Implementados
- `ClienteDTO`: Dados essenciais do cliente
- `RestauranteDTO`: Informações do restaurante
- `PedidoDTO`: Dados do pedido com nomes de cliente e restaurante

#### 6.2 Projection Interfaces
- `ClienteReportProjection`: Relatórios de clientes com total de pedidos
- `RestauranteReportProjection`: Estatísticas de restaurantes
- `PedidoReportProjection`: Dados de pedidos para relatórios
- `VendasReportProjection`: Relatórios de vendas por período

### 7. Queries de Relatório Implementadas

#### 7.1 Clientes com Total de Pedidos
```sql
SELECT c.id as id, c.nome as nome, c.email as email, c.ativo as ativo,
       c.dataCadastro as dataCadastro, COUNT(p.id) as totalPedidos
FROM Cliente c LEFT JOIN Pedido p ON c.id = p.cliente.id
GROUP BY c.id, c.nome, c.email, c.ativo, c.dataCadastro
```

#### 7.2 Restaurantes com Estatísticas
```sql
SELECT r.id as id, r.nome as nome, r.categoria as categoria, 
       r.taxaEntrega as taxaEntrega, r.tempoEntregaMin as tempoEntregaMin,
       r.ativo as ativo, r.dataCadastro as dataCadastro,
       COUNT(p.id) as totalPedidos, COALESCE(SUM(p.valorTotal), 0) as receitaTotal
FROM Restaurante r LEFT JOIN Pedido p ON r.id = p.restaurante.id
GROUP BY r.id, r.nome, r.categoria, r.taxaEntrega, r.tempoEntregaMin, r.ativo, r.dataCadastro
```

### 8. Dados de Teste Carregados

- **Clientes:** 3 registros
- **Restaurantes:** 2 registros  
- **Produtos:** 5 registros
- **Pedidos:** 2 registros
- **Itens de Pedido:** 5 registros

---
**Data de Análise:** 06/11/2025  
**Status:** ✅ Implementação concluída com sucesso