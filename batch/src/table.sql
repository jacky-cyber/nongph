CREATE SCHEMA PH_BATCH;

GRANT ALL PRIVILEGES ON PH_BATCH.* TO ph_dev@'%'
IDENTIFIED BY 'ph654321';
FLUSH PRIVILEGES;

USE ph_batch;
DROP TABLE `PH_JOB_DEFINITION`;
CREATE TABLE `PH_JOB_DEFINITION` (
  `id`   VARCHAR(60) NOT NULL,
  `name` VARCHAR(60) NOT NULL,
  `group`  VARCHAR(255) NOT NULL,
  `type`   VARCHAR(45)  NOT NULL,
  `status` VARCHAR(45)  NOT NULL,
  `cron`   VARCHAR(60)  NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = innoDB
  DEFAULT CHARSET = utf8;

INSERT INTO PH_JOB_DEFINITION (`id`, `name`, `group`, `type`, `status`, `cron`)
VALUES ('SendDeliveryListJob', 'SendDeliveryListJob', 'delivery', 'S', 'A', '0 0/1 * * * ?');
INSERT INTO PH_JOB_DEFINITION (`id`, `name`, `group`, `type`, `status`, `cron`)
VALUES ('CompleteOrderJob', 'CompleteOrderJob', 'order', 'S', 'A', '0 0 0/1 * * ?');
