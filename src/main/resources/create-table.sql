-- Schema for rate-limit DB
-- Auto-created if spring.jpa.hibernate.ddl-auto=update, but keep this file for manual setup / interview demo
-- Run: mysql -u root -p < create-table.sql  OR  source create-table.sql inside mysql

CREATE DATABASE IF NOT EXISTS rate_limit_db;
USE rate_limit_db;

CREATE TABLE IF NOT EXISTS `user_rate` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `request_limit` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed data for demo: user1 = 5 req/min (used in curl/JMeter proof), user2 = 10 req/min
INSERT INTO user_rate (name, request_limit) VALUES ('user1', 5) ON DUPLICATE KEY UPDATE request_limit=VALUES(request_limit);
INSERT INTO user_rate (name, request_limit) VALUES ('user2', 10) ON DUPLICATE KEY UPDATE request_limit=VALUES(request_limit);
INSERT INTO user_rate (name, request_limit) VALUES ('demo', 5) ON DUPLICATE KEY UPDATE request_limit=VALUES(request_limit);
