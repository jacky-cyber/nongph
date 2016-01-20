/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET character_set_client = utf8 */;

CREATE DATABASE IF NOT EXISTS globalph
  DEFAULT CHARSET utf8
  COLLATE utf8_general_ci;
USE globalph;

DROP TABLE IF EXISTS `nph_order_address`;
CREATE TABLE `nph_order_address` (
  `id`           BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `order_id`     BIGINT(20)   NOT NULL,
  `address_id`   BIGINT(20)            DEFAULT NULL,
  `customer_id`  BIGINT(20)            DEFAULT NULL,
  `receiver`     VARCHAR(20)  NOT NULL,
  `phone`        VARCHAR(20)  NOT NULL,
  `province`     VARCHAR(10)  NOT NULL,
  `city`         VARCHAR(10)  NOT NULL,
  `district`     VARCHAR(10)  NOT NULL,
  `address`      VARCHAR(100) NOT NULL,
  `ex_name`      VARCHAR(45)           DEFAULT NULL,
  `ex_no`        VARCHAR(45)           DEFAULT NULL,
  `date_created` DATETIME              DEFAULT NULL,
  `created_by`   BIGINT(20)            DEFAULT NULL,
  `date_updated` DATETIME              DEFAULT NULL,
  `updated_by`   BIGINT(20)            DEFAULT NULL,
  `postal_code`  VARCHAR(10)           DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id_unique` (`order_id`)
);

DROP TABLE IF EXISTS `nph_provider`;
CREATE TABLE `nph_provider` (
  `provider_id`   BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `provider_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`provider_id`)
);

DROP TABLE IF EXISTS `admin_module`;
CREATE TABLE `admin_module` (
  `admin_module_id` BIGINT(20)   NOT NULL,
  `display_order`   INT(11)      DEFAULT NULL,
  `icon`            VARCHAR(255) DEFAULT NULL,
  `module_key`      VARCHAR(255) NOT NULL,
  `name`            VARCHAR(255) NOT NULL,
  PRIMARY KEY (`admin_module_id`),
  KEY `adminmodule_name_index` (`name`)
);

DROP TABLE IF EXISTS `admin_password_token`;
CREATE TABLE `admin_password_token` (
  `password_token`  VARCHAR(255) NOT NULL,
  `admin_user_id`   BIGINT(20)   NOT NULL,
  `create_date`     DATETIME     NOT NULL,
  `token_used_date` DATETIME DEFAULT NULL,
  `token_used_flag` TINYINT(1)   NOT NULL,
  PRIMARY KEY (`password_token`)
);

DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `admin_permission_id` BIGINT(20)   NOT NULL,
  `description`         VARCHAR(255) NOT NULL,
  `is_friendly`         TINYINT(1) DEFAULT NULL,
  `name`                VARCHAR(255) NOT NULL,
  `permission_type`     VARCHAR(255) NOT NULL,
  PRIMARY KEY (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_permission_entity`;
CREATE TABLE `admin_permission_entity` (
  `admin_permission_entity_id` BIGINT(20)   NOT NULL,
  `ceiling_entity`             VARCHAR(255) NOT NULL,
  `admin_permission_id`        BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`admin_permission_entity_id`),
  KEY `fk23c09e3de88b7d38` (`admin_permission_id`),
  CONSTRAINT `fk23c09e3de88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_permission_xref`;
CREATE TABLE `admin_permission_xref` (
  `child_permission_id` BIGINT(20) NOT NULL,
  `admin_permission_id` BIGINT(20) NOT NULL,
  KEY `fkbcad1f5e88b7d38` (`admin_permission_id`),
  KEY `fkbcad1f575a3c445` (`child_permission_id`),
  CONSTRAINT `fkbcad1f575a3c445` FOREIGN KEY (`child_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`),
  CONSTRAINT `fkbcad1f5e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `admin_role_id` BIGINT(20)   NOT NULL,
  `description`   VARCHAR(255) NOT NULL,
  `name`          VARCHAR(255) NOT NULL,
  PRIMARY KEY (`admin_role_id`)
);

DROP TABLE IF EXISTS `admin_role_permission_xref`;
CREATE TABLE `admin_role_permission_xref` (
  `admin_role_id`       BIGINT(20) NOT NULL,
  `admin_permission_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`admin_permission_id`, `admin_role_id`),
  KEY `fk4a819d98e88b7d38` (`admin_permission_id`),
  KEY `fk4a819d985f43aad8` (`admin_role_id`),
  CONSTRAINT `fk4a819d985f43aad8` FOREIGN KEY (`admin_role_id`) REFERENCES `admin_role` (`admin_role_id`),
  CONSTRAINT `fk4a819d98e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_sandbox`;
CREATE TABLE `admin_sandbox` (
  `sandbox_id`        BIGINT(20) NOT NULL,
  `author`            BIGINT(20)   DEFAULT NULL,
  `color`             VARCHAR(100) DEFAULT NULL,
  `description`       VARCHAR(100) DEFAULT NULL,
  `go_live_date`      DATETIME     DEFAULT NULL,
  `sandbox_name`      VARCHAR(100) DEFAULT NULL,
  `sandbox_type`      VARCHAR(100) DEFAULT NULL,
  `parent_sandbox_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`sandbox_id`),
  KEY `sandbox_name_index` (`sandbox_name`),
  KEY `fkdd37a9a174160452` (`parent_sandbox_id`)
);

DROP TABLE IF EXISTS `admin_sandbox_mgmt`;
CREATE TABLE `admin_sandbox_mgmt` (
  `sandbox_mgmt_id` BIGINT(20) NOT NULL,
  `sandbox_id`      BIGINT(20) NOT NULL,
  PRIMARY KEY (`sandbox_mgmt_id`),
  UNIQUE KEY `uk_4845009fe52b6993` (`sandbox_id`),
  KEY `fk4845009f579fe59d` (`sandbox_id`),
  CONSTRAINT `fk4845009f579fe59d` FOREIGN KEY (`sandbox_id`) REFERENCES `admin_sandbox` (`sandbox_id`)
);

DROP TABLE IF EXISTS `admin_sec_perm_xref`;
CREATE TABLE `admin_sec_perm_xref` (
  `admin_section_id`    BIGINT(20) NOT NULL,
  `admin_permission_id` BIGINT(20) NOT NULL,
  KEY `fk5e832966e88b7d38` (`admin_permission_id`),
  KEY `fk5e8329663af7f0fc` (`admin_section_id`),
  CONSTRAINT `fk5e8329663af7f0fc` FOREIGN KEY (`admin_section_id`) REFERENCES `admin_section` (`admin_section_id`),
  CONSTRAINT `fk5e832966e88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_section`;
CREATE TABLE `admin_section` (
  `admin_section_id`    BIGINT(20)   NOT NULL,
  `ceiling_entity`      VARCHAR(100) DEFAULT NULL,
  `display_controller`  VARCHAR(100) DEFAULT NULL,
  `display_order`       INT(11)      DEFAULT NULL,
  `name`                VARCHAR(100) NOT NULL,
  `section_key`         VARCHAR(100) NOT NULL,
  `url`                 VARCHAR(100) DEFAULT NULL,
  `use_default_handler` TINYINT(1)   DEFAULT NULL,
  `admin_module_id`     BIGINT(20)   NOT NULL,
  PRIMARY KEY (`admin_section_id`),
  UNIQUE KEY `uc_blc_admin_section_1` (`section_key`),
  KEY `adminsection_module_index` (`admin_module_id`),
  KEY `adminsection_name_index` (`name`),
  CONSTRAINT `fk7ea7d92fb1a18498` FOREIGN KEY (`admin_module_id`) REFERENCES `admin_module` (`admin_module_id`)
);

DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `admin_user_id`      BIGINT(20)  NOT NULL,
  `active_status_flag` TINYINT(1)   DEFAULT NULL,
  `email`              VARCHAR(50) NOT NULL,
  `login`              VARCHAR(50) NOT NULL,
  `name`               VARCHAR(20) NOT NULL,
  `password`           VARCHAR(100) DEFAULT NULL,
  `phone_number`       VARCHAR(20)  DEFAULT NULL,
  PRIMARY KEY (`admin_user_id`),
  KEY `adminperm_email_index` (`email`),
  KEY `adminuser_name_index` (`name`)
);

DROP TABLE IF EXISTS `admin_user_addtl_fields`;
CREATE TABLE `admin_user_addtl_fields` (
  `admin_user_id` BIGINT(20)   NOT NULL,
  `field_value`   VARCHAR(255) DEFAULT NULL,
  `field_name`    VARCHAR(100) NOT NULL,
  PRIMARY KEY (`admin_user_id`, `field_name`),
  KEY `fk73274cdd46ebc38` (`admin_user_id`),
  CONSTRAINT `fk73274cdd46ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`)
);

DROP TABLE IF EXISTS `admin_user_permission_xref`;
CREATE TABLE `admin_user_permission_xref` (
  `admin_user_id`       BIGINT(20) NOT NULL,
  `admin_permission_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`admin_permission_id`, `admin_user_id`),
  KEY `fkf0b3beede88b7d38` (`admin_permission_id`),
  KEY `fkf0b3beed46ebc38` (`admin_user_id`),
  CONSTRAINT `fkf0b3beed46ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkf0b3beede88b7d38` FOREIGN KEY (`admin_permission_id`) REFERENCES `admin_permission` (`admin_permission_id`)
);

DROP TABLE IF EXISTS `admin_user_role_xref`;
CREATE TABLE `admin_user_role_xref` (
  `admin_user_id` BIGINT(20) NOT NULL,
  `admin_role_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`admin_role_id`, `admin_user_id`),
  KEY `fkffd33a265f43aad8` (`admin_role_id`),
  KEY `fkffd33a2646ebc38` (`admin_user_id`),
  CONSTRAINT `fkffd33a2646ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkffd33a265f43aad8` FOREIGN KEY (`admin_role_id`) REFERENCES `admin_role` (`admin_role_id`)
);

DROP TABLE IF EXISTS `admin_user_sandbox`;
CREATE TABLE `admin_user_sandbox` (
  `sandbox_id`    BIGINT(20) DEFAULT NULL,
  `admin_user_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`admin_user_id`),
  KEY `fkd0a97e09579fe59d` (`sandbox_id`),
  KEY `fkd0a97e0946ebc38` (`admin_user_id`),
  CONSTRAINT `fkd0a97e0946ebc38` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`admin_user_id`),
  CONSTRAINT `fkd0a97e09579fe59d` FOREIGN KEY (`sandbox_id`) REFERENCES `admin_sandbox` (`sandbox_id`)
);

DROP TABLE IF EXISTS `cms_asset_desc_map`;
CREATE TABLE `cms_asset_desc_map` (
  `static_asset_id`      BIGINT(20)   NOT NULL,
  `static_asset_desc_id` BIGINT(20)   NOT NULL,
  `map_key`              VARCHAR(100) NOT NULL,
  PRIMARY KEY (`static_asset_id`, `map_key`),
  KEY `fke886bae3e2ba0c9d` (`static_asset_desc_id`),
  KEY `fke886bae367f70b63` (`static_asset_id`),
  CONSTRAINT `fke886bae3e2ba0c9d` FOREIGN KEY (`static_asset_desc_id`) REFERENCES `cms_static_asset_desc` (`static_asset_desc_id`)
);

DROP TABLE IF EXISTS `cms_fld_define`;
CREATE TABLE `cms_fld_define` (
  `fld_def_id`           BIGINT(20) NOT NULL,
  `allow_multiples`      TINYINT(1)   DEFAULT NULL,
  `column_width`         VARCHAR(100) DEFAULT NULL,
  `fld_order`            INT(11)      DEFAULT NULL,
  `fld_type`             VARCHAR(100) DEFAULT NULL,
  `friendly_name`        VARCHAR(100) DEFAULT NULL,
  `hidden_flag`          TINYINT(1)   DEFAULT NULL,
  `max_length`           INT(11)      DEFAULT NULL,
  `name`                 VARCHAR(100) DEFAULT NULL,
  `required_flag`        TINYINT(1)   DEFAULT NULL,
  `security_level`       VARCHAR(100) DEFAULT NULL,
  `text_area_flag`       TINYINT(1)   DEFAULT NULL,
  `vldtn_error_mssg_key` VARCHAR(255) DEFAULT NULL,
  `vldtn_regex`          VARCHAR(255) DEFAULT NULL,
  `enum_id`              BIGINT(20)   DEFAULT NULL,
  `fld_group_id`         BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`fld_def_id`),
  KEY `fk3fcb575e38d08ab5` (`enum_id`),
  KEY `fk3fcb575e6a79bdb5` (`fld_group_id`)
);

DROP TABLE IF EXISTS `cms_fld_group`;
CREATE TABLE `cms_fld_group` (
  `fld_group_id`        BIGINT(20) NOT NULL,
  `init_collapsed_flag` TINYINT(1)  DEFAULT NULL,
  `name`                VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`fld_group_id`)
);

DROP TABLE IF EXISTS `cms_img_static_asset`;
CREATE TABLE `cms_img_static_asset` (
  `height`          INT(11) DEFAULT NULL,
  `width`           INT(11) DEFAULT NULL,
  `static_asset_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`static_asset_id`),
  KEY `fkcc4b772167f70b63` (`static_asset_id`)
);

