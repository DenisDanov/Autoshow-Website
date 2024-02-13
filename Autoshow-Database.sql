-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: clwxydcjair55xn0.chr7pe7iynqr.eu-west-1.rds.amazonaws.com    Database: mx1at09io7233q3w
-- ------------------------------------------------------
-- Server version	8.0.33

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `car_orders`
--

DROP TABLE IF EXISTS `car_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `car_manufacturer` varchar(255) DEFAULT NULL,
  `car_model` varchar(255) DEFAULT NULL,
  `car_year` int NOT NULL,
  `date_of_order` date DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo19d2pbayr35pd68rvhb18v0w` (`user_id`),
  CONSTRAINT `FKo19d2pbayr35pd68rvhb18v0w` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_orders`
--

LOCK TABLES `car_orders` WRITE;
/*!40000 ALTER TABLE `car_orders` DISABLE KEYS */;
INSERT INTO `car_orders` VALUES (1,'Nissan','GT-R',2017,'2024-01-27','Pending',11);
/*!40000 ALTER TABLE `car_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite_vehicles`
--

DROP TABLE IF EXISTS `favorite_vehicles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite_vehicles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `vehicle_id` varchar(255) DEFAULT NULL,
  `vehicle_img` varchar(255) DEFAULT NULL,
  `vehicle_name` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4omevsc8kvw9x8gjoihlrbp49` (`user_id`),
  CONSTRAINT `FK4omevsc8kvw9x8gjoihlrbp49` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite_vehicles`
--

LOCK TABLES `favorite_vehicles` WRITE;
/*!40000 ALTER TABLE `favorite_vehicles` DISABLE KEYS */;
INSERT INTO `favorite_vehicles` VALUES (4,'https://danov-autoshow-656625355b99.herokuapp.com/showroom.html?car=3D%20Models/Nissan-GT-R-2017.glb','images/Nissan-GT-R-2017.png','2017 NISSAN GT-R',11);
/*!40000 ALTER TABLE `favorite_vehicles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newsletter_emails`
--

DROP TABLE IF EXISTS `newsletter_emails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `newsletter_emails` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `date_of_last_email_sended` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8obiroughgkvl2u9jj5u6xqsg` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newsletter_emails`
--

LOCK TABLES `newsletter_emails` WRITE;
/*!40000 ALTER TABLE `newsletter_emails` DISABLE KEYS */;
INSERT INTO `newsletter_emails` VALUES (6,'vortexlud@gmail.com','2024-01-19 16:31:00.127950'),(21,'denisdanov64@gmail.com','2024-01-21 12:00:00.193802');
/*!40000 ALTER TABLE `newsletter_emails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_f90ivichjaokvmovxpnlm5nin` (`user_id`),
  CONSTRAINT `FK5lwtbncug84d4ero33v3cfxvl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recently_viewed_tokens`
--

DROP TABLE IF EXISTS `recently_viewed_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recently_viewed_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expire_date` varchar(255) DEFAULT NULL,
  `recently_viewed_cars` text,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qq82l7o33xr2l8wu0xlpof1rl` (`user_id`),
  CONSTRAINT `FK4n05afo2t8i3ha00wq2ptaykk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recently_viewed_tokens`
--

LOCK TABLES `recently_viewed_tokens` WRITE;
/*!40000 ALTER TABLE `recently_viewed_tokens` DISABLE KEYS */;
INSERT INTO `recently_viewed_tokens` VALUES (5,'2024-02-26 16:00:19','3D Models/Lamborghini-Urus-2020.glb,3D Models/Lamborghini-Gallardo-2007.glb,3D Models/Toyota-Gr-Supra-2020.glb,3D Models/Porsche-Boxster-2016.glb,3D Models/Porsche-Carrera-2015.glb,3D Models/Lamborghini-Aventador-2020.glb,3D Models/Lamborghini-Urus-2020.glb,3D Models/Mclaren-P1-2015.glb,3D Models/BMW-X5.glb,3D Models/Nissan-GT-R-2017.glb,3D Models/Jeep-Grand Cherokee SRT-2017.glb',11);
/*!40000 ALTER TABLE `recently_viewed_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_sequence`
--

DROP TABLE IF EXISTS `user_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sequence`
--

LOCK TABLES `user_sequence` WRITE;
/*!40000 ALTER TABLE `user_sequence` DISABLE KEYS */;
INSERT INTO `user_sequence` VALUES (22);
/*!40000 ALTER TABLE `user_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(255) DEFAULT NULL,
  `id` bigint NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('Denko',11,'denko123','deniswe@abv.bg'),('Deus Proelium',12,'denkobro2313','denis35@abv.bg'),('Deni69',13,'deni69','DenisDanov@students.softuni.bg'),('Deni342',14,'deni342','Deni342@abv.bg'),('Deni693',15,'deni69','denkowe@abv.bg'),('DeniBro',17,'deni342','Denibro@abv.bg'),('DenkoPiqn',20,'denkojgdiffy','vortexlud@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-28 12:00:59
