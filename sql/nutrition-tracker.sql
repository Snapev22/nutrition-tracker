CREATE DATABASE  IF NOT EXISTS `fittech_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `fittech_db`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: fittech_db
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alimento`
--

DROP TABLE IF EXISTS `alimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alimento` (
  `id_alimento` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `proteina` double NOT NULL,
  `carboidrato` double NOT NULL,
  `gordura` double NOT NULL,
  `calorias` double NOT NULL,
  PRIMARY KEY (`id_alimento`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aluno`
--

DROP TABLE IF EXISTS `aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `aluno` (
  `idaluno` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `idade` int NOT NULL,
  `peso` double NOT NULL,
  `altura` double NOT NULL,
  `sexo` varchar(10) NOT NULL,
  `fator_atividade` varchar(20) NOT NULL,
  `objetivo` varchar(45) NOT NULL,
  `meta_calorica` decimal(10,2) NOT NULL,
  PRIMARY KEY (`idaluno`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_plano`
--

DROP TABLE IF EXISTS `item_plano`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_plano` (
  `id_item_plano` int NOT NULL AUTO_INCREMENT,
  `quantidade` double NOT NULL,
  `calorias_totais` double NOT NULL,
  `id_alimento` int NOT NULL,
  `id_plano_diario` int NOT NULL,
  PRIMARY KEY (`id_item_plano`),
  KEY `id_alimento_idx` (`id_alimento`),
  KEY `fk_item_plano_diario` (`id_plano_diario`),
  CONSTRAINT `fk_item_plano_diario` FOREIGN KEY (`id_plano_diario`) REFERENCES `plano_diario` (`id`),
  CONSTRAINT `id_alimento` FOREIGN KEY (`id_alimento`) REFERENCES `alimento` (`id_alimento`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `plano_diario`
--

DROP TABLE IF EXISTS `plano_diario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plano_diario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idaluno` int NOT NULL,
  `data` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idaluno` (`idaluno`),
  CONSTRAINT `plano_diario_ibfk_1` FOREIGN KEY (`idaluno`) REFERENCES `aluno` (`idaluno`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-29 15:16:56
