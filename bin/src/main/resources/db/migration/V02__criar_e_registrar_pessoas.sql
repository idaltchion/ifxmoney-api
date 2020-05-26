CREATE TABLE pessoas (
	codigo BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
	nome VARCHAR(60) NOT NULL,
	ativo INT NOT NULL,
	logradouro VARCHAR(40),
	numero VARCHAR(20),
	complemento VARCHAR(40),
	cep VARCHAR(10),
	bairro VARCHAR(40),
	cidade VARCHAR(40),
	estado VARCHAR(20)
) ENGINE = InnoDB DEFAULT charset=utf8;

INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Ericka Manross', 1, 'Rua Francisco Schaffer', 's/n', null, '81.435-240', 'Vista Alegre', 'Curitiba', 'PR');
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Perry Blakeman', 1, 'Avenida Manoel Ribas', '35', 'próximo a BR-277', '80.193-290', 'Bigorrilho', 'Curitiba', 'PR');
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Elsy Gidley', 0, 'Largo General Osório', '66', null, '01213-010', 'Santa Ifigênia', 'São Paulo', 'SP'); 
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Vilma Pantano', 1, 'Avenida Nazaré', '481', null, '04263-000', 'Ipiranga', 'São Paulo', 'SP');
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Lenna Millman', 1, 'Praia de Botafogo', '375', 'próximo ao shopping', '22250-040', 'Botafogo', 'Rio de Janeiro', 'RJ');
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Trinidad Dallas', 1, null, null, null, null, null, null, null );
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Monroe Sparks', 1, 'Rua Professor Joca Vieira', '46', null, '64048-301', 'Fátima', 'Teresina', 'PI'); 
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Barbie Whelpley', 0, 'Avenida Afrânio de Melo Franco', '290', 'próximo ao Mercado Capital', '22430-060', 'Leblon', 'Rio de Janeiro', 'RJ'); 
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Jorge Stroble', 1, 'Rua Inhauma', '196', null, '69028-323', 'Flores', 'Manaus', 'AM'); 
INSERT INTO pessoas (nome,ativo,logradouro,numero,complemento,cep,bairro,cidade,estado) VALUES ('Tai Mcnett', 0, null, null, null, null, null, null, null); 