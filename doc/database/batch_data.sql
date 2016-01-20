/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET character_set_client = utf8 */;

DELETE FROM `ph_batch`.`ph_job_definition`;
INSERT INTO `ph_batch`.`ph_job_definition` (`id`, `name`, `group`, `type`, `status`, `cron`) VALUES
  ('CancelOrderJob', 'CancelOrderJob', 'order', 'S', 'A', '0 0/5 * * * ?'),
  ('CompleteOrderJob', 'CompleteOrderJob', 'order', 'S', 'A', '0 0 0/1 * * ?'),
  ('SendDeliveryInfoJob', 'SendDeliveryInfoJob', 'delivery', 'S', 'A', '0 10 0,12 * * ?'),
  ('TrackingJob', 'TrackingJob', 'delivery', 'S', 'A', '0 0/30 * * * ?'),
  ('ShippingJob', 'ShippingJob', 'delivery', 'S', 'A', '0 10 0,12 * * ?'),
  ('DetermineDeliveryTypeJob', 'DetermineDeliveryTypeJob', 'delivery', 'S', 'A', '0 0 0,12 * * ?');

/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;