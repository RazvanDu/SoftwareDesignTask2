-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sdassigment2
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.14-MariaDB

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
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `restaurantID` int(11) DEFAULT NULL,
  UNIQUE KEY `food_id_uindex` (`id`),
  KEY `food_restaurant_id_fk` (`restaurantID`),
  CONSTRAINT `food_restaurant_id_fk` FOREIGN KEY (`restaurantID`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (1,'banana','just a banana',10,0,1),(2,'smoothie','not more than a smoothie',100,0,2),(3,'pizza','idk...',1000,1,3),(4,'soup','...',10000,0,4),(5,'beef','.',100000,1,5),(6,'sadf','123',23,0,1),(7,'sda','123',31,1,1),(8,'fsa','1231',12,0,3),(9,'dfg','23',123,1,4),(10,'ads','123',12312,1,5),(11,'some','sadf',412,0,1),(12,'thing','sadf',12,0,2),(13,'nokt','gas',31,1,3),(14,'know','dfasd',2314,1,4),(53,'testing','hehee',123,0,4),(54,'testing2','hehee2',121,0,4),(55,'testing3','hehee3',412,1,4),(57,'Hello','Testing',123,0,56),(58,'Testing','heheeehe',512,0,56),(59,'Some New Food','Cool cool',412,1,56);
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (62);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderr`
--

DROP TABLE IF EXISTS `orderr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderr` (
  `id` int(11) NOT NULL,
  `restaurantID` int(11) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `foods_ordered` varchar(255) DEFAULT NULL,
  `status_order` int(11) DEFAULT NULL,
  `restaurant_name` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  UNIQUE KEY `Order_id_uindex` (`id`),
  KEY `order_restaurant_id_fk` (`restaurantID`),
  KEY `order_user_id_fk` (`userID`),
  CONSTRAINT `order_restaurant_id_fk` FOREIGN KEY (`RestaurantID`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `order_user_id_fk` FOREIGN KEY (`UserID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderr`
--

LOCK TABLES `orderr` WRITE;
/*!40000 ALTER TABLE `orderr` DISABLE KEYS */;
INSERT INTO `orderr` VALUES (1,2,1,'banana, smoothie',4,NULL,NULL),(2,4,1,'banana, smoothie',5,NULL,NULL),(3,4,1,'banana, smoothie',4,NULL,NULL),(4,2,1,'banana, smoothie',4,NULL,NULL),(5,4,1,'banana, smoothie',5,NULL,NULL),(6,2,1,'banana, smoothie',5,NULL,NULL),(7,4,1,'banana, smoothie',5,NULL,NULL),(8,5,1,'banana, smoothie',5,NULL,NULL),(50,2,1,'banana, things',5,NULL,NULL),(51,3,1,'banana, soup',4,NULL,NULL),(60,4,43,'soup, dfg',4,NULL,NULL),(61,2,1,'smoothie, smoothie, thing',4,NULL,NULL);
/*!40000 ALTER TABLE `orderr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `delivery` varchar(255) DEFAULT NULL,
  `adminID` int(11) NOT NULL,
  UNIQUE KEY `restaurant_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'Best Restaurant','Los Angeles','CA',18),(2,'My Ceva','Sibiu','Hipodrom',15),(3,'Some Idk','Cluj','Gheorgheni, Centru',16),(4,'Software Hello','Cluj','Manastur, Buna Ziua',14),(5,'Development Thingamabob','Bucharest','Centru',17),(56,'Coolest Restaurant','Beverly Hills','Some Area, Another Area',52);
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `hash` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `new_admin` int(11) DEFAULT NULL,
  `newAdmin` int(11) DEFAULT NULL,
  UNIQUE KEY `user_id_uindex` (`id`),
  UNIQUE KEY `user_email_uindex` (`email`),
  UNIQUE KEY `user_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'uhu','202cb962ac59075b964b07152d234b70',0,'huu@gmail.com',NULL,NULL),(2,'testingg','6BC432B68AA99743E57BF6C7E0B773BD',0,'test@gmail.com',NULL,NULL),(3,'test','49A5A960C5714C2E29DD1A7E7B950741',0,'testtttt@gmail.com',NULL,NULL),(6,'hehe','A5037C5C90E0E261E769CC12D4013162',0,'asd@gmail.com',NULL,NULL),(7,'razvan','202cb962ac59075b964b07152d234b70',1,'razvandumm@gmail.com',NULL,NULL),(8,'ratza','B97C72702F44C66C2DE2ECDFE321705F',0,'ratza@gmail.com',NULL,NULL),(13,'hello','49A5A960C5714C2E29DD1A7E7B950741',0,'hello@gmail.com',NULL,NULL),(14,'admin1','202cb962ac59075b964b07152d234b70',1,'admin1@gmail.com',NULL,NULL),(15,'admin2','202cb962ac59075b964b07152d234b70',1,'admin2@gmail.com',NULL,NULL),(16,'admin3','202cb962ac59075b964b07152d234b70',1,'admin3@gmail.com',NULL,NULL),(17,'admin4','202cb962ac59075b964b07152d234b70',1,'admin4@gmail.com',NULL,NULL),(18,'admin5','202cb962ac59075b964b07152d234b70',1,'admin5@gmail.com',NULL,NULL),(42,'gabriel','7363a0d0604902af7b70b271a0b96480',0,'gabriel@gmail.com',NULL,NULL),(43,'uhu2','202cb962ac59075b964b07152d234b70',0,'uhu2@gmail.com',NULL,NULL),(52,'admin6','202cb962ac59075b964b07152d234b70',1,'admin6@gmail.com',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-06  5:06:36
