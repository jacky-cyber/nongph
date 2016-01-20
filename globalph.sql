CREATE DATABASE  IF NOT EXISTS `globalph` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `globalph`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host: localhost    Database: globalph
-- ------------------------------------------------------
-- Server version	5.7.3-m13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_module`
--

DROP TABLE IF EXISTS `admin_module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_module` (
  `admin_module_id` bigint(20) NOT NULL,
  `display_order` int(11) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `module_key` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_module_id`),
  KEY `adminmodule_name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_module`
--

LOCK TABLES `admin_module` WRITE;
/*!40000 ALTER TABLE `admin_module` DISABLE KEYS */;
INSERT INTO `admin_module` VALUES (-7,500,'icon-refresh','BLCWorkflow','Site Updates'),(-6,400,'icon-picture','BLCDesign','Design'),(-5,700,'icon-gear','BLCModuleConfiguration','Settings'),(-4,600,'icon-user','BLCOpenAdmin','Security'),(-3,300,'icon-heart','BLCCustomerCare','Customer Care'),(-2,200,'icon-file','BLCContentManagement','Content'),(-1,100,'icon-barcode','BLCMerchandising','Catalog'),(1,800,'icon-user','Net Node Management','Net Node'),(2,600,'icon-user','Provider','Provider');
/*!40000 ALTER TABLE `admin_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_password_token`
--

DROP TABLE IF EXISTS `admin_password_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_password_token` (
  `password_token` varchar(255) NOT NULL,
  `admin_user_id` bigint(20) NOT NULL,
  `create_date` datetime NOT NULL,
  `token_used_date` datetime DEFAULT NULL,
  `token_used_flag` tinyint(1) NOT NULL,
  PRIMARY KEY (`password_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_password_token`
--

LOCK TABLES `admin_password_token` WRITE;
/*!40000 ALTER TABLE `admin_password_token` DISABLE KEYS */;
INSERT INTO `admin_password_token` VALUES ('637f34315aa7fa04b106228e016ca701bbb5f79e',-1,'2015-11-23 20:23:01',NULL,1),('dbcef1b53e1128a75b0cd5b6e7009bdbc5d0d5fb',-1,'2015-11-23 20:17:26',NULL,0),('f951c7e682ccc55b76142e330a505bba2acb6146',-1,'2015-11-23 20:09:42',NULL,0);
/*!40000 ALTER TABLE `admin_password_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_permission`
--

DROP TABLE IF EXISTS `admin_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_permission` (
  `admin_permission_id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `is_friendly` tinyint(1) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `permission_type` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_permission`
--

LOCK TABLES `admin_permission` WRITE;
/*!40000 ALTER TABLE `admin_permission` DISABLE KEYS */;
INSERT INTO `admin_permission` VALUES (-219,'All Groupon',0,'PERMISSION_ALL_GROUPON','ALL'),(-218,'Read Groupon',0,'PERMISSION_READ_GROUPON','READ'),(-217,'All Customer Message',0,'PERMISSION_ALL_CUSTOMER_MESSAGE','ALL'),(-216,'Read Customer Message',0,'PERMISSION_READ_CUSTOMER_MESSAGE','READ'),(-215,'All Compare Price',0,'PERMISSION_ALL_COMPARE_PRICE','ALL'),(-214,'Read Compare Price',0,'PERMISSION_READ_COMPARE_PRICE','READ'),(-213,'All Coupon',0,'PERMISSION_ALL_COUPON','ALL'),(-212,'Read Coupon',0,'PERMISSION_READ_COUPON','READ'),(-211,'All Refund',0,'PERMISSION_ALL_REFUND','ALL'),(-210,'Read Refund',0,'PERMISSION_READ_REFUND','READ'),(-209,'All Provider',0,'PERMISSION_ALL_PROVIDER','ALL'),(-208,'Read Provider',0,'PERMISSION_READ_PROVIDER','READ'),(-207,'All Order Log',0,'PERMISSION_ALL_ORDER_LOG','ALL'),(-206,'Read Order Log',0,'PERMISSION_READ_ORDER_LOG','READ'),(-205,'Read Provider',0,'PERMISSION_READ_PROVIDER','READ'),(-204,'Maintenance Provider',0,'PERMISSION_ALL_PROVIDER','ALL'),(-203,'Read Order Address',0,'PERMISSION_READ_ORDER_ADDRESS','READ'),(-202,'Maintenance Order Address',0,'PERMISSION_ALL_ORDER_ADDRESS','ALL'),(-201,'Read Structured Content Template',0,'PERMISSION_READ_STRUCTURED_CONTENT_TEMPLATE','READ'),(-200,'Read Page Template',0,'PERMISSION_READ_PAGE_TEMPLATE','READ'),(-151,'Maintain Permissions',1,'PERMISSION_PERM_ALL','ALL'),(-150,'View Permissions',1,'PERMISSION_PERM_VIEW','READ'),(-141,'Maintain Roles',1,'PERMISSION_ROLE_ALL','ALL'),(-140,'View Roles',1,'PERMISSION_ROLE_VIEW','READ'),(-131,'Maintain Translations',1,'PERMISSION_TRANSLATION','ALL'),(-130,'View Translations',1,'PERMISSION_TRANSLATION','READ'),(-127,'Maintain Module Configurations',1,'PERMISSION_MODULECONFIGURATION','ALL'),(-126,'View Module Configurations',1,'PERMISSION_MODULECONFIGURATION','READ'),(-123,'Maintain System Properties',1,'PERMISSION_SYSTEMPROPERTY','ALL'),(-122,'View System Properties',1,'PERMISSION_SYSTEMPROPERTY','READ'),(-121,'Maintain Users',1,'PERMISSION_USER','ALL'),(-120,'View Users',1,'PERMISSION_USER','READ'),(-119,'Maintain Customers',1,'PERMISSION_CUSTOMER','ALL'),(-118,'View Customers',1,'PERMISSION_CUSTOMER','READ'),(-117,'Maintain Orders',1,'PERMISSION_ORDER','ALL'),(-116,'View Orders',1,'PERMISSION_ORDER','READ'),(-115,'Maintain URL Redirects',1,'PERMISSION_URLREDIRECT','ALL'),(-114,'View URL Redirects',1,'PERMISSION_URLREDIRECT','READ'),(-113,'Maintain Structured Contents',1,'PERMISSION_STRUCTUREDCONTENT','ALL'),(-112,'View Structured Contents',1,'PERMISSION_STRUCTUREDCONTENT','READ'),(-111,'Maintain Assets',1,'PERMISSION_ASSET','ALL'),(-110,'View Assets',1,'PERMISSION_ASSET','READ'),(-109,'Maintain Pages',1,'PERMISSION_PAGE','ALL'),(-108,'View Pages',1,'PERMISSION_PAGE','READ'),(-107,'Maintain Offers',1,'PERMISSION_OFFER','ALL'),(-106,'View Offers',1,'PERMISSION_OFFER','READ'),(-105,'Maintain Product Options',1,'PERMISSION_PRODUCTOPTIONS','ALL'),(-104,'View Product Options',1,'PERMISSION_PRODUCTOPTIONS','READ'),(-103,'Maintain Products',1,'PERMISSION_PRODUCT','ALL'),(-102,'View Products',1,'PERMISSION_PRODUCT','READ'),(-101,'Maintain Categories',1,'PERMISSION_CATEGORY','ALL'),(-100,'View Categories',1,'PERMISSION_CATEGORY','READ'),(-49,'All Admin Permissions',0,'PERMISSION_ALL_ADMIN_PERMS','ALL'),(-48,'Read Admin Permissions',0,'PERMISSION_READ_ADMIN_PERMS','READ'),(-47,'All Admin Roles',0,'PERMISSION_ALL_ADMIN_ROLES','ALL'),(-46,'Read Admin Roles',0,'PERMISSION_READ_ADMIN_ROLES','READ'),(-45,'All System Property',0,'PERMISSION_ALL_SYSTEM_PROPERTY','ALL'),(-44,'Read System Property',0,'PERMISSION_READ_SYSTEM_PROPERTY','READ'),(-43,'All Site Map Gen Configuration',0,'PERMISSION_ALL_SITE_MAP_GEN_CONFIG','ALL'),(-42,'Read Site Map Gen Configuration',0,'PERMISSION_READ_SITE_MAP_GEN_CONFIG','READ'),(-41,'All Translation',0,'PERMISSION_ALL_TRANSLATION','ALL'),(-40,'Read Translation',0,'PERMISSION_READ_TRANSLATION','READ'),(-39,'All Enumeration',0,'PERMISSION_ALL_ENUMERATION','ALL'),(-38,'Read Enumeration',0,'PERMISSION_READ_ENUMERATION','READ'),(-37,'All Configuration',0,'PERMISSION_ALL_MODULECONFIGURATION','ALL'),(-36,'Read Configuration',0,'PERMISSION_READ_MODULECONFIGURATION','READ'),(-35,'All Currency',0,'PERMISSION_ALL_CURRENCY','ALL'),(-34,'Read Currency',0,'PERMISSION_READ_CURRENCY','READ'),(-33,'All SearchFacet',0,'PERMISSION_ALL_SEARCHFACET','ALL'),(-32,'Read SearchFacet',0,'PERMISSION_READ_SEARCHFACET','READ'),(-31,'All SearchRedirect',0,'PERMISSION_ALL_SEARCHREDIRECT','ALL'),(-30,'Read SearchRedirect',0,'PERMISSION_READ_SEARCHREDIRECT','READ'),(-29,'All URLHandler',0,'PERMISSION_ALL_URLHANDLER','ALL'),(-28,'Read URLHandler',0,'PERMISSION_READ_URLHANDLER','READ'),(-27,'All Admin User',0,'PERMISSION_ALL_ADMIN_USER','ALL'),(-26,'Read Admin User',0,'PERMISSION_READ_ADMIN_USER','READ'),(-25,'All Structured Content',0,'PERMISSION_ALL_STRUCTURED_CONTENT','ALL'),(-24,'Read Structured Content',0,'PERMISSION_READ_STRUCTURED_CONTENT','READ'),(-23,'All Asset',0,'PERMISSION_ALL_ASSET','ALL'),(-22,'Read Asset',0,'PERMISSION_READ_ASSET','READ'),(-21,'All Page',0,'PERMISSION_ALL_PAGE','ALL'),(-20,'Read Page',0,'PERMISSION_READ_PAGE','READ'),(-19,'All Customer',0,'PERMISSION_ALL_CUSTOMER','ALL'),(-18,'Read Customer',0,'PERMISSION_READ_CUSTOMER','READ'),(-17,'All Order Item',0,'PERMISSION_ALL_ORDER_ITEM','ALL'),(-16,'Read Order Item',0,'PERMISSION_READ_ORDER_ITEM','READ'),(-15,'All Fulfillment Group',0,'PERMISSION_ALL_FULFILLMENT_GROUP','ALL'),(-14,'Read Fulfillment Group',0,'PERMISSION_READ_FULFILLMENT_GROUP','READ'),(-13,'All Order',0,'PERMISSION_ALL_ORDER','ALL'),(-12,'Read Order',0,'PERMISSION_READ_ORDER','READ'),(-11,'All Promotion',0,'PERMISSION_ALL_PROMOTION','ALL'),(-10,'Read Promotion',0,'PERMISSION_READ_PROMOTION','READ'),(-9,'All Sku',0,'PERMISSION_ALL_SKU','ALL'),(-8,'Read Sku',0,'PERMISSION_READ_SKU','READ'),(-7,'All Product Option',0,'PERMISSION_ALL_PRODUCT_OPTION','ALL'),(-6,'Read Product Option',0,'PERMISSION_READ_PRODUCT_OPTION','READ'),(-5,'All Product',0,'PERMISSION_ALL_PRODUCT','ALL'),(-4,'Read Product',0,'PERMISSION_READ_PRODUCT','READ'),(-3,'All Category',0,'PERMISSION_ALL_CATEGORY','ALL'),(-2,'Read Category',0,'PERMISSION_READ_CATEGORY','READ'),(-1,'Default Permission',0,'PERMISSION_OTHER_DEFAULT','OTHER');
/*!40000 ALTER TABLE `admin_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_permission_entity`
--

DROP TABLE IF EXISTS `admin_permission_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_permission_entity` (
  `admin_permission_entity_id` bigint(20) NOT NULL,
  `ceiling_entity` varchar(255) NOT NULL,
  `admin_permission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`admin_permission_entity_id`),
  KEY `fk23c09e3de88b7d38` (`admin_permission_id`),
  CONSTRAINT `fk23c09e3de88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_permission_entity`
--

LOCK TABLES `admin_permission_entity` WRITE;
/*!40000 ALTER TABLE `admin_permission_entity` DISABLE KEYS */;
INSERT INTO `admin_permission_entity` VALUES (-1000,'cn.globalph.b2c.product.domain.GroupOn',-219),(-999,'cn.globalph.b2c.product.domain.GroupOn',-218),(-998,'cn.globalph.passport.domain.CustomerMessage',-217),(-997,'cn.globalph.passport.domain.CustomerMessage',-216),(-996,'cn.globalph.b2c.product.domain.ComparePrice',-215),(-995,'cn.globalph.b2c.product.domain.ComparePrice',-214),(-994,'cn.globalph.coupon.domain.Coupon',-213),(-993,'cn.globalph.coupon.domain.Coupon',-212),(-992,'cn.globalph.b2c.order.domain.Refund',-211),(-991,'cn.globalph.b2c.order.domain.Refund',-210),(-990,'cn.globalph.b2c.order.domain.OrderLog',-207),(-989,'cn.globalph.b2c.order.domain.OrderLog',-206),(-988,'cn.globalph.b2c.product.domain.Provider',-205),(-987,'cn.globalph.b2c.product.domain.Provider',-204),(-986,'cn.globalph.b2c.order.domain.OrderAddress',-203),(-985,'cn.globalph.b2c.order.domain.OrderAddress',-202),(-983,'cn.globalph.openadmin.server.security.domain.AdminPermissionQualifiedEntity',-48),(-982,'cn.globalph.openadmin.server.security.domain.AdminPermissionQualifiedEntity',-49),(-981,'cn.globalph.openadmin.server.security.domain.AdminPermission',-48),(-980,'cn.globalph.openadmin.server.security.domain.AdminPermission',-48),(-971,'cn.globalph.openadmin.server.security.domain.AdminRole',-47),(-970,'cn.globalph.openadmin.server.security.domain.AdminRole',-46),(-961,'cn.globalph.common.config.domain.SystemProperty',-45),(-960,'cn.globalph.common.config.domain.SystemProperty',-44),(-953,'cn.globalph.common.sitemap.domain.SiteMapURLEntry',-43),(-952,'cn.globalph.common.sitemap.domain.SiteMapURLEntry',-42),(-951,'cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration',-43),(-950,'cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration',-42),(-911,'cn.globalph.common.i18n.domain.Translation',-41),(-910,'cn.globalph.common.i18n.domain.Translation',-40),(-903,'cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue',-39),(-902,'cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue',-38),(-901,'cn.globalph.common.enumeration.domain.DataDrivenEnumeration',-39),(-900,'cn.globalph.common.enumeration.domain.DataDrivenEnumeration',-38),(-881,'cn.globalph.common.config.domain.ModuleConfiguration',-37),(-880,'cn.globalph.common.config.domain.ModuleConfiguration',-36),(-809,'cn.globalph.b2c.search.domain.CategoryExcludedSearchFacet',-33),(-808,'cn.globalph.b2c.search.domain.CategoryExcludedSearchFacet',-32),(-807,'cn.globalph.b2c.search.domain.SearchFacetRange',-33),(-806,'cn.globalph.b2c.search.domain.SearchFacetRange',-32),(-805,'cn.globalph.b2c.search.domain.CategorySearchFacet',-33),(-804,'cn.globalph.b2c.search.domain.CategorySearchFacet',-32),(-803,'cn.globalph.b2c.search.domain.Field',-33),(-802,'cn.globalph.b2c.search.domain.Field',-32),(-801,'cn.globalph.b2c.search.domain.SearchFacet',-33),(-800,'cn.globalph.b2c.search.domain.SearchFacet',-32),(-781,'cn.globalph.b2c.search.redirect.domain.SearchRedirect',-31),(-780,'cn.globalph.b2c.search.redirect.domain.SearchRedirect',-30),(-751,'cn.globalph.cms.url.domain.URLHandler',-29),(-750,'cn.globalph.cms.url.domain.URLHandler',-28),(-721,'cn.globalph.openadmin.server.security.domain.AdminUser',-27),(-720,'cn.globalph.openadmin.server.security.domain.AdminUser',-26),(-657,'cn.globalph.cms.structure.domain.StructuredContentFieldTemplate',-201),(-655,'cn.globalph.cms.structure.domain.StructuredContentItemCriteria',-25),(-654,'cn.globalph.cms.structure.domain.StructuredContentItemCriteria',-24),(-653,'cn.globalph.cms.structure.domain.StructuredContentType',-201),(-651,'cn.globalph.cms.structure.domain.StructuredContent',-25),(-650,'cn.globalph.cms.structure.domain.StructuredContent',-24),(-603,'cn.globalph.cms.file.domain.StaticAssetFolder',-23),(-602,'cn.globalph.cms.file.domain.StaticAssetFolder',-22),(-601,'cn.globalph.cms.file.domain.StaticAsset',-23),(-600,'cn.globalph.cms.file.domain.StaticAsset',-22),(-555,'cn.globalph.cms.page.domain.PageItemCriteria',-21),(-554,'cn.globalph.cms.page.domain.PageItemCriteria',-20),(-552,'cn.globalph.cms.page.domain.PageTemplate',-200),(-551,'cn.globalph.cms.page.domain.Page',-21),(-550,'cn.globalph.cms.page.domain.Page',-20),(-514,'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl',-19),(-513,'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl',-18),(-512,'cn.globalph.passport.domain.CustomerPhone',-19),(-511,'cn.globalph.passport.domain.CustomerPhone',-18),(-510,'cn.globalph.passport.domain.CustomerPayment',-19),(-509,'cn.globalph.passport.domain.CustomerPayment',-18),(-508,'cn.globalph.passport.domain.CustomerAddress',-19),(-507,'cn.globalph.passport.domain.CustomerAddress',-18),(-506,'cn.globalph.passport.domain.CustomerAttribute',-19),(-504,'cn.globalph.passport.domain.CustomerAttribute',-18),(-503,'cn.globalph.passport.domain.ChallengeQuestion',-19),(-502,'cn.globalph.passport.domain.ChallengeQuestion',-18),(-501,'cn.globalph.passport.domain.Customer',-19),(-500,'cn.globalph.passport.domain.Customer',-18),(-461,'cn.globalph.b2c.order.domain.BundleOrderItemFeePriceImpl',-17),(-460,'cn.globalph.b2c.order.domain.BundleOrderItemFeePriceImpl',-16),(-459,'cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl',-17),(-458,'cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl',-16),(-457,'cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustmentImpl',-17),(-456,'cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustmentImpl',-16),(-455,'cn.globalph.b2c.offer.domain.OrderItemAdjustment',-17),(-454,'cn.globalph.b2c.offer.domain.OrderItemAdjustment',-16),(-453,'cn.globalph.b2c.order.domain.DiscreteOrderItemFeePrice',-17),(-452,'cn.globalph.b2c.order.domain.DiscreteOrderItemFeePrice',-16),(-451,'cn.globalph.b2c.order.domain.OrderItem',-17),(-450,'cn.globalph.b2c.order.domain.OrderItem',-16),(-407,'cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl',-15),(-406,'cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl',-14),(-405,'cn.globalph.b2c.order.domain.FulfillmentGroupFeeImpl',-15),(-404,'cn.globalph.b2c.order.domain.FulfillmentGroupFeeImpl',-14),(-403,'cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment',-15),(-402,'cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment',-14),(-401,'cn.globalph.b2c.order.domain.FulfillmentGroup',-15),(-400,'cn.globalph.b2c.order.domain.FulfillmentGroup',-14),(-370,'cn.globalph.b2c.payment.domain.PaymentTransactionImpl',-13),(-369,'cn.globalph.b2c.payment.domain.PaymentTransactionImpl',-12),(-368,'org.broadleafcommerce.profile.core.domain.State',-13),(-367,'org.broadleafcommerce.profile.core.domain.State',-12),(-366,'cn.globalph.profile.core.domain.Country',-13),(-365,'cn.globalph.profile.core.domain.Country',-12),(-361,'cn.globalph.b2c.payment.domain.OrderPayment',-13),(-360,'cn.globalph.b2c.payment.domain.OrderPayment',-12),(-356,'cn.globalph.b2c.offer.domain.OrderAdjustment',-13),(-355,'cn.globalph.b2c.offer.domain.OrderAdjustment',-12),(-351,'cn.globalph.b2c.order.domain.Order',-13),(-350,'cn.globalph.b2c.order.domain.Order',-12),(-307,'cn.globalph.b2c.offer.domain.OfferTier',-11),(-306,'cn.globalph.b2c.offer.domain.OfferTier',-10),(-305,'cn.globalph.b2c.offer.domain.OfferCode',-11),(-304,'cn.globalph.b2c.offer.domain.OfferCode',-10),(-303,'cn.globalph.b2c.offer.domain.OfferItemCriteria',-11),(-302,'cn.globalph.b2c.offer.domain.OfferItemCriteria',-10),(-301,'cn.globalph.b2c.offer.domain.Offer',-11),(-300,'cn.globalph.b2c.offer.domain.Offer',-10),(-251,'cn.globalph.b2c.product.domain.Sku',-9),(-250,'cn.globalph.b2c.product.domain.Sku',-8),(-205,'cn.globalph.b2c.product.domain.ProductOptionXref',-7),(-204,'cn.globalph.b2c.product.domain.ProductOptionXref',-6),(-203,'cn.globalph.b2c.product.domain.ProductOptionValue',-7),(-202,'cn.globalph.b2c.product.domain.ProductOptionValue',-6),(-201,'cn.globalph.b2c.product.domain.ProductOption',-7),(-200,'cn.globalph.b2c.product.domain.ProductOption',-6),(-107,'cn.globalph.b2c.product.domain.SkuBundleItemImpl',-5),(-106,'cn.globalph.b2c.product.domain.SkuBundleItemImpl',-4),(-105,'cn.globalph.b2c.catalog.domain.UpSaleProductImpl',-5),(-104,'cn.globalph.b2c.catalog.domain.UpSaleProductImpl',-4),(-103,'cn.globalph.b2c.product.domain.ProductAttribute',-5),(-102,'cn.globalph.b2c.product.domain.ProductAttribute',-4),(-101,'cn.globalph.b2c.product.domain.Product',-5),(-100,'cn.globalph.b2c.product.domain.Product',-4),(-14,'cn.globalph.b2c.catalog.domain.UpSaleProductImpl',-3),(-13,'cn.globalph.b2c.catalog.domain.UpSaleProductImpl',-2),(-12,'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl',-3),(-11,'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl',-2),(-10,'cn.globalph.b2c.catalog.domain.FeaturedProductImpl',-3),(-9,'cn.globalph.b2c.catalog.domain.FeaturedProductImpl',-2),(-8,'cn.globalph.b2c.catalog.domain.CategoryXrefImpl',-3),(-7,'cn.globalph.b2c.catalog.domain.CategoryXrefImpl',-2),(-6,'cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl',-3),(-5,'cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl',-2),(-4,'cn.globalph.b2c.catalog.domain.CategoryAttribute',-3),(-3,'cn.globalph.b2c.catalog.domain.CategoryAttribute',-2),(-2,'cn.globalph.b2c.catalog.domain.Category',-3),(-1,'cn.globalph.b2c.catalog.domain.Category',-2);
/*!40000 ALTER TABLE `admin_permission_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_permission_xref`
--

DROP TABLE IF EXISTS `admin_permission_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_permission_xref` (
  `child_permission_id` bigint(20) NOT NULL,
  `admin_permission_id` bigint(20) NOT NULL,
  KEY `fkbcad1f5e88b7d38` (`admin_permission_id`),
  KEY `fkbcad1f575a3c445` (`child_permission_id`),
  CONSTRAINT `fkbcad1f575a3c445` FOREIGN KEY (`child_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`),
  CONSTRAINT `fkbcad1f5e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_permission_xref`
--

LOCK TABLES `admin_permission_xref` WRITE;
/*!40000 ALTER TABLE `admin_permission_xref` DISABLE KEYS */;
INSERT INTO `admin_permission_xref` VALUES (-2,-100),(-4,-100),(-32,-100),(-3,-101),(-4,-101),(-32,-101),(-4,-102),(-6,-102),(-8,-102),(-34,-102),(-5,-103),(-6,-103),(-9,-103),(-34,-103),(-6,-104),(-32,-104),(-7,-105),(-32,-105),(-10,-106),(-11,-107),(-20,-108),(-200,-108),(-21,-109),(-200,-109),(-22,-110),(-23,-111),(-24,-112),(-201,-112),(-25,-113),(-201,-113),(-28,-114),(-29,-115),(-12,-116),(-16,-116),(-14,-116),(-10,-116),(-13,-117),(-16,-117),(-14,-117),(-10,-117),(-18,-118),(-19,-119),(-26,-120),(-46,-120),(-48,-120),(-27,-121),(-46,-120),(-48,-120),(-44,-122),(-45,-123),(-36,-126),(-37,-127),(-40,-130),(-41,-131),(-46,-140),(-48,-140),(-47,-141),(-48,-141),(-48,-150),(-49,-151);
/*!40000 ALTER TABLE `admin_permission_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_role`
--

DROP TABLE IF EXISTS `admin_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_role` (
  `admin_role_id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role`
--

LOCK TABLES `admin_role` WRITE;
/*!40000 ALTER TABLE `admin_role` DISABLE KEYS */;
INSERT INTO `admin_role` VALUES (-7,'内容管理系统设计者','ROLE_CONTENT_DESIGNER'),(-6,'内容管理系统审批人','ROLE_CONTENT_APPROVER'),(-5,'内容管理系统编辑者','ROLE_CONTENT_EDITOR'),(-4,'用户和订单管理','ROLE_CUSTOMER_SERVICE_REP'),(-3,'促销管理员','ROLE_PROMOTION_MANAGER'),(-2,'商品管理员','ROLE_MERCHANDISE_MANAGER'),(-1,'超级管理员','ROLE_ADMIN');
/*!40000 ALTER TABLE `admin_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_role_permission_xref`
--

DROP TABLE IF EXISTS `admin_role_permission_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_role_permission_xref` (
  `admin_role_id` bigint(20) NOT NULL,
  `admin_permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`admin_permission_id`,`admin_role_id`),
  KEY `fk4a819d98e88b7d38` (`admin_permission_id`),
  KEY `fk4a819d985f43aad8` (`admin_role_id`),
  CONSTRAINT `fk4a819d985f43aad8` FOREIGN KEY (`admin_role_id`) REFERENCES `admin_role` (`admin_role_id`),
  CONSTRAINT `fk4a819d98e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_role_permission_xref`
--

LOCK TABLES `admin_role_permission_xref` WRITE;
/*!40000 ALTER TABLE `admin_role_permission_xref` DISABLE KEYS */;
INSERT INTO `admin_role_permission_xref` VALUES (-1,-219),(-1,-218),(-1,-217),(-1,-216),(-1,-215),(-1,-214),(-1,-213),(-1,-212),(-1,-211),(-1,-210),(-1,-207),(-1,-206),(-1,-205),(-1,-204),(-1,-203),(-1,-202),(-1,-141),(-5,-131),(-2,-131),(-1,-131),(-1,-127),(-1,-123),(-1,-121),(-4,-119),(-1,-119),(-4,-117),(-1,-117),(-1,-115),(-5,-113),(-1,-113),(-6,-111),(-5,-111),(-3,-111),(-2,-111),(-1,-111),(-6,-109),(-5,-109),(-1,-109),(-3,-107),(-1,-107),(-2,-105),(-1,-105),(-2,-103),(-1,-103),(-2,-101),(-1,-101);
/*!40000 ALTER TABLE `admin_role_permission_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_sandbox`
--

DROP TABLE IF EXISTS `admin_sandbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_sandbox` (
  `sandbox_id` bigint(20) NOT NULL,
  `author` bigint(20) DEFAULT NULL,
  `color` varchar(100) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `go_live_date` datetime DEFAULT NULL,
  `sandbox_name` varchar(100) DEFAULT NULL,
  `sandbox_type` varchar(100) DEFAULT NULL,
  `parent_sandbox_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sandbox_id`),
  KEY `sandbox_name_index` (`sandbox_name`),
  KEY `fkdd37a9a174160452` (`parent_sandbox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_sandbox`
--

LOCK TABLES `admin_sandbox` WRITE;
/*!40000 ALTER TABLE `admin_sandbox` DISABLE KEYS */;
INSERT INTO `admin_sandbox` VALUES (1,NULL,'#0B9098',NULL,NULL,'Default','DEFAULT',NULL),(2,-1,NULL,NULL,NULL,'Default','USER',1),(51,-2,NULL,NULL,NULL,'Default','USER',1),(52,-3,NULL,NULL,NULL,'Default','USER',1),(53,-4,NULL,NULL,NULL,'Default','USER',1),(54,-5,NULL,NULL,NULL,'Default','USER',1),(55,-6,NULL,NULL,NULL,'Default','USER',1);
/*!40000 ALTER TABLE `admin_sandbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_sandbox_mgmt`
--

DROP TABLE IF EXISTS `admin_sandbox_mgmt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_sandbox_mgmt` (
  `sandbox_mgmt_id` bigint(20) NOT NULL,
  `sandbox_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sandbox_mgmt_id`),
  UNIQUE KEY `uk_4845009fe52b6993` (`sandbox_id`),
  KEY `fk4845009f579fe59d` (`sandbox_id`),
  CONSTRAINT `fk4845009f579fe59d` FOREIGN KEY (`sandbox_id`) REFERENCES `admin_sandbox` (`sandbox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_sandbox_mgmt`
--

LOCK TABLES `admin_sandbox_mgmt` WRITE;
/*!40000 ALTER TABLE `admin_sandbox_mgmt` DISABLE KEYS */;
INSERT INTO `admin_sandbox_mgmt` VALUES (1,1),(2,2),(51,51),(52,52),(53,53),(54,54),(55,55);
/*!40000 ALTER TABLE `admin_sandbox_mgmt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_sec_perm_xref`
--

DROP TABLE IF EXISTS `admin_sec_perm_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_sec_perm_xref` (
  `admin_section_id` bigint(20) NOT NULL,
  `admin_permission_id` bigint(20) NOT NULL,
  KEY `fk5e832966e88b7d38` (`admin_permission_id`),
  KEY `fk5e8329663af7f0fc` (`admin_section_id`),
  CONSTRAINT `fk5e8329663af7f0fc` FOREIGN KEY (`admin_section_id`) REFERENCES `admin_section` (`admin_section_id`),
  CONSTRAINT `fk5e832966e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_sec_perm_xref`
--

LOCK TABLES `admin_sec_perm_xref` WRITE;
/*!40000 ALTER TABLE `admin_sec_perm_xref` DISABLE KEYS */;
INSERT INTO `admin_sec_perm_xref` VALUES (-1,-100),(-1,-101),(-2,-102),(-2,-103),(-3,-104),(-3,-105),(-4,-106),(-4,-107),(-5,-108),(-5,-109),(-6,-110),(-6,-111),(-7,-112),(-7,-113),(-8,-114),(-8,-115),(-9,-116),(-9,-117),(-10,-118),(-10,-119),(-11,-120),(-11,-121),(-12,-140),(-12,-141),(-13,-126),(-13,-127),(-15,-122),(-15,-123),(-16,-122),(-16,-123),(-17,-150),(-17,-151),(3,-208),(3,-209),(4,-210),(4,-211),(-18,-49),(-19,-212),(-19,-213),(-20,-214),(-20,-215),(-21,-216),(-21,-217),(-22,-218),(-22,-219);
/*!40000 ALTER TABLE `admin_sec_perm_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_section`
--

DROP TABLE IF EXISTS `admin_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_section` (
  `admin_section_id` bigint(20) NOT NULL,
  `ceiling_entity` varchar(100) DEFAULT NULL,
  `display_controller` varchar(100) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `section_key` varchar(100) NOT NULL,
  `url` varchar(100) DEFAULT NULL,
  `use_default_handler` tinyint(1) DEFAULT NULL,
  `admin_module_id` bigint(20) NOT NULL,
  PRIMARY KEY (`admin_section_id`),
  UNIQUE KEY `uc_blc_admin_section_1` (`section_key`),
  KEY `adminsection_module_index` (`admin_module_id`),
  KEY `adminsection_name_index` (`name`),
  CONSTRAINT `fk7ea7d92fb1a18498` FOREIGN KEY (`admin_module_id`) REFERENCES `admin_module` (`admin_module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_section`
--

LOCK TABLES `admin_section` WRITE;
/*!40000 ALTER TABLE `admin_section` DISABLE KEYS */;
INSERT INTO `admin_section` VALUES (-22,'cn.globalph.b2c.product.domain.GroupOn',NULL,5000,'Groupon','Groupon','/Groupon',NULL,-1),(-21,'cn.globalph.passport.domain.CustomerMessage',NULL,5000,'Customer Message','Customer Message','/customerMessage',NULL,-3),(-20,'cn.globalph.b2c.product.domain.ComparePrice',NULL,5000,'Compare Price','Compare Price','/comparePrice',NULL,-1),(-19,'cn.globalph.coupon.domain.Coupon',NULL,4000,'Coupon','Coupon','/coupon',NULL,-1),(-17,'cn.globalph.openadmin.server.security.domain.AdminPermission',NULL,3000,'Permission Management','PermissionManagement','/permission-management',NULL,-4),(-16,'cn.globalph.common.config.domain.SystemProperty',NULL,2000,'System Properties','SystemProperties','/system-properties',NULL,-5),(-15,'cn.globalph.common.config.domain.SystemProperty',NULL,3000,'System Property Management','SystemPropertyManagement','/system-properties-management',NULL,-5),(-13,'cn.globalph.common.config.domain.ModuleConfiguration',NULL,1000,'Configuration Management','ConfigurationManagement','/configuration-management',NULL,-5),(-12,'cn.globalph.openadmin.server.security.domain.AdminRole',NULL,2000,'Role Management','RoleManagement','/role-management',NULL,-4),(-11,'cn.globalph.openadmin.server.security.domain.AdminUser',NULL,1000,'User Management','UserManagement','/user-management',NULL,-4),(-10,'cn.globalph.passport.domain.Customer',NULL,1000,'Customer','Customer','/customer',NULL,-3),(-9,'cn.globalph.b2c.order.domain.Order',NULL,2000,'Order','Order','/order',NULL,-3),(-8,'cn.globalph.cms.url.domain.URLHandler',NULL,4000,'Redirect URL','RedirectURL','/redirect-url',NULL,-2),(-7,'cn.globalph.cms.structure.domain.StructuredContent',NULL,2000,'Structured Content','StructuredContent','/structured-content',NULL,-2),(-6,'cn.globalph.cms.file.domain.StaticAsset',NULL,3000,'Assets','Assets','/assets',NULL,-2),(-5,'cn.globalph.cms.page.domain.Page',NULL,1000,'Pages','Pages','/pages',NULL,-2),(-4,'cn.globalph.b2c.offer.domain.Offer',NULL,4000,'Offer','Offer','/offer',NULL,-1),(-3,'cn.globalph.b2c.product.domain.ProductOption',NULL,3000,'Product Options','ProductOptions','/product-options',NULL,-1),(-2,'cn.globalph.b2c.product.domain.Product',NULL,2000,'Product','Product','/product',NULL,-1),(-1,'cn.globalph.b2c.catalog.domain.Category',NULL,1000,'Category','Category','/category',NULL,-1),(1,'cn.globalph.profile.core.domain.Community',NULL,5000,'Communities','Communities','/community',NULL,1),(2,'cn.globalph.profile.core.domain.NetNode',NULL,6000,'Net Nodes','NetNodes','/net-node',NULL,1),(3,'cn.globalph.b2c.product.domain.Provider',NULL,2000,'Provider','Provider Management','/provider',NULL,2),(4,'cn.globalph.b2c.order.domain.Refund',NULL,3000,'Refund','申请退款','/refund',NULL,-3);
/*!40000 ALTER TABLE `admin_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user` (
  `admin_user_id` bigint(20) NOT NULL,
  `active_status_flag` tinyint(1) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`admin_user_id`),
  KEY `adminperm_email_index` (`email`),
  KEY `adminuser_name_index` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

LOCK TABLES `admin_user` WRITE;
/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES (-6,1,'cms_approver@yourdomain.com','cmsApprover','内容管理系统审批人','admin{-6}',NULL),(-5,1,'cms_edit@yourdomain.com','cmsEditor','内容管理系统编辑者','admin{-5}',NULL),(-4,1,'csr@yourdomain.com','csr','用户和订单管理','admin{-4}',NULL),(-3,1,'promo@yourdomain.com','promo','促销管理员','admin{-3}',NULL),(-2,1,'merchandise@yourdomain.com','merchandise','商品管理员','admin{-2}',NULL),(-1,1,'hongqiutiandi@163.com','admin','超级管理员','864220f46e1009a8690c11ce05b04f8c77ade5ba',NULL);
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_addtl_fields`
--

DROP TABLE IF EXISTS `admin_user_addtl_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_addtl_fields` (
  `admin_user_id` bigint(20) NOT NULL,
  `field_value` varchar(255) DEFAULT NULL,
  `field_name` varchar(100) NOT NULL,
  PRIMARY KEY (`admin_user_id`,`field_name`),
  KEY `fk73274cdd46ebc38` (`admin_user_id`),
  CONSTRAINT `fk73274cdd46ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_addtl_fields`
--

LOCK TABLES `admin_user_addtl_fields` WRITE;
/*!40000 ALTER TABLE `admin_user_addtl_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user_addtl_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_permission_xref`
--

DROP TABLE IF EXISTS `admin_user_permission_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_permission_xref` (
  `admin_user_id` bigint(20) NOT NULL,
  `admin_permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`admin_permission_id`,`admin_user_id`),
  KEY `fkf0b3beede88b7d38` (`admin_permission_id`),
  KEY `fkf0b3beed46ebc38` (`admin_user_id`),
  CONSTRAINT `fkf0b3beed46ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkf0b3beede88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_permission_xref`
--

LOCK TABLES `admin_user_permission_xref` WRITE;
/*!40000 ALTER TABLE `admin_user_permission_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user_permission_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_role_xref`
--

DROP TABLE IF EXISTS `admin_user_role_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_role_xref` (
  `admin_user_id` bigint(20) NOT NULL,
  `admin_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`admin_role_id`,`admin_user_id`),
  KEY `fkffd33a265f43aad8` (`admin_role_id`),
  KEY `fkffd33a2646ebc38` (`admin_user_id`),
  CONSTRAINT `fkffd33a2646ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkffd33a265f43aad8` FOREIGN KEY (`admin_role_id`) REFERENCES `admin_role` (`admin_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_role_xref`
--

LOCK TABLES `admin_user_role_xref` WRITE;
/*!40000 ALTER TABLE `admin_user_role_xref` DISABLE KEYS */;
INSERT INTO `admin_user_role_xref` VALUES (-6,-6),(-5,-5),(-4,-4),(-3,-3),(-2,-2),(-1,-1);
/*!40000 ALTER TABLE `admin_user_role_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_sandbox`
--

DROP TABLE IF EXISTS `admin_user_sandbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_user_sandbox` (
  `sandbox_id` bigint(20) DEFAULT NULL,
  `admin_user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`admin_user_id`),
  KEY `fkd0a97e09579fe59d` (`sandbox_id`),
  KEY `fkd0a97e0946ebc38` (`admin_user_id`),
  CONSTRAINT `fkd0a97e0946ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkd0a97e09579fe59d` FOREIGN KEY (`sandbox_id`) REFERENCES `admin_sandbox` (`sandbox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_sandbox`
--

LOCK TABLES `admin_user_sandbox` WRITE;
/*!40000 ALTER TABLE `admin_user_sandbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_user_sandbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_asset_desc_map`
--

DROP TABLE IF EXISTS `cms_asset_desc_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_asset_desc_map` (
  `static_asset_id` bigint(20) NOT NULL,
  `static_asset_desc_id` bigint(20) NOT NULL,
  `map_key` varchar(100) NOT NULL,
  PRIMARY KEY (`static_asset_id`,`map_key`),
  KEY `fke886bae3e2ba0c9d` (`static_asset_desc_id`),
  KEY `fke886bae367f70b63` (`static_asset_id`),
  CONSTRAINT `fke886bae3e2ba0c9d` FOREIGN KEY (`static_asset_desc_id`) REFERENCES `cms_static_asset_desc` (`static_asset_desc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_asset_desc_map`
--

LOCK TABLES `cms_asset_desc_map` WRITE;
/*!40000 ALTER TABLE `cms_asset_desc_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_asset_desc_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_fld_define`
--

DROP TABLE IF EXISTS `cms_fld_define`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_fld_define` (
  `fld_def_id` bigint(20) NOT NULL,
  `allow_multiples` tinyint(1) DEFAULT NULL,
  `column_width` varchar(100) DEFAULT NULL,
  `fld_order` int(11) DEFAULT NULL,
  `fld_type` varchar(100) DEFAULT NULL,
  `friendly_name` varchar(100) DEFAULT NULL,
  `hidden_flag` tinyint(1) DEFAULT NULL,
  `max_length` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `required_flag` tinyint(1) DEFAULT NULL,
  `security_level` varchar(100) DEFAULT NULL,
  `text_area_flag` tinyint(1) DEFAULT NULL,
  `vldtn_error_mssg_key` varchar(255) DEFAULT NULL,
  `vldtn_regex` varchar(255) DEFAULT NULL,
  `enum_id` bigint(20) DEFAULT NULL,
  `fld_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`fld_def_id`),
  KEY `fk3fcb575e38d08ab5` (`enum_id`),
  KEY `fk3fcb575e6a79bdb5` (`fld_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_fld_define`
--

LOCK TABLES `cms_fld_define` WRITE;
/*!40000 ALTER TABLE `cms_fld_define` DISABLE KEYS */;
INSERT INTO `cms_fld_define` VALUES (-2,0,'*',1,'BOOLEAN','Plain Text',0,NULL,'plainText',NULL,NULL,0,NULL,NULL,NULL,-3),(-1,0,'*',0,'HTML','File Contents',0,NULL,'body',NULL,NULL,0,NULL,NULL,NULL,-3),(2,0,'*',1,'HTML','Body',0,NULL,'body',NULL,NULL,0,NULL,NULL,NULL,1),(3,0,'*',0,'STRING','Title',0,NULL,'title',NULL,NULL,0,NULL,NULL,NULL,1),(7,0,'*',0,'STRING','Image URL',0,150,'imageUrl',NULL,NULL,0,NULL,NULL,NULL,4),(8,0,'*',1,'STRING','Target URL',0,150,'targetUrl',NULL,NULL,0,NULL,NULL,NULL,4),(9,0,'*',0,'STRING','Message Text',0,150,'messageText',NULL,NULL,0,NULL,NULL,NULL,6),(10,0,'*',0,'HTML','HTML Content',0,NULL,'htmlContent',NULL,NULL,0,NULL,NULL,NULL,5);
/*!40000 ALTER TABLE `cms_fld_define` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_fld_group`
--

DROP TABLE IF EXISTS `cms_fld_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_fld_group` (
  `fld_group_id` bigint(20) NOT NULL,
  `init_collapsed_flag` tinyint(1) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`fld_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_fld_group`
--

LOCK TABLES `cms_fld_group` WRITE;
/*!40000 ALTER TABLE `cms_fld_group` DISABLE KEYS */;
INSERT INTO `cms_fld_group` VALUES (-3,0,'None'),(1,0,'Content'),(4,0,'Ad Fields'),(5,0,'HTML Fields'),(6,0,'Message Fields');
/*!40000 ALTER TABLE `cms_fld_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_img_static_asset`
--

DROP TABLE IF EXISTS `cms_img_static_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_img_static_asset` (
  `height` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `static_asset_id` bigint(20) NOT NULL,
  PRIMARY KEY (`static_asset_id`),
  KEY `fkcc4b772167f70b63` (`static_asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_img_static_asset`
--

LOCK TABLES `cms_img_static_asset` WRITE;
/*!40000 ALTER TABLE `cms_img_static_asset` DISABLE KEYS */;
INSERT INTO `cms_img_static_asset` VALUES (280,940,100150),(280,940,100151),(280,940,100200),(320,240,100250),(180,180,100251),(1182,1210,100252),(425,567,100300);
/*!40000 ALTER TABLE `cms_img_static_asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page`
--

DROP TABLE IF EXISTS `cms_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page` (
  `page_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `exclude_from_site_map` tinyint(1) DEFAULT NULL,
  `full_url` varchar(100) DEFAULT NULL,
  `offline_flag` tinyint(1) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `page_tmplt_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`page_id`),
  KEY `page_full_url_index` (`full_url`),
  KEY `fkf41bedd5d49d3961` (`page_tmplt_id`),
  CONSTRAINT `fkf41bedd5d49d3961` FOREIGN KEY (`page_tmplt_id`) REFERENCES `cms_page_tmplt` (`page_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page`
--

LOCK TABLES `cms_page` WRITE;
/*!40000 ALTER TABLE `cms_page` DISABLE KEYS */;
INSERT INTO `cms_page` VALUES (1,NULL,NULL,'2015-05-25 18:27:55',-6,'关于我们',NULL,'/about_us',NULL,NULL,1),(2,NULL,NULL,'2014-10-07 15:09:20',-1,'商品分类',NULL,'/faq',NULL,NULL,1),(3,NULL,NULL,NULL,NULL,'New to Hot Sauce',NULL,'/new-to-hot-sauce',NULL,NULL,1);
/*!40000 ALTER TABLE `cms_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_fld`
--

DROP TABLE IF EXISTS `cms_page_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_fld` (
  `page_fld_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `fld_key` varchar(50) DEFAULT NULL,
  `lob_value` longtext,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`page_fld_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_fld`
--

LOCK TABLES `cms_page_fld` WRITE;
/*!40000 ALTER TABLE `cms_page_fld` DISABLE KEYS */;
INSERT INTO `cms_page_fld` VALUES (1,NULL,NULL,NULL,NULL,'body',NULL,'test content'),(2,NULL,NULL,'2014-10-18 15:21:09',-1,'title',NULL,NULL),(3,NULL,NULL,NULL,NULL,'body',NULL,'<h2 style=\"text-align:center;\">This is an example of a content-managed page.</h2><h4 style=\"text-align:center;\"><a href=\"http://www.broadleafcommerce.com/features/content\">Click Here</a> to see more about Content Management in Broadleaf.</h4>'),(4,NULL,NULL,NULL,NULL,'body',NULL,'<h2 style=\"text-align:center;\">This is an example of a content-managed page.</h2>'),(950,-1,'2014-10-07 15:09:20','2014-10-07 15:09:20',NULL,'title',NULL,NULL);
/*!40000 ALTER TABLE `cms_page_fld` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_fld_map`
--

DROP TABLE IF EXISTS `cms_page_fld_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_fld_map` (
  `page_id` bigint(20) NOT NULL,
  `page_fld_id` bigint(20) NOT NULL,
  `map_key` varchar(50) NOT NULL,
  PRIMARY KEY (`page_id`,`map_key`),
  KEY `fke9ee09515aedd08a` (`page_fld_id`),
  KEY `fke9ee0951883c2667` (`page_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_fld_map`
--

LOCK TABLES `cms_page_fld_map` WRITE;
/*!40000 ALTER TABLE `cms_page_fld_map` DISABLE KEYS */;
INSERT INTO `cms_page_fld_map` VALUES (1,1,'body'),(1,2,'title'),(2,3,'body'),(3,4,'body'),(2,950,'title');
/*!40000 ALTER TABLE `cms_page_fld_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_item_criteria`
--

DROP TABLE IF EXISTS `cms_page_item_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_item_criteria` (
  `page_item_criteria_id` bigint(20) NOT NULL,
  `order_item_match_rule` longtext,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`page_item_criteria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_item_criteria`
--

LOCK TABLES `cms_page_item_criteria` WRITE;
/*!40000 ALTER TABLE `cms_page_item_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_page_item_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_rule`
--

DROP TABLE IF EXISTS `cms_page_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_rule` (
  `page_rule_id` bigint(20) NOT NULL,
  `match_rule` longtext,
  PRIMARY KEY (`page_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_rule`
--

LOCK TABLES `cms_page_rule` WRITE;
/*!40000 ALTER TABLE `cms_page_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_page_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_rule_map`
--

DROP TABLE IF EXISTS `cms_page_rule_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_rule_map` (
  `page_id` bigint(20) NOT NULL,
  `page_rule_id` bigint(20) NOT NULL,
  `map_key` varchar(255) NOT NULL,
  PRIMARY KEY (`page_id`,`map_key`),
  KEY `fk1aba0ca336d91846` (`page_rule_id`),
  KEY `fk1aba0ca3c38455dd` (`page_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_rule_map`
--

LOCK TABLES `cms_page_rule_map` WRITE;
/*!40000 ALTER TABLE `cms_page_rule_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_page_rule_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_page_tmplt`
--

DROP TABLE IF EXISTS `cms_page_tmplt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_page_tmplt` (
  `page_tmplt_id` bigint(20) NOT NULL,
  `tmplt_descr` varchar(100) DEFAULT NULL,
  `tmplt_name` varchar(100) DEFAULT NULL,
  `tmplt_path` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`page_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_page_tmplt`
--

LOCK TABLES `cms_page_tmplt` WRITE;
/*!40000 ALTER TABLE `cms_page_tmplt` DISABLE KEYS */;
INSERT INTO `cms_page_tmplt` VALUES (-3,'Outputs the body field.','NONE','NONE'),(1,'Provides a basic layout with header and footer surrounding the content and title.','Default Template','/content/default'),(2,'This template provides a basic layout with header and footer surrounding the content and title.','Basic Spanish Template','/content/default');
/*!40000 ALTER TABLE `cms_page_tmplt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_pgtmplt_fldgrp_xref`
--

DROP TABLE IF EXISTS `cms_pgtmplt_fldgrp_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_pgtmplt_fldgrp_xref` (
  `page_tmplt_id` bigint(20) NOT NULL,
  `fld_group_id` bigint(20) NOT NULL,
  `group_order` int(11) NOT NULL,
  PRIMARY KEY (`page_tmplt_id`,`group_order`),
  KEY `fk99d625f66a79bdb5` (`fld_group_id`),
  KEY `fk99d625f6d49d3961` (`page_tmplt_id`),
  CONSTRAINT `fk99d625f66a79bdb5` FOREIGN KEY (`fld_group_id`) REFERENCES `cms_fld_group` (`fld_group_id`),
  CONSTRAINT `fk99d625f6d49d3961` FOREIGN KEY (`page_tmplt_id`) REFERENCES `cms_page_tmplt` (`page_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_pgtmplt_fldgrp_xref`
--

LOCK TABLES `cms_pgtmplt_fldgrp_xref` WRITE;
/*!40000 ALTER TABLE `cms_pgtmplt_fldgrp_xref` DISABLE KEYS */;
INSERT INTO `cms_pgtmplt_fldgrp_xref` VALUES (-3,-3,0),(1,1,0),(2,1,0);
/*!40000 ALTER TABLE `cms_pgtmplt_fldgrp_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_qual_crit_page_xref`
--

DROP TABLE IF EXISTS `cms_qual_crit_page_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_qual_crit_page_xref` (
  `page_id` bigint(20) NOT NULL DEFAULT '0',
  `page_item_criteria_id` bigint(20) NOT NULL,
  PRIMARY KEY (`page_id`,`page_item_criteria_id`),
  UNIQUE KEY `uk_874be5902b6bc67f` (`page_item_criteria_id`),
  KEY `fk874be590883c2667` (`page_id`),
  KEY `fk874be590378418cd` (`page_item_criteria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_qual_crit_page_xref`
--

LOCK TABLES `cms_qual_crit_page_xref` WRITE;
/*!40000 ALTER TABLE `cms_qual_crit_page_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_qual_crit_page_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_qual_crit_sc_xref`
--

DROP TABLE IF EXISTS `cms_qual_crit_sc_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_qual_crit_sc_xref` (
  `sc_id` bigint(20) NOT NULL,
  `sc_item_criteria_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sc_id`,`sc_item_criteria_id`),
  UNIQUE KEY `uk_c4a353afff06f4de` (`sc_item_criteria_id`),
  KEY `fkc4a353af13d95585` (`sc_id`),
  KEY `fkc4a353af85c77f2b` (`sc_item_criteria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_qual_crit_sc_xref`
--

LOCK TABLES `cms_qual_crit_sc_xref` WRITE;
/*!40000 ALTER TABLE `cms_qual_crit_sc_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_qual_crit_sc_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc`
--

DROP TABLE IF EXISTS `cms_sc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc` (
  `sc_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `content_name` varchar(50) NOT NULL,
  `offline_flag` tinyint(1) DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `sc_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sc_id`),
  KEY `content_name_index` (`content_name`),
  KEY `sc_offln_flg_indx` (`offline_flag`),
  KEY `content_priority_index` (`priority`),
  KEY `fk74eeb71671ebfa46` (`sc_type_id`),
  CONSTRAINT `fk74eeb71671ebfa46` FOREIGN KEY (`sc_type_id`) REFERENCES `cms_sc_type` (`sc_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc`
--

LOCK TABLES `cms_sc` WRITE;
/*!40000 ALTER TABLE `cms_sc` DISABLE KEYS */;
INSERT INTO `cms_sc` VALUES (130,1,'2014-06-08 14:56:21','2015-05-13 19:39:57',-1,'home page bar',0,3,3),(950,-1,'2014-10-28 23:53:39','2015-05-13 19:39:41',-1,'home page bar',0,2,3),(951,-1,'2014-10-28 23:55:34','2015-05-13 19:39:12',-1,'home page bar',0,1,3),(1000,-1,'2015-05-13 17:51:32','2015-05-13 17:51:32',NULL,'hph banners',0,1,1),(1050,-1,'2015-05-13 18:47:35','2015-05-13 18:47:35',NULL,'lph banners',0,1,1),(1051,-1,'2015-05-13 18:48:35','2015-05-13 18:48:35',NULL,'gph banners',0,1,1);
/*!40000 ALTER TABLE `cms_sc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_fld`
--

DROP TABLE IF EXISTS `cms_sc_fld`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_fld` (
  `sc_fld_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `fld_key` varchar(100) DEFAULT NULL,
  `lob_value` longtext,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sc_fld_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_fld`
--

LOCK TABLES `cms_sc_fld` WRITE;
/*!40000 ALTER TABLE `cms_sc_fld` DISABLE KEYS */;
INSERT INTO `cms_sc_fld` VALUES (11,1,'2014-06-08 14:56:21','2015-05-13 19:39:57',-1,'messageText',NULL,'果品荟特选商品'),(950,-1,'2014-10-28 23:54:12','2015-05-13 19:39:41',-1,'messageText',NULL,'粮品荟特选商品'),(951,-1,'2014-10-28 23:55:57','2015-05-13 19:38:38',-1,'messageText',NULL,'海品荟特选商品'),(1000,-1,'2015-05-13 17:51:47','2015-05-13 18:39:45',-1,'imageUrl',NULL,'/cmsstatic/hph_banner_1.jpg'),(1001,-1,'2015-05-13 17:51:47','2015-05-13 17:51:47',NULL,'targetUrl',NULL,NULL),(1050,-1,'2015-05-13 18:47:57','2015-05-13 18:47:57',NULL,'imageUrl',NULL,'/cmsstatic/lph_banner_1.jpg'),(1051,-1,'2015-05-13 18:47:57','2015-05-13 18:47:57',NULL,'targetUrl',NULL,NULL),(1052,-1,'2015-05-13 18:48:57','2015-05-13 18:58:58',-1,'imageUrl',NULL,'/cmsstatic/gph_banner_1.jpg'),(1053,-1,'2015-05-13 18:48:57','2015-05-13 18:48:57',NULL,'targetUrl',NULL,NULL);
/*!40000 ALTER TABLE `cms_sc_fld` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_fld_map`
--

DROP TABLE IF EXISTS `cms_sc_fld_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_fld_map` (
  `sc_id` bigint(20) NOT NULL,
  `sc_fld_id` bigint(20) NOT NULL,
  `map_key` varchar(100) NOT NULL,
  PRIMARY KEY (`sc_id`,`map_key`),
  KEY `fkd9480192dd6fd28a` (`sc_fld_id`),
  KEY `fkd948019213d95585` (`sc_id`),
  CONSTRAINT `fkd948019213d95585` FOREIGN KEY (`sc_id`) REFERENCES `cms_sc` (`sc_id`),
  CONSTRAINT `fkd9480192dd6fd28a` FOREIGN KEY (`sc_fld_id`) REFERENCES `cms_sc_fld` (`sc_fld_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_fld_map`
--

LOCK TABLES `cms_sc_fld_map` WRITE;
/*!40000 ALTER TABLE `cms_sc_fld_map` DISABLE KEYS */;
INSERT INTO `cms_sc_fld_map` VALUES (130,11,'messageText'),(950,950,'messageText'),(951,951,'messageText'),(1000,1000,'imageUrl'),(1000,1001,'targetUrl'),(1050,1050,'imageUrl'),(1050,1051,'targetUrl'),(1051,1052,'imageUrl'),(1051,1053,'targetUrl');
/*!40000 ALTER TABLE `cms_sc_fld_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_fld_tmplt`
--

DROP TABLE IF EXISTS `cms_sc_fld_tmplt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_fld_tmplt` (
  `sc_fld_tmplt_id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sc_fld_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_fld_tmplt`
--

LOCK TABLES `cms_sc_fld_tmplt` WRITE;
/*!40000 ALTER TABLE `cms_sc_fld_tmplt` DISABLE KEYS */;
INSERT INTO `cms_sc_fld_tmplt` VALUES (1,'Ad Template'),(2,'HTML Template'),(3,'Message Template');
/*!40000 ALTER TABLE `cms_sc_fld_tmplt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_fldgrp_xref`
--

DROP TABLE IF EXISTS `cms_sc_fldgrp_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_fldgrp_xref` (
  `sc_fld_tmplt_id` bigint(20) NOT NULL,
  `fld_group_id` bigint(20) NOT NULL,
  `group_order` int(11) NOT NULL,
  PRIMARY KEY (`sc_fld_tmplt_id`,`group_order`),
  KEY `fk71612aea6a79bdb5` (`fld_group_id`),
  KEY `fk71612aeaf6b0ba84` (`sc_fld_tmplt_id`),
  CONSTRAINT `fk71612aea6a79bdb5` FOREIGN KEY (`fld_group_id`) REFERENCES `cms_fld_group` (`fld_group_id`),
  CONSTRAINT `fk71612aeaf6b0ba84` FOREIGN KEY (`sc_fld_tmplt_id`) REFERENCES `cms_sc_fld_tmplt` (`sc_fld_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_fldgrp_xref`
--

LOCK TABLES `cms_sc_fldgrp_xref` WRITE;
/*!40000 ALTER TABLE `cms_sc_fldgrp_xref` DISABLE KEYS */;
INSERT INTO `cms_sc_fldgrp_xref` VALUES (1,4,0),(2,5,0),(3,6,0);
/*!40000 ALTER TABLE `cms_sc_fldgrp_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_item_criteria`
--

DROP TABLE IF EXISTS `cms_sc_item_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_item_criteria` (
  `sc_item_criteria_id` bigint(20) NOT NULL,
  `order_item_match_rule` longtext,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`sc_item_criteria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_item_criteria`
--

LOCK TABLES `cms_sc_item_criteria` WRITE;
/*!40000 ALTER TABLE `cms_sc_item_criteria` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_sc_item_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_rule`
--

DROP TABLE IF EXISTS `cms_sc_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_rule` (
  `sc_rule_id` bigint(20) NOT NULL,
  `match_rule` longtext,
  PRIMARY KEY (`sc_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_rule`
--

LOCK TABLES `cms_sc_rule` WRITE;
/*!40000 ALTER TABLE `cms_sc_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_sc_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_rule_map`
--

DROP TABLE IF EXISTS `cms_sc_rule_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_rule_map` (
  `cms_sc_sc_id` bigint(20) NOT NULL,
  `sc_rule_id` bigint(20) NOT NULL,
  `map_key` varchar(100) NOT NULL,
  PRIMARY KEY (`cms_sc_sc_id`,`map_key`),
  KEY `fk169f1c8256e51a06` (`sc_rule_id`),
  KEY `fk169f1c82156e72fc` (`cms_sc_sc_id`),
  CONSTRAINT `fk169f1c82156e72fc` FOREIGN KEY (`cms_sc_sc_id`) REFERENCES `cms_sc` (`sc_id`),
  CONSTRAINT `fk169f1c8256e51a06` FOREIGN KEY (`sc_rule_id`) REFERENCES `cms_sc_rule` (`sc_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_rule_map`
--

LOCK TABLES `cms_sc_rule_map` WRITE;
/*!40000 ALTER TABLE `cms_sc_rule_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_sc_rule_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_sc_type`
--

DROP TABLE IF EXISTS `cms_sc_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_sc_type` (
  `sc_type_id` bigint(20) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `sc_fld_tmplt_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sc_type_id`),
  KEY `sc_type_name_index` (`name`),
  KEY `fke19886c3f6b0ba84` (`sc_fld_tmplt_id`),
  CONSTRAINT `fke19886c3f6b0ba84` FOREIGN KEY (`sc_fld_tmplt_id`) REFERENCES `cms_sc_fld_tmplt` (`sc_fld_tmplt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_sc_type`
--

LOCK TABLES `cms_sc_type` WRITE;
/*!40000 ALTER TABLE `cms_sc_type` DISABLE KEYS */;
INSERT INTO `cms_sc_type` VALUES (1,NULL,'Homepage Banner Ad',1),(2,NULL,'Homepage Middle Promo Snippet',2),(3,NULL,'Homepage Featured Products Title',3),(4,NULL,'Right Hand Side Banner Ad',1);
/*!40000 ALTER TABLE `cms_sc_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_static_asset`
--

DROP TABLE IF EXISTS `cms_static_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_static_asset` (
  `static_asset_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `alt_text` varchar(255) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `file_extension` varchar(255) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `full_url` varchar(100) NOT NULL,
  `mime_type` varchar(20) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `storage_type` varchar(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`static_asset_id`),
  KEY `asst_full_url_indx` (`full_url`)
) ENGINE=InnoDB AUTO_INCREMENT=100301 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_static_asset`
--

LOCK TABLES `cms_static_asset` WRITE;
/*!40000 ALTER TABLE `cms_static_asset` DISABLE KEYS */;
INSERT INTO `cms_static_asset` VALUES (100150,NULL,-1,'2015-05-13 17:56:05','2015-05-13 17:56:05',NULL,'jpg',81205,'/lph_banner_1.jpg','image/jpeg','lph_banner_1.jpg','FILESYSTEM',NULL),(100151,NULL,-1,'2015-05-13 17:58:06','2015-05-13 17:58:06',NULL,'jpg',164871,'/hph_banner_1.jpg','image/jpeg','hph_banner_1.jpg','FILESYSTEM',NULL),(100200,NULL,-1,'2015-05-13 19:04:13','2015-05-13 19:04:13',NULL,'jpg',103588,'/gph_banner_1.jpg','image/jpeg','gph_banner_1.jpg','FILESYSTEM',NULL),(100300,NULL,-1,'2015-11-23 20:34:56','2015-11-23 20:34:56',NULL,'jpg',62086,'/product/hongqianfushi.jpg','image/jpeg','hongqianfushi.jpg','FILESYSTEM',NULL);
/*!40000 ALTER TABLE `cms_static_asset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_static_asset_desc`
--

DROP TABLE IF EXISTS `cms_static_asset_desc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_static_asset_desc` (
  `static_asset_desc_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `long_description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`static_asset_desc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_static_asset_desc`
--

LOCK TABLES `cms_static_asset_desc` WRITE;
/*!40000 ALTER TABLE `cms_static_asset_desc` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_static_asset_desc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_static_asset_strg`
--

DROP TABLE IF EXISTS `cms_static_asset_strg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_static_asset_strg` (
  `static_asset_strg_id` bigint(20) NOT NULL,
  `file_data` longblob,
  `static_asset_id` bigint(20) NOT NULL,
  PRIMARY KEY (`static_asset_strg_id`),
  KEY `static_asset_id_index` (`static_asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_static_asset_strg`
--

LOCK TABLES `cms_static_asset_strg` WRITE;
/*!40000 ALTER TABLE `cms_static_asset_strg` DISABLE KEYS */;
/*!40000 ALTER TABLE `cms_static_asset_strg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cms_url_handler`
--

DROP TABLE IF EXISTS `cms_url_handler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cms_url_handler` (
  `url_handler_id` bigint(20) NOT NULL,
  `incoming_url` varchar(255) NOT NULL,
  `new_url` varchar(255) NOT NULL,
  `url_redirect_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`url_handler_id`),
  KEY `incoming_url_index` (`incoming_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cms_url_handler`
--

LOCK TABLES `cms_url_handler` WRITE;
/*!40000 ALTER TABLE `cms_url_handler` DISABLE KEYS */;
INSERT INTO `cms_url_handler` VALUES (1,'/googlePerm','http://www.google.com','REDIRECT_PERM'),(2,'/googleTemp','http://www.google.com','REDIRECT_TEMP'),(3,'/insanity','/hot-sauces/insanity_sauce','FORWARD'),(4,'/jalepeno','/hot-sauces/hurtin_jalepeno_hot_sauce','REDIRECT_TEMP');
/*!40000 ALTER TABLE `cms_url_handler` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_additional_offer_info`
--

DROP TABLE IF EXISTS `nph_additional_offer_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_additional_offer_info` (
  `blc_order_order_id` bigint(20) NOT NULL,
  `offer_info_id` bigint(20) NOT NULL,
  `offer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`blc_order_order_id`,`offer_id`),
  KEY `fk3bfdbd63b5d9c34d` (`offer_info_id`),
  KEY `fk3bfdbd63d5f3faf4` (`offer_id`),
  KEY `fk3bfdbd631891ff79` (`blc_order_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_additional_offer_info`
--

LOCK TABLES `nph_additional_offer_info` WRITE;
/*!40000 ALTER TABLE `nph_additional_offer_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_additional_offer_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_address`
--

DROP TABLE IF EXISTS `nph_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_address` (
  `address_id` bigint(20) NOT NULL,
  `address` varchar(100) NOT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `receiver` varchar(20) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `is_business` tinyint(1) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `standardized` tinyint(1) DEFAULT NULL,
  `tokenized_address` varchar(100) DEFAULT NULL,
  `verification_level` varchar(100) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `first_level_community_id` bigint(20) DEFAULT NULL,
  `second_level_community_id` bigint(20) DEFAULT NULL,
  `net_node_id` bigint(20) DEFAULT NULL,
  `province` varchar(10) DEFAULT NULL,
  `city` varchar(10) DEFAULT NULL,
  `district` varchar(10) DEFAULT NULL,
  `community` varchar(20) DEFAULT NULL,
  `is_from_rrm` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_address`
--

LOCK TABLES `nph_address` WRITE;
/*!40000 ALTER TABLE `nph_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_area`
--

DROP TABLE IF EXISTS `nph_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `area_no` int(11) unsigned DEFAULT NULL,
  `area_name` varchar(255) DEFAULT NULL,
  `parent_no` int(255) DEFAULT NULL,
  `area_code` varchar(255) DEFAULT NULL,
  `area_level` int(255) DEFAULT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_area`
--

LOCK TABLES `nph_area` WRITE;
/*!40000 ALTER TABLE `nph_area` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_bank_account_payment`
--

DROP TABLE IF EXISTS `nph_bank_account_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_bank_account_payment` (
  `payment_id` bigint(20) NOT NULL,
  `account_number` varchar(30) NOT NULL,
  `reference_number` varchar(50) NOT NULL,
  `routing_number` varchar(50) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `bankaccount_index` (`reference_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_bank_account_payment`
--

LOCK TABLES `nph_bank_account_payment` WRITE;
/*!40000 ALTER TABLE `nph_bank_account_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_bank_account_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_bundle_sku`
--

DROP TABLE IF EXISTS `nph_bundle_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_bundle_sku` (
  `sku_id` bigint(20) NOT NULL,
  `auto_bundle` tinyint(1) DEFAULT NULL,
  `bundle_promotable` tinyint(1) DEFAULT NULL,
  `items_promotable` tinyint(1) DEFAULT NULL,
  `bundle_priority` int(11) DEFAULT NULL,
  PRIMARY KEY (`sku_id`),
  CONSTRAINT `fk_nph_sku_bundle_1` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_bundle_sku`
--

LOCK TABLES `nph_bundle_sku` WRITE;
/*!40000 ALTER TABLE `nph_bundle_sku` DISABLE KEYS */;
INSERT INTO `nph_bundle_sku` VALUES (10250,0,0,0,99);
/*!40000 ALTER TABLE `nph_bundle_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_bundle_sku_item`
--

DROP TABLE IF EXISTS `nph_bundle_sku_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_bundle_sku_item` (
  `sku_bundle_item_id` bigint(20) NOT NULL,
  `item_sale_price` decimal(19,5) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `bundle_sku_id` bigint(20) NOT NULL,
  `sku_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sku_bundle_item_id`),
  KEY `fkd55968b78c9977` (`sku_id`),
  CONSTRAINT `fkd55968b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_bundle_sku_item`
--

LOCK TABLES `nph_bundle_sku_item` WRITE;
/*!40000 ALTER TABLE `nph_bundle_sku_item` DISABLE KEYS */;
INSERT INTO `nph_bundle_sku_item` VALUES (-103,NULL,1,993,2),(-102,NULL,1,993,1),(-101,NULL,1,992,2),(-100,NULL,1,992,1),(1,20.00000,2,10250,2),(2,2.00000,2,10250,3);
/*!40000 ALTER TABLE `nph_bundle_sku_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_candidate_fg_offer`
--

DROP TABLE IF EXISTS `nph_candidate_fg_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_candidate_fg_offer` (
  `candidate_fg_offer_id` bigint(20) NOT NULL,
  `discounted_price` decimal(19,5) DEFAULT NULL,
  `fulfillment_group_id` bigint(20) DEFAULT NULL,
  `offer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`candidate_fg_offer_id`),
  KEY `candidate_fg_index` (`fulfillment_group_id`),
  KEY `candidate_fgoffer_index` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_candidate_fg_offer`
--

LOCK TABLES `nph_candidate_fg_offer` WRITE;
/*!40000 ALTER TABLE `nph_candidate_fg_offer` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_candidate_fg_offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_candidate_item_offer`
--

DROP TABLE IF EXISTS `nph_candidate_item_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_candidate_item_offer` (
  `candidate_item_offer_id` bigint(20) NOT NULL,
  `discounted_price` decimal(19,5) DEFAULT NULL,
  `offer_id` bigint(20) NOT NULL,
  `order_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`candidate_item_offer_id`),
  KEY `candidate_itemoffer_index` (`offer_id`),
  KEY `candidate_item_index` (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_candidate_item_offer`
--

LOCK TABLES `nph_candidate_item_offer` WRITE;
/*!40000 ALTER TABLE `nph_candidate_item_offer` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_candidate_item_offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_candidate_order_offer`
--

DROP TABLE IF EXISTS `nph_candidate_order_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_candidate_order_offer` (
  `candidate_order_offer_id` bigint(20) NOT NULL,
  `discounted_price` decimal(19,5) DEFAULT NULL,
  `offer_id` bigint(20) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`candidate_order_offer_id`),
  KEY `candidate_orderoffer_index` (`offer_id`),
  KEY `candidate_order_index` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_candidate_order_offer`
--

LOCK TABLES `nph_candidate_order_offer` WRITE;
/*!40000 ALTER TABLE `nph_candidate_order_offer` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_candidate_order_offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_cat_search_facet_value`
--

DROP TABLE IF EXISTS `nph_cat_search_facet_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_cat_search_facet_value` (
  `cat_search_facet_value_id` bigint(20) NOT NULL,
  `value` varchar(45) DEFAULT NULL,
  `cat_search_facet_xref_id` bigint(20) NOT NULL,
  `search_field_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`cat_search_facet_value_id`,`cat_search_facet_xref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_cat_search_facet_value`
--

LOCK TABLES `nph_cat_search_facet_value` WRITE;
/*!40000 ALTER TABLE `nph_cat_search_facet_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_cat_search_facet_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_cat_search_facet_xref`
--

DROP TABLE IF EXISTS `nph_cat_search_facet_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_cat_search_facet_xref` (
  `category_search_facet_id` bigint(20) NOT NULL,
  `sequence` decimal(19,2) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `search_facet_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`category_search_facet_id`),
  KEY `fk32210eeb15d1a13d` (`category_id`),
  KEY `fk32210eebb96b1c93` (`search_facet_id`),
  CONSTRAINT `fk32210eeb15d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk32210eebb96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_cat_search_facet_xref`
--

LOCK TABLES `nph_cat_search_facet_xref` WRITE;
/*!40000 ALTER TABLE `nph_cat_search_facet_xref` DISABLE KEYS */;
INSERT INTO `nph_cat_search_facet_xref` VALUES (1,1.00,2002,1),(3,3.00,1,3),(4,1.00,2003,4),(5,2.00,2002,5),(6,3.00,2002,6);
/*!40000 ALTER TABLE `nph_cat_search_facet_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_cat_site_map_gen_cfg`
--

DROP TABLE IF EXISTS `nph_cat_site_map_gen_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_cat_site_map_gen_cfg` (
  `ending_depth` int(11) NOT NULL,
  `starting_depth` int(11) NOT NULL,
  `gen_config_id` bigint(20) NOT NULL,
  `root_category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk1ba4e695c5f3d60` (`root_category_id`),
  KEY `fk1ba4e69bcab9f56` (`gen_config_id`),
  CONSTRAINT `fk1ba4e695c5f3d60` FOREIGN KEY (`root_category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk1ba4e69bcab9f56` FOREIGN KEY (`gen_config_id`) REFERENCES `blc_site_map_gen_cfg` (`gen_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_cat_site_map_gen_cfg`
--

LOCK TABLES `nph_cat_site_map_gen_cfg` WRITE;
/*!40000 ALTER TABLE `nph_cat_site_map_gen_cfg` DISABLE KEYS */;
INSERT INTO `nph_cat_site_map_gen_cfg` VALUES (1,1,-4,2);
/*!40000 ALTER TABLE `nph_cat_site_map_gen_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_catalog`
--

DROP TABLE IF EXISTS `nph_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_catalog` (
  `catalog_id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`catalog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_catalog`
--

LOCK TABLES `nph_catalog` WRITE;
/*!40000 ALTER TABLE `nph_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_category`
--

DROP TABLE IF EXISTS `nph_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_category` (
  `category_id` bigint(20) NOT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `active_start_date` datetime DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `display_template` varchar(255) DEFAULT NULL,
  `fulfillment_type` varchar(255) DEFAULT NULL,
  `inventory_type` varchar(255) DEFAULT NULL,
  `long_description` longtext,
  `name` varchar(20) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `url_key` varchar(255) DEFAULT NULL,
  `default_parent_category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `category_parent_index` (`default_parent_category_id`),
  KEY `category_name_index` (`name`),
  KEY `category_url_index` (`url`),
  KEY `category_urlkey_index` (`url_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_category`
--

LOCK TABLES `nph_category` WRITE;
/*!40000 ALTER TABLE `nph_category` DISABLE KEYS */;
INSERT INTO `nph_category` VALUES (1,NULL,'2014-06-08 14:55:53',NULL,'Root',NULL,NULL,NULL,NULL,'根栏目',NULL,NULL,NULL),(2,NULL,'2014-06-08 14:55:53','N','Primary Nav',NULL,NULL,'ALWAYS_AVAILABLE',NULL,'Nav',NULL,NULL,1),(2001,NULL,'2014-06-08 14:55:53','N','Home','layout/home',NULL,NULL,NULL,'首页','/',NULL,2),(2002,NULL,'2014-06-08 14:55:53','N','Hot Sauces','layout/lphhome',NULL,NULL,NULL,'苹果','/apple',NULL,2),(2003,NULL,'2014-06-08 14:55:53','N','Merchandise','layout/hphhome',NULL,NULL,NULL,'梨','/pear',NULL,2),(2004,NULL,'2014-06-08 14:55:53','N','Clearance','layout/gphhome',NULL,NULL,NULL,'更多','/more',NULL,2),(10050,NULL,'2014-10-18 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,'关于果品惠','/about_us',NULL,NULL),(10100,NULL,'2014-10-30 00:00:00','N',NULL,NULL,'PHYSICAL_PICKUP',NULL,NULL,'推荐水果',NULL,NULL,2001);
/*!40000 ALTER TABLE `nph_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_category_attribute`
--

DROP TABLE IF EXISTS `nph_category_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_category_attribute` (
  `category_attribute_id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `searchable` tinyint(1) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`category_attribute_id`),
  KEY `categoryattribute_index` (`category_id`),
  KEY `categoryattribute_name_index` (`name`),
  CONSTRAINT `fk4e441d4115d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_category_attribute`
--

LOCK TABLES `nph_category_attribute` WRITE;
/*!40000 ALTER TABLE `nph_category_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_category_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_category_media_map`
--

DROP TABLE IF EXISTS `nph_category_media_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_category_media_map` (
  `category_id` bigint(20) NOT NULL,
  `media_id` bigint(20) NOT NULL,
  `map_key` varchar(50) NOT NULL,
  PRIMARY KEY (`category_id`,`map_key`),
  KEY `fkcd24b1066e4720e0` (`media_id`),
  KEY `fkcd24b106d786cea2` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_category_media_map`
--

LOCK TABLES `nph_category_media_map` WRITE;
/*!40000 ALTER TABLE `nph_category_media_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_category_media_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_category_product_xref`
--

DROP TABLE IF EXISTS `nph_category_product_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_category_product_xref` (
  `category_product_id` bigint(20) NOT NULL,
  `display_order` decimal(10,6) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`category_product_id`),
  KEY `fk635eb1a615d1a13d` (`category_id`),
  KEY `fk635eb1a65f11a0b7` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_category_product_xref`
--

LOCK TABLES `nph_category_product_xref` WRITE;
/*!40000 ALTER TABLE `nph_category_product_xref` DISABLE KEYS */;
INSERT INTO `nph_category_product_xref` VALUES (1650,NULL,10100,10250);
/*!40000 ALTER TABLE `nph_category_product_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_category_xref`
--

DROP TABLE IF EXISTS `nph_category_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_category_xref` (
  `category_xref_id` bigint(20) NOT NULL,
  `display_order` decimal(10,6) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  `sub_category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`category_xref_id`),
  KEY `fke889733615d1a13d` (`category_id`),
  KEY `fke8897336d6d45dbe` (`sub_category_id`),
  CONSTRAINT `fke889733615d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fke8897336d6d45dbe` FOREIGN KEY (`sub_category_id`) REFERENCES `nph_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_category_xref`
--

LOCK TABLES `nph_category_xref` WRITE;
/*!40000 ALTER TABLE `nph_category_xref` DISABLE KEYS */;
INSERT INTO `nph_category_xref` VALUES (1,1.000000,1,2),(2,1.000000,2,2001),(3,2.000000,2,2002),(4,3.000000,2,2003),(5,4.000000,2,2004),(1150,NULL,2001,10100);
/*!40000 ALTER TABLE `nph_category_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_challenge_question`
--

DROP TABLE IF EXISTS `nph_challenge_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_challenge_question` (
  `question_id` bigint(20) NOT NULL,
  `question` varchar(255) NOT NULL,
  PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_challenge_question`
--

LOCK TABLES `nph_challenge_question` WRITE;
/*!40000 ALTER TABLE `nph_challenge_question` DISABLE KEYS */;
INSERT INTO `nph_challenge_question` VALUES (1,'您父亲叫什么?'),(2,'您小学的校名是什么?'),(3,'你最好的朋友叫什么?'),(4,'您最喜欢的一首歌是什么?'),(5,'您的配偶叫什么'),(6,'What school did you attend for sixth grade?'),(7,'Where does your nearest sibling live?'),(8,'What is your youngest brother\'s birthday?'),(9,'In what city or town was your first job?');
/*!40000 ALTER TABLE `nph_challenge_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_community`
--

DROP TABLE IF EXISTS `nph_community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_community` (
  `community_id` bigint(20) NOT NULL,
  `community_name` varchar(45) DEFAULT NULL,
  `ishead` tinyint(1) DEFAULT NULL,
  `parent_community_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_community`
--

LOCK TABLES `nph_community` WRITE;
/*!40000 ALTER TABLE `nph_community` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_credit_card_payment`
--

DROP TABLE IF EXISTS `nph_credit_card_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_credit_card_payment` (
  `payment_id` bigint(20) NOT NULL,
  `expiration_month` int(11) NOT NULL,
  `expiration_year` int(11) NOT NULL,
  `name_on_card` varchar(30) NOT NULL,
  `pan` varchar(30) NOT NULL,
  `reference_number` varchar(30) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `creditcard_index` (`reference_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_credit_card_payment`
--

LOCK TABLES `nph_credit_card_payment` WRITE;
/*!40000 ALTER TABLE `nph_credit_card_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_credit_card_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_cust_site_map_gen_cfg`
--

DROP TABLE IF EXISTS `nph_cust_site_map_gen_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_cust_site_map_gen_cfg` (
  `gen_config_id` bigint(20) NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk67c0dba0bcab9f56` (`gen_config_id`),
  CONSTRAINT `fk67c0dba0bcab9f56` FOREIGN KEY (`gen_config_id`) REFERENCES `blc_site_map_gen_cfg` (`gen_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_cust_site_map_gen_cfg`
--

LOCK TABLES `nph_cust_site_map_gen_cfg` WRITE;
/*!40000 ALTER TABLE `nph_cust_site_map_gen_cfg` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_cust_site_map_gen_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_customer`
--

DROP TABLE IF EXISTS `nph_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_customer` (
  `customer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `login_name` varchar(32) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `email_address` varchar(50) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `deactivated` tinyint(1) DEFAULT NULL,
  `is_preview` tinyint(1) DEFAULT NULL,
  `receive_email` tinyint(1) DEFAULT NULL,
  `is_registered` tinyint(1) DEFAULT NULL,
  `password_change_required` tinyint(1) DEFAULT NULL,
  `challenge_answer` varchar(100) DEFAULT NULL,
  `challenge_question_id` bigint(20) DEFAULT NULL,
  `bonus_point` int(11) DEFAULT '0',
  `used_bonus_point` int(11) DEFAULT NULL,
  `validation_code` varchar(10) DEFAULT NULL,
  `email_token` varchar(45) DEFAULT NULL,
  `validation_status` int(11) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `customer_challenge_index` (`challenge_question_id`),
  KEY `customer_email_index` (`email_address`),
  KEY `fk7716f024a1e1c128` (`phone`),
  CONSTRAINT `fk7716f0241422b204` FOREIGN KEY (`challenge_question_id`) REFERENCES `nph_challenge_question` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_customer`
--

LOCK TABLES `nph_customer` WRITE;
/*!40000 ALTER TABLE `nph_customer` DISABLE KEYS */;
INSERT INTO `nph_customer` VALUES (1,'m18901547402','18901547402','eadaf0e273634ae5ce57795f7a32d77bff09bfc2',NULL,'18901547402','2015-11-23 20:40:24','2015-11-23 20:41:54',0,NULL,1,1,0,NULL,NULL,0,0,'177465',NULL,2,0,1),(3,NULL,NULL,NULL,NULL,NULL,'2015-11-23 21:03:22',NULL,0,NULL,1,0,0,NULL,NULL,0,0,NULL,NULL,0,1,NULL),(4,NULL,'4',NULL,NULL,NULL,'2015-12-29 21:40:34','2015-12-29 21:40:34',0,NULL,1,0,0,NULL,NULL,0,0,NULL,NULL,0,0,4);
/*!40000 ALTER TABLE `nph_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_customer_password_token`
--

DROP TABLE IF EXISTS `nph_customer_password_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_customer_password_token` (
  `password_token` varchar(100) NOT NULL,
  `create_date` datetime NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `token_used_date` datetime DEFAULT NULL,
  `token_used_flag` tinyint(1) NOT NULL,
  PRIMARY KEY (`password_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_customer_password_token`
--

LOCK TABLES `nph_customer_password_token` WRITE;
/*!40000 ALTER TABLE `nph_customer_password_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_customer_password_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_customer_payment`
--

DROP TABLE IF EXISTS `nph_customer_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_customer_payment` (
  `customer_payment_id` bigint(20) NOT NULL,
  `is_default` tinyint(1) DEFAULT NULL,
  `payment_token` varchar(100) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`customer_payment_id`),
  UNIQUE KEY `cstmr_pay_unique_cnstrnt` (`customer_id`,`payment_token`),
  KEY `fk8b3df0cb7470f437` (`customer_id`),
  CONSTRAINT `fk8b3df0cb7470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_customer_payment`
--

LOCK TABLES `nph_customer_payment` WRITE;
/*!40000 ALTER TABLE `nph_customer_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_customer_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_customer_payment_fields`
--

DROP TABLE IF EXISTS `nph_customer_payment_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_customer_payment_fields` (
  `customer_payment_id` bigint(20) NOT NULL,
  `field_value` varchar(100) DEFAULT NULL,
  `field_name` varchar(30) NOT NULL,
  PRIMARY KEY (`customer_payment_id`,`field_name`),
  KEY `fk5ccb14adca0b98e0` (`customer_payment_id`),
  CONSTRAINT `fk5ccb14adca0b98e0` FOREIGN KEY (`customer_payment_id`) REFERENCES `nph_customer_payment` (`customer_payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_customer_payment_fields`
--

LOCK TABLES `nph_customer_payment_fields` WRITE;
/*!40000 ALTER TABLE `nph_customer_payment_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_customer_payment_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_customer_role`
--

DROP TABLE IF EXISTS `nph_customer_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_customer_role` (
  `customer_role_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`customer_role_id`),
  KEY `custrole_customer_index` (`customer_id`),
  KEY `custrole_role_index` (`role_id`),
  CONSTRAINT `fk548eb7b17470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk548eb7b1b8587b7` FOREIGN KEY (`role_id`) REFERENCES `nph_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_customer_role`
--

LOCK TABLES `nph_customer_role` WRITE;
/*!40000 ALTER TABLE `nph_customer_role` DISABLE KEYS */;
INSERT INTO `nph_customer_role` VALUES (5201,1,1);
/*!40000 ALTER TABLE `nph_customer_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_data_drvn_enum`
--

DROP TABLE IF EXISTS `nph_data_drvn_enum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_data_drvn_enum` (
  `enum_id` bigint(20) NOT NULL,
  `enum_key` varchar(50) DEFAULT NULL,
  `modifiable` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`enum_id`),
  KEY `enum_key_index` (`enum_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_data_drvn_enum`
--

LOCK TABLES `nph_data_drvn_enum` WRITE;
/*!40000 ALTER TABLE `nph_data_drvn_enum` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_data_drvn_enum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_data_drvn_enum_val`
--

DROP TABLE IF EXISTS `nph_data_drvn_enum_val`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_data_drvn_enum_val` (
  `enum_val_id` bigint(20) NOT NULL,
  `display` varchar(255) DEFAULT NULL,
  `hidden` tinyint(1) DEFAULT NULL,
  `enum_key` varchar(255) DEFAULT NULL,
  `enum_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`enum_val_id`),
  KEY `hidden_index` (`hidden`),
  KEY `enum_val_key_index` (`enum_key`),
  KEY `fkb2d5700da60e0554` (`enum_type`),
  CONSTRAINT `fkb2d5700da60e0554` FOREIGN KEY (`enum_type`) REFERENCES `nph_data_drvn_enum` (`enum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_data_drvn_enum_val`
--

LOCK TABLES `nph_data_drvn_enum_val` WRITE;
/*!40000 ALTER TABLE `nph_data_drvn_enum_val` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_data_drvn_enum_val` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_email_tracking`
--

DROP TABLE IF EXISTS `nph_email_tracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_email_tracking` (
  `email_tracking_id` bigint(20) NOT NULL,
  `date_sent` datetime DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email_tracking_id`),
  KEY `emailtracking_index` (`email_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_email_tracking`
--

LOCK TABLES `nph_email_tracking` WRITE;
/*!40000 ALTER TABLE `nph_email_tracking` DISABLE KEYS */;
INSERT INTO `nph_email_tracking` VALUES (4151,'2015-11-23 20:09:42','admin@yourdomain.com',NULL),(4201,'2015-11-23 20:17:26','admin@yourdomain.com',NULL),(4202,'2015-11-23 20:19:09','hongqiutiandi@163.com',NULL),(4251,'2015-11-23 20:23:01','hongqiutiandi@163.com',NULL);
/*!40000 ALTER TABLE `nph_email_tracking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_email_tracking_clicks`
--

DROP TABLE IF EXISTS `nph_email_tracking_clicks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_email_tracking_clicks` (
  `click_id` bigint(20) NOT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  `date_clicked` datetime NOT NULL,
  `destination_uri` varchar(255) DEFAULT NULL,
  `query_string` varchar(255) DEFAULT NULL,
  `email_tracking_id` bigint(20) NOT NULL,
  PRIMARY KEY (`click_id`),
  KEY `trackingclicks_customer_index` (`customer_id`),
  KEY `trackingclicks_tracking_index` (`email_tracking_id`),
  CONSTRAINT `fkfdf9f52afa1e5d61` FOREIGN KEY (`email_tracking_id`) REFERENCES `nph_email_tracking` (`email_tracking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_email_tracking_clicks`
--

LOCK TABLES `nph_email_tracking_clicks` WRITE;
/*!40000 ALTER TABLE `nph_email_tracking_clicks` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_email_tracking_clicks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_email_tracking_opens`
--

DROP TABLE IF EXISTS `nph_email_tracking_opens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_email_tracking_opens` (
  `open_id` bigint(20) NOT NULL,
  `date_opened` datetime DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `email_tracking_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`open_id`),
  KEY `trackingopen_tracking` (`email_tracking_id`),
  CONSTRAINT `fka5c3722afa1e5d61` FOREIGN KEY (`email_tracking_id`) REFERENCES `nph_email_tracking` (`email_tracking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_email_tracking_opens`
--

LOCK TABLES `nph_email_tracking_opens` WRITE;
/*!40000 ALTER TABLE `nph_email_tracking_opens` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_email_tracking_opens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fg_adjustment`
--

DROP TABLE IF EXISTS `nph_fg_adjustment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fg_adjustment` (
  `fg_adjustment_id` bigint(20) NOT NULL,
  `adjustment_reason` varchar(255) NOT NULL,
  `adjustment_value` decimal(19,5) NOT NULL,
  `fulfillment_group_id` bigint(20) DEFAULT NULL,
  `offer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fg_adjustment_id`),
  KEY `fgadjustment_index` (`fulfillment_group_id`),
  KEY `fgadjustment_offer_index` (`offer_id`),
  CONSTRAINT `fk468c8f255028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`),
  CONSTRAINT `fk468c8f25d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fg_adjustment`
--

LOCK TABLES `nph_fg_adjustment` WRITE;
/*!40000 ALTER TABLE `nph_fg_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fg_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_group`
--

DROP TABLE IF EXISTS `nph_fulfillment_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_group` (
  `fulfillment_group_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `fulfillment_option_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `retail_price` decimal(19,5) DEFAULT NULL,
  `sale_price` decimal(19,5) DEFAULT NULL,
  `price` decimal(19,5) DEFAULT NULL,
  `merchandise_total` decimal(19,5) DEFAULT NULL,
  `total` decimal(19,5) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `fulfillment_group_sequnce` int(11) DEFAULT NULL,
  `shipping_override` tinyint(1) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`fulfillment_group_id`),
  KEY `fg_address_index` (`address_id`),
  KEY `fg_order_index` (`order_id`),
  KEY `fg_reference_index` (`reference_number`),
  KEY `fg_status_index` (`status`),
  KEY `fkc5b9ef1881f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkc5b9ef1881f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fkc5b9ef1889fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`),
  CONSTRAINT `fkc5b9ef18c13085dd` FOREIGN KEY (`address_id`) REFERENCES `nph_address` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_group`
--

LOCK TABLES `nph_fulfillment_group` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_group_fee`
--

DROP TABLE IF EXISTS `nph_fulfillment_group_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_group_fee` (
  `fulfillment_group_fee_id` bigint(20) NOT NULL,
  `amount` decimal(19,5) DEFAULT NULL,
  `fee_taxable_flag` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `reporting_code` varchar(255) DEFAULT NULL,
  `total_fee_tax` decimal(19,5) DEFAULT NULL,
  `fulfillment_group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fulfillment_group_fee_id`),
  KEY `fk6aa8e1bf5028dc55` (`fulfillment_group_id`),
  CONSTRAINT `fk6aa8e1bf5028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_group_fee`
--

LOCK TABLES `nph_fulfillment_group_fee` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_group_fee` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_group_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_group_item`
--

DROP TABLE IF EXISTS `nph_fulfillment_group_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_group_item` (
  `fulfillment_group_item_id` bigint(20) NOT NULL,
  `prorated_order_adj` decimal(19,2) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_item_amount` decimal(19,5) DEFAULT NULL,
  `fulfillment_group_id` bigint(20) NOT NULL,
  `order_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fulfillment_group_item_id`),
  KEY `fgitem_fg_index` (`fulfillment_group_id`),
  KEY `fgitem_status_index` (`status`),
  KEY `fkea74ebda9af166df` (`order_item_id`),
  CONSTRAINT `fkea74ebda5028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`),
  CONSTRAINT `fkea74ebda9af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_group_item`
--

LOCK TABLES `nph_fulfillment_group_item` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_group_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_group_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_opt_banded_prc`
--

DROP TABLE IF EXISTS `nph_fulfillment_opt_banded_prc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_opt_banded_prc` (
  `fulfillment_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fkb1fd71e981f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkb1fd71e981f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_opt_banded_prc`
--

LOCK TABLES `nph_fulfillment_opt_banded_prc` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_opt_banded_prc` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_opt_banded_prc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_opt_banded_wgt`
--

DROP TABLE IF EXISTS `nph_fulfillment_opt_banded_wgt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_opt_banded_wgt` (
  `fulfillment_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fkb1fd8aec81f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkb1fd8aec81f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_opt_banded_wgt`
--

LOCK TABLES `nph_fulfillment_opt_banded_wgt` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_opt_banded_wgt` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_opt_banded_wgt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_option`
--

DROP TABLE IF EXISTS `nph_fulfillment_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_option` (
  `fulfillment_option_id` bigint(20) NOT NULL,
  `fulfillment_type` varchar(255) NOT NULL,
  `long_description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `use_flat_rates` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`fulfillment_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_option`
--

LOCK TABLES `nph_fulfillment_option` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_option_fixed`
--

DROP TABLE IF EXISTS `nph_fulfillment_option_fixed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_option_fixed` (
  `fulfillment_option_id` bigint(20) NOT NULL,
  `price` decimal(19,5) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fk4083603181f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fk4083603181f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_option_fixed`
--

LOCK TABLES `nph_fulfillment_option_fixed` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_option_fixed` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_option_fixed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_price_band`
--

DROP TABLE IF EXISTS `nph_fulfillment_price_band`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_price_band` (
  `fulfillment_price_band_id` bigint(20) NOT NULL,
  `result_amount` decimal(19,5) NOT NULL,
  `result_amount_type` varchar(255) NOT NULL,
  `retail_price_minimum_amount` decimal(19,5) NOT NULL,
  `fulfillment_option_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`fulfillment_price_band_id`),
  KEY `fk46c9ea726cdf59ca` (`fulfillment_option_id`),
  CONSTRAINT `fk46c9ea726cdf59ca` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_opt_banded_prc` (`fulfillment_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_price_band`
--

LOCK TABLES `nph_fulfillment_price_band` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_price_band` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_price_band` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_fulfillment_weight_band`
--

DROP TABLE IF EXISTS `nph_fulfillment_weight_band`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_fulfillment_weight_band` (
  `fulfillment_weight_band_id` bigint(20) NOT NULL,
  `result_amount` decimal(19,5) NOT NULL,
  `result_amount_type` varchar(255) NOT NULL,
  `minimum_weight` decimal(19,5) DEFAULT NULL,
  `weight_unit_of_measure` varchar(255) DEFAULT NULL,
  `fulfillment_option_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`fulfillment_weight_band_id`),
  KEY `fk6a048d95a0b429c3` (`fulfillment_option_id`),
  CONSTRAINT `fk6a048d95a0b429c3` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_opt_banded_wgt` (`fulfillment_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_fulfillment_weight_band`
--

LOCK TABLES `nph_fulfillment_weight_band` WRITE;
/*!40000 ALTER TABLE `nph_fulfillment_weight_band` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_fulfillment_weight_band` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_gift_card_payment`
--

DROP TABLE IF EXISTS `nph_gift_card_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_gift_card_payment` (
  `payment_id` bigint(20) NOT NULL,
  `pan` varchar(255) NOT NULL,
  `pin` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `giftcard_index` (`reference_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_gift_card_payment`
--

LOCK TABLES `nph_gift_card_payment` WRITE;
/*!40000 ALTER TABLE `nph_gift_card_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_gift_card_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_item_offer_qualifier`
--

DROP TABLE IF EXISTS `nph_item_offer_qualifier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_item_offer_qualifier` (
  `item_offer_qualifier_id` bigint(20) NOT NULL,
  `quantity` bigint(20) DEFAULT NULL,
  `offer_id` bigint(20) NOT NULL,
  `order_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_offer_qualifier_id`),
  KEY `fkd9c50c61d5f3faf4` (`offer_id`),
  KEY `fkd9c50c619af166df` (`order_item_id`),
  CONSTRAINT `fkd9c50c619af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`),
  CONSTRAINT `fkd9c50c61d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_item_offer_qualifier`
--

LOCK TABLES `nph_item_offer_qualifier` WRITE;
/*!40000 ALTER TABLE `nph_item_offer_qualifier` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_item_offer_qualifier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_media`
--

DROP TABLE IF EXISTS `nph_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_media` (
  `media_id` bigint(20) NOT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`media_id`),
  KEY `media_title_index` (`title`),
  KEY `media_url_index` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_media`
--

LOCK TABLES `nph_media` WRITE;
/*!40000 ALTER TABLE `nph_media` DISABLE KEYS */;
INSERT INTO `nph_media` VALUES (100650,NULL,NULL,NULL,'/cmsstatic/product/hongqianfushi.jpg');
/*!40000 ALTER TABLE `nph_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_module_configuration`
--

DROP TABLE IF EXISTS `nph_module_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_module_configuration` (
  `module_config_id` bigint(20) NOT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `active_start_date` datetime DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `config_type` varchar(255) NOT NULL,
  `is_default` tinyint(1) NOT NULL,
  `module_name` varchar(255) NOT NULL,
  `module_priority` int(11) NOT NULL,
  PRIMARY KEY (`module_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_module_configuration`
--

LOCK TABLES `nph_module_configuration` WRITE;
/*!40000 ALTER TABLE `nph_module_configuration` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_module_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_net_node`
--

DROP TABLE IF EXISTS `nph_net_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_net_node` (
  `net_node_id` bigint(20) NOT NULL,
  `net_node_name` varchar(45) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `contact_with` varchar(20) DEFAULT NULL COMMENT '联系人 ',
  `phone_number` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_net_node`
--

LOCK TABLES `nph_net_node` WRITE;
/*!40000 ALTER TABLE `nph_net_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_net_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer`
--

DROP TABLE IF EXISTS `nph_offer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer` (
  `offer_id` bigint(20) NOT NULL,
  `applies_when_rules` longtext,
  `applies_to_rules` longtext,
  `apply_offer_to_marked_items` tinyint(1) DEFAULT NULL,
  `apply_to_sale_price` tinyint(1) DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `automatically_added` tinyint(1) DEFAULT NULL,
  `combinable_with_other_offers` tinyint(1) DEFAULT NULL,
  `offer_delivery_type` varchar(255) DEFAULT NULL,
  `offer_description` varchar(255) DEFAULT NULL,
  `offer_discount_type` varchar(255) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `marketing_messasge` varchar(255) DEFAULT NULL,
  `max_uses_per_customer` bigint(20) DEFAULT NULL,
  `max_uses` int(11) DEFAULT NULL,
  `offer_name` varchar(255) NOT NULL,
  `offer_item_qualifier_rule` varchar(255) DEFAULT NULL,
  `offer_item_target_rule` varchar(255) DEFAULT NULL,
  `offer_priority` int(11) DEFAULT NULL,
  `qualifying_item_min_total` decimal(19,5) DEFAULT NULL,
  `requires_related_tar_qual` tinyint(1) DEFAULT NULL,
  `stackable` tinyint(1) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `target_system` varchar(255) DEFAULT NULL,
  `totalitarian_offer` tinyint(1) DEFAULT NULL,
  `use_new_format` tinyint(1) DEFAULT NULL,
  `offer_type` varchar(255) NOT NULL,
  `uses` int(11) DEFAULT NULL,
  `offer_value` decimal(19,5) NOT NULL,
  PRIMARY KEY (`offer_id`),
  KEY `offer_discount_index` (`offer_discount_type`),
  KEY `offer_marketing_message_index` (`marketing_messasge`),
  KEY `offer_name_index` (`offer_name`),
  KEY `offer_type_index` (`offer_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer`
--

LOCK TABLES `nph_offer` WRITE;
/*!40000 ALTER TABLE `nph_offer` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_offer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_audit`
--

DROP TABLE IF EXISTS `nph_offer_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_audit` (
  `offer_audit_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `offer_code_id` bigint(20) DEFAULT NULL,
  `offer_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `redeemed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`offer_audit_id`),
  KEY `offeraudit_customer_index` (`customer_id`),
  KEY `offeraudit_offer_code_index` (`offer_code_id`),
  KEY `offeraudit_offer_index` (`offer_id`),
  KEY `offeraudit_order_index` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_audit`
--

LOCK TABLES `nph_offer_audit` WRITE;
/*!40000 ALTER TABLE `nph_offer_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_offer_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_info`
--

DROP TABLE IF EXISTS `nph_offer_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_info` (
  `offer_info_id` bigint(20) NOT NULL,
  PRIMARY KEY (`offer_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_info`
--

LOCK TABLES `nph_offer_info` WRITE;
/*!40000 ALTER TABLE `nph_offer_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_offer_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_info_fields`
--

DROP TABLE IF EXISTS `nph_offer_info_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_info_fields` (
  `offer_info_fields_id` bigint(20) NOT NULL,
  `field_value` varchar(100) DEFAULT NULL,
  `field_name` varchar(50) NOT NULL,
  PRIMARY KEY (`offer_info_fields_id`,`field_name`),
  KEY `fka901886183ae7237` (`offer_info_fields_id`),
  CONSTRAINT `fka901886183ae7237` FOREIGN KEY (`offer_info_fields_id`) REFERENCES `nph_offer_info` (`offer_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_info_fields`
--

LOCK TABLES `nph_offer_info_fields` WRITE;
/*!40000 ALTER TABLE `nph_offer_info_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_offer_info_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_item_criteria`
--

DROP TABLE IF EXISTS `nph_offer_item_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_item_criteria` (
  `offer_item_criteria_id` bigint(20) NOT NULL,
  `order_item_match_rule` longtext,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`offer_item_criteria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_item_criteria`
--

LOCK TABLES `nph_offer_item_criteria` WRITE;
/*!40000 ALTER TABLE `nph_offer_item_criteria` DISABLE KEYS */;
INSERT INTO `nph_offer_item_criteria` VALUES (1,'MVEL.eval(\"toUpperCase()\",discreteOrderItem.category.name)==MVEL.eval(\"toUpperCase()\",\"merchandise\")',1),(950,'orderItem.?name!=null',1),(1000,'orderItem.?name!=null',1),(1050,'orderItem.?name!=null',1),(1051,'orderItem.?price.getAmount()>1',1);
/*!40000 ALTER TABLE `nph_offer_item_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_rule`
--

DROP TABLE IF EXISTS `nph_offer_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_rule` (
  `offer_rule_id` bigint(20) NOT NULL,
  `match_rule` longtext,
  PRIMARY KEY (`offer_rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_rule`
--

LOCK TABLES `nph_offer_rule` WRITE;
/*!40000 ALTER TABLE `nph_offer_rule` DISABLE KEYS */;
INSERT INTO `nph_offer_rule` VALUES (3,'order.?subTotal.getAmount()>100');
/*!40000 ALTER TABLE `nph_offer_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_offer_rule_map`
--

DROP TABLE IF EXISTS `nph_offer_rule_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_offer_rule_map` (
  `offer_id` bigint(20) NOT NULL,
  `offer_rule_id` bigint(20) NOT NULL,
  `map_key` varchar(255) NOT NULL,
  PRIMARY KEY (`offer_id`,`map_key`),
  KEY `fkca468fe2c11a218d` (`offer_rule_id`),
  KEY `fkca468fe245c66d1d` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_offer_rule_map`
--

LOCK TABLES `nph_offer_rule_map` WRITE;
/*!40000 ALTER TABLE `nph_offer_rule_map` DISABLE KEYS */;
INSERT INTO `nph_offer_rule_map` VALUES (1000,3,'ORDER');
/*!40000 ALTER TABLE `nph_offer_rule_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order`
--

DROP TABLE IF EXISTS `nph_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `order_number` varchar(50) DEFAULT NULL,
  `is_preview` tinyint(1) DEFAULT NULL,
  `order_status` varchar(50) DEFAULT NULL,
  `order_subtotal` decimal(19,5) DEFAULT NULL,
  `submit_date` datetime DEFAULT NULL,
  `confirm_date` datetime DEFAULT NULL,
  `COMPLETE_DATE` datetime DEFAULT NULL,
  `order_total` decimal(19,5) DEFAULT NULL,
  `total_shipping` decimal(19,5) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  `order_address_id` bigint(20) DEFAULT NULL,
  `apply_coupon_id` bigint(20) DEFAULT NULL,
  `APPLY_COUPON_CODE_ID` bigint(20) DEFAULT NULL,
  `order_coupon_discount` decimal(19,5) DEFAULT NULL,
  `ORDER_COUPON_CODE_DISCOUNT` decimal(19,5) DEFAULT NULL,
  `parent_order_id` bigint(20) DEFAULT NULL,
  `is_review` tinyint(1) DEFAULT '0',
  `is_delivery_info_sent` tinyint(1) DEFAULT '0',
  `is_shipped` tinyint(1) DEFAULT '0',
  `delivery_type` varchar(20) DEFAULT NULL COMMENT 'PICKUP - 自提, LOGISTICS - 物流, EXPRESS - 快递',
  `provider_id` bigint(20) DEFAULT NULL,
  `group_on_id` bigint(20) DEFAULT NULL,
  `is_group_on` tinyint(1) DEFAULT '0',
  `remark` varchar(500) DEFAULT NULL,
  `ORDER_DEDUCTIONBONUS` int(11) DEFAULT '0',
  PRIMARY KEY (`order_id`),
  KEY `order_customer_index` (`customer_id`),
  KEY `order_name_index` (`name`),
  KEY `order_number_index` (`order_number`),
  KEY `order_status_index` (`order_status`),
  CONSTRAINT `fk8f5b64a87470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order`
--

LOCK TABLES `nph_order` WRITE;
/*!40000 ALTER TABLE `nph_order` DISABLE KEYS */;
INSERT INTO `nph_order` VALUES (1,1,'2015-11-23 20:40:24','2015-11-23 20:40:25',1,NULL,NULL,NULL,'IN_PROCESS',60.00000,NULL,NULL,NULL,60.00000,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,NULL,NULL,NULL,0,NULL,0),(2,4,'2015-12-29 21:40:34','2015-12-29 21:40:36',4,NULL,NULL,NULL,'IN_PROCESS',120.00000,NULL,NULL,NULL,120.00000,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,NULL,NULL,NULL,0,NULL,0);
/*!40000 ALTER TABLE `nph_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_address`
--

DROP TABLE IF EXISTS `nph_order_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `receiver` varchar(20) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `province` varchar(10) NOT NULL,
  `city` varchar(10) NOT NULL,
  `district` varchar(10) NOT NULL,
  `community` varchar(20) DEFAULT NULL,
  `address` varchar(100) NOT NULL,
  `ex_name` varchar(45) DEFAULT NULL,
  `ex_no` varchar(45) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `delivery_type` varchar(20) DEFAULT NULL,
  `pickup_address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id_unique` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_address`
--

LOCK TABLES `nph_order_address` WRITE;
/*!40000 ALTER TABLE `nph_order_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_adjustment`
--

DROP TABLE IF EXISTS `nph_order_adjustment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_adjustment` (
  `order_adjustment_id` bigint(20) NOT NULL,
  `adjustment_reason` varchar(100) NOT NULL,
  `adjustment_value` decimal(19,5) NOT NULL,
  `offer_id` bigint(20) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_adjustment_id`),
  KEY `orderadjust_offer_index` (`offer_id`),
  KEY `orderadjust_order_index` (`order_id`),
  CONSTRAINT `fk1e92d16489fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_adjustment`
--

LOCK TABLES `nph_order_adjustment` WRITE;
/*!40000 ALTER TABLE `nph_order_adjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_adjustment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_item`
--

DROP TABLE IF EXISTS `nph_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_item` (
  `order_item_id` bigint(20) NOT NULL,
  `discounts_allowed` tinyint(1) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `order_item_type` varchar(50) DEFAULT NULL,
  `price` decimal(19,5) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `retail_price` decimal(19,5) DEFAULT NULL,
  `retail_price_override` tinyint(1) DEFAULT NULL,
  `sale_price` decimal(19,5) DEFAULT NULL,
  `sale_price_override` tinyint(1) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `sku_id` bigint(20) DEFAULT NULL,
  `is_used` tinyint(1) DEFAULT '0',
  `is_review` tinyint(1) DEFAULT '0',
  `delivery_type` varchar(20) DEFAULT NULL,
  `pickup_address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `orderitem_order_index` (`order_id`),
  KEY `orderitem_type_index` (`order_item_type`),
  KEY `orderitem_message_index` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_item`
--

LOCK TABLES `nph_order_item` WRITE;
/*!40000 ALTER TABLE `nph_order_item` DISABLE KEYS */;
INSERT INTO `nph_order_item` VALUES (8301,NULL,'苹果-泓前富士[陕西黄土高原][一级]-15斤/箱','cn.globalph.b2c.order.domain.OrderItem',60.00000,1,60.00000,0,60.00000,0,1,1,1,0,NULL,NULL),(8351,NULL,'苹果-泓前富士[陕西黄土高原][一级]-15斤/箱','cn.globalph.b2c.order.domain.OrderItem',60.00000,2,60.00000,0,60.00000,0,2,1,1,0,NULL,NULL);
/*!40000 ALTER TABLE `nph_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_item_dtl_adj`
--

DROP TABLE IF EXISTS `nph_order_item_dtl_adj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_item_dtl_adj` (
  `order_item_dtl_adj_id` bigint(20) NOT NULL,
  `applied_to_sale_price` tinyint(1) DEFAULT NULL,
  `offer_name` varchar(255) DEFAULT NULL,
  `adjustment_reason` varchar(255) NOT NULL,
  `adjustment_value` decimal(19,5) NOT NULL,
  `offer_id` bigint(20) NOT NULL,
  `order_item_price_dtl_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_item_dtl_adj_id`),
  KEY `fk85f0248fd5f3faf4` (`offer_id`),
  KEY `fk85f0248fd4aea2c0` (`order_item_price_dtl_id`),
  CONSTRAINT `fk85f0248fd4aea2c0` FOREIGN KEY (`order_item_price_dtl_id`) REFERENCES `nph_order_item_price_dtl` (`order_item_price_dtl_id`),
  CONSTRAINT `fk85f0248fd5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_item_dtl_adj`
--

LOCK TABLES `nph_order_item_dtl_adj` WRITE;
/*!40000 ALTER TABLE `nph_order_item_dtl_adj` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_item_dtl_adj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_item_price_dtl`
--

DROP TABLE IF EXISTS `nph_order_item_price_dtl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_item_price_dtl` (
  `order_item_price_dtl_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `use_sale_price` tinyint(1) DEFAULT NULL,
  `order_item_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_item_price_dtl_id`),
  KEY `fk1fb64bf19af166df` (`order_item_id`),
  CONSTRAINT `fk1fb64bf19af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_item_price_dtl`
--

LOCK TABLES `nph_order_item_price_dtl` WRITE;
/*!40000 ALTER TABLE `nph_order_item_price_dtl` DISABLE KEYS */;
INSERT INTO `nph_order_item_price_dtl` VALUES (8051,1,1,8301),(8101,2,1,8351);
/*!40000 ALTER TABLE `nph_order_item_price_dtl` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_log`
--

DROP TABLE IF EXISTS `nph_order_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `type` varchar(10) NOT NULL,
  `message` varchar(255) NOT NULL,
  `operator` varchar(45) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `is_display` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_log`
--

LOCK TABLES `nph_order_log` WRITE;
/*!40000 ALTER TABLE `nph_order_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_payment`
--

DROP TABLE IF EXISTS `nph_order_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_payment` (
  `order_payment_id` bigint(20) NOT NULL,
  `amount` decimal(19,5) DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `gateway_type` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`order_payment_id`),
  KEY `orderpayment_order_index` (`order_id`),
  KEY `orderpayment_reference_index` (`reference_number`),
  KEY `orderpayment_type_index` (`payment_type`),
  CONSTRAINT `fk9517a14f89fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_payment`
--

LOCK TABLES `nph_order_payment` WRITE;
/*!40000 ALTER TABLE `nph_order_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_order_payment_transaction`
--

DROP TABLE IF EXISTS `nph_order_payment_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_order_payment_transaction` (
  `payment_transaction_id` bigint(20) NOT NULL,
  `transaction_amount` decimal(19,2) DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `customer_ip_address` varchar(255) DEFAULT NULL,
  `date_recorded` datetime DEFAULT NULL,
  `raw_response` longtext,
  `success` tinyint(1) DEFAULT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `order_payment` bigint(20) NOT NULL,
  `parent_transaction` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`payment_transaction_id`),
  KEY `fk86fde7ce6a69dd9d` (`order_payment`),
  KEY `fk86fde7cee1b66c71` (`parent_transaction`),
  CONSTRAINT `fk86fde7ce6a69dd9d` FOREIGN KEY (`order_payment`) REFERENCES `nph_order_payment` (`order_payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_order_payment_transaction`
--

LOCK TABLES `nph_order_payment_transaction` WRITE;
/*!40000 ALTER TABLE `nph_order_payment_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_order_payment_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product`
--

DROP TABLE IF EXISTS `nph_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product` (
  `product_id` bigint(20) NOT NULL,
  `product_name` varchar(50) DEFAULT NULL,
  `archived` char(1) DEFAULT NULL,
  `active_start_date` datetime DEFAULT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `display_template` varchar(50) DEFAULT NULL,
  `is_featured_product` tinyint(1) NOT NULL,
  `manufacture` varchar(30) DEFAULT NULL,
  `breed` varchar(30) DEFAULT NULL,
  `grade` varchar(30) DEFAULT NULL,
  `model` varchar(30) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `url_key` varchar(255) DEFAULT NULL,
  `provider_id` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`product_id`),
  KEY `product_url_index` (`url`,`url_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product`
--

LOCK TABLES `nph_product` WRITE;
/*!40000 ALTER TABLE `nph_product` DISABLE KEYS */;
INSERT INTO `nph_product` VALUES (10250,'苹果-泓前富士[陕西黄土高原]','N',NULL,NULL,NULL,0,'陕西','泓前富士','一级',NULL,'/apple/hongqianfushi',NULL,51);
/*!40000 ALTER TABLE `nph_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_attribute`
--

DROP TABLE IF EXISTS `nph_product_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_attribute` (
  `product_attribute_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `searchable` tinyint(1) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_attribute_id`),
  KEY `productattribute_name_index` (`name`),
  KEY `productattribute_index` (`product_id`),
  CONSTRAINT `fk56ce05865f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_attribute`
--

LOCK TABLES `nph_product_attribute` WRITE;
/*!40000 ALTER TABLE `nph_product_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_cross_sale`
--

DROP TABLE IF EXISTS `nph_product_cross_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_cross_sale` (
  `cross_sale_product_id` bigint(20) NOT NULL,
  `promotion_message` varchar(255) DEFAULT NULL,
  `sequence` decimal(10,6) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `related_sale_product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cross_sale_product_id`),
  KEY `crosssale_category_index` (`category_id`),
  KEY `crosssale_index` (`product_id`),
  KEY `crosssale_related_index` (`related_sale_product_id`),
  CONSTRAINT `fk8324fb3c15d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk8324fb3c5f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fk8324fb3c62d84f9b` FOREIGN KEY (`related_sale_product_id`) REFERENCES `nph_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_cross_sale`
--

LOCK TABLES `nph_product_cross_sale` WRITE;
/*!40000 ALTER TABLE `nph_product_cross_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_cross_sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_featured`
--

DROP TABLE IF EXISTS `nph_product_featured`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_featured` (
  `featured_product_id` bigint(20) NOT NULL,
  `promotion_message` varchar(255) DEFAULT NULL,
  `sequence` decimal(10,6) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`featured_product_id`),
  KEY `prodfeatured_category_index` (`category_id`),
  KEY `prodfeatured_product_index` (`product_id`),
  CONSTRAINT `fk4c49ffe415d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk4c49ffe45f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_featured`
--

LOCK TABLES `nph_product_featured` WRITE;
/*!40000 ALTER TABLE `nph_product_featured` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_featured` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_option`
--

DROP TABLE IF EXISTS `nph_product_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_option` (
  `product_option_id` bigint(20) NOT NULL,
  `attribute_name` varchar(30) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `error_code` varchar(30) DEFAULT NULL,
  `error_message` varchar(100) DEFAULT NULL,
  `label` varchar(30) DEFAULT NULL,
  `validation_strategy_type` varchar(30) DEFAULT NULL,
  `validation_type` varchar(30) DEFAULT NULL,
  `required` tinyint(1) DEFAULT NULL,
  `option_type` varchar(30) DEFAULT NULL,
  `use_in_sku_generation` tinyint(1) DEFAULT NULL,
  `validation_string` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`product_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_option`
--

LOCK TABLES `nph_product_option` WRITE;
/*!40000 ALTER TABLE `nph_product_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_option_value`
--

DROP TABLE IF EXISTS `nph_product_option_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_option_value` (
  `product_option_value_id` bigint(20) NOT NULL,
  `attribute_value` varchar(50) DEFAULT NULL,
  `display_order` bigint(20) DEFAULT NULL,
  `price_adjustment` decimal(19,5) DEFAULT NULL,
  `product_option_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`product_option_value_id`),
  KEY `fk6deeedbd92ea8136` (`product_option_id`),
  CONSTRAINT `fk6deeedbd92ea8136` FOREIGN KEY (`product_option_id`) REFERENCES `nph_product_option` (`product_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_option_value`
--

LOCK TABLES `nph_product_option_value` WRITE;
/*!40000 ALTER TABLE `nph_product_option_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_option_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_option_xref`
--

DROP TABLE IF EXISTS `nph_product_option_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_option_xref` (
  `product_option_xref_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_option_xref_id`),
  KEY `fkda42ab2f5f11a0b7` (`product_id`),
  KEY `fkda42ab2f92ea8136` (`product_option_id`),
  CONSTRAINT `fkda42ab2f5f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fkda42ab2f92ea8136` FOREIGN KEY (`product_option_id`) REFERENCES `nph_product_option` (`product_option_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_option_xref`
--

LOCK TABLES `nph_product_option_xref` WRITE;
/*!40000 ALTER TABLE `nph_product_option_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_option_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_product_up_sale`
--

DROP TABLE IF EXISTS `nph_product_up_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_product_up_sale` (
  `up_sale_product_id` bigint(20) NOT NULL,
  `promotion_message` varchar(255) DEFAULT NULL,
  `sequence` decimal(10,6) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `related_sale_product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`up_sale_product_id`),
  KEY `upsale_category_index` (`category_id`),
  KEY `upsale_product_index` (`product_id`),
  KEY `upsale_related_index` (`related_sale_product_id`),
  CONSTRAINT `fkf69054f515d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fkf69054f55f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fkf69054f562d84f9b` FOREIGN KEY (`related_sale_product_id`) REFERENCES `nph_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_product_up_sale`
--

LOCK TABLES `nph_product_up_sale` WRITE;
/*!40000 ALTER TABLE `nph_product_up_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_product_up_sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_provider`
--

DROP TABLE IF EXISTS `nph_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_provider` (
  `provider_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `provider_name` varchar(45) NOT NULL,
  `is_support_pickup` tinyint(1) DEFAULT '0',
  `PROVIDER_EN_NAME` varchar(45) DEFAULT NULL,
  `PROVIDER_EMAIL` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`provider_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_provider`
--

LOCK TABLES `nph_provider` WRITE;
/*!40000 ALTER TABLE `nph_provider` DISABLE KEYS */;
INSERT INTO `nph_provider` VALUES (51,'农品荟',0,'nongph',NULL);
/*!40000 ALTER TABLE `nph_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_qual_crit_offer_xref`
--

DROP TABLE IF EXISTS `nph_qual_crit_offer_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_qual_crit_offer_xref` (
  `offer_item_criteria_id` bigint(20) NOT NULL,
  `offer_id` bigint(20) NOT NULL,
  PRIMARY KEY (`offer_id`,`offer_item_criteria_id`),
  UNIQUE KEY `uk_d592e919e7ab0252` (`offer_item_criteria_id`),
  KEY `fkd592e9193615a91a` (`offer_item_criteria_id`),
  KEY `fkd592e919d5f3faf4` (`offer_id`),
  CONSTRAINT `fkd592e9193615a91a` FOREIGN KEY (`offer_item_criteria_id`) REFERENCES `nph_offer_item_criteria` (`offer_item_criteria_id`),
  CONSTRAINT `fkd592e919d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_qual_crit_offer_xref`
--

LOCK TABLES `nph_qual_crit_offer_xref` WRITE;
/*!40000 ALTER TABLE `nph_qual_crit_offer_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_qual_crit_offer_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_rating_detail`
--

DROP TABLE IF EXISTS `nph_rating_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_rating_detail` (
  `rating_detail_id` bigint(20) NOT NULL,
  `rating` double NOT NULL,
  `rating_submitted_date` datetime NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `rating_summary_id` bigint(20) NOT NULL,
  `LOGISTICS_RATING` double NOT NULL,
  `SERVE_RATING` double NOT NULL,
  PRIMARY KEY (`rating_detail_id`),
  KEY `rating_customer_index` (`customer_id`),
  KEY `fkc9d04add4e76bf4` (`rating_summary_id`),
  CONSTRAINT `fkc9d04ad7470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fkc9d04add4e76bf4` FOREIGN KEY (`rating_summary_id`) REFERENCES `nph_rating_summary` (`rating_summary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_rating_detail`
--

LOCK TABLES `nph_rating_detail` WRITE;
/*!40000 ALTER TABLE `nph_rating_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_rating_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_rating_summary`
--

DROP TABLE IF EXISTS `nph_rating_summary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_rating_summary` (
  `rating_summary_id` bigint(20) NOT NULL,
  `average_rating` double NOT NULL,
  `item_id` varchar(255) NOT NULL,
  `rating_type` varchar(255) NOT NULL,
  PRIMARY KEY (`rating_summary_id`),
  KEY `ratingsumm_item_index` (`item_id`),
  KEY `ratingsumm_type_index` (`rating_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_rating_summary`
--

LOCK TABLES `nph_rating_summary` WRITE;
/*!40000 ALTER TABLE `nph_rating_summary` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_rating_summary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_review_detail`
--

DROP TABLE IF EXISTS `nph_review_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_review_detail` (
  `review_detail_id` bigint(20) NOT NULL,
  `helpful_count` int(11) NOT NULL,
  `not_helpful_count` int(11) NOT NULL,
  `review_submitted_date` datetime NOT NULL,
  `review_status` varchar(255) NOT NULL,
  `review_text` varchar(255) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `rating_detail_id` bigint(20) DEFAULT NULL,
  `rating_summary_id` bigint(20) NOT NULL,
  `order_item_id` bigint(20) NOT NULL,
  PRIMARY KEY (`review_detail_id`),
  KEY `reviewdetail_customer_index` (`customer_id`),
  KEY `reviewdetail_rating_index` (`rating_detail_id`),
  KEY `reviewdetail_summary_index` (`rating_summary_id`),
  KEY `reviewdetail_status_index` (`review_status`),
  CONSTRAINT `fk9cd7e69245dc39e0` FOREIGN KEY (`rating_detail_id`) REFERENCES `nph_rating_detail` (`rating_detail_id`),
  CONSTRAINT `fk9cd7e6927470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk9cd7e692d4e76bf4` FOREIGN KEY (`rating_summary_id`) REFERENCES `nph_rating_summary` (`rating_summary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_review_detail`
--

LOCK TABLES `nph_review_detail` WRITE;
/*!40000 ALTER TABLE `nph_review_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_review_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_review_feedback`
--

DROP TABLE IF EXISTS `nph_review_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_review_feedback` (
  `review_feedback_id` bigint(20) NOT NULL,
  `is_helpful` tinyint(1) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `review_detail_id` bigint(20) NOT NULL,
  PRIMARY KEY (`review_feedback_id`),
  KEY `reviewfeed_customer_index` (`customer_id`),
  KEY `reviewfeed_detail_index` (`review_detail_id`),
  CONSTRAINT `fk7cc929867470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk7cc92986ae4769d6` FOREIGN KEY (`review_detail_id`) REFERENCES `nph_review_detail` (`review_detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_review_feedback`
--

LOCK TABLES `nph_review_feedback` WRITE;
/*!40000 ALTER TABLE `nph_review_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_review_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_role`
--

DROP TABLE IF EXISTS `nph_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_role` (
  `role_id` bigint(20) NOT NULL,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `role_name_index` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_role`
--

LOCK TABLES `nph_role` WRITE;
/*!40000 ALTER TABLE `nph_role` DISABLE KEYS */;
INSERT INTO `nph_role` VALUES (1,'ROLE_USER');
/*!40000 ALTER TABLE `nph_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_facet`
--

DROP TABLE IF EXISTS `nph_search_facet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_facet` (
  `search_facet_id` bigint(20) NOT NULL,
  `multiselect` tinyint(1) DEFAULT NULL,
  `label` varchar(20) DEFAULT NULL,
  `requires_all_dependent` tinyint(1) DEFAULT NULL,
  `search_display_priority` int(11) DEFAULT NULL,
  `show_on_search` tinyint(1) DEFAULT NULL,
  `field_id` bigint(20) NOT NULL,
  PRIMARY KEY (`search_facet_id`),
  KEY `fk4ffcc9863c3907c4` (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_facet`
--

LOCK TABLES `nph_search_facet` WRITE;
/*!40000 ALTER TABLE `nph_search_facet` DISABLE KEYS */;
INSERT INTO `nph_search_facet` VALUES (1,1,'产地',NULL,0,0,1),(2,1,'Heat Range',NULL,0,0,2),(3,1,'价格',NULL,1,1,3),(4,1,'颜色',NULL,0,1,8),(5,1,'品种',NULL,2,0,9),(6,1,'等级',NULL,2,0,10);
/*!40000 ALTER TABLE `nph_search_facet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_facet_range`
--

DROP TABLE IF EXISTS `nph_search_facet_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_facet_range` (
  `search_facet_range_id` bigint(20) NOT NULL,
  `max_value` decimal(19,5) DEFAULT NULL,
  `min_value` decimal(19,5) NOT NULL,
  `search_facet_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`search_facet_range_id`),
  KEY `search_facet_index` (`search_facet_id`),
  CONSTRAINT `fk7ec3b124b96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_facet_range`
--

LOCK TABLES `nph_search_facet_range` WRITE;
/*!40000 ALTER TABLE `nph_search_facet_range` DISABLE KEYS */;
INSERT INTO `nph_search_facet_range` VALUES (1,5.00000,0.00000,3),(2,10.00000,5.00000,3),(3,15.00000,10.00000,3),(4,NULL,15.00000,3);
/*!40000 ALTER TABLE `nph_search_facet_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_facet_xref`
--

DROP TABLE IF EXISTS `nph_search_facet_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_facet_xref` (
  `id` bigint(20) NOT NULL,
  `required_facet_id` bigint(20) NOT NULL,
  `search_facet_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk35a63034da7e1c7c` (`required_facet_id`),
  KEY `fk35a63034b96b1c93` (`search_facet_id`),
  CONSTRAINT `fk35a63034b96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`),
  CONSTRAINT `fk35a63034da7e1c7c` FOREIGN KEY (`required_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_facet_xref`
--

LOCK TABLES `nph_search_facet_xref` WRITE;
/*!40000 ALTER TABLE `nph_search_facet_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_search_facet_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_field`
--

DROP TABLE IF EXISTS `nph_search_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_field` (
  `field_id` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `entity_type` varchar(255) NOT NULL,
  `facet_field_type` varchar(255) DEFAULT NULL,
  `property_name` varchar(255) NOT NULL,
  `searchable` tinyint(1) DEFAULT NULL,
  `translatable` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`field_id`),
  KEY `entity_type_index` (`entity_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_field`
--

LOCK TABLES `nph_search_field` WRITE;
/*!40000 ALTER TABLE `nph_search_field` DISABLE KEYS */;
INSERT INTO `nph_search_field` VALUES (1,'mfg','PRODUCT','s','manufacturer',1,NULL),(2,'heatRange','PRODUCT','i','productAttributes.heatRange',0,NULL),(3,'price','SKU','p','retailPrice',0,NULL),(4,'name','SKU','s','name',1,1),(5,'model','PRODUCT','s','model',1,NULL),(6,'desc','SKU',NULL,'description',1,1),(7,'ldesc','SKU',NULL,'longDescription',1,1),(8,'color','PRODUCT','s','productAttributes.color',1,NULL),(9,'breed','PRODUCT','s','breed',1,NULL),(10,'grade','PRODUCT','s','grade',1,NULL);
/*!40000 ALTER TABLE `nph_search_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_field_types`
--

DROP TABLE IF EXISTS `nph_search_field_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_field_types` (
  `field_id` bigint(20) NOT NULL,
  `searchable_field_type` varchar(100) DEFAULT NULL,
  KEY `fkf52d130d3c3907c4` (`field_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_field_types`
--

LOCK TABLES `nph_search_field_types` WRITE;
/*!40000 ALTER TABLE `nph_search_field_types` DISABLE KEYS */;
INSERT INTO `nph_search_field_types` VALUES (1,'t'),(4,'t'),(5,'t'),(6,'t'),(7,'t');
/*!40000 ALTER TABLE `nph_search_field_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_intercept`
--

DROP TABLE IF EXISTS `nph_search_intercept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_intercept` (
  `search_redirect_id` bigint(20) NOT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `active_start_date` datetime DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `search_term` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`search_redirect_id`),
  KEY `search_active_index` (`active_end_date`),
  KEY `search_priority_index` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_intercept`
--

LOCK TABLES `nph_search_intercept` WRITE;
/*!40000 ALTER TABLE `nph_search_intercept` DISABLE KEYS */;
INSERT INTO `nph_search_intercept` VALUES (1,NULL,NULL,1,'insanity','/hot-sauces/insanity_sauce'),(2,'1999-10-15 21:28:36','1992-10-15 14:28:36',-10,'new york','/search?q=pace picante');
/*!40000 ALTER TABLE `nph_search_intercept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_search_synonym`
--

DROP TABLE IF EXISTS `nph_search_synonym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_search_synonym` (
  `search_synonym_id` bigint(20) NOT NULL,
  `synonyms` varchar(255) DEFAULT NULL,
  `term` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`search_synonym_id`),
  KEY `searchsynonym_term_index` (`term`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_search_synonym`
--

LOCK TABLES `nph_search_synonym` WRITE;
/*!40000 ALTER TABLE `nph_search_synonym` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_search_synonym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_site`
--

DROP TABLE IF EXISTS `nph_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_site` (
  `site_id` bigint(20) NOT NULL,
  `archived` char(1) DEFAULT NULL,
  `deactivated` tinyint(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `site_identifier_type` varchar(255) DEFAULT NULL,
  `site_identifier_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`site_id`),
  KEY `blc_site_id_val_index` (`site_identifier_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_site`
--

LOCK TABLES `nph_site` WRITE;
/*!40000 ALTER TABLE `nph_site` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_site_catalog`
--

DROP TABLE IF EXISTS `nph_site_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_site_catalog` (
  `catalog_id` bigint(20) NOT NULL,
  `site_id` bigint(20) NOT NULL,
  KEY `fk5f3f2047843a8b63` (`site_id`),
  KEY `fk5f3f2047a350c7f1` (`catalog_id`),
  CONSTRAINT `fk5f3f2047843a8b63` FOREIGN KEY (`site_id`) REFERENCES `nph_site` (`site_id`),
  CONSTRAINT `fk5f3f2047a350c7f1` FOREIGN KEY (`catalog_id`) REFERENCES `nph_catalog` (`catalog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_site_catalog`
--

LOCK TABLES `nph_site_catalog` WRITE;
/*!40000 ALTER TABLE `nph_site_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_site_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_site_map_cfg`
--

DROP TABLE IF EXISTS `nph_site_map_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_site_map_cfg` (
  `indexed_site_map_file_name` varchar(255) DEFAULT NULL,
  `indexed_site_map_file_pattern` varchar(255) DEFAULT NULL,
  `max_url_entries_per_file` int(11) DEFAULT NULL,
  `site_map_file_name` varchar(255) DEFAULT NULL,
  `module_config_id` bigint(20) NOT NULL,
  PRIMARY KEY (`module_config_id`),
  KEY `fk7012930fc50d449` (`module_config_id`),
  CONSTRAINT `fk7012930fc50d449` FOREIGN KEY (`module_config_id`) REFERENCES `nph_module_configuration` (`module_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_site_map_cfg`
--

LOCK TABLES `nph_site_map_cfg` WRITE;
/*!40000 ALTER TABLE `nph_site_map_cfg` DISABLE KEYS */;
INSERT INTO `nph_site_map_cfg` VALUES (NULL,NULL,NULL,NULL,-1);
/*!40000 ALTER TABLE `nph_site_map_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_site_map_gen_cfg`
--

DROP TABLE IF EXISTS `nph_site_map_gen_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_site_map_gen_cfg` (
  `gen_config_id` bigint(20) NOT NULL,
  `change_freq` varchar(255) NOT NULL,
  `disabled` tinyint(1) NOT NULL,
  `generator_type` varchar(255) NOT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `module_config_id` bigint(20) NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk1d76000a340ed71` (`module_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_site_map_gen_cfg`
--

LOCK TABLES `nph_site_map_gen_cfg` WRITE;
/*!40000 ALTER TABLE `nph_site_map_gen_cfg` DISABLE KEYS */;
INSERT INTO `nph_site_map_gen_cfg` VALUES (-4,'HOURLY',0,'CATEGORY','0.5',-1),(-3,'HOURLY',0,'PAGE','0.5',-1),(-2,'HOURLY',0,'PRODUCT','0.5',-1),(-1,'HOURLY',0,'CUSTOM','0.5',-1);
/*!40000 ALTER TABLE `nph_site_map_gen_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_site_map_url_entry`
--

DROP TABLE IF EXISTS `nph_site_map_url_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_site_map_url_entry` (
  `url_entry_id` bigint(20) NOT NULL,
  `change_freq` varchar(255) NOT NULL,
  `last_modified` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `priority` varchar(255) NOT NULL,
  `gen_config_id` bigint(20) NOT NULL,
  PRIMARY KEY (`url_entry_id`),
  KEY `fke2004fed36afe1ee` (`gen_config_id`),
  CONSTRAINT `fke2004fed36afe1ee` FOREIGN KEY (`gen_config_id`) REFERENCES `nph_cust_site_map_gen_cfg` (`gen_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_site_map_url_entry`
--

LOCK TABLES `nph_site_map_url_entry` WRITE;
/*!40000 ALTER TABLE `nph_site_map_url_entry` DISABLE KEYS */;
INSERT INTO `nph_site_map_url_entry` VALUES (-8,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/8','0.5',-1),(-7,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/7','0.5',-1),(-6,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/6','0.5',-1),(-5,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/5','0.5',-1),(-4,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/4','0.5',-1),(-3,'HOURLY','2014-06-08 14:56:32','http://www.heatclinic.com/3','0.5',-1),(-2,'HOURLY','2014-06-08 14:56:31','http://www.heatclinic.com/2','0.5',-1),(-1,'HOURLY','2014-06-08 14:56:31','http://www.heatclinic.com/1','0.5',-1);
/*!40000 ALTER TABLE `nph_site_map_url_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku`
--

DROP TABLE IF EXISTS `nph_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku` (
  `sku_id` bigint(20) NOT NULL,
  `active_end_date` datetime DEFAULT NULL,
  `active_start_date` datetime DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `container_shape` varchar(255) DEFAULT NULL,
  `depth` decimal(19,2) DEFAULT NULL,
  `dimension_unit_of_measure` varchar(255) DEFAULT NULL,
  `girth` decimal(19,2) DEFAULT NULL,
  `height` decimal(19,2) DEFAULT NULL,
  `container_size` varchar(255) DEFAULT NULL,
  `width` decimal(19,2) DEFAULT NULL,
  `discountable_flag` char(1) DEFAULT NULL,
  `fulfillment_type` varchar(20) DEFAULT NULL,
  `inventory_type` varchar(50) DEFAULT NULL,
  `is_machine_sortable` tinyint(1) DEFAULT NULL,
  `long_description` longtext,
  `origin_description` longtext,
  `name` varchar(50) DEFAULT NULL,
  `retail_price` decimal(19,5) DEFAULT NULL,
  `sale_price` decimal(19,5) DEFAULT NULL,
  `weight` decimal(19,2) DEFAULT NULL,
  `weight_unit_of_measure` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `sale_count` int(11) DEFAULT '0',
  `quantity_available` int(11) DEFAULT '0',
  `param_description` longtext,
  `IS_GROUP_ON` tinyint(1) DEFAULT '0',
  `IS_SECKILLING` tinyint(1) DEFAULT '0' COMMENT '是否为秒杀商品',
  `SECKILLING_START_DATE` datetime DEFAULT NULL COMMENT '秒杀开始时间',
  `SECKILLING_END_DATE` datetime DEFAULT NULL COMMENT '秒杀结束时间',
  `LIMIT_NUM` int(11) DEFAULT NULL COMMENT '秒杀活动单个用户购买此商品的最大数目',
  `IS_OFF_LINE` tinyint(1) DEFAULT '0',
  `IS_CODE_SUPPORT` tinyint(1) DEFAULT '0',
  `IS_PRESALE` tinyint(1) DEFAULT '0',
  `IS_POINT_SUPPORT` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`sku_id`),
  KEY `sku_active_end_index` (`active_end_date`),
  KEY `sku_active_start_index` (`active_start_date`),
  KEY `sku_discountable_index` (`discountable_flag`),
  KEY `sku_name_index` (`name`),
  KEY `fk28e82cf77e555d75` (`product_id`),
  CONSTRAINT `fk28e82cf77e555d75` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku`
--

LOCK TABLES `nph_sku` WRITE;
/*!40000 ALTER TABLE `nph_sku` DISABLE KEYS */;
INSERT INTO `nph_sku` VALUES (1,NULL,'2015-11-23 00:00:00','果大，甜度好，口感脆，果肉为淡黄色。高原上空气透过度高，充足的阳光保证了苹果的甜度',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Y',NULL,'ALWAYS_AVAILABLE',1,'<p>有裂纹[1]<a href=\"http://baike.baidu.com/view/617114.htm\">富士苹果</a>系日本青森县从富士中选出的易着色极早熟红富士[1]<a href=\"http://baike.baidu.com/view/1331.htm\">苹果</a>佳品。 1、果实经济性状： 果实近园形，果形指数0.88，单果重220克—520克居多，最大750g。果面底色黄白、条状浓红（条红），着色鲜艳，<a href=\"http://baike.baidu.com/view/396179.htm\">果肉</a>黄白色，汁液多，甜酸适口，可溶性固形物含量大于15%，硬度6.8公斤/平方厘米。常温下存放3个月风味不变，可贮藏至翌年3月，经济效益可观。 2、生长结果习性： 树势强健，萌芽率高成枝率中等，果枝连续结果能力较强。高接换头次年能见花果，大树平茬地面插接3—4个枝条当年可生长3.8米，<a href=\"http://baike.baidu.com/view/36406.htm\">嫁接</a>树当年新梢量大，扩冠迅速，建园3年即有经济效益，4—6年进入盛果期。丰产稳产性优于普通晚熟<a href=\"http://baike.baidu.com/view/150649.htm\">红富士</a>。花期与普遍富士近同。该品种7月中下旬果实开始着色，9月上中旬成熟。果实生育期145—150天，较长富2号提早40天比红将军早熟10天以上。果实商品率高，<a href=\"http://baike.baidu.com/view/1117987.htm\">病虫害</a>较晚熟品种轻。套袋果8月下旬可提前采收。其它习性同普通红富士。</p>',NULL,'苹果-泓前富士[陕西黄土高原][一级]-15斤/箱',60.00000,60.00000,NULL,NULL,10250,NULL,NULL,'<p>苹果品种：泓前富士</p><p>产 地：陕西黄土高原</p><p>生长套袋：膜袋</p><p>等 级：一级 (单果直径85CM以上，重量325g左右)</p><p><strong>特  点：果大，甜度好，口感脆，果肉为淡黄色。</strong><strong>高原上空气透过度高，充足的阳光保证了苹果的甜度。</strong></p>',0,0,NULL,NULL,NULL,0,0,0,0);
/*!40000 ALTER TABLE `nph_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_attribute`
--

DROP TABLE IF EXISTS `nph_sku_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_attribute` (
  `sku_attr_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `searchable` tinyint(1) DEFAULT NULL,
  `value` varchar(255) NOT NULL,
  `sku_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sku_attr_id`),
  KEY `skuattr_name_index` (`name`),
  KEY `skuattr_sku_index` (`sku_id`),
  CONSTRAINT `fk6c6a5934b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_attribute`
--

LOCK TABLES `nph_sku_attribute` WRITE;
/*!40000 ALTER TABLE `nph_sku_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_availability`
--

DROP TABLE IF EXISTS `nph_sku_availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_availability` (
  `sku_availability_id` bigint(20) NOT NULL,
  `availability_date` datetime DEFAULT NULL,
  `availability_status` varchar(255) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `qty_on_hand` int(11) DEFAULT NULL,
  `reserve_qty` int(11) DEFAULT NULL,
  `sku_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sku_availability_id`),
  KEY `skuavail_status_index` (`availability_status`),
  KEY `skuavail_location_index` (`location_id`),
  KEY `skuavail_sku_index` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_availability`
--

LOCK TABLES `nph_sku_availability` WRITE;
/*!40000 ALTER TABLE `nph_sku_availability` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_fee`
--

DROP TABLE IF EXISTS `nph_sku_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_fee` (
  `sku_fee_id` bigint(20) NOT NULL,
  `amount` decimal(19,5) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `expression` longtext,
  `fee_type` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sku_fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_fee`
--

LOCK TABLES `nph_sku_fee` WRITE;
/*!40000 ALTER TABLE `nph_sku_fee` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_fee_xref`
--

DROP TABLE IF EXISTS `nph_sku_fee_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_fee_xref` (
  `sku_fee_id` bigint(20) NOT NULL,
  `sku_id` bigint(20) NOT NULL,
  KEY `fkd88d409cb78c9977` (`sku_id`),
  KEY `fkd88d409ccf4c9a82` (`sku_fee_id`),
  CONSTRAINT `fkd88d409cb78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`),
  CONSTRAINT `fkd88d409ccf4c9a82` FOREIGN KEY (`sku_fee_id`) REFERENCES `nph_sku_fee` (`sku_fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_fee_xref`
--

LOCK TABLES `nph_sku_fee_xref` WRITE;
/*!40000 ALTER TABLE `nph_sku_fee_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_fee_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_fulfillment_excluded`
--

DROP TABLE IF EXISTS `nph_sku_fulfillment_excluded`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_fulfillment_excluded` (
  `sku_id` bigint(20) NOT NULL,
  `fulfillment_option_id` bigint(20) NOT NULL,
  KEY `fk84162d7381f34c7f` (`fulfillment_option_id`),
  KEY `fk84162d73b78c9977` (`sku_id`),
  CONSTRAINT `fk84162d7381f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fk84162d73b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_fulfillment_excluded`
--

LOCK TABLES `nph_sku_fulfillment_excluded` WRITE;
/*!40000 ALTER TABLE `nph_sku_fulfillment_excluded` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_fulfillment_excluded` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_fulfillment_flat_rates`
--

DROP TABLE IF EXISTS `nph_sku_fulfillment_flat_rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_fulfillment_flat_rates` (
  `sku_id` bigint(20) NOT NULL,
  `rate` decimal(19,5) DEFAULT NULL,
  `fulfillment_option_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sku_id`,`fulfillment_option_id`),
  KEY `fkc1988c9681f34c7f` (`fulfillment_option_id`),
  KEY `fkc1988c96b78c9977` (`sku_id`),
  CONSTRAINT `fkc1988c9681f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fkc1988c96b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_fulfillment_flat_rates`
--

LOCK TABLES `nph_sku_fulfillment_flat_rates` WRITE;
/*!40000 ALTER TABLE `nph_sku_fulfillment_flat_rates` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_fulfillment_flat_rates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_media_map`
--

DROP TABLE IF EXISTS `nph_sku_media_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_media_map` (
  `nph_sku_sku_id` bigint(20) NOT NULL,
  `media_id` bigint(20) NOT NULL,
  `map_key` varchar(255) NOT NULL,
  PRIMARY KEY (`nph_sku_sku_id`,`map_key`),
  KEY `fkeb4aecf96e4720e0` (`media_id`),
  KEY `fkeb4aecf9d93d857f` (`nph_sku_sku_id`),
  CONSTRAINT `fkeb4aecf9d93d857f` FOREIGN KEY (`nph_sku_sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_media_map`
--

LOCK TABLES `nph_sku_media_map` WRITE;
/*!40000 ALTER TABLE `nph_sku_media_map` DISABLE KEYS */;
INSERT INTO `nph_sku_media_map` VALUES (1,100650,'primary');
/*!40000 ALTER TABLE `nph_sku_media_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_sku_option_value_xref`
--

DROP TABLE IF EXISTS `nph_sku_option_value_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_sku_option_value_xref` (
  `sku_id` bigint(20) NOT NULL,
  `product_option_value_id` bigint(20) NOT NULL,
  KEY `fk7b61dc0bb0c16a73` (`product_option_value_id`),
  KEY `fk7b61dc0bb78c9977` (`sku_id`),
  CONSTRAINT `fk7b61dc0bb0c16a73` FOREIGN KEY (`product_option_value_id`) REFERENCES `nph_product_option_value` (`product_option_value_id`),
  CONSTRAINT `fk7b61dc0bb78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_sku_option_value_xref`
--

LOCK TABLES `nph_sku_option_value_xref` WRITE;
/*!40000 ALTER TABLE `nph_sku_option_value_xref` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_sku_option_value_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_store`
--

DROP TABLE IF EXISTS `nph_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_store` (
  `store_id` bigint(20) NOT NULL,
  `address_1` varchar(255) DEFAULT NULL,
  `address_2` varchar(255) DEFAULT NULL,
  `store_city` varchar(255) DEFAULT NULL,
  `store_country` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `store_name` varchar(255) NOT NULL,
  `store_phone` varchar(255) DEFAULT NULL,
  `store_state` varchar(255) DEFAULT NULL,
  `store_zip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_store`
--

LOCK TABLES `nph_store` WRITE;
/*!40000 ALTER TABLE `nph_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_system_property`
--

DROP TABLE IF EXISTS `nph_system_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_system_property` (
  `id` bigint(20) NOT NULL,
  `friendly_group` varchar(255) DEFAULT NULL,
  `friendly_name` varchar(255) DEFAULT NULL,
  `friendly_tab` varchar(255) DEFAULT NULL,
  `property_name` varchar(255) NOT NULL,
  `property_type` varchar(255) DEFAULT NULL,
  `property_value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_system_property`
--

LOCK TABLES `nph_system_property` WRITE;
/*!40000 ALTER TABLE `nph_system_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_system_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_tar_crit_offer_xref`
--

DROP TABLE IF EXISTS `nph_tar_crit_offer_xref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_tar_crit_offer_xref` (
  `offer_id` bigint(20) NOT NULL,
  `offer_item_criteria_id` bigint(20) NOT NULL,
  PRIMARY KEY (`offer_id`,`offer_item_criteria_id`),
  UNIQUE KEY `uk_125f5803e7ab0252` (`offer_item_criteria_id`),
  KEY `fk125f58033615a91a` (`offer_item_criteria_id`),
  KEY `fk125f5803d5f3faf4` (`offer_id`),
  CONSTRAINT `fk125f58033615a91a` FOREIGN KEY (`offer_item_criteria_id`) REFERENCES `nph_offer_item_criteria` (`offer_item_criteria_id`),
  CONSTRAINT `fk125f5803d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_tar_crit_offer_xref`
--

LOCK TABLES `nph_tar_crit_offer_xref` WRITE;
/*!40000 ALTER TABLE `nph_tar_crit_offer_xref` DISABLE KEYS */;
INSERT INTO `nph_tar_crit_offer_xref` VALUES (1000,1050);
/*!40000 ALTER TABLE `nph_tar_crit_offer_xref` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_trans_additnl_fields`
--

DROP TABLE IF EXISTS `nph_trans_additnl_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_trans_additnl_fields` (
  `payment_transaction_id` bigint(20) NOT NULL,
  `field_value` longtext,
  `field_name` varchar(255) NOT NULL,
  PRIMARY KEY (`payment_transaction_id`,`field_name`),
  KEY `fk376dde4b9e955b1d` (`payment_transaction_id`),
  CONSTRAINT `fk376dde4b9e955b1d` FOREIGN KEY (`payment_transaction_id`) REFERENCES `nph_order_payment_transaction` (`payment_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_trans_additnl_fields`
--

LOCK TABLES `nph_trans_additnl_fields` WRITE;
/*!40000 ALTER TABLE `nph_trans_additnl_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_trans_additnl_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_translation`
--

DROP TABLE IF EXISTS `nph_translation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_translation` (
  `translation_id` bigint(20) NOT NULL,
  `entity_id` varchar(255) DEFAULT NULL,
  `entity_type` varchar(255) DEFAULT NULL,
  `field_name` varchar(255) DEFAULT NULL,
  `locale_code` varchar(255) DEFAULT NULL,
  `translated_value` longtext,
  PRIMARY KEY (`translation_id`),
  KEY `translation_index` (`entity_type`,`entity_id`,`field_name`,`locale_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_translation`
--

LOCK TABLES `nph_translation` WRITE;
/*!40000 ALTER TABLE `nph_translation` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_translation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nph_wechat_customer`
--

DROP TABLE IF EXISTS `nph_wechat_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nph_wechat_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(45) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `is_active` tinyint(1) DEFAULT '0',
  `is_properties_loaded` tinyint(1) DEFAULT '0',
  `is_from_rrm` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nph_wechat_customer`
--

LOCK TABLES `nph_wechat_customer` WRITE;
/*!40000 ALTER TABLE `nph_wechat_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `nph_wechat_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_compare_price`
--

DROP TABLE IF EXISTS `ph_compare_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_compare_price` (
  `COMPARE_PRICE_ID` bigint(20) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL COMMENT '商品名称',
  `PRICE` decimal(19,2) DEFAULT NULL COMMENT '商品价格',
  `SOURCE` varchar(100) DEFAULT NULL COMMENT '商品信息来源名称',
  `SOURCE_URL` varchar(100) DEFAULT NULL COMMENT '商品信息来源URL链接',
  `CREATE_TIME` datetime DEFAULT NULL,
  `SKU_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`COMPARE_PRICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_compare_price`
--

LOCK TABLES `ph_compare_price` WRITE;
/*!40000 ALTER TABLE `ph_compare_price` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_compare_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_coupon`
--

DROP TABLE IF EXISTS `ph_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_coupon` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `strategy` varchar(100) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `EVENT` varchar(45) DEFAULT NULL,
  `ISSUE_PRIORITY` int(11) NOT NULL DEFAULT '10',
  `NUMBER` int(11) NOT NULL DEFAULT '1',
  `VALUE` decimal(19,2) DEFAULT NULL,
  `SCOPE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_coupon`
--

LOCK TABLES `ph_coupon` WRITE;
/*!40000 ALTER TABLE `ph_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_coupon_attribute`
--

DROP TABLE IF EXISTS `ph_coupon_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_coupon_attribute` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint(10) NOT NULL,
  `attr` varchar(20) DEFAULT NULL,
  `value` decimal(10,5) DEFAULT NULL,
  `CATEGORY` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`,`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_coupon_attribute`
--

LOCK TABLES `ph_coupon_attribute` WRITE;
/*!40000 ALTER TABLE `ph_coupon_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_coupon_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_coupon_code`
--

DROP TABLE IF EXISTS `ph_coupon_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_coupon_code` (
  `COUPON_CODE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CUSTOMER_ID` bigint(20) DEFAULT NULL,
  `COUPON_ID` bigint(20) DEFAULT NULL,
  `START_DATE` datetime DEFAULT NULL,
  `END_DATE` datetime DEFAULT NULL,
  `COUPON_CODE` varchar(20) DEFAULT NULL,
  `COUPON_AMOUNT` decimal(9,3) DEFAULT NULL,
  `COUPON_AMOUNT_TWO` decimal(9,3) DEFAULT NULL,
  `STATUS` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`COUPON_CODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_coupon_code`
--

LOCK TABLES `ph_coupon_code` WRITE;
/*!40000 ALTER TABLE `ph_coupon_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_coupon_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_customer_coupon`
--

DROP TABLE IF EXISTS `ph_customer_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_customer_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(20) DEFAULT NULL,
  `coupon_id` bigint(10) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `NUMBER` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_customer_coupon`
--

LOCK TABLES `ph_customer_coupon` WRITE;
/*!40000 ALTER TABLE `ph_customer_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_customer_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_customer_message`
--

DROP TABLE IF EXISTS `ph_customer_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_customer_message` (
  `CUSTOMER_MESSAGE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MESSAGE_TEXT` longtext,
  `MESSAGE_TITLE` varchar(50) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CUSTOMER_ID` bigint(20) DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `URL` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CUSTOMER_MESSAGE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_customer_message`
--

LOCK TABLES `ph_customer_message` WRITE;
/*!40000 ALTER TABLE `ph_customer_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_customer_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_express`
--

DROP TABLE IF EXISTS `ph_express`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_express` (
  `express_no` varchar(60) NOT NULL,
  `order_no` varchar(60) NOT NULL,
  `receiver` varchar(60) DEFAULT NULL,
  `receiver_phone` varchar(60) DEFAULT NULL,
  `receiver_address` varchar(255) DEFAULT NULL,
  `sender` varchar(60) DEFAULT NULL,
  `sender_phone` varchar(60) DEFAULT NULL,
  `sender_address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  PRIMARY KEY (`express_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_express`
--

LOCK TABLES `ph_express` WRITE;
/*!40000 ALTER TABLE `ph_express` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_express` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_group_on`
--

DROP TABLE IF EXISTS `ph_group_on`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_group_on` (
  `GROUP_ON_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SKU_ID` bigint(20) DEFAULT NULL,
  `SKU_QUANTITY` int(11) DEFAULT NULL,
  `PARTICIPATE_IN_COUNT` int(11) DEFAULT '0' COMMENT '参与团购人数',
  `RULE_DESC` longtext COMMENT '团购规则描述',
  `START_DATE` datetime DEFAULT NULL COMMENT '团购开始时间',
  `END_DATE` datetime DEFAULT NULL COMMENT '团购截止时间',
  `GROUP_PRICE` decimal(19,5) NOT NULL,
  `GROUP_UNIT` varchar(20) NOT NULL,
  `MIN_COUNT` int(11) DEFAULT '1',
  PRIMARY KEY (`GROUP_ON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_group_on`
--

LOCK TABLES `ph_group_on` WRITE;
/*!40000 ALTER TABLE `ph_group_on` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_group_on` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_group_on_attr`
--

DROP TABLE IF EXISTS `ph_group_on_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_group_on_attr` (
  `GROUP_ON_ATTR_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `GROUP_ON_ID` bigint(20) DEFAULT NULL,
  `COUNT_RANGE_FROM` int(11) DEFAULT NULL,
  `COUNT_RANGE_TO` int(11) DEFAULT NULL,
  `PRICE` decimal(19,5) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ON_ATTR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_group_on_attr`
--

LOCK TABLES `ph_group_on_attr` WRITE;
/*!40000 ALTER TABLE `ph_group_on_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_group_on_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_pickup_address`
--

DROP TABLE IF EXISTS `ph_pickup_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_pickup_address` (
  `pickup_address_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `provider_id` bigint(20) NOT NULL,
  `sender` varchar(20) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(100) NOT NULL,
  `remark` varchar(100) NOT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  `date_created` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`pickup_address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_pickup_address`
--

LOCK TABLES `ph_pickup_address` WRITE;
/*!40000 ALTER TABLE `ph_pickup_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_pickup_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_refund`
--

DROP TABLE IF EXISTS `ph_refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_refund` (
  `refund_id` bigint(10) NOT NULL,
  `refund_desc` varchar(200) DEFAULT NULL COMMENT '退货描述',
  `refund_status` varchar(10) DEFAULT NULL COMMENT '退货状态 PENDING-待审核 APPROVE-审核通过 REFUSED-拒绝通过',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_by` varchar(45) DEFAULT NULL COMMENT '退款经办人',
  `ORDER_ID` bigint(20) DEFAULT NULL,
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '申请退款订单项',
  `refund_num` int(11) DEFAULT NULL COMMENT '申请退款数量',
  PRIMARY KEY (`refund_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_refund`
--

LOCK TABLES `ph_refund` WRITE;
/*!40000 ALTER TABLE `ph_refund` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ph_refund_media`
--

DROP TABLE IF EXISTS `ph_refund_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ph_refund_media` (
  `refund_media_id` bigint(10) NOT NULL,
  `refund_id` bigint(10) DEFAULT NULL,
  `media_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`refund_media_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ph_refund_media`
--

LOCK TABLES `ph_refund_media` WRITE;
/*!40000 ALTER TABLE `ph_refund_media` DISABLE KEYS */;
/*!40000 ALTER TABLE `ph_refund_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence_generator`
--

DROP TABLE IF EXISTS `sequence_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_generator` (
  `id_name` varchar(255) NOT NULL,
  `id_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence_generator`
--

LOCK TABLES `sequence_generator` WRITE;
/*!40000 ALTER TABLE `sequence_generator` DISABLE KEYS */;
INSERT INTO `sequence_generator` VALUES ('AddressImpl',1),('CategoryExcludedSearchFacetImpl',1000),('CategoryImpl',10250),('CategoryProductImpl',1000),('CategoryProductXrefImpl',1750),('CategorySearchFacetImpl',1000),('CategoryXrefImpl',1250),('ChallengeQuestionImpl',1000),('CrossSaleProductImpl',101),('CustomerAddressImpl',101),('CustomerRoleImpl',5301),('EmailTrackingImpl',4351),('FeaturedProductImpl',1000),('FieldDefinitionImpl',1000),('FieldEnumerationImpl',1000),('FieldEnumerationItemImpl',1000),('FieldGroupImpl',1000),('FieldImpl',1000),('FulfillmentGroupImpl',7701),('FulfillmentGroupItemImpl',8101),('FulfillmentOptionImpl',1000),('MediaImpl',100750),('OfferAuditImpl',451),('OfferCodeImpl',1050),('OfferImpl',1100),('OfferItemCriteriaImpl',1150),('OfferRuleImpl',101),('OrderAdjustmentImpl',701),('OrderImpl',1),('OrderItemAttributeImpl',101),('OrderItemImpl',8451),('OrderItemPriceDetailAdjustmentImpl',901),('OrderItemPriceDetailImpl',8201),('OrderPaymentImpl',3401),('PageFieldImpl',1050),('PageImpl',1000),('PageTemplateImpl',1000),('PaymentTransactionImpl',3201),('PersonalMessageImpl',201),('PhoneImpl',151),('ProductAttributeImpl',1050),('ProductImpl',10350),('ProductOptionImpl',1100),('ProductOptionValueImpl',1150),('ProductOptionXrefImpl',1550),('ProviderImpl',151),('RatingDetailImpl',201),('RatingSummaryImpl',201),('ReviewDetailImpl',201),('RoleImpl',1000),('SandBoxImpl',151),('SandBoxManagementImpl',151),('SearchFacetImpl',1000),('SearchFacetRangeImpl',1000),('SearchInterceptImpl',1000),('SkuBundleItemImpl',101),('SkuImpl',101),('StaticAssetImpl',100400),('StructuredContentFieldImpl',1150),('StructuredContentImpl',1150),('StructuredContentRuleImpl',1000),('StructuredContentTypeImpl',1000),('URLHandlerImpl',1000);
/*!40000 ALTER TABLE `sequence_generator` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-29 22:32:31