DROP TABLE IF EXISTS `cms_page`;
CREATE TABLE `cms_page` (
  `page_id`               BIGINT(20) NOT NULL,
  `created_by`            BIGINT(20)   DEFAULT NULL,
  `date_created`          DATETIME     DEFAULT NULL,
  `date_updated`          DATETIME     DEFAULT NULL,
  `updated_by`            BIGINT(20)   DEFAULT NULL,
  `description`           VARCHAR(100) DEFAULT NULL,
  `exclude_from_site_map` TINYINT(1)   DEFAULT NULL,
  `full_url`              VARCHAR(100) DEFAULT NULL,
  `offline_flag`          TINYINT(1)   DEFAULT NULL,
  `priority`              INT(11)      DEFAULT NULL,
  `page_tmplt_id`         BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`page_id`),
  KEY `page_full_url_index` (`full_url`),
  KEY `fkf41bedd5d49d3961` (`page_tmplt_id`),
  CONSTRAINT `fkf41bedd5d49d3961` FOREIGN KEY (`page_tmplt_id`) REFERENCES `cms_page_tmplt` (`page_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_page_fld`;
CREATE TABLE `cms_page_fld` (
  `page_fld_id`  BIGINT(20) NOT NULL,
  `created_by`   BIGINT(20)   DEFAULT NULL,
  `date_created` DATETIME     DEFAULT NULL,
  `date_updated` DATETIME     DEFAULT NULL,
  `updated_by`   BIGINT(20)   DEFAULT NULL,
  `fld_key`      VARCHAR(50)  DEFAULT NULL,
  `lob_value`    LONGTEXT,
  `value`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`page_fld_id`)
);

DROP TABLE IF EXISTS `cms_page_fld_map`;
CREATE TABLE `cms_page_fld_map` (
  `page_id`     BIGINT(20)  NOT NULL,
  `page_fld_id` BIGINT(20)  NOT NULL,
  `map_key`     VARCHAR(50) NOT NULL,
  PRIMARY KEY (`page_id`, `map_key`),
  KEY `fke9ee09515aedd08a` (`page_fld_id`),
  KEY `fke9ee0951883c2667` (`page_id`)
);

DROP TABLE IF EXISTS `cms_page_item_criteria`;
CREATE TABLE `cms_page_item_criteria` (
  `page_item_criteria_id` BIGINT(20) NOT NULL,
  `order_item_match_rule` LONGTEXT,
  `quantity`              INT(11)    NOT NULL,
  PRIMARY KEY (`page_item_criteria_id`)
);

DROP TABLE IF EXISTS `cms_page_rule`;
CREATE TABLE `cms_page_rule` (
  `page_rule_id` BIGINT(20) NOT NULL,
  `match_rule`   LONGTEXT,
  PRIMARY KEY (`page_rule_id`)
);

DROP TABLE IF EXISTS `cms_page_rule_map`;
CREATE TABLE `cms_page_rule_map` (
  `page_id`      BIGINT(20)   NOT NULL,
  `page_rule_id` BIGINT(20)   NOT NULL,
  `map_key`      VARCHAR(255) NOT NULL,
  PRIMARY KEY (`page_id`, `map_key`),
  KEY `fk1aba0ca336d91846` (`page_rule_id`),
  KEY `fk1aba0ca3c38455dd` (`page_id`)
);

DROP TABLE IF EXISTS `cms_page_tmplt`;
CREATE TABLE `cms_page_tmplt` (
  `page_tmplt_id` BIGINT(20) NOT NULL,
  `tmplt_descr`   VARCHAR(100) DEFAULT NULL,
  `tmplt_name`    VARCHAR(100) DEFAULT NULL,
  `tmplt_path`    VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`page_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_pgtmplt_fldgrp_xref`;
CREATE TABLE `cms_pgtmplt_fldgrp_xref` (
  `page_tmplt_id` BIGINT(20) NOT NULL,
  `fld_group_id`  BIGINT(20) NOT NULL,
  `group_order`   INT(11)    NOT NULL,
  PRIMARY KEY (`page_tmplt_id`, `group_order`),
  KEY `fk99d625f66a79bdb5` (`fld_group_id`),
  KEY `fk99d625f6d49d3961` (`page_tmplt_id`),
  CONSTRAINT `fk99d625f66a79bdb5` FOREIGN KEY (`fld_group_id`) REFERENCES `cms_fld_group` (`fld_group_id`),
  CONSTRAINT `fk99d625f6d49d3961` FOREIGN KEY (`page_tmplt_id`) REFERENCES `cms_page_tmplt` (`page_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_qual_crit_page_xref`;
CREATE TABLE `cms_qual_crit_page_xref` (
  `page_id`               BIGINT(20) NOT NULL DEFAULT '0',
  `page_item_criteria_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`page_id`, `page_item_criteria_id`),
  UNIQUE KEY `uk_874be5902b6bc67f` (`page_item_criteria_id`),
  KEY `fk874be590883c2667` (`page_id`),
  KEY `fk874be590378418cd` (`page_item_criteria_id`)
);

DROP TABLE IF EXISTS `cms_qual_crit_sc_xref`;
CREATE TABLE `cms_qual_crit_sc_xref` (
  `sc_id`               BIGINT(20) NOT NULL,
  `sc_item_criteria_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`sc_id`, `sc_item_criteria_id`),
  UNIQUE KEY `uk_c4a353afff06f4de` (`sc_item_criteria_id`),
  KEY `fkc4a353af13d95585` (`sc_id`),
  KEY `fkc4a353af85c77f2b` (`sc_item_criteria_id`)
);

DROP TABLE IF EXISTS `cms_sc`;
CREATE TABLE `cms_sc` (
  `sc_id`        BIGINT(20)  NOT NULL,
  `created_by`   BIGINT(20) DEFAULT NULL,
  `date_created` DATETIME   DEFAULT NULL,
  `date_updated` DATETIME   DEFAULT NULL,
  `updated_by`   BIGINT(20) DEFAULT NULL,
  `content_name` VARCHAR(50) NOT NULL,
  `offline_flag` TINYINT(1) DEFAULT NULL,
  `priority`     INT(11)     NOT NULL,
  `sc_type_id`   BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`sc_id`),
  KEY `content_name_index` (`content_name`),
  KEY `sc_offln_flg_indx` (`offline_flag`),
  KEY `content_priority_index` (`priority`),
  KEY `fk74eeb71671ebfa46` (`sc_type_id`),
  CONSTRAINT `fk74eeb71671ebfa46` FOREIGN KEY (`sc_type_id`) REFERENCES `cms_sc_type` (`sc_type_id`)
);

DROP TABLE IF EXISTS `cms_sc_fld`;
CREATE TABLE `cms_sc_fld` (
  `sc_fld_id`    BIGINT(20) NOT NULL,
  `created_by`   BIGINT(20)   DEFAULT NULL,
  `date_created` DATETIME     DEFAULT NULL,
  `date_updated` DATETIME     DEFAULT NULL,
  `updated_by`   BIGINT(20)   DEFAULT NULL,
  `fld_key`      VARCHAR(100) DEFAULT NULL,
  `lob_value`    LONGTEXT,
  `value`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`sc_fld_id`)
);

DROP TABLE IF EXISTS `cms_sc_fld_map`;
CREATE TABLE `cms_sc_fld_map` (
  `sc_id`     BIGINT(20)   NOT NULL,
  `sc_fld_id` BIGINT(20)   NOT NULL,
  `map_key`   VARCHAR(100) NOT NULL,
  PRIMARY KEY (`sc_id`, `map_key`),
  KEY `fkd9480192dd6fd28a` (`sc_fld_id`),
  KEY `fkd948019213d95585` (`sc_id`),
  CONSTRAINT `fkd948019213d95585` FOREIGN KEY (`sc_id`) REFERENCES `cms_sc` (`sc_id`),
  CONSTRAINT `fkd9480192dd6fd28a` FOREIGN KEY (`sc_fld_id`) REFERENCES `cms_sc_fld` (`sc_fld_id`)
);

