-- Inserir dados de teste para relatórios

-- Clientes
INSERT INTO cliente (nome, email, telefone, endereco, ativo, data_cadastro) VALUES
('João Silva', 'joao@email.com', '11999999999', 'Rua A, 123', true, '2024-01-01 10:00:00'),
('Maria Santos', 'maria@email.com', '11888888888', 'Rua B, 456', true, '2024-01-02 11:00:00'),
('Pedro Oliveira', 'pedro@email.com', '11777777777', 'Rua C, 789', true, '2024-01-03 12:00:00');

-- Restaurantes
INSERT INTO restaurante (nome, categoria, endereco, telefone, taxa_entrega, tempo_entrega_min, ativo, data_cadastro) VALUES
('Pizza Express', 'Italiana', 'Av. Principal, 100', '11333311111', 5.00, 30, true, '2024-01-01 09:00:00'),
('Burger House', 'Americana', 'Rua das Flores, 200', '11444422222', 3.50, 25, true, '2024-01-01 09:30:00'),
('Sushi Zen', 'Japonesa', 'Av. Central, 300', '11555533333', 8.00, 40, true, '2024-01-01 10:00:00');

-- Produtos
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
('Pizza Margherita', 'Pizza tradicional com molho de tomate, mussarela e manjericão', 35.90, 'Pizza', true, 1),
('Pizza Pepperoni', 'Pizza com molho de tomate, mussarela e pepperoni', 42.90, 'Pizza', true, 1),
('Refrigerante 350ml', 'Coca-Cola 350ml', 4.50, 'Bebida', true, 1),
('Hambúrguer Clássico', 'Hambúrguer com carne, alface, tomate e queijo', 25.90, 'Hambúrguer', true, 2),
('Batata Frita', 'Porção de batata frita crocante', 12.90, 'Acompanhamento', true, 2),
('Sushi Combo', '12 peças de sushi variados', 45.90, 'Sushi', true, 3),
('Temaki Salmão', 'Temaki de salmão com cream cheese', 18.90, 'Temaki', true, 3);

-- Pedidos de Janeiro 2024
INSERT INTO pedido (cliente_id, restaurante_id, status, data_pedido, valor_total, endereco_entrega, observacoes) VALUES
(1, 1, 'ENTREGUE', '2024-01-15 12:30:00', 83.30, 'Rua A, 123', 'Sem cebola'),
(2, 1, 'ENTREGUE', '2024-01-16 19:45:00', 47.40, 'Rua B, 456', NULL),
(3, 2, 'ENTREGUE', '2024-01-17 20:15:00', 38.80, 'Rua C, 789', 'Bem passado'),
(1, 3, 'ENTREGUE', '2024-01-18 21:00:00', 64.80, 'Rua A, 123', NULL),
(2, 2, 'ENTREGUE', '2024-01-19 18:30:00', 51.70, 'Rua B, 456', NULL),
(3, 1, 'ENTREGUE', '2024-01-20 13:15:00', 114.70, 'Rua C, 789', 'Pizza bem assada'),
(1, 3, 'ENTREGUE', '2024-01-25 19:20:00', 45.90, 'Rua A, 123', NULL),
(2, 1, 'ENTREGUE', '2024-01-28 20:45:00', 78.80, 'Rua B, 456', NULL);

-- Itens dos pedidos
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal) VALUES
-- Pedido 1: Pizza Margherita (2x) + Pizza Pepperoni (1x) + Refrigerante (1x)
(1, 1, 2, 35.90, 71.80),
(1, 2, 1, 42.90, 42.90),
(1, 3, 2, 4.50, 9.00),

-- Pedido 2: Pizza Margherita (1x) + Refrigerante (1x)
(2, 1, 1, 35.90, 35.90),
(2, 3, 1, 4.50, 4.50),

-- Pedido 3: Hambúrguer Clássico (1x) + Batata Frita (1x)
(3, 4, 1, 25.90, 25.90),
(3, 5, 1, 12.90, 12.90),

-- Pedido 4: Sushi Combo (1x) + Temaki Salmão (1x)
(4, 6, 1, 45.90, 45.90),
(4, 7, 1, 18.90, 18.90),

-- Pedido 5: Hambúrguer Clássico (2x) + Batata Frita (2x)
(5, 4, 2, 25.90, 51.80),
(5, 5, 2, 12.90, 25.80),

-- Pedido 6: Pizza Margherita (2x) + Pizza Pepperoni (1x)
(6, 1, 2, 35.90, 71.80),
(6, 2, 1, 42.90, 42.90),

-- Pedido 7: Sushi Combo (1x)
(7, 6, 1, 45.90, 45.90),

-- Pedido 8: Pizza Margherita (2x) + Refrigerante (1x)
(8, 1, 2, 35.90, 71.80),
(8, 3, 1, 4.50, 4.50);