/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET character_set_client = utf8 */;

/*refund*/
INSERT INTO `admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `ADMIN_MODULE_ID`) VALUES ('4', 'cn.globalph.b2c.order.domain.Refund', '3000', 'Refund', '申请退款', '/refund', '-3');

INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-210', 'Read Refund', '0', 'PERMISSION_READ_REFUND', 'READ');

INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-211', 'All Refund', '0', 'PERMISSION_ALL_REFUND', 'ALL');
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('4', '-210');

INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('4', '-211');
INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-991', 'cn.globalph.b2c.order.domain.Refund', '-210');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-992', 'cn.globalph.b2c.order.domain.Refund', '-211');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-210');
INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-211');

/**/
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-18', '-49');

/*coupon*/
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-212', 'Read Coupon', '0', 'PERMISSION_READ_COUPON', 'READ');
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-213', 'All Coupon', '0', 'PERMISSION_ALL_COUPON', 'ALL');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-993', 'cn.globalph.coupon.domain.Coupon', '-212');
INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-994', 'cn.globalph.coupon.domain.Coupon', '-213');

INSERT INTO `admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `ADMIN_MODULE_ID`) VALUES ('-19', 'cn.globalph.coupon.domain.Coupon', '4000', 'Coupon', 'Coupon', '/coupon', '-1');

INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-19', '-212');
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-19', '-213');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-212');
INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-213');

/*ComparePrice*/
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-214', 'Read Compare Price', '0', 'PERMISSION_READ_COMPARE_PRICE', 'READ');
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-215', 'All Compare Price', '0', 'PERMISSION_ALL_COMPARE_PRICE', 'ALL');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-995', 'cn.globalph.b2c.product.domain.ComparePrice', '-214');
INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-996', 'cn.globalph.b2c.product.domain.ComparePrice', '-215');

INSERT INTO `admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `ADMIN_MODULE_ID`) VALUES ('-20', 'cn.globalph.b2c.product.domain.ComparePrice', '5000', 'Compare Price', 'Compare Price', '/comparePrice', '-1');

INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-20', '-214');
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-20', '-215');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-214');
INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-215');

/*CustomerMessage*/
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-216', 'Read Customer Message', '0', 'PERMISSION_READ_CUSTOMER_MESSAGE', 'READ');
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-217', 'All Customer Message', '0', 'PERMISSION_ALL_CUSTOMER_MESSAGE', 'ALL');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-997', 'cn.globalph.passport.domain.CustomerMessage', '-216');
INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-998', 'cn.globalph.passport.domain.CustomerMessage', '-217');

INSERT INTO `admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `ADMIN_MODULE_ID`) VALUES ('-21', 'cn.globalph.passport.domain.CustomerMessage', '5000', 'Customer Message', 'Customer Message', '/customerMessage', '-3');

INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-21', '-216');
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-21', '-217');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-216');
INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-217');

/*GroupOn*/
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-218', 'Read Groupon', '0', 'PERMISSION_READ_GROUPON', 'READ');
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`) VALUES ('-219', 'All Groupon', '0', 'PERMISSION_ALL_GROUPON', 'ALL');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-999', 'cn.globalph.b2c.product.domain.GroupOn', '-218');
INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`) VALUES ('-1000', 'cn.globalph.b2c.product.domain.GroupOn', '-219');

INSERT INTO `admin_section` (`ADMIN_SECTION_ID`, `CEILING_ENTITY`, `DISPLAY_ORDER`, `NAME`, `SECTION_KEY`, `URL`, `ADMIN_MODULE_ID`) VALUES ('-22', 'cn.globalph.b2c.product.domain.GroupOn', '5000', 'Groupon', 'Groupon', '/Groupon', '-1');

INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-22', '-218');
INSERT INTO `admin_sec_perm_xref` (`ADMIN_SECTION_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-22', '-219');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-218');
INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`) VALUES ('-1', '-219');

/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;