DROP TABLE IF EXISTS `cms_sc_fld_tmplt`;
CREATE TABLE `cms_sc_fld_tmplt` (
  `sc_fld_tmplt_id` BIGINT(20) NOT NULL,
  `name`            VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`sc_fld_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_sc_fldgrp_xref`;
CREATE TABLE `cms_sc_fldgrp_xref` (
  `sc_fld_tmplt_id` BIGINT(20) NOT NULL,
  `fld_group_id`    BIGINT(20) NOT NULL,
  `group_order`     INT(11)    NOT NULL,
  PRIMARY KEY (`sc_fld_tmplt_id`, `group_order`),
  KEY `fk71612aea6a79bdb5` (`fld_group_id`),
  KEY `fk71612aeaf6b0ba84` (`sc_fld_tmplt_id`),
  CONSTRAINT `fk71612aea6a79bdb5` FOREIGN KEY (`fld_group_id`) REFERENCES `cms_fld_group` (`fld_group_id`),
  CONSTRAINT `fk71612aeaf6b0ba84` FOREIGN KEY (`sc_fld_tmplt_id`) REFERENCES `cms_sc_fld_tmplt` (`sc_fld_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_sc_item_criteria`;
CREATE TABLE `cms_sc_item_criteria` (
  `sc_item_criteria_id`   BIGINT(20) NOT NULL,
  `order_item_match_rule` LONGTEXT,
  `quantity`              INT(11)    NOT NULL,
  PRIMARY KEY (`sc_item_criteria_id`)
);

DROP TABLE IF EXISTS `cms_sc_rule`;
CREATE TABLE `cms_sc_rule` (
  `sc_rule_id` BIGINT(20) NOT NULL,
  `match_rule` LONGTEXT,
  PRIMARY KEY (`sc_rule_id`)
);

DROP TABLE IF EXISTS `cms_sc_rule_map`;
CREATE TABLE `cms_sc_rule_map` (
  `cms_sc_sc_id` BIGINT(20)   NOT NULL,
  `sc_rule_id`   BIGINT(20)   NOT NULL,
  `map_key`      VARCHAR(100) NOT NULL,
  PRIMARY KEY (`cms_sc_sc_id`, `map_key`),
  KEY `fk169f1c8256e51a06` (`sc_rule_id`),
  KEY `fk169f1c82156e72fc` (`cms_sc_sc_id`),
  CONSTRAINT `fk169f1c82156e72fc` FOREIGN KEY (`cms_sc_sc_id`) REFERENCES `cms_sc` (`sc_id`),
  CONSTRAINT `fk169f1c8256e51a06` FOREIGN KEY (`sc_rule_id`) REFERENCES `cms_sc_rule` (`sc_rule_id`)
);

DROP TABLE IF EXISTS `cms_sc_type`;
CREATE TABLE `cms_sc_type` (
  `sc_type_id`      BIGINT(20) NOT NULL,
  `description`     VARCHAR(100) DEFAULT NULL,
  `name`            VARCHAR(50)  DEFAULT NULL,
  `sc_fld_tmplt_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`sc_type_id`),
  KEY `sc_type_name_index` (`name`),
  KEY `fke19886c3f6b0ba84` (`sc_fld_tmplt_id`),
  CONSTRAINT `fke19886c3f6b0ba84` FOREIGN KEY (`sc_fld_tmplt_id`) REFERENCES `cms_sc_fld_tmplt` (`sc_fld_tmplt_id`)
);

DROP TABLE IF EXISTS `cms_static_asset`;
CREATE TABLE `cms_static_asset` (
  `static_asset_id` BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `alt_text`        VARCHAR(255)          DEFAULT NULL,
  `created_by`      BIGINT(20)            DEFAULT NULL,
  `date_created`    DATETIME              DEFAULT NULL,
  `date_updated`    DATETIME              DEFAULT NULL,
  `updated_by`      BIGINT(20)            DEFAULT NULL,
  `file_extension`  VARCHAR(255)          DEFAULT NULL,
  `file_size`       BIGINT(20)            DEFAULT NULL,
  `full_url`        VARCHAR(100) NOT NULL,
  `mime_type`       VARCHAR(20)           DEFAULT NULL,
  `name`            VARCHAR(50)  NOT NULL,
  `storage_type`    VARCHAR(20)           DEFAULT NULL,
  `title`           VARCHAR(255)          DEFAULT NULL,
  PRIMARY KEY (`static_asset_id`),
  KEY `asst_full_url_indx` (`full_url`)
);

DROP TABLE IF EXISTS `cms_static_asset_desc`;
CREATE TABLE `cms_static_asset_desc` (
  `static_asset_desc_id` BIGINT(20) NOT NULL,
  `created_by`           BIGINT(20)   DEFAULT NULL,
  `date_created`         DATETIME     DEFAULT NULL,
  `date_updated`         DATETIME     DEFAULT NULL,
  `updated_by`           BIGINT(20)   DEFAULT NULL,
  `description`          VARCHAR(255) DEFAULT NULL,
  `long_description`     VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`static_asset_desc_id`)
);

DROP TABLE IF EXISTS `cms_static_asset_strg`;
CREATE TABLE `cms_static_asset_strg` (
  `static_asset_strg_id` BIGINT(20) NOT NULL,
  `file_data`            LONGBLOB,
  `static_asset_id`      BIGINT(20) NOT NULL,
  PRIMARY KEY (`static_asset_strg_id`),
  KEY `static_asset_id_index` (`static_asset_id`)
);

DROP TABLE IF EXISTS `cms_url_handler`;
CREATE TABLE `cms_url_handler` (
  `url_handler_id`    BIGINT(20)   NOT NULL,
  `incoming_url`      VARCHAR(255) NOT NULL,
  `new_url`           VARCHAR(255) NOT NULL,
  `url_redirect_type` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`url_handler_id`),
  KEY `incoming_url_index` (`incoming_url`)
);

DROP TABLE IF EXISTS `nph_additional_offer_info`;
CREATE TABLE `nph_additional_offer_info` (
  `blc_order_order_id` BIGINT(20) NOT NULL,
  `offer_info_id`      BIGINT(20) NOT NULL,
  `offer_id`           BIGINT(20) NOT NULL,
  PRIMARY KEY (`blc_order_order_id`, `offer_id`),
  KEY `fk3bfdbd63b5d9c34d` (`offer_info_id`),
  KEY `fk3bfdbd63d5f3faf4` (`offer_id`),
  KEY `fk3bfdbd631891ff79` (`blc_order_order_id`)
);

DROP TABLE IF EXISTS `nph_address`;
CREATE TABLE `nph_address` (
  `address_id`                BIGINT(20)   NOT NULL,
  `address`                   VARCHAR(100) NOT NULL,
  `postal_code`               VARCHAR(10)  DEFAULT NULL,
  `receiver`                  VARCHAR(20)  DEFAULT NULL,
  `phone`                     VARCHAR(15)  DEFAULT NULL,
  `is_active`                 TINYINT(1)   DEFAULT NULL,
  `is_business`               TINYINT(1)   DEFAULT NULL,
  `is_default`                TINYINT(1)   DEFAULT NULL,
  `standardized`              TINYINT(1)   DEFAULT NULL,
  `tokenized_address`         VARCHAR(100) DEFAULT NULL,
  `verification_level`        VARCHAR(100) DEFAULT NULL,
  `customer_id`               BIGINT(20)   DEFAULT NULL,
  `first_level_community_id`  BIGINT(20)   DEFAULT NULL,
  `second_level_community_id` BIGINT(20)   DEFAULT NULL,
  `net_node_id`               BIGINT(20)   DEFAULT NULL,
  `province`                  VARCHAR(10)  DEFAULT NULL,
  `city`                      VARCHAR(10)  DEFAULT NULL,
  `district`                  VARCHAR(10)  DEFAULT NULL,
  `is_from_wechat`            TINYINT(1)   DEFAULT '0',
  PRIMARY KEY (`address_id`)
);

DROP TABLE IF EXISTS `nph_area`;
CREATE TABLE `nph_area` (
  `id`         INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `area_no`    INT(11) UNSIGNED          DEFAULT NULL,
  `area_name`  VARCHAR(255)              DEFAULT NULL,
  `parent_no`  INT(255)                  DEFAULT NULL,
  `area_code`  VARCHAR(255)              DEFAULT NULL,
  `area_level` INT(255)                  DEFAULT NULL,
  `type_name`  VARCHAR(255)              DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `nph_bank_account_payment`;
CREATE TABLE `nph_bank_account_payment` (
  `payment_id`       BIGINT(20)  NOT NULL,
  `account_number`   VARCHAR(30) NOT NULL,
  `reference_number` VARCHAR(50) NOT NULL,
  `routing_number`   VARCHAR(50) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `bankaccount_index` (`reference_number`)
);

DROP TABLE IF EXISTS `nph_bundle_sku`;
CREATE TABLE `nph_bundle_sku` (
  `sku_id`            BIGINT(20) NOT NULL,
  `auto_bundle`       TINYINT(1) DEFAULT NULL,
  `bundle_promotable` TINYINT(1) DEFAULT NULL,
  `items_promotable`  TINYINT(1) DEFAULT NULL,
  `bundle_priority`   INT(11)    DEFAULT NULL,
  PRIMARY KEY (`sku_id`),
  CONSTRAINT `fk_nph_sku_bundle_1` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS `nph_bundle_sku_item`;
CREATE TABLE `nph_bundle_sku_item` (
  `sku_bundle_item_id` BIGINT(20) NOT NULL,
  `item_sale_price`    DECIMAL(19, 5) DEFAULT NULL,
  `quantity`           INT(11)    NOT NULL,
  `bundle_sku_id`      BIGINT(20) NOT NULL,
  `sku_id`             BIGINT(20) NOT NULL,
  PRIMARY KEY (`sku_bundle_item_id`),
  KEY `fkd55968b78c9977` (`sku_id`),
  CONSTRAINT `fkd55968b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_candidate_fg_offer`;
CREATE TABLE `nph_candidate_fg_offer` (
  `candidate_fg_offer_id` BIGINT(20) NOT NULL,
  `discounted_price`      DECIMAL(19, 5) DEFAULT NULL,
  `fulfillment_group_id`  BIGINT(20)     DEFAULT NULL,
  `offer_id`              BIGINT(20) NOT NULL,
  PRIMARY KEY (`candidate_fg_offer_id`),
  KEY `candidate_fg_index` (`fulfillment_group_id`),
  KEY `candidate_fgoffer_index` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_candidate_item_offer`;
CREATE TABLE `nph_candidate_item_offer` (
  `candidate_item_offer_id` BIGINT(20) NOT NULL,
  `discounted_price`        DECIMAL(19, 5) DEFAULT NULL,
  `offer_id`                BIGINT(20) NOT NULL,
  `order_item_id`           BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`candidate_item_offer_id`),
  KEY `candidate_itemoffer_index` (`offer_id`),
  KEY `candidate_item_index` (`order_item_id`)
);

DROP TABLE IF EXISTS `nph_candidate_order_offer`;
CREATE TABLE `nph_candidate_order_offer` (
  `candidate_order_offer_id` BIGINT(20) NOT NULL,
  `discounted_price`         DECIMAL(19, 5) DEFAULT NULL,
  `offer_id`                 BIGINT(20) NOT NULL,
  `order_id`                 BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`candidate_order_offer_id`),
  KEY `candidate_orderoffer_index` (`offer_id`),
  KEY `candidate_order_index` (`order_id`)
);

DROP TABLE IF EXISTS `nph_cat_search_facet_value`;
CREATE TABLE `nph_cat_search_facet_value` (
  `cat_search_facet_value_id` BIGINT(20) NOT NULL,
  `value`                     VARCHAR(45) DEFAULT NULL,
  `cat_search_facet_xref_id`  BIGINT(20) NOT NULL,
  `search_field_id`           BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`cat_search_facet_value_id`, `cat_search_facet_xref_id`)
);

DROP TABLE IF EXISTS `nph_cat_search_facet_xref`;
CREATE TABLE `nph_cat_search_facet_xref` (
  `category_search_facet_id` BIGINT(20) NOT NULL,
  `sequence`                 DECIMAL(19, 2) DEFAULT NULL,
  `category_id`              BIGINT(20)     DEFAULT NULL,
  `search_facet_id`          BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`category_search_facet_id`),
  KEY `fk32210eeb15d1a13d` (`category_id`),
  KEY `fk32210eebb96b1c93` (`search_facet_id`),
  CONSTRAINT `fk32210eeb15d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk32210eebb96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
);

DROP TABLE IF EXISTS `nph_cat_site_map_gen_cfg`;
CREATE TABLE `nph_cat_site_map_gen_cfg` (
  `ending_depth`     INT(11)    NOT NULL,
  `starting_depth`   INT(11)    NOT NULL,
  `gen_config_id`    BIGINT(20) NOT NULL,
  `root_category_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk1ba4e695c5f3d60` (`root_category_id`),
  KEY `fk1ba4e69bcab9f56` (`gen_config_id`),
  CONSTRAINT `fk1ba4e695c5f3d60` FOREIGN KEY (`root_category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk1ba4e69bcab9f56` FOREIGN KEY (`gen_config_id`) REFERENCES `blc_site_map_gen_cfg` (`gen_config_id`)
);

DROP TABLE IF EXISTS `nph_catalog`;
CREATE TABLE `nph_catalog` (
  `catalog_id` BIGINT(20) NOT NULL,
  `name`       VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`catalog_id`)
);

DROP TABLE IF EXISTS `nph_category`;
CREATE TABLE `nph_category` (
  `category_id`                BIGINT(20)  NOT NULL,
  `active_end_date`            DATETIME     DEFAULT NULL,
  `active_start_date`          DATETIME     DEFAULT NULL,
  `archived`                   CHAR(1)      DEFAULT NULL,
  `description`                VARCHAR(255) DEFAULT NULL,
  `display_template`           VARCHAR(255) DEFAULT NULL,
  `fulfillment_type`           VARCHAR(255) DEFAULT NULL,
  `inventory_type`             VARCHAR(255) DEFAULT NULL,
  `long_description`           LONGTEXT,
  `name`                       VARCHAR(20) NOT NULL,
  `url`                        VARCHAR(255) DEFAULT NULL,
  `url_key`                    VARCHAR(255) DEFAULT NULL,
  `default_parent_category_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `category_parent_index` (`default_parent_category_id`),
  KEY `category_name_index` (`name`),
  KEY `category_url_index` (`url`),
  KEY `category_urlkey_index` (`url_key`)
);

DROP TABLE IF EXISTS `nph_category_attribute`;
CREATE TABLE `nph_category_attribute` (
  `category_attribute_id` BIGINT(20)  NOT NULL,
  `name`                  VARCHAR(50) NOT NULL,
  `searchable`            TINYINT(1)   DEFAULT NULL,
  `value`                 VARCHAR(100) DEFAULT NULL,
  `category_id`           BIGINT(20)  NOT NULL,
  PRIMARY KEY (`category_attribute_id`),
  KEY `categoryattribute_index` (`category_id`),
  KEY `categoryattribute_name_index` (`name`),
  CONSTRAINT `fk4e441d4115d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`)
);

DROP TABLE IF EXISTS `nph_category_media_map`;
CREATE TABLE `nph_category_media_map` (
  `category_id` BIGINT(20)  NOT NULL,
  `media_id`    BIGINT(20)  NOT NULL,
  `map_key`     VARCHAR(50) NOT NULL,
  PRIMARY KEY (`category_id`, `map_key`),
  KEY `fkcd24b1066e4720e0` (`media_id`),
  KEY `fkcd24b106d786cea2` (`category_id`)
);

DROP TABLE IF EXISTS `nph_category_product_xref`;
CREATE TABLE `nph_category_product_xref` (
  `category_product_id` BIGINT(20) NOT NULL,
  `display_order`       DECIMAL(10, 6) DEFAULT NULL,
  `category_id`         BIGINT(20) NOT NULL,
  `product_id`          BIGINT(20) NOT NULL,
  PRIMARY KEY (`category_product_id`),
  KEY `fk635eb1a615d1a13d` (`category_id`),
  KEY `fk635eb1a65f11a0b7` (`product_id`)
);

