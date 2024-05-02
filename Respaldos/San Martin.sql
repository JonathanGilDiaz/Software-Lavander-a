-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: softwarelavanderia
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `anticipo`
--

DROP TABLE IF EXISTS `anticipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anticipo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `folio` int NOT NULL,
  `fecha` date DEFAULT NULL,
  `cantidad` decimal(10,2) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `codigoC` int NOT NULL,
  `Observaciones` varchar(250) DEFAULT NULL,
  `formaPago` int DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `idEmpleado` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_folio_idx` (`folio`),
  KEY `fk_cliente_idx` (`codigoC`),
  KEY `fk_empleado_idx` (`idEmpleado`),
  CONSTRAINT `fk_cliente` FOREIGN KEY (`codigoC`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_IdEmpleado` FOREIGN KEY (`idEmpleado`) REFERENCES `empleados` (`idempleados`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nota` FOREIGN KEY (`folio`) REFERENCES `notas` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anticipo`
--

LOCK TABLES `anticipo` WRITE;
/*!40000 ALTER TABLE `anticipo` DISABLE KEYS */;
INSERT INTO `anticipo` VALUES (1,15678,'2023-01-01',162.00,'Victoria','Lopez',62,'',1,'06:34:39',1);
/*!40000 ALTER TABLE `anticipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cancelar`
--

DROP TABLE IF EXISTS `cancelar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancelar` (
  `idcancelar` int NOT NULL AUTO_INCREMENT,
  `folio` int NOT NULL,
  `fecha` date NOT NULL,
  `comentario` varchar(100) DEFAULT NULL,
  `idEmpleada` int NOT NULL,
  `hora` time DEFAULT NULL,
  PRIMARY KEY (`idcancelar`),
  KEY `fk_cancelar_idx` (`folio`),
  KEY `fk_empleado_idx` (`idEmpleada`),
  KEY `fk_empleadoid_idx` (`idEmpleada`),
  CONSTRAINT `fk_cancelar` FOREIGN KEY (`folio`) REFERENCES `notas` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_empleadoid` FOREIGN KEY (`idEmpleada`) REFERENCES `empleados` (`idempleados`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancelar`
--

LOCK TABLES `cancelar` WRITE;
/*!40000 ALTER TABLE `cancelar` DISABLE KEYS */;
/*!40000 ALTER TABLE `cancelar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `adeudo` decimal(10,0) NOT NULL,
  `vecesAsistidas` int NOT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `fechaCreacion` date DEFAULT NULL,
  `estado` int DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Nelva','Lopez','',0,1,'','\n','2023-01-01',1),(2,'Jordan','Gonzales','',0,3,'','\n','2023-01-01',1),(3,'Javier','Martinez','',0,3,'','\n','2023-01-01',1),(4,'Veronica','Sanchez','',0,1,'','\n','2023-01-01',1),(5,'Brenda','Sanchez','',0,1,'','\n','2023-01-01',1),(6,'Vianet','SA','',0,1,'','\n','2023-01-01',1),(7,'Laura','Perez','',0,1,'','\n','2023-01-01',1),(8,'Yuri','Aguilar','',0,1,'','\n','2023-01-01',1),(9,'Diego','Garcia','',0,1,'','\n','2023-01-01',1),(10,'Paulina','Lopez','',0,1,'','\n','2023-01-01',1),(11,'Ricardo','Santiago','',0,1,'','\n','2023-01-01',1),(12,'Maribel','Gonzales','',0,3,'','\n','2023-01-01',1),(13,'Luis','Matias','',0,1,'','\n','2023-01-01',1),(14,'Clienta','Yahuiche','',0,1,'','\n','2023-01-01',1),(15,'Rodrigo','Vargas','',0,1,'','\n','2023-01-01',1),(16,'Adrian','Estrada','',0,1,'','\n','2023-01-01',1),(17,'Frida','Muoz','',0,2,'','\n','2023-01-01',1),(18,'Oficina','SA','',0,1,'','\n','2023-01-01',1),(19,'Morizza','Jimenez','',0,0,'','\n','2023-01-01',1),(20,'Fruteria','Carmelita','',0,1,'','\n','2023-01-01',1),(21,'Francisco','Vazquez','',0,1,'','\n','2023-01-01',1),(22,'Lourdes','Alvala','',0,2,'','\n','2023-01-01',1),(23,'Julia','Adevalos','',0,1,'','\n','2023-01-01',1),(24,'Hugos','Fernandez','',0,2,'','\n','2023-01-01',1),(25,'Nestor','Caballero','',0,3,'','\n','2023-01-01',1),(26,'Teresa','Sierra','',0,1,'','\n','2023-01-01',1),(27,'Rosalinda','Navarrete','',0,1,'','\n','2023-01-01',1),(28,'Alan','Sanchez','',0,1,'','\n','2023-01-01',1),(29,'Marciala','Jimenez','',0,1,'','\n','2023-01-01',1),(30,'Anna','Hernandez','',0,1,'','\n','2023-01-01',1),(31,'Eleazor','Feria','',0,1,'','\n','2023-01-01',1),(32,'Nadia','Guillem','',0,1,'','\n','2023-01-01',1),(33,'Karen','Teran','',0,1,'','\n','2023-01-01',1),(34,'Javier','Hernandez','',0,1,'','\n','2023-01-01',1),(35,'Ericilia','Morales','',0,1,'','\n','2023-01-01',1),(36,'Sarai','Sanchez','',0,1,'','\n','2023-01-01',1),(37,'Rodrigo','Juarez','',0,1,'','\n','2023-01-01',1),(38,'Armando','Aragon','',0,1,'','\n','2023-01-01',1),(39,'Joaquin','Sierra','',0,1,'','\n','2023-01-01',1),(40,'Hector','Rios','',0,1,'','\n','2023-01-01',1),(41,'Fernanda','Aquino','',0,1,'','\n','2023-01-01',1),(42,'Amelia','Santos','',0,2,'','\n','2023-01-01',1),(43,'Leticia','Mendez','',0,1,'','\n','2023-01-01',1),(44,'Zuriel','Sanchez','',0,1,'','\n','2023-01-01',1),(45,'Rocio','Casas','',0,1,'','\n','2023-01-01',1),(46,'Victor','Garcia','',0,1,'','\n','2023-01-01',1),(47,'Casa','Huespedes','',0,2,'','\n','2023-01-01',1),(48,'Oliver','Leyva','',0,1,'','\n','2023-01-01',1),(49,'Porfirio','Gomez','',0,1,'','\n','2023-01-01',1),(50,'Esteban','Garcia','',0,1,'','\n','2023-01-01',1),(51,'Aracely','Vazquez','',0,1,'','\n','2023-01-01',1),(52,'Aure','Anaya','',0,1,'','\n','2023-01-01',1),(53,'Franscisco','Muñoz','',0,2,'','\n','2023-03-01',1),(54,'Carmen','Muñoz','',0,2,'','\n','2023-03-01',1),(55,'Antonia','Muñoz','',0,2,'','\n','2023-03-01',1),(56,'Hugo','Fernandez','',0,0,'','\n','2023-03-01',1),(57,'Carmen','Rajes','',0,1,'','\n','2023-03-01',1),(58,'Estela','Aragon','',0,1,'','\n','2023-01-01',1),(59,'Bersain','Martinez','',0,1,'','\n','2023-01-01',1),(60,'Moises','Benitez','',0,3,'','\n','2023-01-01',1),(61,'Carlos','Hernandez','',0,2,'','\n','2023-01-01',1),(62,'Victoria','Lopez','',0,1,'','\n','2023-01-01',1),(63,'Maria','Martinez','',0,1,'','\n','2023-01-01',1),(64,'Daniela','Pacheco','',0,2,'','\n','2023-01-01',1),(65,'Ximena','Altamirano','',0,1,'','\n','2023-01-01',1),(66,'Ana','Pacheco','',0,1,'','\n','2023-01-01',1),(67,'Rogelio','Muñoz','',0,1,'','\n','2023-01-01',1),(68,'Eduardo','Pacheco','',0,1,'','\n','2023-01-01',1),(69,'Gabino','Cruz','',0,1,'','\n','2023-01-01',1),(70,'Jose','Miguel','',0,1,'','\n','2023-03-02',1),(71,'Jorge','Luis','',0,1,'','\n','2023-03-02',1),(72,'Gustavo','Vazquez','',0,1,'','\n','2023-03-02',1),(73,'Erealisa','Garcia','',0,1,'','\n','2023-03-02',1),(74,'Alan','Vazquez','',0,1,'','\n','2023-03-02',1),(75,'Frida','Muñoz','',0,0,'','\n','2023-03-02',1),(76,'Yenni','Cota','',0,0,'','\n','2023-03-02',1),(77,'Estela','Sanchez','',0,1,'','\n','2023-01-01',1),(78,'Dulce','Mate','',0,1,'','\n','2023-01-01',1),(79,'Julio','Arambula','',0,1,'','\n','2023-01-01',1),(80,'Nera','Cortez','',0,1,'','\n','2023-01-01',1),(81,'Consuelo','Vargas','',0,1,'','\n','2023-01-01',1),(82,'Virginia','Hernandez','',0,1,'','\n','2023-01-01',1),(83,'Patricia','Rosas','',0,1,'','\n','2023-01-01',1),(84,'Angel','Ricardo','',0,1,'','\n','2023-01-01',1),(85,'Alejandra','Calvo','',0,2,'','\n','2023-01-01',1),(86,'Vecina','Muñoz','',0,1,'','\n','2023-01-01',1),(87,'Emilio','Santiago','',0,1,'','\n','2023-03-03',1),(88,'Dinora','Medina','',0,1,'','\n','2023-03-03',1),(89,'Emilio','Breton','',0,1,'','\n','2023-03-03',1),(90,'Gladys','Leon','',0,1,'','\n','2023-03-03',1),(91,'Alicia','Garcia','',0,1,'','\n','2023-03-03',1),(92,'Karla','Becerril','',0,1,'','\n','2023-03-03',1),(93,'Dulce','Cruz','',0,1,'','\n','2023-03-03',1),(94,'Magnolia','Ramirez','',0,1,'','\n','2023-03-03',1),(95,'Edith','Santiago','',0,1,'','\n','2023-03-03',1),(96,'Diana','Mejia','',0,1,'','\n','2023-03-04',1),(97,'Hector','Saucedo','',0,1,'','\n','2023-03-04',1),(98,'Juan','Siguenza','',0,1,'','\n','2023-03-04',1),(99,'Luis','Valencia','',0,1,'','\n','2023-03-04',1),(100,'Kenya','Moises','',0,1,'','\n','2023-03-04',1),(101,'Alejandro','SA','',0,1,'','\n','2023-03-04',1),(102,'Viviana','Gonzalez','',0,1,'','\n','2023-03-04',1),(103,'Salvador','SA','',0,1,'','\n','2023-03-04',1);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rfc` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `telefono` varchar(45) NOT NULL,
  `razonSocial` varchar(45) NOT NULL,
  `direccion` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'fff','LAVANDERIA GLSan Martin','9513556396','POPSOPOSD','Av. Montoya #100-A Col. Casa Blanca Oaxaca de Juarez, CP 68140');
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `corte`
--

DROP TABLE IF EXISTS `corte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `corte` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fechaInicio` date DEFAULT NULL,
  `horaInicio` time DEFAULT NULL,
  `saldoInicial` decimal(10,0) DEFAULT NULL,
  `totalVentas` decimal(10,0) DEFAULT '0',
  `totalCobros` decimal(10,0) DEFAULT '0',
  `saldoEnCaja` decimal(10,0) DEFAULT '0',
  `totalGastos` decimal(10,0) DEFAULT '0',
  `efectivo` decimal(10,0) DEFAULT '0',
  `diferencia` decimal(10,0) DEFAULT '0',
  `retiro` decimal(10,0) DEFAULT '0',
  `saldoFinal` decimal(10,0) DEFAULT '0',
  `persona` varchar(45) DEFAULT '',
  `fechaCierre` date DEFAULT NULL,
  `horaCierre` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `corte`
--

LOCK TABLES `corte` WRITE;
/*!40000 ALTER TABLE `corte` DISABLE KEYS */;
INSERT INTO `corte` VALUES (1,'2023-03-01','08:00:00',283,1055,2218,2471,30,2474,-3,2000,474,'Eliezer','2023-03-01','06:44:12'),(2,'2023-03-02','08:00:00',474,1700,2200,2434,240,2176,258,1700,476,'Eliezer','2023-03-02','07:23:24'),(3,'2023-03-03','08:00:00',476,730,780,951,305,958,-7,600,358,'Eliezer','2023-03-03','07:36:51'),(4,'2023-03-04','08:00:00',358,0,0,0,0,0,0,0,0,'',NULL,NULL);
/*!40000 ALTER TABLE `corte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle`
--

DROP TABLE IF EXISTS `detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idVenta` int NOT NULL,
  `cantidad` decimal(10,0) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `precioUnitario` decimal(10,0) NOT NULL,
  `precioFinal` decimal(10,0) NOT NULL,
  `detalle` varchar(100) DEFAULT NULL,
  `codigoPrecio` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_folio_idx` (`idVenta`),
  KEY `fk_precio_idx` (`codigoPrecio`),
  CONSTRAINT `fk_precio` FOREIGN KEY (`codigoPrecio`) REFERENCES `precios` (`idprecios`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle`
--

LOCK TABLES `detalle` WRITE;
/*!40000 ALTER TABLE `detalle` DISABLE KEYS */;
INSERT INTO `detalle` VALUES (1,1,9,'Kg ropa',15,137,'',1),(2,1,240,'Comodin',1,240,'',3),(3,14163,7,'Kg ropa',15,99,'',1),(4,14163,1,'Sabana',15,15,'',10),(5,15226,4,'Sabana',15,60,'',10),(6,15226,4,'Kg ropa',15,63,'',1),(7,15226,8,'Comodin',1,8,'',3),(8,15697,80,'Comodin',1,80,'',3),(9,15698,1,'Cobijas Promocion',120,120,'',8),(10,15699,6,'Planchado',10,60,'',5),(11,15699,6,'Gancho',3,15,'',6),(12,15700,280,'Comodin',1,280,'',3),(13,15701,3,'Kg ropa',15,45,'',1),(14,15702,1,'Cobija Matrimonial',50,50,'',4),(15,15702,6,'Kg ropa',15,90,'',1),(16,15703,175,'Comodin',1,175,'',3),(17,15703,1,'Cobija Matrimonial',50,50,'',4),(18,15718,1,'Cobija Matrimonial',50,50,'',4),(19,15718,1,'Toalla',10,10,'',2),(20,15719,1,'Cobijas Promocion',120,120,'',8),(21,15720,70,'Comodin',1,70,'',3),(22,15721,180,'Comodin',1,180,'',3),(23,15722,1,'Cobijas Promocion',120,120,'',8),(24,15723,190,'Comodin',1,190,'',3),(25,15724,1,'Toalla',10,10,'',2),(26,15724,87,'Comodin',1,87,'',3),(27,15725,1,'Cobijas Promocion',120,120,'',8),(28,15726,4,'Sabana',15,60,'',10),(29,15727,65,'Comodin',1,65,'',3),(30,15728,47,'Comodin',1,47,'',3),(31,15729,6,'Kg ropa interior',17,102,'',9),(32,15730,123,'Comodin',1,123,'',3),(33,15731,70,'Comodin',1,70,'',3),(34,15732,1,'Cobijas Promocion',120,120,'',8),(35,15733,4,'Kg ropa',15,60,'',1),(36,15734,115,'Comodin',1,115,'',3),(37,15735,10,'Kg ropa',15,146,'',1),(38,15736,9,'Kg ropa',15,135,'',1),(39,15737,150,'Comodin',1,150,'',3),(40,15738,1,'Cobija Matrimonial',50,50,'',4),(41,15739,350,'Comodin',1,350,'',3),(42,15740,4,'Kg ropa',15,54,'',1),(43,15741,35,'Comodin',1,35,'',3),(44,15742,280,'Comodin',1,280,'',3),(45,15743,5,'Kg ropa interior',17,83,'',9),(46,15744,9,'Kg ropa',15,134,'',1),(47,15745,185,'Comodin',1,185,'',3),(48,15746,9,'Kg ropa',15,132,'',1),(49,15747,35,'Comodin',1,35,'',3),(50,15748,310,'Comodin',1,310,'',3),(51,15749,8,'Kg ropa',15,117,'',1),(52,15750,7,'Kg ropa',15,101,'',1),(53,15750,1,'Toalla',10,10,'',2),(54,15751,9,'Kg ropa',15,135,'',1),(55,15752,112,'Comodin',1,112,'',3),(56,15753,6,'Kg ropa',15,83,'',1),(57,15754,9,'Kg ropa',15,131,'',1),(58,15755,7,'Kg ropa',15,99,'',1),(59,15756,4,'Kg ropa',15,66,'',1),(60,15757,5,'Kg ropa',15,69,'',1),(61,15758,190,'Comodin',1,190,'',3),(62,15759,150,'Comodin',1,150,'',3),(63,15760,170,'Comodin',1,170,'',3),(64,15761,229,'Comodin',1,229,'',3),(65,15762,3,'Kg ropa',15,48,'',1),(66,15763,1,'Cobija Matrimonial',50,50,'',4),(67,15764,60,'Comodin',1,60,'',3),(68,15765,5,'Kg ropa',15,78,'',1),(69,15766,88,'Comodin',1,88,'',3),(70,15767,444,'Comodin',1,444,'',3),(71,15768,4,'Kg ropa',15,60,'',1),(72,15769,115,'Comodin',1,115,'',3),(73,15770,107,'Comodin',1,107,'.',3),(74,15771,49,'Comodin',1,49,'',3),(75,15772,564,'Comodin',1,564,'',3),(76,15773,234,'Comodin',1,234,'',3),(77,15774,69,'Comodin',1,69,'',3),(78,15775,184,'Comodin',1,184,'',3),(79,15776,162,'Comodin',1,162,'',3),(80,15777,153,'Comodin',1,153,'',3),(81,15778,3,'Kg ropa',15,45,'',1),(82,15779,3,'Kg ropa',15,45,'',1),(83,15780,118,'Comodin',1,118,'',3),(84,15781,3,'Kg ropa',15,45,'',1),(85,15782,120,'Comodin',1,120,'',3),(86,15783,160,'Comodin',1,160,'',3),(87,15784,99,'Comodin',1,99,'',3),(88,15785,178,'Comodin',1,178,'',3),(89,15720,152,'Comodin',1,152,'',3),(90,15721,120,'Comodin',1,120,'',3),(91,15722,95,'Comodin',1,95,'',3),(92,15723,3,'Kg ropa',15,45,'',1),(93,15724,195,'Comodin',1,195,'',3),(94,15725,120,'Comodin',1,120,'',3),(95,15726,135,'Comodin',1,135,'',3),(96,15727,151,'Comodin',1,151,'',3),(97,15728,325,'Comodin',1,325,'',3),(98,15729,120,'Comodin',1,120,'',3),(99,15730,400,'Comodin',1,400,'',3),(100,15731,180,'Comodin',1,180,'',3),(101,15732,133,'Comodin',1,133,'',3),(102,15733,106,'Comodin',1,106,'',3),(103,15734,136,'Comodin',1,136,'',3),(104,15725,3,'Kg ropa',15,48,'',1),(105,15726,46,'Comodin',1,46,'',3),(106,15727,63,'Comodin',1,63,'',3),(107,15728,11,'Kg ropa',15,165,'',1),(108,15729,4,'Kg ropa',15,60,'',1),(109,15731,1,'Cobija Matrimonial',50,50,'',4),(110,15732,85,'Comodin',1,85,'',3),(111,15732,68,'Comodin',1,68,'',3),(112,15733,145,'Comodin',1,145,'',3),(113,15734,5,'Kg ropa',15,75,'',1),(114,15735,3,'Kg ropa',15,45,'',1),(115,15736,99,'Comodin',1,99,'',3),(116,15737,119,'Comodin',1,119,'',3),(117,15738,132,'Comodin',1,132,'',3),(118,15739,163,'Comodin',1,163,'',3),(119,15740,380,'Comodin',1,380,'',3),(120,15741,95,'Comodin',1,95,'',3),(121,15742,110,'Comodin',1,110,'',3),(122,15743,256,'Comodin',1,256,'',3),(123,15744,210,'Comodin',1,210,'',3),(124,15745,343,'Comodin',1,343,'',3),(125,15746,3,'Kg ropa',15,45,'',1),(126,15747,5,'Kg ropa',15,75,'',1),(127,15748,139,'Comodin',1,139,'',3),(128,15749,3,'Kg ropa',15,45,'',1),(129,15750,163,'Comodin',1,163,'',3),(130,15751,150,'Comodin',1,150,'',3);
/*!40000 ALTER TABLE `detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `idempleados` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `contraseña` varchar(45) NOT NULL,
  `fechaCreacion` date NOT NULL,
  `nivel` int DEFAULT '0',
  `estado` int DEFAULT '1',
  PRIMARY KEY (`idempleados`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'Eliezer','aa','2023-01-01',0,1),(2,'Lesly','a','2023-01-01',0,1),(3,'Flor','a','2023-01-01',0,1);
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entregas`
--

DROP TABLE IF EXISTS `entregas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entregas` (
  `identregas` int NOT NULL AUTO_INCREMENT,
  `idNota` int NOT NULL,
  `CantidadAEntregar` decimal(10,0) NOT NULL,
  `Pago` decimal(10,0) NOT NULL,
  `fechaEntrega` varchar(45) NOT NULL,
  `horaEntrega` varchar(45) NOT NULL,
  `comentario` varchar(200) DEFAULT NULL,
  `idEmpleado` int NOT NULL,
  `formaPago` int DEFAULT NULL,
  PRIMARY KEY (`identregas`),
  KEY `fk_entrega_idx` (`idNota`),
  KEY `fk_empleado_idx` (`idEmpleado`),
  CONSTRAINT `fk_empleado` FOREIGN KEY (`idEmpleado`) REFERENCES `empleados` (`idempleados`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_folio` FOREIGN KEY (`idNota`) REFERENCES `notas` (`folio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entregas`
--

LOCK TABLES `entregas` WRITE;
/*!40000 ALTER TABLE `entregas` DISABLE KEYS */;
INSERT INTO `entregas` VALUES (1,15657,107,107,'2023-03-01','6:35:9','',1,1),(2,15676,0,49,'2023-03-01','6:35:19','',1,1),(3,15622,0,564,'2023-03-01','6:35:30','',1,1),(4,15638,0,234,'2023-03-01','6:35:44','',1,1),(5,15655,0,69,'2023-03-01','6:35:53','',1,1),(6,15639,0,184,'2023-03-01','6:36:7','',1,1),(7,15678,0,0,'2023-03-01','6:36:22','',1,0),(8,15685,0,153,'2023-03-01','6:36:40','',1,1),(9,15681,0,45,'2023-03-01','6:36:52','',1,1),(10,15683,0,45,'2023-03-01','6:37:3','',1,1),(11,15602,0,118,'2023-03-01','6:38:7','',1,1),(12,15684,0,45,'2023-03-01','6:38:36','',1,1),(13,15705,0,88,'2023-03-01','6:38:47','',1,1),(14,15704,0,78,'2023-03-01','6:39:0','',1,1),(15,15661,0,120,'2023-03-01','6:39:8','',1,1),(16,15599,0,160,'2023-03-01','6:39:19','',1,1),(17,15621,0,99,'2023-03-01','6:39:29','',1,1),(18,15657,0,107,'2023-03-01','6:43:31','',1,1),(19,15706,0,444,'2023-03-02','7:1:58','',1,1),(20,15663,0,136,'2023-03-02','7:12:58','',1,1),(21,15650,0,120,'2023-03-02','7:13:20','',1,1),(22,15686,0,135,'2023-03-02','7:13:30','',1,1),(23,15641,0,151,'2023-03-02','7:13:42','',1,1),(24,15634,0,325,'2023-03-02','7:13:54','',1,1),(25,15646,0,120,'2023-03-02','7:14:3','',1,1),(26,15691,0,0,'2023-03-02','7:14:16','',1,0),(27,15590,0,180,'2023-03-02','7:14:33','',1,1),(28,15692,0,133,'2023-03-02','7:14:49','',1,1),(29,15693,0,106,'2023-03-02','7:15:1','',1,1),(30,15701,0,115,'2023-03-03','7:35:0','',1,1),(31,15702,1,145,'2023-03-03','7:35:9','',1,1),(32,15703,0,99,'2023-03-03','7:35:19','',1,1),(33,15708,0,0,'2023-03-03','7:35:32','',1,0),(34,15694,0,45,'2023-03-03','7:35:47','',1,1),(35,15647,0,180,'2023-03-03','7:36:8','',1,1),(36,15697,0,150,'2023-03-03','7:36:19','',1,1),(37,15695,0,185,'2023-03-04','7:50:14','',1,1),(38,15696,0,131,'2023-03-04','7:50:26','',1,1),(39,15698,0,97,'2023-03-04','7:50:36','',1,1),(40,15690,1,110,'2023-03-04','7:50:52','',1,1),(41,15707,1,133,'2023-03-04','7:51:6','',1,1),(42,15459,0,120,'2023-03-04','7:51:21','',1,1),(43,15669,0,35,'2023-03-04','7:51:33','',1,1),(44,15658,1,130,'2023-03-04','7:51:45','',1,1),(45,15723,0,45,'2023-03-04','7:52:4','',1,1),(46,15714,0,0,'2023-03-04','7:52:17','',1,0),(47,15717,0,225,'2023-03-04','7:52:30','',1,1),(48,15721,0,120,'2023-03-04','7:52:49','',1,1),(49,15575,0,135,'2023-03-04','7:52:59','',1,1),(50,15576,0,70,'2023-03-04','7:53:16','',1,1),(51,15726,0,0,'2023-03-04','7:53:28','',1,0),(52,15713,0,66,'2023-03-04','7:53:42','',1,1);
/*!40000 ALTER TABLE `entregas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gastos`
--

DROP TABLE IF EXISTS `gastos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gastos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comprobante` varchar(45) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  `precio` decimal(10,0) NOT NULL,
  `fecha` date NOT NULL,
  `formaPago` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gastos`
--

LOCK TABLES `gastos` WRITE;
/*!40000 ALTER TABLE `gastos` DISABLE KEYS */;
INSERT INTO `gastos` VALUES (1,'Ticket','Fabuloso',30,'2023-03-01',1),(2,'Nota','2 Tinacos de agua',240,'2023-03-02',1),(3,'ticket','Suavitel',25,'2023-03-03',1),(4,'Nota','Candado',30,'2023-03-03',1),(5,'-','Pipa de agua',250,'2023-03-03',1),(6,'Nota','2 tinacos de agua',240,'2023-03-04',1),(7,'-','Papel Higienico',10,'2023-03-04',1);
/*!40000 ALTER TABLE `gastos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nomina`
--

DROP TABLE IF EXISTS `nomina`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nomina` (
  `codigo` int NOT NULL AUTO_INCREMENT,
  `fechaInicio` date DEFAULT NULL,
  `fechaFinal` date DEFAULT NULL,
  `estado` int DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nomina`
--

LOCK TABLES `nomina` WRITE;
/*!40000 ALTER TABLE `nomina` DISABLE KEYS */;
/*!40000 ALTER TABLE `nomina` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nominadiario`
--

DROP TABLE IF EXISTS `nominadiario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nominadiario` (
  `codigo` int NOT NULL AUTO_INCREMENT,
  `nombreEmpleado` varchar(45) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `horaInicio` time DEFAULT NULL,
  `horaFinal` time DEFAULT NULL,
  `codigoEmpleado` int NOT NULL,
  `codigoNomina` int NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fk_empleado_idx` (`codigoEmpleado`),
  KEY `fk_nomina_idx` (`codigoNomina`),
  CONSTRAINT `fk_empleada` FOREIGN KEY (`codigoEmpleado`) REFERENCES `empleados` (`idempleados`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_nomina` FOREIGN KEY (`codigoNomina`) REFERENCES `nomina` (`codigo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nominadiario`
--

LOCK TABLES `nominadiario` WRITE;
/*!40000 ALTER TABLE `nominadiario` DISABLE KEYS */;
/*!40000 ALTER TABLE `nominadiario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notas`
--

DROP TABLE IF EXISTS `notas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notas` (
  `folio` int NOT NULL AUTO_INCREMENT,
  `codigoCliente` int NOT NULL,
  `anticipo` decimal(10,2) NOT NULL,
  `diaEntrega` varchar(45) NOT NULL,
  `horaEntrega` varchar(45) NOT NULL,
  `nombreCliente` varchar(45) NOT NULL,
  `apellidoCliente` varchar(45) NOT NULL,
  `totalPagar` decimal(10,2) NOT NULL,
  `totalVenta` decimal(10,2) NOT NULL,
  `entrega` varchar(45) NOT NULL,
  `saldo` int NOT NULL,
  `estado` int NOT NULL,
  `entregaDia` date NOT NULL DEFAULT '0001-01-01',
  `entregaHora` time NOT NULL DEFAULT '00:01:00',
  `diaRecibido` date NOT NULL,
  `horaRecibido` time NOT NULL,
  `folioEntrega` int DEFAULT NULL,
  `idRecibe` int NOT NULL,
  `idCancelacion` int DEFAULT NULL,
  `iva` decimal(10,2) DEFAULT NULL,
  `subTotal` decimal(10,2) DEFAULT NULL,
  `formaPago` int DEFAULT NULL,
  `notascol` varchar(45) DEFAULT 'null',
  PRIMARY KEY (`folio`),
  KEY `fk_cliente_idx` (`codigoCliente`),
  KEY `fk_cancelar_idx` (`idCancelacion`),
  KEY `fk_entregas_idx` (`folioEntrega`),
  KEY `fk_recibe_idx` (`idRecibe`),
  CONSTRAINT `fk_cancelarID` FOREIGN KEY (`idCancelacion`) REFERENCES `cancelar` (`idcancelar`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_clientes` FOREIGN KEY (`codigoCliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_entregas` FOREIGN KEY (`folioEntrega`) REFERENCES `entregas` (`identregas`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_recibe` FOREIGN KEY (`idRecibe`) REFERENCES `empleados` (`idempleados`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15752 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notas`
--

LOCK TABLES `notas` WRITE;
/*!40000 ALTER TABLE `notas` DISABLE KEYS */;
INSERT INTO `notas` VALUES (11091,11,0.00,'2023-03-03','18:30','Ricardo','Santiago',60.00,60.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:59:55',NULL,1,NULL,0.00,0.00,1,'null'),(11315,20,0.00,'2023-03-03','18:30','Fruteria','Carmelita',47.00,47.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:20:46',NULL,1,NULL,0.00,0.00,0,'null'),(11817,26,0.00,'2023-03-09','18:30','Teresa','Sierra',150.00,150.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:38:37',NULL,1,NULL,0.00,0.00,1,'null'),(12154,27,0.00,'2023-03-03','18:30','Rosalinda','Navarrete',50.00,50.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:39:28',NULL,1,NULL,0.00,0.00,1,'null'),(12965,28,0.00,'2023-03-03','18:30','Alan','Sanchez',350.00,350.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:40:21',NULL,1,NULL,0.00,0.00,1,'null'),(14162,1,0.00,'2023-03-03','18:30','Nelva','Lopez',376.50,376.50,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:47:51',NULL,1,NULL,0.00,0.00,0,'null'),(14313,45,0.00,'2023-03-03','18:30','Rocio','Casas',69.00,69.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:00:41',NULL,1,NULL,0.00,0.00,0,'null'),(14360,9,100.00,'2023-03-03','18:30','Diego','Garcia',40.00,140.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:57:57',NULL,1,NULL,0.00,0.00,1,'null'),(14752,2,0.00,'2023-03-03','18:30','Jordan','Gonzales',190.00,190.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:13:24',NULL,1,NULL,0.00,0.00,1,'null'),(14833,19,0.00,'2023-03-03','18:30','Morizza','Jimenes',65.00,65.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:18:57',NULL,1,NULL,0.00,0.00,0,'null'),(15121,2,0.00,'2023-03-03','18:30','Jordan','Gonzales',123.00,123.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:23:38',NULL,1,NULL,0.00,0.00,0,'null'),(15225,2,70.00,'2023-03-03','18:30','Jordan','Gonzales',44.00,114.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:50:03',NULL,1,NULL,0.00,0.00,1,'null'),(15251,37,0.00,'2023-03-03','18:30','Rodrigo','Juarez',310.00,310.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:50:06',NULL,1,NULL,0.00,0.00,1,'null'),(15313,21,0.00,'2023-03-03','18:30','Francisco','Vazquez',102.00,102.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:21:41',NULL,1,NULL,0.00,0.00,0,'null'),(15377,17,0.00,'2023-03-10','18:30','Frida','Muoz',120.00,120.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:15:44',NULL,1,NULL,0.00,0.00,1,'null'),(15459,12,0.00,'2023-03-03','18:30','Maribel','Gonzales',0.00,120.00,'1',1,0,'2023-03-04','07:51:21','2023-01-01','13:00:42',1,1,NULL,0.00,0.00,1,'null'),(15480,30,0.00,'2023-03-03','18:30','Anna','Hernandez',35.00,35.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:42:08',NULL,1,NULL,0.00,0.00,1,'null'),(15490,52,0.00,'2023-03-03','18:30','Aure','Anaya',60.00,60.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:06:34',NULL,1,NULL,0.00,0.00,0,'null'),(15525,18,0.00,'2023-03-03','18:30','Oficina','SA',60.00,60.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:17:37',NULL,1,NULL,0.00,0.00,1,'null'),(15563,35,0.00,'2023-03-03','18:30','Ericilia','Morales',132.00,132.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:48:29',NULL,1,NULL,0.00,0.00,1,'null'),(15570,5,0.00,'2023-03-03','18:30','Brenda','Sanchez',120.00,120.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:53:38',NULL,1,NULL,0.00,0.00,0,'null'),(15575,22,0.00,'2023-03-03','18:30','Lourdes','Alvala',0.00,135.00,'1',1,0,'2023-03-04','07:52:59','2023-01-01','13:37:46',1,1,NULL,0.00,0.00,1,'null'),(15576,22,0.00,'2023-03-03','18:30','Lourdes','Alvala',0.00,70.00,'1',1,0,'2023-03-04','07:53:16','2023-01-01','13:24:35',1,1,NULL,0.00,0.00,0,'null'),(15581,49,0.00,'2023-03-03','18:30','Porfirio','Gomez',229.00,229.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:03:58',NULL,1,NULL,0.00,0.00,0,'null'),(15585,48,0.00,'2023-03-03','18:30','Oliver','Leyva',170.00,170.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:03:19',NULL,1,NULL,0.00,0.00,0,'null'),(15590,83,0.00,'2023-03-05','18:30','Patricia','Rosas',0.00,180.00,'1',1,0,'2023-03-02','07:14:33','2023-01-01','07:06:28',1,1,NULL,0.00,0.00,1,'null'),(15599,68,0.00,'2023-03-05','18:30','Eduardo','Pacheco',0.00,160.00,'1',1,0,'2023-03-01','06:39:19','2023-01-01','06:29:02',1,1,NULL,0.00,0.00,0,'null'),(15600,51,0.00,'2023-03-03','18:30','Aracely','Vazquez',50.00,50.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:05:52',NULL,1,NULL,0.00,0.00,0,'null'),(15602,64,0.00,'2023-03-05','18:30','Daniela','Pacheco',0.00,118.00,'1',1,0,'2023-03-01','06:38:07','2023-01-01','06:27:13',1,1,NULL,0.00,0.00,0,'null'),(15619,40,0.00,'2023-03-03','18:30','Hector','Rios',135.00,135.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:52:42',NULL,1,NULL,0.00,0.00,1,'null'),(15621,69,0.00,'2023-03-05','18:30','Gabino','Cruz',0.00,99.00,'1',1,0,'2023-03-01','06:39:29','2023-01-01','06:29:24',1,1,NULL,0.00,0.00,0,'null'),(15622,60,0.00,'2023-03-05','18:30','Moises','Benitez',0.00,564.00,'1',1,0,'2023-03-01','06:35:30','2023-01-01','06:22:14',1,1,NULL,0.00,0.00,0,'null'),(15627,6,0.00,'2023-03-03','18:30','Vianet','SA',75.00,75.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:54:37',NULL,1,NULL,0.00,0.00,0,'null'),(15634,80,0.00,'2023-03-05','18:30','Nera','Cortez',0.00,325.00,'1',1,0,'2023-03-02','07:13:54','2023-01-01','07:04:51',1,1,NULL,0.00,0.00,0,'null'),(15638,60,0.00,'2023-03-05','18:30','Moises','Benitez',0.00,234.00,'1',1,0,'2023-03-01','06:35:44','2023-01-01','06:22:33',1,1,NULL,0.00,0.00,0,'null'),(15639,42,0.00,'2023-03-05','18:30','Amelia','Santos',0.00,184.00,'1',1,0,'2023-03-01','06:36:07','2023-01-01','06:24:21',1,1,NULL,0.00,0.00,0,'null'),(15641,79,0.00,'2023-03-05','18:30','Julio','Arambula',0.00,151.00,'1',1,0,'2023-03-02','07:13:42','2023-01-01','07:04:23',1,1,NULL,0.00,0.00,0,'null'),(15646,81,0.00,'2023-03-05','18:30','Consuelo','Vargas',0.00,120.00,'1',1,0,'2023-03-02','07:14:03','2023-01-01','07:05:24',1,1,NULL,0.00,0.00,0,'null'),(15647,14,0.00,'2023-03-03','18:30','Clienta','Yahuiche',0.00,180.00,'1',1,0,'2023-03-03','07:36:08','2023-01-01','13:02:47',1,1,NULL,0.00,0.00,1,'null'),(15650,77,0.00,'2023-03-05','18:30','Estela','Sanchez',0.00,120.00,'1',1,0,'2023-03-02','07:13:20','2023-01-01','07:03:33',1,1,NULL,0.00,0.00,0,'null'),(15655,61,0.00,'2023-03-05','18:30','Carlos','Hernandez',0.00,69.00,'1',1,0,'2023-03-01','06:35:53','2023-01-01','06:23:05',1,1,NULL,0.00,0.00,0,'null'),(15657,58,0.00,'2023-03-05','18:30','Estela','Aragon',0.00,107.00,'1',1,0,'2023-03-01','06:43:31','2023-01-01','06:21:19',1,1,NULL,0.00,0.00,0,'null'),(15658,43,0.00,'2023-03-03','18:30','Leticia','Mendez',0.50,130.50,'1',0,0,'2023-03-04','07:51:45','2023-01-01','13:57:57',1,1,NULL,0.00,0.00,0,'null'),(15661,67,0.00,'2023-03-05','18:30','Rogelio','Muñoz',0.00,120.00,'1',1,0,'2023-03-01','06:39:08','2023-01-01','06:28:38',1,1,NULL,0.00,0.00,0,'null'),(15663,86,0.00,'2023-03-05','18:30','Vecina','Muñoz',0.00,136.00,'1',1,0,'2023-03-02','07:12:58','2023-01-01','07:09:41',1,1,NULL,0.00,0.00,0,'null'),(15669,36,0.00,'2023-03-03','18:30','Sarai','Sanchez',0.00,35.00,'1',1,0,'2023-03-04','07:51:33','2023-01-01','13:49:17',1,1,NULL,0.00,0.00,1,'null'),(15674,41,0.00,'2023-03-03','18:30','Fernanda','Aquino',112.00,112.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:56:12',NULL,1,NULL,0.00,0.00,0,'null'),(15675,15,0.00,'2023-03-03','18:30','Rodrigo','Vargas',120.00,120.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:04:03',NULL,1,NULL,0.00,0.00,1,'null'),(15676,59,0.00,'2023-03-05','18:30','Bersain','Martinez',0.00,49.00,'1',1,0,'2023-03-01','06:35:19','2023-01-01','06:21:50',1,1,NULL,0.00,0.00,0,'null'),(15678,62,0.00,'2023-03-05','18:30','Victoria','Lopez',0.00,162.00,'1',1,0,'2023-03-01','06:36:22','2023-01-01','06:24:51',1,1,NULL,0.00,0.00,0,'null'),(15681,64,0.00,'2023-03-05','18:30','Daniela','Pacheco',0.00,45.00,'1',1,0,'2023-03-01','06:36:52','2023-01-01','06:26:14',1,1,NULL,0.00,0.00,0,'null'),(15683,65,0.00,'2023-03-05','18:30','Ximena','Altamirano',0.00,45.00,'1',1,0,'2023-03-01','06:37:03','2023-01-01','06:26:49',1,1,NULL,0.00,0.00,0,'null'),(15684,66,0.00,'2023-03-05','18:30','Ana','Pacheco',0.00,45.00,'1',1,0,'2023-03-01','06:38:36','2023-01-01','06:27:42',1,1,NULL,0.00,0.00,0,'null'),(15685,63,0.00,'2023-03-05','18:30','Maria','Martinez',0.00,153.00,'1',1,0,'2023-03-01','06:36:40','2023-01-01','06:25:37',1,1,NULL,0.00,0.00,0,'null'),(15686,78,0.00,'2023-03-05','18:30','Dulce','Mate',0.00,135.00,'1',1,0,'2023-03-02','07:13:30','2023-01-01','07:03:59',1,1,NULL,0.00,0.00,0,'null'),(15687,32,0.00,'2023-03-03','18:30','Nadia','Guillem',83.30,83.30,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:44:28',NULL,1,NULL,0.00,0.00,1,'null'),(15688,4,0.00,'2023-03-03','18:30','Veronica','Sanchez',80.00,80.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:52:45',NULL,1,NULL,0.00,0.00,0,'null'),(15689,7,0.00,'2023-03-03','18:30','Laura','Perez',280.00,280.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','12:55:29',NULL,1,NULL,0.00,0.00,0,'null'),(15690,39,0.00,'2023-03-03','18:30','Joaquin','Sierra',0.50,110.50,'1',0,0,'2023-03-04','07:50:52','2023-01-01','13:51:59',1,1,NULL,0.00,0.00,1,'null'),(15691,82,400.00,'2023-03-05','18:30','Virginia','Hernandez',0.00,400.00,'1',1,0,'2023-03-02','07:14:16','2023-01-01','07:06:04',1,1,NULL,0.00,0.00,1,'null'),(15692,84,0.00,'2023-03-05','18:30','Angel','Ricardo',0.00,133.00,'1',1,0,'2023-03-02','07:14:49','2023-01-01','07:06:56',1,1,NULL,0.00,0.00,1,'null'),(15693,85,0.00,'2023-03-05','18:30','Alejandra','Calvo',0.00,106.00,'1',1,0,'2023-03-02','07:15:01','2023-01-01','07:07:24',1,1,NULL,0.00,0.00,1,'null'),(15694,8,0.00,'2023-03-03','18:30','Yuri','Aguilar',0.00,45.00,'1',1,0,'2023-03-03','07:35:47','2023-01-01','12:56:33',1,1,NULL,0.00,0.00,0,'null'),(15695,34,0.00,'2023-03-03','18:30','Javier','Hernandez',0.00,185.00,'1',1,0,'2023-03-04','07:50:14','2023-01-01','13:46:20',1,1,NULL,0.00,0.00,1,'null'),(15696,3,0.00,'2023-03-03','18:30','Javier','Martinez',0.00,131.00,'1',1,0,'2023-03-04','07:50:26','2023-01-01','12:51:48',1,1,NULL,0.00,0.00,1,'null'),(15697,47,0.00,'2023-03-03','18:30','Casa','Huespedes',0.00,150.00,'1',1,0,'2023-03-03','07:36:19','2023-01-01','14:02:27',1,1,NULL,0.00,0.00,0,'null'),(15698,16,0.00,'2023-03-03','18:30','Adrian','Estrada',0.00,97.00,'1',1,0,'2023-03-04','07:50:36','2023-01-01','13:14:51',1,1,NULL,0.00,0.00,1,'null'),(15699,46,0.00,'2023-03-03','18:30','Victor','Garcia',190.00,190.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','14:01:29',NULL,1,NULL,0.00,0.00,0,'null'),(15700,23,0.00,'2023-03-03','18:30','Julia','Adevalos',120.00,120.00,'0',0,0,'0001-01-01','00:01:00','2023-01-01','13:25:34',NULL,1,NULL,0.00,0.00,0,'null'),(15701,25,0.00,'2023-03-03','18:30','Nestor','Caballero',0.00,115.00,'1',1,0,'2023-03-03','07:35:00','2023-01-01','13:29:24',1,1,NULL,0.00,0.00,1,'null'),(15702,25,0.00,'2023-03-03','18:30','Nestor','Caballero',0.50,145.50,'1',0,0,'2023-03-03','07:35:09','2023-01-01','13:30:10',1,1,NULL,0.00,0.00,1,'null'),(15703,25,0.00,'2023-03-03','18:30','Nestor','Caballero',0.00,99.00,'1',1,0,'2023-03-03','07:35:19','2023-01-01','13:58:43',1,1,NULL,0.00,0.00,0,'null'),(15704,53,0.00,'2023-03-05','18:30','Franscisco','Muñoz',0.00,78.00,'1',1,0,'2023-03-01','06:39:00','2023-03-01','06:11:37',1,3,NULL,0.00,0.00,0,'null'),(15705,54,0.00,'2023-03-05','18:30','Carmen','Muñoz',0.00,88.00,'1',1,0,'2023-03-01','06:38:47','2023-03-01','06:12:09',1,3,NULL,0.00,0.00,0,'null'),(15706,55,0.00,'2023-03-05','18:30','Antonia','Muñoz',0.00,444.00,'1',1,0,'2023-03-02','07:01:58','2023-03-01','06:13:55',1,3,NULL,0.00,0.00,0,'null'),(15707,33,0.00,'2023-03-03','18:30','Karen','Teran',0.50,133.50,'1',0,0,'2023-03-04','07:51:06','2023-03-01','13:45:25',1,1,NULL,0.00,0.00,1,'null'),(15708,24,60.00,'2023-03-03','18:30','Hugos','Fernandez',0.00,60.00,'1',1,0,'2023-03-03','07:35:32','2023-03-01','13:27:58',1,1,NULL,0.00,0.00,1,'null'),(15709,29,0.00,'2023-03-03','18:30','Marciala','Jimenez',54.00,54.00,'0',0,0,'0001-01-01','00:01:00','2023-03-01','13:41:16',NULL,1,NULL,0.00,0.00,1,'null'),(15710,42,0.00,'2023-03-03','18:30','Amelia','Santos',82.50,82.50,'0',0,0,'0001-01-01','00:01:00','2023-03-01','13:57:04',NULL,1,NULL,0.00,0.00,0,'null'),(15711,57,0.00,'2023-03-05','18:30','Carmen','Rajes',115.00,115.00,'0',0,0,'0001-01-01','00:01:00','2023-03-01','06:17:29',NULL,2,NULL,0.00,0.00,0,'null'),(15712,76,0.00,'2023-03-05','18:30','Yenni','Cota',109.00,109.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','06:56:06',NULL,1,NULL,0.00,0.00,0,'null'),(15713,44,0.00,'2023-03-03','18:30','Zuriel','Sanchez',0.00,66.00,'1',1,0,'2023-03-04','07:53:42','2023-03-02','13:59:33',1,1,NULL,0.00,0.00,0,'null'),(15714,13,70.00,'2023-03-03','18:30','Luis','Matias',0.00,70.00,'1',1,0,'2023-03-04','07:52:17','2023-03-02','13:01:43',1,1,NULL,0.00,0.00,1,'null'),(15715,38,0.00,'2023-03-03','18:30','Armando','Aragon',117.00,117.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','13:51:02',NULL,1,NULL,0.00,0.00,1,'null'),(15716,50,0.00,'2023-03-03','18:30','Esteban','Garcia',48.00,48.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','14:04:58',NULL,1,NULL,0.00,0.00,0,'null'),(15717,10,0.00,'2023-03-03','18:30','Paulina','Lopez',0.00,225.00,'1',1,0,'2023-03-04','07:52:30','2023-03-02','12:58:57',1,1,NULL,0.00,0.00,1,'null'),(15718,70,0.00,'2023-03-05','18:30','Jose','Miguel',178.00,178.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','06:51:44',NULL,1,NULL,0.00,0.00,0,'null'),(15719,31,280.00,'2023-03-03','18:30','Eleazor','Feria',0.00,280.00,'0',1,0,'0001-01-01','00:01:00','2023-03-02','13:43:18',NULL,1,NULL,0.00,0.00,1,'null'),(15720,71,0.00,'2023-03-05','18:30','Jorge','Luis',152.00,152.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','06:54:53',NULL,1,NULL,0.00,0.00,0,'null'),(15721,72,0.00,'2023-03-05','18:30','Gustavo','Vazquez',0.00,120.00,'1',1,0,'2023-03-04','07:52:49','2023-03-02','06:55:36',1,1,NULL,0.00,0.00,0,'null'),(15722,73,0.00,'2023-03-05','18:30','Erealisa','Garcia',95.00,95.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','06:56:06',NULL,1,NULL,0.00,0.00,0,'null'),(15723,74,0.00,'2023-03-05','18:30','Alan','Vazquez',0.00,45.00,'1',1,0,'2023-03-04','07:52:04','2023-03-02','06:56:50',1,1,NULL,0.00,0.00,0,'null'),(15724,17,0.00,'2023-03-05','18:30','Frida','Muoz',195.00,195.00,'0',0,0,'0001-01-01','00:01:00','2023-03-02','06:57:15',NULL,1,NULL,0.00,0.00,0,'null'),(15725,87,0.00,'2023-03-05','18:30','Emilio','Santiago',48.00,48.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:25:24',NULL,1,NULL,0.00,0.00,0,'null'),(15726,88,46.00,'2023-03-05','18:30','Dinora','Medina',0.00,46.00,'1',1,0,'2023-03-04','07:53:28','2023-03-03','07:26:07',1,1,NULL,0.00,0.00,1,'null'),(15727,89,0.00,'2023-03-05','18:30','Emilio','Breton',63.00,63.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:26:31',NULL,1,NULL,0.00,0.00,1,'null'),(15728,90,0.00,'2023-03-05','18:30','Gladys','Leon',165.00,165.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:27:17',NULL,1,NULL,0.00,0.00,1,'null'),(15729,92,0.00,'2023-03-05','18:30','Karla','Becerril',50.00,50.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:29:09',NULL,1,NULL,0.00,0.00,0,'null'),(15730,91,0.00,'2023-03-05','18:30','Alicia','Garcia',60.00,60.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:27:54',NULL,1,NULL,0.00,0.00,0,'null'),(15731,93,0.00,'2023-03-05','18:30','Dulce','Cruz',85.00,85.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:30:48',NULL,1,NULL,0.00,0.00,0,'null'),(15732,94,0.00,'2023-03-05','18:30','Magnolia','Ramirez',68.00,68.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:32:24',NULL,1,NULL,0.00,0.00,0,'null'),(15733,95,0.00,'2023-03-05','18:30','Edith','Santiago',145.00,145.00,'0',0,0,'0001-01-01','00:01:00','2023-03-03','07:33:02',NULL,1,NULL,0.00,0.00,0,'null'),(15734,61,0.00,'2023-03-05','18:30','Carlos','Hernandez',75.00,75.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:41:13',NULL,1,NULL,0.00,0.00,0,'null'),(15735,96,0.00,'2023-03-05','18:30','Diana','Mejia',45.00,45.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:41:39',NULL,1,NULL,0.00,0.00,0,'null'),(15736,3,0.00,'2023-03-05','18:30','Javier','Martinez',99.00,99.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:42:06',NULL,1,NULL,0.00,0.00,0,'null'),(15737,3,0.00,'2023-03-05','18:30','Javier','Martinez',119.00,119.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:42:30',NULL,1,NULL,0.00,0.00,0,'null'),(15738,54,0.00,'2023-03-05','18:30','Carmen','Muñoz',132.00,132.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:42:48',NULL,1,NULL,0.00,0.00,0,'null'),(15739,53,0.00,'2023-03-05','18:30','Franscisco','Muñoz',163.00,163.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:43:06',NULL,1,NULL,0.00,0.00,0,'null'),(15740,55,0.00,'2023-03-05','18:30','Antonia','Muñoz',380.00,380.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:43:29',NULL,1,NULL,0.00,0.00,0,'null'),(15741,97,0.00,'2023-03-05','18:30','Hector','Saucedo',95.00,95.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:43:55',NULL,1,NULL,0.00,0.00,0,'null'),(15742,12,0.00,'2023-03-05','18:30','Maribel','Gonzales',110.00,110.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:44:12',NULL,1,NULL,0.00,0.00,0,'null'),(15743,98,0.00,'2023-03-05','18:30','Juan','Siguenza',256.00,256.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:44:46',NULL,1,NULL,0.00,0.00,0,'null'),(15744,60,0.00,'2023-03-05','18:30','Moises','Benitez',210.00,210.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:45:00',NULL,1,NULL,0.00,0.00,0,'null'),(15745,47,0.00,'2023-03-05','18:30','Casa','Huespedes',343.00,343.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:45:54',NULL,1,NULL,0.00,0.00,0,'null'),(15746,99,0.00,'2023-03-05','18:30','Luis','Valencia',45.00,45.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:46:21',NULL,1,NULL,0.00,0.00,0,'null'),(15747,100,0.00,'2023-03-05','18:30','Kenya','Moises',75.00,75.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:46:57',NULL,1,NULL,0.00,0.00,0,'null'),(15748,85,0.00,'2023-03-05','18:30','Alejandra','Calvo',139.00,139.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:47:17',NULL,1,NULL,0.00,0.00,0,'null'),(15749,101,0.00,'2023-03-05','18:30','Alejandro','SA',45.00,45.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:47:48',NULL,1,NULL,0.00,0.00,0,'null'),(15750,102,0.00,'2023-03-12','18:30','Viviana','Gonzalez',163.00,163.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:48:38',NULL,1,NULL,0.00,0.00,0,'null'),(15751,103,0.00,'2023-03-05','18:30','Salvador','SA',150.00,150.00,'0',0,0,'0001-01-01','00:01:00','2023-03-04','07:49:16',NULL,1,NULL,0.00,0.00,0,'null');
/*!40000 ALTER TABLE `notas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `precios`
--

DROP TABLE IF EXISTS `precios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `precios` (
  `idprecios` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `precioU` decimal(10,2) NOT NULL,
  `precioPromocion` decimal(10,2) DEFAULT NULL,
  `cantidadMinima` decimal(10,2) DEFAULT NULL,
  `fechaCreacion` date NOT NULL,
  `usada` int DEFAULT '0',
  `estado` int DEFAULT '0',
  `especificaciones` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idprecios`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `precios`
--

LOCK TABLES `precios` WRITE;
/*!40000 ALTER TABLE `precios` DISABLE KEYS */;
INSERT INTO `precios` VALUES (1,'Kg ropa',15.00,NULL,NULL,'2023-01-30',0,0,'Kilos de ropa'),(2,'Toalla',10.00,NULL,NULL,'2023-01-31',0,0,'Toallas grandes'),(3,'Comodin',1.00,NULL,NULL,'2023-01-31',0,0,'Precios raros'),(4,'Cobija Matrimonial',50.00,NULL,NULL,'2023-01-31',0,0,'Cobija tamaño matrimonial'),(5,'Planchado',10.00,NULL,NULL,'2023-02-01',0,0,'Pieza para planchar'),(6,'Gancho',2.50,NULL,NULL,'2023-02-01',0,0,'Gancho por pieza para planchar'),(7,'Docena Planchado',108.00,NULL,NULL,'2023-02-02',0,0,'Docena planchado precio promocional'),(8,'Cobijas Promocion',120.00,NULL,NULL,'2023-02-02',0,0,'3 cobijas por 120'),(9,'Kg ropa interior',17.00,NULL,NULL,'2023-02-04',0,0,'Ropa con ropa interior'),(10,'Sabana',15.00,NULL,NULL,'2023-01-01',0,0,'Sabana'),(11,'Domicilio',18.00,NULL,NULL,'2023-02-11',0,0,'Costo adicional ');
/*!40000 ALTER TABLE `precios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'LavanderiaGL','LavanderiaGL','uno');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-05 10:34:18
