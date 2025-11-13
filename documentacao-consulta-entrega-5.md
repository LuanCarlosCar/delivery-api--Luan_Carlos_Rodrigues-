# Documentação - Entrega 5
## Endpoint de Consulta de Restaurantes com Filtros

### Criação do Endpoint

Foi implementado um novo endpoint para consulta de restaurantes com suporte a filtros e paginação.

### Endpoint

```
GET /api/restaurantes
```

### Parâmetros de Query

| Parâmetro | Tipo | Obrigatório | Descrição | Exemplo |
|-----------|------|-------------|-----------|---------|
| `categoria` | String | Não | Filtro por categoria do restaurante | `Italiana` |
| `ativo` | Boolean | Não | Filtro por status ativo/inativo | `true` |
| `page` | Integer | Não | Número da página (padrão: 0) | `0` |
| `size` | Integer | Não | Itens por página (padrão: 10) | `10` |

### Como Usar

#### URL de Exemplo
```
http://localhost:8080/api/restaurantes?categoria=Italiana&ativo=true&page=0&size=10
```

#### Exemplos de Uso

1. **Buscar todos os restaurantes (primeira página)**
   ```
   GET /api/restaurantes
   ```

2. **Filtrar por categoria**
   ```
   GET /api/restaurantes?categoria=Italiana
   ```

3. **Filtrar apenas restaurantes ativos**
   ```
   GET /api/restaurantes?ativo=true
   ```

4. **Buscar com paginação**
   ```
   GET /api/restaurantes?page=1&size=5
   ```

5. **Combinação de filtros**
   ```
   GET /api/restaurantes?categoria=Italiana&ativo=true&page=0&size=10
   ```

### Resposta

A resposta é um objeto paginado com as seguintes propriedades:

```json
{
    "content": [
        {
            "id": 1,
            "nome": "Pizza Express",
            "categoria": "Italiana",
            "endereco": "Av. Principal, 100",
            "telefone": "(11) 3333-1111",
            "taxaEntrega": 5.00,
            "tempoEntregaMin": 30,
            "ativo": true,
            "dataCadastro": "2025-11-13T19:21:46.120504"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "first": true,
    "numberOfElements": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "empty": false
}
```