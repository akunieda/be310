-- MySQL dump 10.13  Distrib 5.6.11, for osx10.7 (x86_64)
--
-- Host: localhost    Database: be310
-- ------------------------------------------------------
-- Server version	5.6.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Passaro`
--

DROP TABLE IF EXISTS `Passaro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Passaro` (
  `NomeCientifico` varchar(255) NOT NULL,
  `Nome` varchar(255) NOT NULL,
  PRIMARY KEY (`NomeCientifico`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Passaro`
--

LOCK TABLES `Passaro` WRITE;
/*!40000 ALTER TABLE `Passaro` DISABLE KEYS */;
INSERT INTO `Passaro` VALUES ('Ara chloropterus','Arara vermelha grande'),('Furnarius rufus','Joao de barro'),('Picumnus rufiventris','Pica-pau-anao-vermelho'),('Progne subis','Andorinha azul');
/*!40000 ALTER TABLE `Passaro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RegistroPassaro`
--

DROP TABLE IF EXISTS `RegistroPassaro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RegistroPassaro` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `NomeCientifico` varchar(255) NOT NULL,
  `Localizacao` varchar(255) NOT NULL,
  `DataHora` datetime NOT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `URISom` varchar(255) DEFAULT NULL,
  `URIFoto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `NomeCientifico` (`NomeCientifico`),
  CONSTRAINT `registropassaro_ibfk_1` FOREIGN KEY (`NomeCientifico`) REFERENCES `Passaro` (`NomeCientifico`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RegistroPassaro`
--

LOCK TABLES `RegistroPassaro` WRITE;
/*!40000 ALTER TABLE `RegistroPassaro` DISABLE KEYS */;
INSERT INTO `RegistroPassaro` VALUES (6,'Picumnus rufiventris','-9.9739989, -67.807568','2013-04-04 09:31:00','2013-06-28 23:21:30','915136.mp3','926507g.jpg'),(7,'Picumnus rufiventris','-9.9739989, -67.807568','2013-04-04 09:33:00','2013-06-28 23:22:02','915156.mp3','926518g.jpg'),(8,'Picumnus rufiventris','-9.9739989, -67.807568','2013-04-22 09:33:00','2013-06-28 23:23:44','915169.mp3','941496g.jpg'),(9,'Ara chloropterus','-9.0140883, -42.6967611','2010-04-22 11:02:00','2013-06-28 23:28:46','167156.mp3','350126.jpg'),(10,'Ara chloropterus','-3.1064093, -60.026429699999994','2011-01-25 15:42:00','2013-06-28 23:29:35','291422.mp3','736596g.jpg'),(11,'Ara chloropterus','-21.4755178, -56.149482199999966','2008-12-15 08:58:00','2013-06-28 23:30:19','10434.mp3','416656.jpg'),(12,'Ara chloropterus','-21.1312979, -56.48014089999998','2011-04-15 11:10:00','2013-06-28 23:30:59','574286.mp3','928842g.jpg'),(13,'Progne subis','-20.1286963, -40.308266900000035','2010-05-01 14:24:00','2013-06-28 23:33:34','96073.mp3','96014.jpg'),(14,'Furnarius rufus','-19.9190677, -43.938574700000004','2009-08-16 15:48:00','2013-06-28 23:38:17','46276.mp3','207602g.jpg'),(15,'Furnarius rufus','-23.5515189, -51.46137809999999','2010-08-09 07:53:00','2013-06-28 23:38:50','180274.mp3','875429g.jpg'),(16,'Furnarius rufus','-22.3577319, -47.384945500000015','2009-10-26 10:02:00','2013-06-28 23:39:24','73008.mp3','272313g.jpg');
/*!40000 ALTER TABLE `RegistroPassaro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-29 17:08:28
