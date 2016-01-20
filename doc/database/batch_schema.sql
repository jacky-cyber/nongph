/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET character_set_client = utf8 */;

CREATE DATABASE IF NOT EXISTS ph_batch
  DEFAULT CHARSET utf8
  COLLATE utf8_general_ci;
USE ph_batch;

DROP TABLE IF EXISTS `ph_job_definition`;
CREATE TABLE `ph_job_definition` (
  `id`     VARCHAR(45)  NOT NULL,
  `name`   VARCHAR(45)  NOT NULL,
  `group`  VARCHAR(255) NOT NULL,
  `type`   VARCHAR(45)  NOT NULL,
  `status` VARCHAR(45)  NOT NULL,
  `cron`   VARCHAR(60)  NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;