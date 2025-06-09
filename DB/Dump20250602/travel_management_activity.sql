-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: travel_management
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `activity_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `package_id` bigint DEFAULT NULL,
  `itinerary_id` bigint DEFAULT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `FKc174utpw0ltile1dqx7ka2ewd` (`package_id`),
  KEY `FKhvx3oujjdyivpostrag6mu5dj` (`itinerary_id`),
  CONSTRAINT `FKc174utpw0ltile1dqx7ka2ewd` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`),
  CONSTRAINT `FKhvx3oujjdyivpostrag6mu5dj` FOREIGN KEY (`itinerary_id`) REFERENCES `itinerary` (`itinerary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (1,'Boat Party with Water Sport where you will explore wildness','Goa','Boat Party with Water Sport',600,NULL,NULL),(2,'Adventure awaits','Goa','North Goa sightseeing',700,NULL,NULL),(3,'Cruise to Mandovi and Zuari backwater Cruise','Goa','Mandovi and Zuari backwater Cruise',1200,NULL,NULL),(4,'Explore the greatness of Rock Beach','Pondicherry','Explore Rock Beach',400,NULL,NULL),(5,'Explore the memorials of the french','Pondicherry','French War Memorial',500,NULL,NULL),(6,'Explore the freshness','Pondicherry','Visit to Auroville',600,NULL,NULL),(7,'Explore the Scandal point and have fun','Manali','Visit to Scandal Point ',500,NULL,NULL),(8,'Feel the breeze in the valley','Manali','Solang Valley',500,NULL,NULL),(9,'Emerge in the beauty of the Abbi falls','Coorg','Abbi Falls',600,NULL,2),(10,'Fly Fly Fly through the mountains..........','Coorg','Zip lining ',400,NULL,2);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-02 12:27:19
