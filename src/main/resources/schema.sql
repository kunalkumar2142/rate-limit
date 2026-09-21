-- Duplicate of create-table.sql for Spring Boot schema.sql auto-init support (if ddl-auto is switched to validate/none)
CREATE TABLE IF NOT EXISTS `user_rate` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    `request_limit` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
