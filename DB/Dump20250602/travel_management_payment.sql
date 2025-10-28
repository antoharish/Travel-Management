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
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `amount` bigint NOT NULL,
  `booking_id` bigint DEFAULT NULL,
  `payment_method` varchar(255) NOT NULL,
  `payment_type` enum('FLIGHT','HOTEL','ITINERARY') NOT NULL,
  `session_id` varchar(255) NOT NULL,
  `session_url` varchar(2048) NOT NULL,
  `status` varchar(255) NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `FKmi2669nkjesvp7cd257fptl6f` (`user_id`),
  CONSTRAINT `FKmi2669nkjesvp7cd257fptl6f` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,590000,1,'Stripe','HOTEL','cs_test_a1g9iJYFOKCqRvoYcoQAoZ3HVYmMFPNAidX7vyKCjvQjT5XQmN56ypiLQj','https://checkout.stripe.com/c/pay/cs_test_a1g9iJYFOKCqRvoYcoQAoZ3HVYmMFPNAidX7vyKCjvQjT5XQmN56ypiLQj#fidkdWxOYHwnPyd1blpxYHZxWjA0V0BpdGo3U3Vzc1VnaW9IXFJucDRRXXU3XEhhbkBOMHU9ZmgxSlBSQ1A9V31uUlFIMEtvQ2ByNjFiV1AwdWlDYn1XQUhcTzZIMDdvSFNLbnZTPHNmUWRiNTVsdEdXXzZtcycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl','Pending',2),(2,424800,1,'Stripe','ITINERARY','cs_test_a1LHETDpLMtuUuk6H99DqAOHhxgZLKVH8yHbkdQQus9ZfwstaaVQQrzdz0','https://checkout.stripe.com/c/pay/cs_test_a1LHETDpLMtuUuk6H99DqAOHhxgZLKVH8yHbkdQQus9ZfwstaaVQQrzdz0#fidkdWxOYHwnPyd1blpxYHZxWjA0V0BpdGo3U3Vzc1VnaW9IXFJucDRRXXU3XEhhbkBOMHU9ZmgxSlBSQ1A9V31uUlFIMEtvQ2ByNjFiV1AwdWlDYn1XQUhcTzZIMDdvSFNLbnZTPHNmUWRiNTVsdEdXXzZtcycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl','Pending',2),(3,354000,2,'Stripe','ITINERARY','cs_test_a1sM7CBnf6VFBO9mp6NTKM9xwUj8kIXWoUkf26ik4KyhZgpk59Qxs4OJym','https://checkout.stripe.com/c/pay/cs_test_a1sM7CBnf6VFBO9mp6NTKM9xwUj8kIXWoUkf26ik4KyhZgpk59Qxs4OJym#fidkdWxOYHwnPyd1blpxYHZxWjA0V0BpdGo3U3Vzc1VnaW9IXFJucDRRXXU3XEhhbkBOMHU9ZmgxSlBSQ1A9V31uUlFIMEtvQ2ByNjFiV1AwdWlDYn1XQUhcTzZIMDdvSFNLbnZTPHNmUWRiNTVsdEdXXzZtcycpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl','Pending',2);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-02 12:27:26