DROP TABLE IF EXISTS `nph_category_xref`;
CREATE TABLE `nph_category_xref` (
  `category_xref_id` BIGINT(20) NOT NULL,
  `display_order`    DECIMAL(10, 6) DEFAULT NULL,
  `category_id`      BIGINT(20) NOT NULL,
  `sub_category_id`  BIGINT(20) NOT NULL,
  PRIMARY KEY (`category_xref_id`),
  KEY `fke889733615d1a13d` (`category_id`),
  KEY `fke8897336d6d45dbe` (`sub_category_id`),
  CONSTRAINT `fke889733615d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fke8897336d6d45dbe` FOREIGN KEY (`sub_category_id`) REFERENCES `nph_category` (`category_id`)
);

DROP TABLE IF EXISTS `nph_challenge_question`;
CREATE TABLE `nph_challenge_question` (
  `question_id` BIGINT(20)   NOT NULL,
  `question`    VARCHAR(255) NOT NULL,
  PRIMARY KEY (`question_id`)
);

DROP TABLE IF EXISTS `nph_community`;
CREATE TABLE `nph_community` (
  `community_id`        BIGINT(20) NOT NULL,
  `community_name`      VARCHAR(45) DEFAULT NULL,
  `ishead`              TINYINT(1)  DEFAULT NULL,
  `parent_community_id` BIGINT(20)  DEFAULT NULL,
  PRIMARY KEY (`community_id`)
);

DROP TABLE IF EXISTS `nph_credit_card_payment`;
CREATE TABLE `nph_credit_card_payment` (
  `payment_id`       BIGINT(20)  NOT NULL,
  `expiration_month` INT(11)     NOT NULL,
  `expiration_year`  INT(11)     NOT NULL,
  `name_on_card`     VARCHAR(30) NOT NULL,
  `pan`              VARCHAR(30) NOT NULL,
  `reference_number` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `creditcard_index` (`reference_number`)
);

DROP TABLE IF EXISTS `nph_cust_site_map_gen_cfg`;
CREATE TABLE `nph_cust_site_map_gen_cfg` (
  `gen_config_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk67c0dba0bcab9f56` (`gen_config_id`),
  CONSTRAINT `fk67c0dba0bcab9f56` FOREIGN KEY (`gen_config_id`) REFERENCES `blc_site_map_gen_cfg` (`gen_config_id`)
);

DROP TABLE IF EXISTS `nph_customer`;
CREATE TABLE `nph_customer` (
  `customer_id`              BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name`                     VARCHAR(20)         DEFAULT NULL,
  `login_name`               VARCHAR(32)         DEFAULT NULL,
  `password`                 VARCHAR(50)         DEFAULT NULL,
  `email_address`            VARCHAR(50)         DEFAULT NULL,
  `phone`                    VARCHAR(15)         DEFAULT NULL,
  `date_created`             DATETIME            DEFAULT NULL,
  `date_updated`             DATETIME            DEFAULT NULL,
  `deactivated`              TINYINT(1)          DEFAULT NULL,
  `is_preview`               TINYINT(1)          DEFAULT NULL,
  `receive_email`            TINYINT(1)          DEFAULT NULL,
  `is_registered`            TINYINT(1)          DEFAULT NULL,
  `password_change_required` TINYINT(1)          DEFAULT NULL,
  `challenge_answer`         VARCHAR(100)        DEFAULT NULL,
  `challenge_question_id`    BIGINT(20)          DEFAULT NULL,
  `bonus_point`              INT(11)             DEFAULT '0',
  `used_bonus_point`         INT(11)             DEFAULT NULL,
  `validation_code`          VARCHAR(10)         DEFAULT NULL,
  `email_token`              VARCHAR(45)         DEFAULT NULL,
  `validation_status`        INT(11)             DEFAULT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `customer_challenge_index` (`challenge_question_id`),
  KEY `customer_email_index` (`email_address`),
  KEY `fk7716f024a1e1c128` (`phone`),
  CONSTRAINT `fk7716f0241422b204` FOREIGN KEY (`challenge_question_id`) REFERENCES `nph_challenge_question` (`question_id`)
);

DROP TABLE IF EXISTS `nph_customer_password_token`;
CREATE TABLE `nph_customer_password_token` (
  `password_token`  VARCHAR(100) NOT NULL,
  `create_date`     DATETIME     NOT NULL,
  `customer_id`     BIGINT(20)   NOT NULL,
  `token_used_date` DATETIME DEFAULT NULL,
  `token_used_flag` TINYINT(1)   NOT NULL,
  PRIMARY KEY (`password_token`)
);

DROP TABLE IF EXISTS `nph_customer_payment`;
CREATE TABLE `nph_customer_payment` (
  `customer_payment_id` BIGINT(20) NOT NULL,
  `is_default`          TINYINT(1)   DEFAULT NULL,
  `payment_token`       VARCHAR(100) DEFAULT NULL,
  `customer_id`         BIGINT(20) NOT NULL,
  PRIMARY KEY (`customer_payment_id`),
  UNIQUE KEY `cstmr_pay_unique_cnstrnt` (`customer_id`, `payment_token`),
  KEY `fk8b3df0cb7470f437` (`customer_id`),
  CONSTRAINT `fk8b3df0cb7470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`)
);

DROP TABLE IF EXISTS `nph_customer_payment_fields`;
CREATE TABLE `nph_customer_payment_fields` (
  `customer_payment_id` BIGINT(20)  NOT NULL,
  `field_value`         VARCHAR(100) DEFAULT NULL,
  `field_name`          VARCHAR(30) NOT NULL,
  PRIMARY KEY (`customer_payment_id`, `field_name`),
  KEY `fk5ccb14adca0b98e0` (`customer_payment_id`),
  CONSTRAINT `fk5ccb14adca0b98e0` FOREIGN KEY (`customer_payment_id`) REFERENCES `nph_customer_payment` (`customer_payment_id`)
);

DROP TABLE IF EXISTS `nph_customer_role`;
CREATE TABLE `nph_customer_role` (
  `customer_role_id` BIGINT(20) NOT NULL,
  `customer_id`      BIGINT(20) NOT NULL,
  `role_id`          BIGINT(20) NOT NULL,
  PRIMARY KEY (`customer_role_id`),
  KEY `custrole_customer_index` (`customer_id`),
  KEY `custrole_role_index` (`role_id`),
  CONSTRAINT `fk548eb7b17470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk548eb7b1b8587b7` FOREIGN KEY (`role_id`) REFERENCES `nph_role` (`role_id`)
);

DROP TABLE IF EXISTS `nph_data_drvn_enum`;
CREATE TABLE `nph_data_drvn_enum` (
  `enum_id`    BIGINT(20) NOT NULL,
  `enum_key`   VARCHAR(50) DEFAULT NULL,
  `modifiable` TINYINT(1)  DEFAULT NULL,
  PRIMARY KEY (`enum_id`),
  KEY `enum_key_index` (`enum_key`)
);

DROP TABLE IF EXISTS `nph_data_drvn_enum_val`;
CREATE TABLE `nph_data_drvn_enum_val` (
  `enum_val_id` BIGINT(20) NOT NULL,
  `display`     VARCHAR(255) DEFAULT NULL,
  `hidden`      TINYINT(1)   DEFAULT NULL,
  `enum_key`    VARCHAR(255) DEFAULT NULL,
  `enum_type`   BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`enum_val_id`),
  KEY `hidden_index` (`hidden`),
  KEY `enum_val_key_index` (`enum_key`),
  KEY `fkb2d5700da60e0554` (`enum_type`),
  CONSTRAINT `fkb2d5700da60e0554` FOREIGN KEY (`enum_type`) REFERENCES `nph_data_drvn_enum` (`enum_id`)
);

DROP TABLE IF EXISTS `nph_email_tracking`;
CREATE TABLE `nph_email_tracking` (
  `email_tracking_id` BIGINT(20) NOT NULL,
  `date_sent`         DATETIME     DEFAULT NULL,
  `email_address`     VARCHAR(255) DEFAULT NULL,
  `type`              VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`email_tracking_id`),
  KEY `emailtracking_index` (`email_address`)
);

DROP TABLE IF EXISTS `nph_email_tracking_clicks`;
CREATE TABLE `nph_email_tracking_clicks` (
  `click_id`          BIGINT(20) NOT NULL,
  `customer_id`       VARCHAR(255) DEFAULT NULL,
  `date_clicked`      DATETIME   NOT NULL,
  `destination_uri`   VARCHAR(255) DEFAULT NULL,
  `query_string`      VARCHAR(255) DEFAULT NULL,
  `email_tracking_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`click_id`),
  KEY `trackingclicks_customer_index` (`customer_id`),
  KEY `trackingclicks_tracking_index` (`email_tracking_id`),
  CONSTRAINT `fkfdf9f52afa1e5d61` FOREIGN KEY (`email_tracking_id`) REFERENCES `nph_email_tracking` (`email_tracking_id`)
);

DROP TABLE IF EXISTS `nph_email_tracking_opens`;
CREATE TABLE `nph_email_tracking_opens` (
  `open_id`           BIGINT(20) NOT NULL,
  `date_opened`       DATETIME     DEFAULT NULL,
  `user_agent`        VARCHAR(255) DEFAULT NULL,
  `email_tracking_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`open_id`),
  KEY `trackingopen_tracking` (`email_tracking_id`),
  CONSTRAINT `fka5c3722afa1e5d61` FOREIGN KEY (`email_tracking_id`) REFERENCES `nph_email_tracking` (`email_tracking_id`)
);

DROP TABLE IF EXISTS `nph_fg_adjustment`;
CREATE TABLE `nph_fg_adjustment` (
  `fg_adjustment_id`     BIGINT(20)     NOT NULL,
  `adjustment_reason`    VARCHAR(255)   NOT NULL,
  `adjustment_value`     DECIMAL(19, 5) NOT NULL,
  `fulfillment_group_id` BIGINT(20) DEFAULT NULL,
  `offer_id`             BIGINT(20)     NOT NULL,
  PRIMARY KEY (`fg_adjustment_id`),
  KEY `fgadjustment_index` (`fulfillment_group_id`),
  KEY `fgadjustment_offer_index` (`offer_id`),
  CONSTRAINT `fk468c8f255028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`),
  CONSTRAINT `fk468c8f25d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_group`;
CREATE TABLE `nph_fulfillment_group` (
  `fulfillment_group_id`      BIGINT(20) NOT NULL,
  `order_id`                  BIGINT(20) NOT NULL,
  `address_id`                BIGINT(20)     DEFAULT NULL,
  `fulfillment_option_id`     BIGINT(20)     DEFAULT NULL,
  `type`                      VARCHAR(255)   DEFAULT NULL,
  `retail_price`              DECIMAL(19, 5) DEFAULT NULL,
  `sale_price`                DECIMAL(19, 5) DEFAULT NULL,
  `price`                     DECIMAL(19, 5) DEFAULT NULL,
  `merchandise_total`         DECIMAL(19, 5) DEFAULT NULL,
  `total`                     DECIMAL(19, 5) DEFAULT NULL,
  `reference_number`          VARCHAR(255)   DEFAULT NULL,
  `fulfillment_group_sequnce` INT(11)        DEFAULT NULL,
  `shipping_override`         TINYINT(1)     DEFAULT NULL,
  `status`                    VARCHAR(30)    DEFAULT NULL,
  PRIMARY KEY (`fulfillment_group_id`),
  KEY `fg_address_index` (`address_id`),
  KEY `fg_order_index` (`order_id`),
  KEY `fg_reference_index` (`reference_number`),
  KEY `fg_status_index` (`status`),
  KEY `fkc5b9ef1881f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkc5b9ef1881f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fkc5b9ef1889fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`),
  CONSTRAINT `fkc5b9ef18c13085dd` FOREIGN KEY (`address_id`) REFERENCES `nph_address` (`address_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_group_fee`;
CREATE TABLE `nph_fulfillment_group_fee` (
  `fulfillment_group_fee_id` BIGINT(20) NOT NULL,
  `amount`                   DECIMAL(19, 5) DEFAULT NULL,
  `fee_taxable_flag`         TINYINT(1)     DEFAULT NULL,
  `name`                     VARCHAR(255)   DEFAULT NULL,
  `reporting_code`           VARCHAR(255)   DEFAULT NULL,
  `total_fee_tax`            DECIMAL(19, 5) DEFAULT NULL,
  `fulfillment_group_id`     BIGINT(20) NOT NULL,
  PRIMARY KEY (`fulfillment_group_fee_id`),
  KEY `fk6aa8e1bf5028dc55` (`fulfillment_group_id`),
  CONSTRAINT `fk6aa8e1bf5028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_group_item`;
CREATE TABLE `nph_fulfillment_group_item` (
  `fulfillment_group_item_id` BIGINT(20) NOT NULL,
  `prorated_order_adj`        DECIMAL(19, 2) DEFAULT NULL,
  `quantity`                  INT(11)    NOT NULL,
  `status`                    VARCHAR(255)   DEFAULT NULL,
  `total_item_amount`         DECIMAL(19, 5) DEFAULT NULL,
  `fulfillment_group_id`      BIGINT(20) NOT NULL,
  `order_item_id`             BIGINT(20) NOT NULL,
  PRIMARY KEY (`fulfillment_group_item_id`),
  KEY `fgitem_fg_index` (`fulfillment_group_id`),
  KEY `fgitem_status_index` (`status`),
  CONSTRAINT `fkea74ebda5028dc55` FOREIGN KEY (`fulfillment_group_id`) REFERENCES `nph_fulfillment_group` (`fulfillment_group_id`),
  CONSTRAINT `fkea74ebda9af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_opt_banded_prc`;
CREATE TABLE `nph_fulfillment_opt_banded_prc` (
  `fulfillment_option_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fkb1fd71e981f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkb1fd71e981f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_opt_banded_wgt`;
CREATE TABLE `nph_fulfillment_opt_banded_wgt` (
  `fulfillment_option_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fkb1fd8aec81f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fkb1fd8aec81f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_option`;
CREATE TABLE `nph_fulfillment_option` (
  `fulfillment_option_id` BIGINT(20)   NOT NULL,
  `fulfillment_type`      VARCHAR(255) NOT NULL,
  `long_description`      LONGTEXT,
  `name`                  VARCHAR(255) DEFAULT NULL,
  `use_flat_rates`        TINYINT(1)   DEFAULT NULL,
  PRIMARY KEY (`fulfillment_option_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_option_fixed`;
CREATE TABLE `nph_fulfillment_option_fixed` (
  `fulfillment_option_id` BIGINT(20)     NOT NULL,
  `price`                 DECIMAL(19, 5) NOT NULL,
  PRIMARY KEY (`fulfillment_option_id`),
  KEY `fk4083603181f34c7f` (`fulfillment_option_id`),
  CONSTRAINT `fk4083603181f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS `nph_fulfillment_price_band`;
CREATE TABLE `nph_fulfillment_price_band` (
  `fulfillment_price_band_id`   BIGINT(20)     NOT NULL,
  `result_amount`               DECIMAL(19, 5) NOT NULL,
  `result_amount_type`          VARCHAR(255)   NOT NULL,
  `retail_price_minimum_amount` DECIMAL(19, 5) NOT NULL,
  `fulfillment_option_id`       BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`fulfillment_price_band_id`),
  KEY `fk46c9ea726cdf59ca` (`fulfillment_option_id`),
  CONSTRAINT `fk46c9ea726cdf59ca` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_opt_banded_prc` (`fulfillment_option_id`)
);

DROP TABLE IF EXISTS `nph_fulfillment_weight_band`;
CREATE TABLE `nph_fulfillment_weight_band` (
  `fulfillment_weight_band_id` BIGINT(20)     NOT NULL,
  `result_amount`              DECIMAL(19, 5) NOT NULL,
  `result_amount_type`         VARCHAR(255)   NOT NULL,
  `minimum_weight`             DECIMAL(19, 5) DEFAULT NULL,
  `weight_unit_of_measure`     VARCHAR(255)   DEFAULT NULL,
  `fulfillment_option_id`      BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`fulfillment_weight_band_id`),
  KEY `fk6a048d95a0b429c3` (`fulfillment_option_id`),
  CONSTRAINT `fk6a048d95a0b429c3` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_opt_banded_wgt` (`fulfillment_option_id`)
);

