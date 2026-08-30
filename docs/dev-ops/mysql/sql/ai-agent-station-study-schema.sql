# ************************************************************
# Sequel Ace SQL dump
# 鐗堟湰鍙凤細 20094
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 涓绘満: 127.0.0.1 (MySQL 8.0.42)
# 鏁版嵁搴? ai-agent-station-study
# 鐢熸垚鏃堕棿: 2025-10-04 23:24:31 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE database if NOT EXISTS `ai-agent-station-study` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `ai-agent-station-study`;

# 杞偍琛?admin_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `admin_user`;

CREATE TABLE `admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(128) NOT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_agent
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_agent`;

CREATE TABLE `ai_agent` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(64) NOT NULL,
  `agent_name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `channel` varchar(32) DEFAULT NULL,
  `strategy` varchar(64) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_id` (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_agent_draw_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_agent_draw_config`;

CREATE TABLE `ai_agent_draw_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_id` varchar(64) NOT NULL,
  `config_name` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `agent_id` varchar(64) DEFAULT NULL,
  `config_data` longtext NOT NULL,
  `version` int DEFAULT '1',
  `status` tinyint(1) DEFAULT '1',
  `create_by` varchar(64) DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_id` (`config_id`),
  KEY `idx_agent_id` (`agent_id`),
  KEY `idx_config_name` (`config_name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_agent_flow_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_agent_flow_config`;

CREATE TABLE `ai_agent_flow_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(64) NOT NULL,
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `client_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `client_type` varchar(64) DEFAULT NULL,
  `sequence` int NOT NULL,
  `step_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_client_seq` (`agent_id`,`client_id`,`sequence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_agent_task_schedule
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_agent_task_schedule`;

CREATE TABLE `ai_agent_task_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `agent_id` bigint NOT NULL,
  `task_name` varchar(64) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `cron_expression` varchar(50) NOT NULL,
  `task_param` text,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_agent_id` (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client`;

CREATE TABLE `ai_client` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` varchar(64) NOT NULL,
  `client_name` varchar(50) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_advisor
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_advisor`;

CREATE TABLE `ai_client_advisor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `advisor_id` varchar(64) NOT NULL,
  `advisor_name` varchar(50) NOT NULL,
  `advisor_type` varchar(50) NOT NULL,
  `order_num` int DEFAULT '0',
  `ext_param` varchar(2048) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_advisor_id` (`advisor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_api
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_api`;

CREATE TABLE `ai_client_api` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_id` varchar(64) NOT NULL,
  `base_url` varchar(255) NOT NULL,
  `api_key` varchar(255) NOT NULL,
  `completions_path` varchar(255) NOT NULL,
  `embeddings_path` varchar(255) NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_api_id` (`api_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_config`;

CREATE TABLE `ai_client_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_type` varchar(32) NOT NULL,
  `source_id` varchar(64) NOT NULL,
  `target_type` varchar(32) NOT NULL,
  `target_id` varchar(64) NOT NULL,
  `ext_param` varchar(1024) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_source_id` (`source_id`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_model
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_model`;

CREATE TABLE `ai_client_model` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `model_id` varchar(64) NOT NULL,
  `api_id` varchar(64) NOT NULL,
  `model_usage` varchar(128) NOT NULL DEFAULT '',
  `model_name` varchar(64) NOT NULL,
  `model_type` varchar(32) NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_model_id` (`model_id`),
  KEY `idx_api_config_id` (`api_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_rag_order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_rag_order`;

CREATE TABLE `ai_client_rag_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rag_id` varchar(50) NOT NULL,
  `rag_name` varchar(50) NOT NULL,
  `knowledge_tag` varchar(50) NOT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rag_id` (`rag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_system_prompt
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_system_prompt`;

CREATE TABLE `ai_client_system_prompt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `prompt_id` varchar(64) NOT NULL,
  `prompt_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prompt_content` text NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prompt_id` (`prompt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



# 杞偍琛?ai_client_tool_mcp
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ai_client_tool_mcp`;

CREATE TABLE `ai_client_tool_mcp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mcp_id` varchar(64) NOT NULL,
  `mcp_name` varchar(50) NOT NULL,
  `transport_type` varchar(20) NOT NULL,
  `transport_config` varchar(1024) DEFAULT NULL,
  `request_timeout` int DEFAULT '180',
  `status` tinyint(1) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mcp_id` (`mcp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


USE `ai-agent-station-study`;
USE `ai-agent-station-study`;
INSERT INTO `admin_user` (`id`, `user_id`, `username`, `password`, `status`) VALUES (1, '10001', 'admin', '123456', 1);
