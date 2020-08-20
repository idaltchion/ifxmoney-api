CREATE TABLE contato (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codigo_pessoa BIGINT(20) NOT NULL,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	telefone VARCHAR(20) NOT NULL,
	FOREIGN KEY (codigo_pessoa) REFERENCES pessoas(codigo)
) ENGINE=InnoDB DEFAULT charset=utf8;


INSERT INTO contato (codigo, codigo_pessoa, nome, email, telefone) VALUES (1, 1, "Fernanda Coutinha", "fernanda.coutinho@email.com", "55 3248-2917");