DROP TABLE IF EXISTS `nph_gift_card_payment`;
CREATE TABLE `nph_gift_card_payment` (
  `payment_id`       BIGINT(20)   NOT NULL,
  `pan`              VARCHAR(255) NOT NULL,
  `pin`              VARCHAR(255) DEFAULT NULL,
  `reference_number` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `giftcard_index` (`reference_number`)
);

DROP TABLE IF EXISTS `nph_item_offer_qualifier`;
CREATE TABLE `nph_item_offer_qualifier` (
  `item_offer_qualifier_id` BIGINT(20) NOT NULL,
  `quantity`                BIGINT(20) DEFAULT NULL,
  `offer_id`                BIGINT(20) NOT NULL,
  `order_item_id`           BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`item_offer_qualifier_id`),
  KEY `fkd9c50c61d5f3faf4` (`offer_id`),
  KEY `fkd9c50c619af166df` (`order_item_id`),
  CONSTRAINT `fkd9c50c619af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`),
  CONSTRAINT `fkd9c50c61d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_media`;
CREATE TABLE `nph_media` (
  `media_id` BIGINT(20)   NOT NULL,
  `alt_text` VARCHAR(255) DEFAULT NULL,
  `tags`     VARCHAR(255) DEFAULT NULL,
  `title`    VARCHAR(255) DEFAULT NULL,
  `url`      VARCHAR(255) NOT NULL,
  PRIMARY KEY (`media_id`),
  KEY `media_title_index` (`title`),
  KEY `media_url_index` (`url`)
);

DROP TABLE IF EXISTS `nph_module_configuration`;
CREATE TABLE `nph_module_configuration` (
  `module_config_id`  BIGINT(20)   NOT NULL,
  `active_end_date`   DATETIME   DEFAULT NULL,
  `active_start_date` DATETIME   DEFAULT NULL,
  `archived`          CHAR(1)    DEFAULT NULL,
  `created_by`        BIGINT(20) DEFAULT NULL,
  `date_created`      DATETIME   DEFAULT NULL,
  `date_updated`      DATETIME   DEFAULT NULL,
  `updated_by`        BIGINT(20) DEFAULT NULL,
  `config_type`       VARCHAR(255) NOT NULL,
  `is_default`        TINYINT(1)   NOT NULL,
  `module_name`       VARCHAR(255) NOT NULL,
  `module_priority`   INT(11)      NOT NULL,
  PRIMARY KEY (`module_config_id`)
);

DROP TABLE IF EXISTS `nph_net_node`;
CREATE TABLE `nph_net_node` (
  `net_node_id`   BIGINT(20) NOT NULL,
  `net_node_name` VARCHAR(45)  DEFAULT NULL,
  `address`       VARCHAR(255) DEFAULT NULL,
  `community_id`  BIGINT(20)   DEFAULT NULL,
  `contact_with`  VARCHAR(20)  DEFAULT NULL
  COMMENT '联系人 ',
  `phone_number`  VARCHAR(30)  DEFAULT NULL
);

DROP TABLE IF EXISTS `nph_offer`;
CREATE TABLE `nph_offer` (
  `offer_id`                     BIGINT(20)     NOT NULL,
  `applies_when_rules`           LONGTEXT,
  `applies_to_rules`             LONGTEXT,
  `apply_offer_to_marked_items`  TINYINT(1)     DEFAULT NULL,
  `apply_to_sale_price`          TINYINT(1)     DEFAULT NULL,
  `archived`                     CHAR(1)        DEFAULT NULL,
  `automatically_added`          TINYINT(1)     DEFAULT NULL,
  `combinable_with_other_offers` TINYINT(1)     DEFAULT NULL,
  `offer_delivery_type`          VARCHAR(255)   DEFAULT NULL,
  `offer_description`            VARCHAR(255)   DEFAULT NULL,
  `offer_discount_type`          VARCHAR(255)   DEFAULT NULL,
  `end_date`                     DATETIME       DEFAULT NULL,
  `marketing_messasge`           VARCHAR(255)   DEFAULT NULL,
  `max_uses_per_customer`        BIGINT(20)     DEFAULT NULL,
  `max_uses`                     INT(11)        DEFAULT NULL,
  `offer_name`                   VARCHAR(255)   NOT NULL,
  `offer_item_qualifier_rule`    VARCHAR(255)   DEFAULT NULL,
  `offer_item_target_rule`       VARCHAR(255)   DEFAULT NULL,
  `offer_priority`               INT(11)        DEFAULT NULL,
  `qualifying_item_min_total`    DECIMAL(19, 5) DEFAULT NULL,
  `requires_related_tar_qual`    TINYINT(1)     DEFAULT NULL,
  `stackable`                    TINYINT(1)     DEFAULT NULL,
  `start_date`                   DATETIME       DEFAULT NULL,
  `target_system`                VARCHAR(255)   DEFAULT NULL,
  `totalitarian_offer`           TINYINT(1)     DEFAULT NULL,
  `use_new_format`               TINYINT(1)     DEFAULT NULL,
  `offer_type`                   VARCHAR(255)   NOT NULL,
  `uses`                         INT(11)        DEFAULT NULL,
  `offer_value`                  DECIMAL(19, 5) NOT NULL,
  PRIMARY KEY (`offer_id`),
  KEY `offer_discount_index` (`offer_discount_type`),
  KEY `offer_marketing_message_index` (`marketing_messasge`),
  KEY `offer_name_index` (`offer_name`),
  KEY `offer_type_index` (`offer_type`)
);

DROP TABLE IF EXISTS `nph_offer_audit`;
CREATE TABLE `nph_offer_audit` (
  `offer_audit_id` BIGINT(20) NOT NULL,
  `customer_id`    BIGINT(20) DEFAULT NULL,
  `offer_code_id`  BIGINT(20) DEFAULT NULL,
  `offer_id`       BIGINT(20) DEFAULT NULL,
  `order_id`       BIGINT(20) DEFAULT NULL,
  `redeemed_date`  DATETIME   DEFAULT NULL,
  PRIMARY KEY (`offer_audit_id`),
  KEY `offeraudit_customer_index` (`customer_id`),
  KEY `offeraudit_offer_code_index` (`offer_code_id`),
  KEY `offeraudit_offer_index` (`offer_id`),
  KEY `offeraudit_order_index` (`order_id`)
);

