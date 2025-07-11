-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: nurse-track-back
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `is_active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `location` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_j6cwks7xecs5jov19ro8ge3qk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `is_read` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` tinytext COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('SHIFT_CHANGE','VACATION_REQUEST','GENERAL','SYSTEM','EMERGENCY') COLLATE utf8mb4_unicode_ci DEFAULT 'GENERAL',
  PRIMARY KEY (`id`),
  KEY `idx_notifications_user` (`user_id`,`is_read`),
  KEY `idx_notifications_type` (`type`),
  CONSTRAINT `FK9y21adhxn0ayjhfocscqox7bh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nurses_departments`
--

DROP TABLE IF EXISTS `nurses_departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurses_departments` (
  `assigned_at` datetime(6) DEFAULT NULL,
  `department_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nurse_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_nurses_departments` (`department_id`,`nurse_id`),
  KEY `FK66nehir5xhyhevc44mgs0iduj` (`nurse_id`),
  CONSTRAINT `FK66nehir5xhyhevc44mgs0iduj` FOREIGN KEY (`nurse_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKgrxakxbx98llgrvfaddkp0hyy` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shift_change_requests`
--

DROP TABLE IF EXISTS `shift_change_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift_change_requests` (
  `is_interchange` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `desired_shift_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `offered_shift_id` bigint NOT NULL,
  `recipient_id` bigint NOT NULL,
  `requester_id` bigint NOT NULL,
  `reviewed_at` datetime(6) DEFAULT NULL,
  `reviewed_by_id` bigint DEFAULT NULL,
  `reason` tinytext COLLATE utf8mb4_unicode_ci,
  `reviewed_notes` tinytext COLLATE utf8mb4_unicode_ci,
  `status` enum('CANCELLED','PENDING','APPROVED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  KEY `idx_shift_change_requester` (`requester_id`,`status`),
  KEY `idx_shift_change_timestamps` (`created_at`,`reviewed_at`),
  KEY `FKn2ox6meccjqnccyxgomamimpw` (`desired_shift_id`),
  KEY `FK2r5gqscow07vuriyf3w4agifl` (`offered_shift_id`),
  KEY `FKditvskppmv6l1trh86iupxcn1` (`recipient_id`),
  KEY `FK1ev2kcfvbtquvsw10uoy8uebo` (`reviewed_by_id`),
  CONSTRAINT `FK1ev2kcfvbtquvsw10uoy8uebo` FOREIGN KEY (`reviewed_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK2r5gqscow07vuriyf3w4agifl` FOREIGN KEY (`offered_shift_id`) REFERENCES `shifts` (`id`),
  CONSTRAINT `FKditvskppmv6l1trh86iupxcn1` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKn2ox6meccjqnccyxgomamimpw` FOREIGN KEY (`desired_shift_id`) REFERENCES `shifts` (`id`),
  CONSTRAINT `FKnrca4n8ia72sfdw5guo6xvi7o` FOREIGN KEY (`requester_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shift_templates`
--

DROP TABLE IF EXISTS `shift_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift_templates` (
  `end_time` time(6) NOT NULL,
  `start_time` time(6) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('MORNING','AFTERNOON','NIGHT','HALF_MORNING','HALF_NIGHT') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shifts`
--

DROP TABLE IF EXISTS `shifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shifts` (
  `shift_date` date NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by_id` bigint NOT NULL,
  `department_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nurse_id` bigint NOT NULL,
  `shift_template_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `notes` tinytext COLLATE utf8mb4_unicode_ci,
  `status` enum('SCHEDULED','COMPLETED','CANCELLED','SWAPPED') COLLATE utf8mb4_unicode_ci DEFAULT 'SCHEDULED',
  PRIMARY KEY (`id`),
  KEY `idx_shifts_coverage` (`department_id`,`shift_date`,`status`),
  KEY `idx_shifts_nurse_date` (`nurse_id`,`shift_date`),
  KEY `FKc2iesw0dm3e9hjy64a29ifjnr` (`created_by_id`),
  KEY `FKsfqlu3dq1pluyaqgclodl4rhe` (`shift_template_id`),
  CONSTRAINT `FKc2iesw0dm3e9hjy64a29ifjnr` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfalfj5kldqkp1mol31gubssrq` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `FKjk3htlj202yjo7jr3ab4km1nb` FOREIGN KEY (`nurse_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsfqlu3dq1pluyaqgclodl4rhe` FOREIGN KEY (`shift_template_id`) REFERENCES `shift_templates` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `supervisors_departments`
--

DROP TABLE IF EXISTS `supervisors_departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supervisors_departments` (
  `assigned_at` datetime(6) DEFAULT NULL,
  `department_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supervisor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc6f80jle47x1y2u6wwltwf4r8` (`department_id`),
  KEY `FKpxtjaqdlf16dphxlqr5kdbn6k` (`supervisor_id`),
  CONSTRAINT `FK1wea54ev14vsrpiion0lmyoni` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `FKpxtjaqdlf16dphxlqr5kdbn6k` FOREIGN KEY (`supervisor_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `is_active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `firstname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `license_number` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','SUPERVISOR','NURSE') COLLATE utf8mb4_unicode_ci DEFAULT 'NURSE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK_ku9tp2fuk9n37gfoq27j02ji3` (`license_number`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vacation_requests`
--

DROP TABLE IF EXISTS `vacation_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vacation_requests` (
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `requester_id` bigint NOT NULL,
  `reviewed_at` datetime(6) DEFAULT NULL,
  `reviewed_by_id` bigint DEFAULT NULL,
  `reason` tinytext COLLATE utf8mb4_unicode_ci,
  `reviewed_notes` tinytext COLLATE utf8mb4_unicode_ci,
  `status` enum('CANCELLED','PENDING','APPROVED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm9ra3og74270toljcyqxer6eq` (`requester_id`,`start_date`,`end_date`),
  KEY `idx_vacation_dates` (`start_date`,`end_date`),
  KEY `idx_vacation_requester_status` (`requester_id`,`status`),
  KEY `FKbnpyvv0uf9nbr8f9fqnlnp2dk` (`reviewed_by_id`),
  CONSTRAINT `FK2o8nh18jrsptt27xu9l9ovbco` FOREIGN KEY (`requester_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKbnpyvv0uf9nbr8f9fqnlnp2dk` FOREIGN KEY (`reviewed_by_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 16:06:05
