CREATE TABLE usuario (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(60) NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE aluno ADD COLUMN id_usuario BIGINT NULL;
ALTER TABLE aluno ADD CONSTRAINT fk_aluno_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);