DROP TABLE IF EXISTS `nph_offer_info`;
CREATE TABLE `nph_offer_info` (
  `offer_info_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`offer_info_id`)
);

DROP TABLE IF EXISTS `nph_offer_info_fields`;
CREATE TABLE `nph_offer_info_fields` (
  `offer_info_fields_id` BIGINT(20)  NOT NULL,
  `field_value`          VARCHAR(100) DEFAULT NULL,
  `field_name`           VARCHAR(50) NOT NULL,
  PRIMARY KEY (`offer_info_fields_id`, `field_name`),
  KEY `fka901886183ae7237` (`offer_info_fields_id`),
  CONSTRAINT `fka901886183ae7237` FOREIGN KEY (`offer_info_fields_id`) REFERENCES `nph_offer_info` (`offer_info_id`)
);

DROP TABLE IF EXISTS `nph_offer_item_criteria`;
CREATE TABLE `nph_offer_item_criteria` (
  `offer_item_criteria_id` BIGINT(20) NOT NULL,
  `order_item_match_rule`  LONGTEXT,
  `quantity`               INT(11)    NOT NULL,
  PRIMARY KEY (`offer_item_criteria_id`)
);

DROP TABLE IF EXISTS `nph_offer_rule`;
CREATE TABLE `nph_offer_rule` (
  `offer_rule_id` BIGINT(20) NOT NULL,
  `match_rule`    LONGTEXT,
  PRIMARY KEY (`offer_rule_id`)
);

DROP TABLE IF EXISTS `nph_offer_rule_map`;
CREATE TABLE `nph_offer_rule_map` (
  `offer_id`      BIGINT(20)   NOT NULL,
  `offer_rule_id` BIGINT(20)   NOT NULL,
  `map_key`       VARCHAR(255) NOT NULL,
  PRIMARY KEY (`offer_id`, `map_key`),
  KEY `fkca468fe2c11a218d` (`offer_rule_id`),
  KEY `fkca468fe245c66d1d` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_order`;
CREATE TABLE `nph_order` (
  `order_id`              BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created_by`            BIGINT(20)          DEFAULT NULL,
  `date_created`          DATETIME            DEFAULT NULL,
  `date_updated`          DATETIME            DEFAULT NULL,
  `updated_by`            BIGINT(20)          DEFAULT NULL,
  `name`                  VARCHAR(50)         DEFAULT NULL,
  `order_number`          VARCHAR(50)         DEFAULT NULL,
  `is_preview`            TINYINT(1)          DEFAULT NULL,
  `order_status`          VARCHAR(50)         DEFAULT NULL,
  `order_subtotal`        DECIMAL(19, 5)      DEFAULT NULL,
  `submit_date`           DATETIME            DEFAULT NULL,
  `order_total`           DECIMAL(19, 5)      DEFAULT NULL,
  `total_shipping`        DECIMAL(19, 5)      DEFAULT NULL,
  `customer_id`           BIGINT(20) NOT NULL,
  `order_address_id`      BIGINT(20)          DEFAULT NULL,
  `apply_coupon_id`       BIGINT(20)          DEFAULT NULL,
  `order_coupon_discount` DECIMAL(19, 5)      DEFAULT NULL,
  `parent_order_id`       BIGINT(20)          DEFAULT NULL,
  `is_review`             TINYINT(1)          DEFAULT '0',
  PRIMARY KEY (`order_id`),
  KEY `order_customer_index` (`customer_id`),
  KEY `order_name_index` (`name`),
  KEY `order_number_index` (`order_number`),
  KEY `order_status_index` (`order_status`),
  CONSTRAINT `fk8f5b64a87470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`)
);

DROP TABLE IF EXISTS `nph_order_adjustment`;
CREATE TABLE `nph_order_adjustment` (
  `order_adjustment_id` BIGINT(20)     NOT NULL,
  `adjustment_reason`   VARCHAR(100)   NOT NULL,
  `adjustment_value`    DECIMAL(19, 5) NOT NULL,
  `offer_id`            BIGINT(20)     NOT NULL,
  `order_id`            BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`order_adjustment_id`),
  KEY `orderadjust_offer_index` (`offer_id`),
  KEY `orderadjust_order_index` (`order_id`),
  CONSTRAINT `fk1e92d16489fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`)
);

DROP TABLE IF EXISTS `nph_order_item`;
CREATE TABLE `nph_order_item` (
  `order_item_id`         BIGINT(20) NOT NULL,
  `discounts_allowed`     TINYINT(1)     DEFAULT NULL,
  `name`                  VARCHAR(50)    DEFAULT NULL,
  `order_item_type`       VARCHAR(50)    DEFAULT NULL,
  `price`                 DECIMAL(19, 5) DEFAULT NULL,
  `quantity`              INT(11)    NOT NULL,
  `retail_price`          DECIMAL(19, 5) DEFAULT NULL,
  `retail_price_override` TINYINT(1)     DEFAULT NULL,
  `sale_price`            DECIMAL(19, 5) DEFAULT NULL,
  `sale_price_override`   TINYINT(1)     DEFAULT NULL,
  `order_id`              BIGINT(20)     DEFAULT NULL,
  `sku_id`                BIGINT(20)     DEFAULT NULL,
  `is_used`               TINYINT(1)     DEFAULT '0',
  `is_review`             TINYINT(1)     DEFAULT '0',
  PRIMARY KEY (`order_item_id`),
  KEY `orderitem_order_index` (`order_id`),
  KEY `orderitem_type_index` (`order_item_type`),
  KEY `orderitem_message_index` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_order_item_dtl_adj`;
CREATE TABLE `nph_order_item_dtl_adj` (
  `order_item_dtl_adj_id`   BIGINT(20)     NOT NULL,
  `applied_to_sale_price`   TINYINT(1)   DEFAULT NULL,
  `offer_name`              VARCHAR(255) DEFAULT NULL,
  `adjustment_reason`       VARCHAR(255)   NOT NULL,
  `adjustment_value`        DECIMAL(19, 5) NOT NULL,
  `offer_id`                BIGINT(20)     NOT NULL,
  `order_item_price_dtl_id` BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`order_item_dtl_adj_id`),
  KEY `fk85f0248fd5f3faf4` (`offer_id`),
  KEY `fk85f0248fd4aea2c0` (`order_item_price_dtl_id`),
  CONSTRAINT `fk85f0248fd4aea2c0` FOREIGN KEY (`order_item_price_dtl_id`) REFERENCES `nph_order_item_price_dtl` (`order_item_price_dtl_id`),
  CONSTRAINT `fk85f0248fd5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_order_item_price_dtl`;
CREATE TABLE `nph_order_item_price_dtl` (
  `order_item_price_dtl_id` BIGINT(20) NOT NULL,
  `quantity`                INT(11)    NOT NULL,
  `use_sale_price`          TINYINT(1) DEFAULT NULL,
  `order_item_id`           BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`order_item_price_dtl_id`),
  KEY `fk1fb64bf19af166df` (`order_item_id`),
  CONSTRAINT `fk1fb64bf19af166df` FOREIGN KEY (`order_item_id`) REFERENCES `nph_order_item` (`order_item_id`)
);

DROP TABLE IF EXISTS `nph_order_log`;
CREATE TABLE `nph_order_log` (
  `log_id`       BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `order_id`     BIGINT(20)   NOT NULL,
  `type`         VARCHAR(10)  NOT NULL,
  `message`      VARCHAR(255) NOT NULL,
  `operator`     VARCHAR(45)           DEFAULT NULL,
  `date_created` DATETIME     NOT NULL,
  PRIMARY KEY (`log_id`)
);

DROP TABLE IF EXISTS `nph_order_payment`;
CREATE TABLE `nph_order_payment` (
  `order_payment_id` BIGINT(20)   NOT NULL,
  `amount`           DECIMAL(19, 5) DEFAULT NULL,
  `archived`         CHAR(1)        DEFAULT NULL,
  `gateway_type`     VARCHAR(255)   DEFAULT NULL,
  `reference_number` VARCHAR(255)   DEFAULT NULL,
  `payment_type`     VARCHAR(255) NOT NULL,
  `order_id`         BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`order_payment_id`),
  KEY `orderpayment_order_index` (`order_id`),
  KEY `orderpayment_reference_index` (`reference_number`),
  KEY `orderpayment_type_index` (`payment_type`),
  CONSTRAINT `fk9517a14f89fe8a02` FOREIGN KEY (`order_id`) REFERENCES `nph_order` (`order_id`)
);

DROP TABLE IF EXISTS `nph_order_payment_transaction`;
CREATE TABLE `nph_order_payment_transaction` (
  `payment_transaction_id` BIGINT(20) NOT NULL,
  `transaction_amount`     DECIMAL(19, 2) DEFAULT NULL,
  `archived`               CHAR(1)        DEFAULT NULL,
  `customer_ip_address`    VARCHAR(255)   DEFAULT NULL,
  `date_recorded`          DATETIME       DEFAULT NULL,
  `raw_response`           LONGTEXT,
  `success`                TINYINT(1)     DEFAULT NULL,
  `transaction_type`       VARCHAR(255)   DEFAULT NULL,
  `order_payment`          BIGINT(20) NOT NULL,
  `parent_transaction`     BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`payment_transaction_id`),
  KEY `fk86fde7ce6a69dd9d` (`order_payment`),
  KEY `fk86fde7cee1b66c71` (`parent_transaction`),
  CONSTRAINT `fk86fde7ce6a69dd9d` FOREIGN KEY (`order_payment`) REFERENCES `nph_order_payment` (`order_payment_id`)
);

DROP TABLE IF EXISTS `nph_product`;
CREATE TABLE `nph_product` (
  `product_id`          BIGINT(20) NOT NULL,
  `product_name`        VARCHAR(50)         DEFAULT NULL,
  `archived`            CHAR(1)             DEFAULT NULL,
  `active_start_date`   DATETIME            DEFAULT NULL,
  `active_end_date`     DATETIME            DEFAULT NULL,
  `display_template`    VARCHAR(50)         DEFAULT NULL,
  `is_featured_product` TINYINT(1) NOT NULL,
  `manufacture`         VARCHAR(30)         DEFAULT NULL,
  `breed`               VARCHAR(30)         DEFAULT NULL,
  `grade`               VARCHAR(30)         DEFAULT NULL,
  `model`               VARCHAR(30)         DEFAULT NULL,
  `url`                 VARCHAR(255)        DEFAULT NULL,
  `url_key`             VARCHAR(255)        DEFAULT NULL,
  `provider_id`         BIGINT(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`product_id`),
  KEY `product_url_index` (`url`, `url_key`)
);

DROP TABLE IF EXISTS `nph_product_attribute`;
CREATE TABLE `nph_product_attribute` (
  `product_attribute_id` BIGINT(20)   NOT NULL,
  `name`                 VARCHAR(255) NOT NULL,
  `searchable`           TINYINT(1)   DEFAULT NULL,
  `value`                VARCHAR(255) DEFAULT NULL,
  `product_id`           BIGINT(20)   NOT NULL,
  PRIMARY KEY (`product_attribute_id`),
  KEY `productattribute_name_index` (`name`),
  KEY `productattribute_index` (`product_id`),
  CONSTRAINT `fk56ce05865f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
);

DROP TABLE IF EXISTS `nph_product_cross_sale`;
CREATE TABLE `nph_product_cross_sale` (
  `cross_sale_product_id`   BIGINT(20) NOT NULL,
  `promotion_message`       VARCHAR(255)   DEFAULT NULL,
  `sequence`                DECIMAL(10, 6) DEFAULT NULL,
  `category_id`             BIGINT(20)     DEFAULT NULL,
  `product_id`              BIGINT(20)     DEFAULT NULL,
  `related_sale_product_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`cross_sale_product_id`),
  KEY `crosssale_category_index` (`category_id`),
  KEY `crosssale_index` (`product_id`),
  KEY `crosssale_related_index` (`related_sale_product_id`),
  CONSTRAINT `fk8324fb3c15d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk8324fb3c5f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fk8324fb3c62d84f9b` FOREIGN KEY (`related_sale_product_id`) REFERENCES `nph_product` (`product_id`)
);

DROP TABLE IF EXISTS `nph_product_featured`;
CREATE TABLE `nph_product_featured` (
  `featured_product_id` BIGINT(20) NOT NULL,
  `promotion_message`   VARCHAR(255)   DEFAULT NULL,
  `sequence`            DECIMAL(10, 6) DEFAULT NULL,
  `category_id`         BIGINT(20)     DEFAULT NULL,
  `product_id`          BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`featured_product_id`),
  KEY `prodfeatured_category_index` (`category_id`),
  KEY `prodfeatured_product_index` (`product_id`),
  CONSTRAINT `fk4c49ffe415d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fk4c49ffe45f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
);

DROP TABLE IF EXISTS `nph_product_option`;
CREATE TABLE `nph_product_option` (
  `product_option_id`        BIGINT(20) NOT NULL,
  `attribute_name`           VARCHAR(30)  DEFAULT NULL,
  `display_order`            INT(11)      DEFAULT NULL,
  `error_code`               VARCHAR(30)  DEFAULT NULL,
  `error_message`            VARCHAR(100) DEFAULT NULL,
  `label`                    VARCHAR(30)  DEFAULT NULL,
  `validation_strategy_type` VARCHAR(30)  DEFAULT NULL,
  `validation_type`          VARCHAR(30)  DEFAULT NULL,
  `required`                 TINYINT(1)   DEFAULT NULL,
  `option_type`              VARCHAR(30)  DEFAULT NULL,
  `use_in_sku_generation`    TINYINT(1)   DEFAULT NULL,
  `validation_string`        VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`product_option_id`)
);

DROP TABLE IF EXISTS `nph_product_option_value`;
CREATE TABLE `nph_product_option_value` (
  `product_option_value_id` BIGINT(20) NOT NULL,
  `attribute_value`         VARCHAR(50)    DEFAULT NULL,
  `display_order`           BIGINT(20)     DEFAULT NULL,
  `price_adjustment`        DECIMAL(19, 5) DEFAULT NULL,
  `product_option_id`       BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`product_option_value_id`),
  KEY `fk6deeedbd92ea8136` (`product_option_id`),
  CONSTRAINT `fk6deeedbd92ea8136` FOREIGN KEY (`product_option_id`) REFERENCES `nph_product_option` (`product_option_id`)
);

DROP TABLE IF EXISTS `nph_product_option_xref`;
CREATE TABLE `nph_product_option_xref` (
  `product_option_xref_id` BIGINT(20) NOT NULL,
  `product_id`             BIGINT(20) NOT NULL,
  `product_option_id`      BIGINT(20) NOT NULL,
  PRIMARY KEY (`product_option_xref_id`),
  KEY `fkda42ab2f5f11a0b7` (`product_id`),
  KEY `fkda42ab2f92ea8136` (`product_option_id`),
  CONSTRAINT `fkda42ab2f5f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fkda42ab2f92ea8136` FOREIGN KEY (`product_option_id`) REFERENCES `nph_product_option` (`product_option_id`)
);

DROP TABLE IF EXISTS `nph_product_up_sale`;
CREATE TABLE `nph_product_up_sale` (
  `up_sale_product_id`      BIGINT(20) NOT NULL,
  `promotion_message`       VARCHAR(255)   DEFAULT NULL,
  `sequence`                DECIMAL(10, 6) DEFAULT NULL,
  `category_id`             BIGINT(20)     DEFAULT NULL,
  `product_id`              BIGINT(20)     DEFAULT NULL,
  `related_sale_product_id` BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`up_sale_product_id`),
  KEY `upsale_category_index` (`category_id`),
  KEY `upsale_product_index` (`product_id`),
  KEY `upsale_related_index` (`related_sale_product_id`),
  CONSTRAINT `fkf69054f515d1a13d` FOREIGN KEY (`category_id`) REFERENCES `nph_category` (`category_id`),
  CONSTRAINT `fkf69054f55f11a0b7` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`),
  CONSTRAINT `fkf69054f562d84f9b` FOREIGN KEY (`related_sale_product_id`) REFERENCES `nph_product` (`product_id`)
);

