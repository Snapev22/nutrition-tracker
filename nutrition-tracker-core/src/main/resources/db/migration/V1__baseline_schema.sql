CREATE TABLE `alimento` (
  `id_alimento` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `proteina` double NOT NULL,
  `carboidrato` double NOT NULL,
  `gordura` double NOT NULL,
  `calorias` double NOT NULL,
  `unidade_medida` varchar(20) NOT NULL DEFAULT 'GRAMAS',
  PRIMARY KEY (`id_alimento`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `aluno` (
  `id_aluno` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `idade` int NOT NULL,
  `peso` double NOT NULL,
  `altura` double NOT NULL,
  `sexo` varchar(10) NOT NULL,
  `fator_atividade` varchar(20) NOT NULL,
  `objetivo` varchar(45) NOT NULL,
  `meta_calorica_estimada` double NOT NULL DEFAULT '0',
  `meta_calorica_definida` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_aluno`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `plano_diario` (
    `id_plano` bigint NOT NULL AUTO_INCREMENT,
    `id_aluno` bigint DEFAULT NULL,
    `data` date NOT NULL,
    `version` bigint NOT NULL DEFAULT '0',
    PRIMARY KEY (`id_plano`),
    KEY `plano_diario_ibfk_1` (`id_aluno`),
    CONSTRAINT `plano_diario_ibfk_1` FOREIGN KEY (`id_aluno`) REFERENCES `aluno` (`id_aluno`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `item_plano` (
  `id_item_plano` bigint NOT NULL AUTO_INCREMENT,
  `quantidade` double NOT NULL,
  `calorias_totais` double NOT NULL DEFAULT '0',
  `id_alimento` bigint DEFAULT NULL,
  `id_plano_diario` bigint DEFAULT NULL,
  `calorias` double NOT NULL DEFAULT '0',
  `proteina` double NOT NULL DEFAULT '0',
  `carboidrato` double NOT NULL DEFAULT '0',
  `gordura` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_item_plano`),
  KEY `id_alimento_idx` (`id_alimento`),
  KEY `fk_item_plano_diario` (`id_plano_diario`),
  CONSTRAINT `fk_item_plano_diario` FOREIGN KEY (`id_plano_diario`) REFERENCES `plano_diario` (`id_plano`),
  CONSTRAINT `item_plano_ifbk_1` FOREIGN KEY (`id_alimento`) REFERENCES `alimento` (`id_alimento`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