DROP TABLE IF EXISTS `nph_qual_crit_offer_xref`;
CREATE TABLE `nph_qual_crit_offer_xref` (
  `offer_item_criteria_id` BIGINT(20) NOT NULL,
  `offer_id`               BIGINT(20) NOT NULL,
  PRIMARY KEY (`offer_id`, `offer_item_criteria_id`),
  UNIQUE KEY `uk_d592e919e7ab0252` (`offer_item_criteria_id`),
  KEY `fkd592e9193615a91a` (`offer_item_criteria_id`),
  KEY `fkd592e919d5f3faf4` (`offer_id`),
  CONSTRAINT `fkd592e9193615a91a` FOREIGN KEY (`offer_item_criteria_id`) REFERENCES `nph_offer_item_criteria` (`offer_item_criteria_id`),
  CONSTRAINT `fkd592e919d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_rating_detail`;
CREATE TABLE `nph_rating_detail` (
  `rating_detail_id`      BIGINT(20) NOT NULL,
  `rating`                DOUBLE     NOT NULL,
  `rating_submitted_date` DATETIME   NOT NULL,
  `customer_id`           BIGINT(20) NOT NULL,
  `rating_summary_id`     BIGINT(20) NOT NULL,
  PRIMARY KEY (`rating_detail_id`),
  KEY `rating_customer_index` (`customer_id`),
  KEY `fkc9d04add4e76bf4` (`rating_summary_id`),
  CONSTRAINT `fkc9d04ad7470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fkc9d04add4e76bf4` FOREIGN KEY (`rating_summary_id`) REFERENCES `nph_rating_summary` (`rating_summary_id`)
);

DROP TABLE IF EXISTS `nph_rating_summary`;
CREATE TABLE `nph_rating_summary` (
  `rating_summary_id` BIGINT(20)   NOT NULL,
  `average_rating`    DOUBLE       NOT NULL,
  `item_id`           VARCHAR(255) NOT NULL,
  `rating_type`       VARCHAR(255) NOT NULL,
  PRIMARY KEY (`rating_summary_id`),
  KEY `ratingsumm_item_index` (`item_id`),
  KEY `ratingsumm_type_index` (`rating_type`)
);

DROP TABLE IF EXISTS `nph_review_detail`;
CREATE TABLE `nph_review_detail` (
  `review_detail_id`      BIGINT(20)   NOT NULL,
  `helpful_count`         INT(11)      NOT NULL,
  `not_helpful_count`     INT(11)      NOT NULL,
  `review_submitted_date` DATETIME     NOT NULL,
  `review_status`         VARCHAR(255) NOT NULL,
  `review_text`           VARCHAR(255) NOT NULL,
  `customer_id`           BIGINT(20)   NOT NULL,
  `rating_detail_id`      BIGINT(20) DEFAULT NULL,
  `rating_summary_id`     BIGINT(20)   NOT NULL,
  `order_item_id`         BIGINT(20)   NOT NULL,
  PRIMARY KEY (`review_detail_id`),
  KEY `reviewdetail_customer_index` (`customer_id`),
  KEY `reviewdetail_rating_index` (`rating_detail_id`),
  KEY `reviewdetail_summary_index` (`rating_summary_id`),
  KEY `reviewdetail_status_index` (`review_status`),
  CONSTRAINT `fk9cd7e69245dc39e0` FOREIGN KEY (`rating_detail_id`) REFERENCES `nph_rating_detail` (`rating_detail_id`),
  CONSTRAINT `fk9cd7e6927470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk9cd7e692d4e76bf4` FOREIGN KEY (`rating_summary_id`) REFERENCES `nph_rating_summary` (`rating_summary_id`)
);

DROP TABLE IF EXISTS `nph_review_feedback`;
CREATE TABLE `nph_review_feedback` (
  `review_feedback_id` BIGINT(20) NOT NULL,
  `is_helpful`         TINYINT(1) NOT NULL,
  `customer_id`        BIGINT(20) NOT NULL,
  `review_detail_id`   BIGINT(20) NOT NULL,
  PRIMARY KEY (`review_feedback_id`),
  KEY `reviewfeed_customer_index` (`customer_id`),
  KEY `reviewfeed_detail_index` (`review_detail_id`),
  CONSTRAINT `fk7cc929867470f437` FOREIGN KEY (`customer_id`) REFERENCES `nph_customer` (`customer_id`),
  CONSTRAINT `fk7cc92986ae4769d6` FOREIGN KEY (`review_detail_id`) REFERENCES `nph_review_detail` (`review_detail_id`)
);

DROP TABLE IF EXISTS `nph_role`;
CREATE TABLE `nph_role` (
  `role_id`   BIGINT(20)   NOT NULL,
  `role_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `role_name_index` (`role_name`)
);

DROP TABLE IF EXISTS `nph_search_facet`;
CREATE TABLE `nph_search_facet` (
  `search_facet_id`         BIGINT(20) NOT NULL,
  `multiselect`             TINYINT(1)  DEFAULT NULL,
  `label`                   VARCHAR(20) DEFAULT NULL,
  `requires_all_dependent`  TINYINT(1)  DEFAULT NULL,
  `search_display_priority` INT(11)     DEFAULT NULL,
  `show_on_search`          TINYINT(1)  DEFAULT NULL,
  `field_id`                BIGINT(20) NOT NULL,
  PRIMARY KEY (`search_facet_id`),
  KEY `fk4ffcc9863c3907c4` (`field_id`)
);

DROP TABLE IF EXISTS `nph_search_facet_range`;
CREATE TABLE `nph_search_facet_range` (
  `search_facet_range_id` BIGINT(20)     NOT NULL,
  `max_value`             DECIMAL(19, 5) DEFAULT NULL,
  `min_value`             DECIMAL(19, 5) NOT NULL,
  `search_facet_id`       BIGINT(20)     DEFAULT NULL,
  PRIMARY KEY (`search_facet_range_id`),
  KEY `search_facet_index` (`search_facet_id`),
  CONSTRAINT `fk7ec3b124b96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
);

DROP TABLE IF EXISTS `nph_search_facet_xref`;
CREATE TABLE `nph_search_facet_xref` (
  `id`                BIGINT(20) NOT NULL,
  `required_facet_id` BIGINT(20) NOT NULL,
  `search_facet_id`   BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk35a63034da7e1c7c` (`required_facet_id`),
  KEY `fk35a63034b96b1c93` (`search_facet_id`),
  CONSTRAINT `fk35a63034b96b1c93` FOREIGN KEY (`search_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`),
  CONSTRAINT `fk35a63034da7e1c7c` FOREIGN KEY (`required_facet_id`) REFERENCES `nph_search_facet` (`search_facet_id`)
);

DROP TABLE IF EXISTS `nph_search_field`;
CREATE TABLE `nph_search_field` (
  `field_id`         BIGINT(20)   NOT NULL,
  `abbreviation`     VARCHAR(255) DEFAULT NULL,
  `entity_type`      VARCHAR(255) NOT NULL,
  `facet_field_type` VARCHAR(255) DEFAULT NULL,
  `property_name`    VARCHAR(255) NOT NULL,
  `searchable`       TINYINT(1)   DEFAULT NULL,
  `translatable`     TINYINT(1)   DEFAULT NULL,
  PRIMARY KEY (`field_id`),
  KEY `entity_type_index` (`entity_type`)
);

DROP TABLE IF EXISTS `nph_search_field_types`;
CREATE TABLE `nph_search_field_types` (
  `field_id`              BIGINT(20) NOT NULL,
  `searchable_field_type` VARCHAR(100) DEFAULT NULL,
  KEY `fkf52d130d3c3907c4` (`field_id`)
);

DROP TABLE IF EXISTS `nph_search_intercept`;
CREATE TABLE `nph_search_intercept` (
  `search_redirect_id` BIGINT(20)   NOT NULL,
  `active_end_date`    DATETIME DEFAULT NULL,
  `active_start_date`  DATETIME DEFAULT NULL,
  `priority`           INT(11)  DEFAULT NULL,
  `search_term`        VARCHAR(255) NOT NULL,
  `url`                VARCHAR(255) NOT NULL,
  PRIMARY KEY (`search_redirect_id`),
  KEY `search_active_index` (`active_end_date`),
  KEY `search_priority_index` (`priority`)
);

DROP TABLE IF EXISTS `nph_search_synonym`;
CREATE TABLE `nph_search_synonym` (
  `search_synonym_id` BIGINT(20) NOT NULL,
  `synonyms`          VARCHAR(255) DEFAULT NULL,
  `term`              VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`search_synonym_id`),
  KEY `searchsynonym_term_index` (`term`)
);

DROP TABLE IF EXISTS `nph_site`;
CREATE TABLE `nph_site` (
  `site_id`               BIGINT(20) NOT NULL,
  `archived`              CHAR(1)      DEFAULT NULL,
  `deactivated`           TINYINT(1)   DEFAULT NULL,
  `name`                  VARCHAR(255) DEFAULT NULL,
  `site_identifier_type`  VARCHAR(255) DEFAULT NULL,
  `site_identifier_value` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`site_id`),
  KEY `blc_site_id_val_index` (`site_identifier_value`)
);

DROP TABLE IF EXISTS `nph_site_catalog`;
CREATE TABLE `nph_site_catalog` (
  `catalog_id` BIGINT(20) NOT NULL,
  `site_id`    BIGINT(20) NOT NULL,
  KEY `fk5f3f2047843a8b63` (`site_id`),
  KEY `fk5f3f2047a350c7f1` (`catalog_id`),
  CONSTRAINT `fk5f3f2047843a8b63` FOREIGN KEY (`site_id`) REFERENCES `nph_site` (`site_id`),
  CONSTRAINT `fk5f3f2047a350c7f1` FOREIGN KEY (`catalog_id`) REFERENCES `nph_catalog` (`catalog_id`)
);

DROP TABLE IF EXISTS `nph_site_map_cfg`;
CREATE TABLE `nph_site_map_cfg` (
  `indexed_site_map_file_name`    VARCHAR(255) DEFAULT NULL,
  `indexed_site_map_file_pattern` VARCHAR(255) DEFAULT NULL,
  `max_url_entries_per_file`      INT(11)      DEFAULT NULL,
  `site_map_file_name`            VARCHAR(255) DEFAULT NULL,
  `module_config_id`              BIGINT(20) NOT NULL,
  PRIMARY KEY (`module_config_id`),
  KEY `fk7012930fc50d449` (`module_config_id`),
  CONSTRAINT `fk7012930fc50d449` FOREIGN KEY (`module_config_id`) REFERENCES `nph_module_configuration` (`module_config_id`)
);

DROP TABLE IF EXISTS `nph_site_map_gen_cfg`;
CREATE TABLE `nph_site_map_gen_cfg` (
  `gen_config_id`    BIGINT(20)   NOT NULL,
  `change_freq`      VARCHAR(255) NOT NULL,
  `disabled`         TINYINT(1)   NOT NULL,
  `generator_type`   VARCHAR(255) NOT NULL,
  `priority`         VARCHAR(255) DEFAULT NULL,
  `module_config_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`gen_config_id`),
  KEY `fk1d76000a340ed71` (`module_config_id`)
);

DROP TABLE IF EXISTS `nph_site_map_url_entry`;
CREATE TABLE `nph_site_map_url_entry` (
  `url_entry_id`  BIGINT(20)   NOT NULL,
  `change_freq`   VARCHAR(255) NOT NULL,
  `last_modified` DATETIME     NOT NULL,
  `location`      VARCHAR(255) NOT NULL,
  `priority`      VARCHAR(255) NOT NULL,
  `gen_config_id` BIGINT(20)   NOT NULL,
  PRIMARY KEY (`url_entry_id`),
  KEY `fke2004fed36afe1ee` (`gen_config_id`),
  CONSTRAINT `fke2004fed36afe1ee` FOREIGN KEY (`gen_config_id`) REFERENCES `nph_cust_site_map_gen_cfg` (`gen_config_id`)
);

DROP TABLE IF EXISTS `nph_sku`;
CREATE TABLE `nph_sku` (
  `sku_id`                    BIGINT(20) NOT NULL,
  `active_end_date`           DATETIME       DEFAULT NULL,
  `active_start_date`         DATETIME       DEFAULT NULL,
  `description`               VARCHAR(100)   DEFAULT NULL,
  `container_shape`           VARCHAR(255)   DEFAULT NULL,
  `depth`                     DECIMAL(19, 2) DEFAULT NULL,
  `dimension_unit_of_measure` VARCHAR(255)   DEFAULT NULL,
  `girth`                     DECIMAL(19, 2) DEFAULT NULL,
  `height`                    DECIMAL(19, 2) DEFAULT NULL,
  `container_size`            VARCHAR(255)   DEFAULT NULL,
  `width`                     DECIMAL(19, 2) DEFAULT NULL,
  `discountable_flag`         CHAR(1)        DEFAULT NULL,
  `fulfillment_type`          VARCHAR(20)    DEFAULT NULL,
  `inventory_type`            VARCHAR(50)    DEFAULT NULL,
  `is_machine_sortable`       TINYINT(1)     DEFAULT NULL,
  `long_description`          LONGTEXT,
  `origin_description`        LONGTEXT,
  `name`                      VARCHAR(50)    DEFAULT NULL,
  `retail_price`              DECIMAL(19, 5) DEFAULT NULL,
  `sale_price`                DECIMAL(19, 5) DEFAULT NULL,
  `weight`                    DECIMAL(19, 2) DEFAULT NULL,
  `weight_unit_of_measure`    VARCHAR(255)   DEFAULT NULL,
  `product_id`                BIGINT(20)     DEFAULT NULL,
  `sale_count`         INT(11) DEFAULT 0,
  `quantity_available` INT(11) DEFAULT 0,
  PRIMARY KEY (`sku_id`),
  KEY `sku_active_end_index` (`active_end_date`),
  KEY `sku_active_start_index` (`active_start_date`),
  KEY `sku_discountable_index` (`discountable_flag`),
  KEY `sku_name_index` (`name`),
  KEY `fk28e82cf77e555d75` (`product_id`),
  CONSTRAINT `fk28e82cf77e555d75` FOREIGN KEY (`product_id`) REFERENCES `nph_product` (`product_id`)
);

DROP TABLE IF EXISTS `nph_sku_attribute`;
CREATE TABLE `nph_sku_attribute` (
  `sku_attr_id` BIGINT(20)   NOT NULL,
  `name`        VARCHAR(255) NOT NULL,
  `searchable`  TINYINT(1) DEFAULT NULL,
  `value`       VARCHAR(255) NOT NULL,
  `sku_id`      BIGINT(20)   NOT NULL,
  PRIMARY KEY (`sku_attr_id`),
  KEY `skuattr_name_index` (`name`),
  KEY `skuattr_sku_index` (`sku_id`),
  CONSTRAINT `fk6c6a5934b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_sku_availability`;
CREATE TABLE `nph_sku_availability` (
  `sku_availability_id` BIGINT(20) NOT NULL,
  `availability_date`   DATETIME     DEFAULT NULL,
  `availability_status` VARCHAR(255) DEFAULT NULL,
  `location_id`         BIGINT(20)   DEFAULT NULL,
  `qty_on_hand`         INT(11)      DEFAULT NULL,
  `reserve_qty`         INT(11)      DEFAULT NULL,
  `sku_id`              BIGINT(20)   DEFAULT NULL,
  PRIMARY KEY (`sku_availability_id`),
  KEY `skuavail_status_index` (`availability_status`),
  KEY `skuavail_location_index` (`location_id`),
  KEY `skuavail_sku_index` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_sku_fee`;
CREATE TABLE `nph_sku_fee` (
  `sku_fee_id`  BIGINT(20)     NOT NULL,
  `amount`      DECIMAL(19, 5) NOT NULL,
  `description` VARCHAR(100) DEFAULT NULL,
  `expression`  LONGTEXT,
  `fee_type`    VARCHAR(100) DEFAULT NULL,
  `name`        VARCHAR(50)  DEFAULT NULL,
  PRIMARY KEY (`sku_fee_id`)
);

DROP TABLE IF EXISTS `nph_sku_fee_xref`;
CREATE TABLE `nph_sku_fee_xref` (
  `sku_fee_id` BIGINT(20) NOT NULL,
  `sku_id`     BIGINT(20) NOT NULL,
  KEY `fkd88d409cb78c9977` (`sku_id`),
  KEY `fkd88d409ccf4c9a82` (`sku_fee_id`),
  CONSTRAINT `fkd88d409cb78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`),
  CONSTRAINT `fkd88d409ccf4c9a82` FOREIGN KEY (`sku_fee_id`) REFERENCES `nph_sku_fee` (`sku_fee_id`)
);

DROP TABLE IF EXISTS `nph_sku_fulfillment_excluded`;
CREATE TABLE `nph_sku_fulfillment_excluded` (
  `sku_id`                BIGINT(20) NOT NULL,
  `fulfillment_option_id` BIGINT(20) NOT NULL,
  KEY `fk84162d7381f34c7f` (`fulfillment_option_id`),
  KEY `fk84162d73b78c9977` (`sku_id`),
  CONSTRAINT `fk84162d7381f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fk84162d73b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_sku_fulfillment_flat_rates`;
CREATE TABLE `nph_sku_fulfillment_flat_rates` (
  `sku_id`                BIGINT(20) NOT NULL,
  `rate`                  DECIMAL(19, 5) DEFAULT NULL,
  `fulfillment_option_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`sku_id`, `fulfillment_option_id`),
  KEY `fkc1988c9681f34c7f` (`fulfillment_option_id`),
  KEY `fkc1988c96b78c9977` (`sku_id`),
  CONSTRAINT `fkc1988c9681f34c7f` FOREIGN KEY (`fulfillment_option_id`) REFERENCES `nph_fulfillment_option` (`fulfillment_option_id`),
  CONSTRAINT `fkc1988c96b78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_sku_media_map`;
CREATE TABLE `nph_sku_media_map` (
  `nph_sku_sku_id` BIGINT(20)   NOT NULL,
  `media_id`       BIGINT(20)   NOT NULL,
  `map_key`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`nph_sku_sku_id`, `map_key`),
  KEY `fkeb4aecf96e4720e0` (`media_id`),
  KEY `fkeb4aecf9d93d857f` (`nph_sku_sku_id`),
  CONSTRAINT `fkeb4aecf9d93d857f` FOREIGN KEY (`nph_sku_sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_sku_option_value_xref`;
CREATE TABLE `nph_sku_option_value_xref` (
  `sku_id`                  BIGINT(20) NOT NULL,
  `product_option_value_id` BIGINT(20) NOT NULL,
  KEY `fk7b61dc0bb0c16a73` (`product_option_value_id`),
  KEY `fk7b61dc0bb78c9977` (`sku_id`),
  CONSTRAINT `fk7b61dc0bb0c16a73` FOREIGN KEY (`product_option_value_id`) REFERENCES `nph_product_option_value` (`product_option_value_id`),
  CONSTRAINT `fk7b61dc0bb78c9977` FOREIGN KEY (`sku_id`) REFERENCES `nph_sku` (`sku_id`)
);

DROP TABLE IF EXISTS `nph_store`;
CREATE TABLE `nph_store` (
  `store_id`      BIGINT(20)   NOT NULL,
  `address_1`     VARCHAR(255) DEFAULT NULL,
  `address_2`     VARCHAR(255) DEFAULT NULL,
  `store_city`    VARCHAR(255) DEFAULT NULL,
  `store_country` VARCHAR(255) DEFAULT NULL,
  `latitude`      DOUBLE       DEFAULT NULL,
  `longitude`     DOUBLE       DEFAULT NULL,
  `store_name`    VARCHAR(255) NOT NULL,
  `store_phone`   VARCHAR(255) DEFAULT NULL,
  `store_state`   VARCHAR(255) DEFAULT NULL,
  `store_zip`     VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`store_id`)
);

DROP TABLE IF EXISTS `nph_system_property`;
CREATE TABLE `nph_system_property` (
  `id`             BIGINT(20)   NOT NULL,
  `friendly_group` VARCHAR(255) DEFAULT NULL,
  `friendly_name`  VARCHAR(255) DEFAULT NULL,
  `friendly_tab`   VARCHAR(255) DEFAULT NULL,
  `property_name`  VARCHAR(255) NOT NULL,
  `property_type`  VARCHAR(255) DEFAULT NULL,
  `property_value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `nph_tar_crit_offer_xref`;
CREATE TABLE `nph_tar_crit_offer_xref` (
  `offer_id`               BIGINT(20) NOT NULL,
  `offer_item_criteria_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`offer_id`, `offer_item_criteria_id`),
  UNIQUE KEY `uk_125f5803e7ab0252` (`offer_item_criteria_id`),
  KEY `fk125f58033615a91a` (`offer_item_criteria_id`),
  KEY `fk125f5803d5f3faf4` (`offer_id`),
  CONSTRAINT `fk125f58033615a91a` FOREIGN KEY (`offer_item_criteria_id`) REFERENCES `nph_offer_item_criteria` (`offer_item_criteria_id`),
  CONSTRAINT `fk125f5803d5f3faf4` FOREIGN KEY (`offer_id`) REFERENCES `nph_offer` (`offer_id`)
);

DROP TABLE IF EXISTS `nph_trans_additnl_fields`;
CREATE TABLE `nph_trans_additnl_fields` (
  `payment_transaction_id` BIGINT(20)   NOT NULL,
  `field_value`            LONGTEXT,
  `field_name`             VARCHAR(255) NOT NULL,
  PRIMARY KEY (`payment_transaction_id`, `field_name`),
  KEY `fk376dde4b9e955b1d` (`payment_transaction_id`),
  CONSTRAINT `fk376dde4b9e955b1d` FOREIGN KEY (`payment_transaction_id`) REFERENCES `nph_order_payment_transaction` (`payment_transaction_id`)
);

DROP TABLE IF EXISTS `nph_translation`;
CREATE TABLE `nph_translation` (
  `translation_id`   BIGINT(20) NOT NULL,
  `entity_id`        VARCHAR(255) DEFAULT NULL,
  `entity_type`      VARCHAR(255) DEFAULT NULL,
  `field_name`       VARCHAR(255) DEFAULT NULL,
  `locale_code`      VARCHAR(255) DEFAULT NULL,
  `translated_value` LONGTEXT,
  PRIMARY KEY (`translation_id`),
  KEY `translation_index` (`entity_type`, `entity_id`, `field_name`, `locale_code`)
);

DROP TABLE IF EXISTS `nph_wechat_customer`;
CREATE TABLE `nph_wechat_customer` (
  `id`                   BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `open_id`              VARCHAR(45) NOT NULL,
  `customer_id`          BIGINT(20)  NOT NULL,
  `is_active`            TINYINT(1)           DEFAULT '0',
  `is_properties_loaded` TINYINT(1)           DEFAULT '0',
  `is_from_wechat`       TINYINT(1)           DEFAULT '0',
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `ph_coupon`;
CREATE TABLE `ph_coupon` (
  `id`         BIGINT(10) NOT NULL AUTO_INCREMENT,
  `strategy`   VARCHAR(100)        DEFAULT NULL,
  `start_date` DATETIME            DEFAULT NULL,
  `end_date`   DATETIME            DEFAULT NULL,
  `desc`       VARCHAR(45)         DEFAULT NULL,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `ph_coupon_attribute`;
CREATE TABLE `ph_coupon_attribute` (
  `id`        BIGINT(10) NOT NULL AUTO_INCREMENT,
  `coupon_id` BIGINT(10) NOT NULL,
  `attr`      VARCHAR(20)         DEFAULT NULL,
  `value`     DECIMAL(10, 5)      DEFAULT NULL,
  PRIMARY KEY (`id`, `coupon_id`)
);


DROP TABLE IF EXISTS `ph_customer_coupon`;
CREATE TABLE `ph_customer_coupon` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT(20)          DEFAULT NULL,
  `coupon_id`   BIGINT(10)          DEFAULT NULL,
  `status`      CHAR(1)             DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `sequence_generator`;
CREATE TABLE `sequence_generator` (
  `id_name` VARCHAR(255) NOT NULL,
  `id_val`  BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id_name`)
);

/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;