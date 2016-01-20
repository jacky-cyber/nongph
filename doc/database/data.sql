/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET character_set_client = utf8 */;

INSERT INTO `admin_module` (`ADMIN_MODULE_ID`, `DISPLAY_ORDER`, `ICON`, `MODULE_KEY`, `NAME`)
VALUES (-7, 500, 'icon-refresh', 'BLCWorkflow', 'Site Updates'), (-6, 400, 'icon-picture', 'BLCDesign', 'Design'),
  (-5, 700, 'icon-gear', 'BLCModuleConfiguration', 'Settings'), (-4, 600, 'icon-user', 'BLCOpenAdmin', 'Security'),
  (-3, 300, 'icon-heart', 'BLCCustomerCare', 'Customer Care'),
  (-2, 200, 'icon-file', 'BLCContentManagement', 'Content'), (-1, 100, 'icon-barcode', 'BLCMerchandising', 'Catalog'),
  (1, 800, 'icon-user', 'Net Node Management', 'Net Node'), (2, 600, 'icon-user', 'Provider', 'Provider');
INSERT INTO `admin_permission` (`ADMIN_PERMISSION_ID`, `DESCRIPTION`, `IS_FRIENDLY`, `NAME`, `PERMISSION_TYPE`)
VALUES (-209, 'All Provider', 0, 'PERMISSION_ALL_PROVIDER', 'ALL'),
  (-208, 'Read Provider', 0, 'PERMISSION_READ_PROVIDER', 'READ'),
  (-207, 'All Order Log', 0, 'PERMISSION_ALL_ORDER_LOG', 'ALL'),
  (-206, 'Read Order Log', 0, 'PERMISSION_READ_ORDER_LOG', 'READ'),
  (-205, 'Read Provider', 0, 'PERMISSION_READ_PROVIDER', 'READ'),
  (-204, 'Maintenance Provider', 0, 'PERMISSION_ALL_PROVIDER', 'ALL'),
  (-203, 'Read Order Address', 0, 'PERMISSION_READ_ORDER_ADDRESS', 'READ'),
  (-202, 'Maintenance Order Address', 0, 'PERMISSION_ALL_ORDER_ADDRESS', 'ALL'),
  (-201, 'Read Structured Content Template', 0, 'PERMISSION_READ_STRUCTURED_CONTENT_TEMPLATE', 'READ'),
  (-200, 'Read Page Template', 0, 'PERMISSION_READ_PAGE_TEMPLATE', 'READ'),
  (-151, 'Maintain Permissions', 1, 'PERMISSION_PERM_ALL', 'ALL'),
  (-150, 'View Permissions', 1, 'PERMISSION_PERM_VIEW', 'READ'),
  (-141, 'Maintain Roles', 1, 'PERMISSION_ROLE_ALL', 'ALL'), (-140, 'View Roles', 1, 'PERMISSION_ROLE_VIEW', 'READ'),
  (-131, 'Maintain Translations', 1, 'PERMISSION_TRANSLATION', 'ALL'),
  (-130, 'View Translations', 1, 'PERMISSION_TRANSLATION', 'READ'),
  (-127, 'Maintain Module Configurations', 1, 'PERMISSION_MODULECONFIGURATION', 'ALL'),
  (-126, 'View Module Configurations', 1, 'PERMISSION_MODULECONFIGURATION', 'READ'),
  (-123, 'Maintain System Properties', 1, 'PERMISSION_SYSTEMPROPERTY', 'ALL'),
  (-122, 'View System Properties', 1, 'PERMISSION_SYSTEMPROPERTY', 'READ'),
  (-121, 'Maintain Users', 1, 'PERMISSION_USER', 'ALL'), (-120, 'View Users', 1, 'PERMISSION_USER', 'READ'),
  (-119, 'Maintain Customers', 1, 'PERMISSION_CUSTOMER', 'ALL'),
  (-118, 'View Customers', 1, 'PERMISSION_CUSTOMER', 'READ'), (-117, 'Maintain Orders', 1, 'PERMISSION_ORDER', 'ALL'),
  (-116, 'View Orders', 1, 'PERMISSION_ORDER', 'READ'),
  (-115, 'Maintain URL Redirects', 1, 'PERMISSION_URLREDIRECT', 'ALL'),
  (-114, 'View URL Redirects', 1, 'PERMISSION_URLREDIRECT', 'READ'),
  (-113, 'Maintain Structured Contents', 1, 'PERMISSION_STRUCTUREDCONTENT', 'ALL'),
  (-112, 'View Structured Contents', 1, 'PERMISSION_STRUCTUREDCONTENT', 'READ'),
  (-111, 'Maintain Assets', 1, 'PERMISSION_ASSET', 'ALL'), (-110, 'View Assets', 1, 'PERMISSION_ASSET', 'READ'),
  (-109, 'Maintain Pages', 1, 'PERMISSION_PAGE', 'ALL'), (-108, 'View Pages', 1, 'PERMISSION_PAGE', 'READ'),
  (-107, 'Maintain Offers', 1, 'PERMISSION_OFFER', 'ALL'), (-106, 'View Offers', 1, 'PERMISSION_OFFER', 'READ'),
  (-105, 'Maintain Product Options', 1, 'PERMISSION_PRODUCTOPTIONS', 'ALL'),
  (-104, 'View Product Options', 1, 'PERMISSION_PRODUCTOPTIONS', 'READ'),
  (-103, 'Maintain Products', 1, 'PERMISSION_PRODUCT', 'ALL'), (-102, 'View Products', 1, 'PERMISSION_PRODUCT', 'READ'),
  (-101, 'Maintain Categories', 1, 'PERMISSION_CATEGORY', 'ALL'),
  (-100, 'View Categories', 1, 'PERMISSION_CATEGORY', 'READ'),
  (-49, 'All Admin Permissions', 0, 'PERMISSION_ALL_ADMIN_PERMS', 'ALL'),
  (-48, 'Read Admin Permissions', 0, 'PERMISSION_READ_ADMIN_PERMS', 'READ'),
  (-47, 'All Admin Roles', 0, 'PERMISSION_ALL_ADMIN_ROLES', 'ALL'),
  (-46, 'Read Admin Roles', 0, 'PERMISSION_READ_ADMIN_ROLES', 'READ'),
  (-45, 'All System Property', 0, 'PERMISSION_ALL_SYSTEM_PROPERTY', 'ALL'),
  (-44, 'Read System Property', 0, 'PERMISSION_READ_SYSTEM_PROPERTY', 'READ'),
  (-43, 'All Site Map Gen Configuration', 0, 'PERMISSION_ALL_SITE_MAP_GEN_CONFIG', 'ALL'),
  (-42, 'Read Site Map Gen Configuration', 0, 'PERMISSION_READ_SITE_MAP_GEN_CONFIG', 'READ'),
  (-41, 'All Translation', 0, 'PERMISSION_ALL_TRANSLATION', 'ALL'),
  (-40, 'Read Translation', 0, 'PERMISSION_READ_TRANSLATION', 'READ'),
  (-39, 'All Enumeration', 0, 'PERMISSION_ALL_ENUMERATION', 'ALL'),
  (-38, 'Read Enumeration', 0, 'PERMISSION_READ_ENUMERATION', 'READ'),
  (-37, 'All Configuration', 0, 'PERMISSION_ALL_MODULECONFIGURATION', 'ALL'),
  (-36, 'Read Configuration', 0, 'PERMISSION_READ_MODULECONFIGURATION', 'READ'),
  (-35, 'All Currency', 0, 'PERMISSION_ALL_CURRENCY', 'ALL'),
  (-34, 'Read Currency', 0, 'PERMISSION_READ_CURRENCY', 'READ'),
  (-33, 'All SearchFacet', 0, 'PERMISSION_ALL_SEARCHFACET', 'ALL'),
  (-32, 'Read SearchFacet', 0, 'PERMISSION_READ_SEARCHFACET', 'READ'),
  (-31, 'All SearchRedirect', 0, 'PERMISSION_ALL_SEARCHREDIRECT', 'ALL'),
  (-30, 'Read SearchRedirect', 0, 'PERMISSION_READ_SEARCHREDIRECT', 'READ'),
  (-29, 'All URLHandler', 0, 'PERMISSION_ALL_URLHANDLER', 'ALL'),
  (-28, 'Read URLHandler', 0, 'PERMISSION_READ_URLHANDLER', 'READ'),
  (-27, 'All Admin User', 0, 'PERMISSION_ALL_ADMIN_USER', 'ALL'),
  (-26, 'Read Admin User', 0, 'PERMISSION_READ_ADMIN_USER', 'READ'),
  (-25, 'All Structured Content', 0, 'PERMISSION_ALL_STRUCTURED_CONTENT', 'ALL'),
  (-24, 'Read Structured Content', 0, 'PERMISSION_READ_STRUCTURED_CONTENT', 'READ'),
  (-23, 'All Asset', 0, 'PERMISSION_ALL_ASSET', 'ALL'), (-22, 'Read Asset', 0, 'PERMISSION_READ_ASSET', 'READ'),
  (-21, 'All Page', 0, 'PERMISSION_ALL_PAGE', 'ALL'), (-20, 'Read Page', 0, 'PERMISSION_READ_PAGE', 'READ'),
  (-19, 'All Customer', 0, 'PERMISSION_ALL_CUSTOMER', 'ALL'),
  (-18, 'Read Customer', 0, 'PERMISSION_READ_CUSTOMER', 'READ'),
  (-17, 'All Order Item', 0, 'PERMISSION_ALL_ORDER_ITEM', 'ALL'),
  (-16, 'Read Order Item', 0, 'PERMISSION_READ_ORDER_ITEM', 'READ'),
  (-15, 'All Fulfillment Group', 0, 'PERMISSION_ALL_FULFILLMENT_GROUP', 'ALL'),
  (-14, 'Read Fulfillment Group', 0, 'PERMISSION_READ_FULFILLMENT_GROUP', 'READ'),
  (-13, 'All Order', 0, 'PERMISSION_ALL_ORDER', 'ALL'), (-12, 'Read Order', 0, 'PERMISSION_READ_ORDER', 'READ'),
  (-11, 'All Promotion', 0, 'PERMISSION_ALL_PROMOTION', 'ALL'),
  (-10, 'Read Promotion', 0, 'PERMISSION_READ_PROMOTION', 'READ'), (-9, 'All Sku', 0, 'PERMISSION_ALL_SKU', 'ALL'),
  (-8, 'Read Sku', 0, 'PERMISSION_READ_SKU', 'READ'),
  (-7, 'All Product Option', 0, 'PERMISSION_ALL_PRODUCT_OPTION', 'ALL'),
  (-6, 'Read Product Option', 0, 'PERMISSION_READ_PRODUCT_OPTION', 'READ'),
  (-5, 'All Product', 0, 'PERMISSION_ALL_PRODUCT', 'ALL'), (-4, 'Read Product', 0, 'PERMISSION_READ_PRODUCT', 'READ'),
  (-3, 'All Category', 0, 'PERMISSION_ALL_CATEGORY', 'ALL'),
  (-2, 'Read Category', 0, 'PERMISSION_READ_CATEGORY', 'READ'),
  (-1, 'Default Permission', 0, 'PERMISSION_OTHER_DEFAULT', 'OTHER');

INSERT INTO `admin_permission_entity` (`ADMIN_PERMISSION_ENTITY_ID`, `CEILING_ENTITY`, `ADMIN_PERMISSION_ID`)
VALUES (-990, 'cn.globalph.b2c.order.domain.OrderLog', -207), (-989, 'cn.globalph.b2c.order.domain.OrderLog', -206),
  (-988, 'cn.globalph.b2c.product.domain.Provider', -205), (-987, 'cn.globalph.b2c.product.domain.Provider', -204),
  (-986, 'cn.globalph.b2c.order.domain.OrderAddress', -203), (-985, 'cn.globalph.b2c.order.domain.OrderAddress', -202),
  (-983, 'cn.globalph.openadmin.server.security.domain.AdminPermissionQualifiedEntity', -48),
  (-982, 'cn.globalph.openadmin.server.security.domain.AdminPermissionQualifiedEntity', -49),
  (-981, 'cn.globalph.openadmin.server.security.domain.AdminPermission', -48),
  (-980, 'cn.globalph.openadmin.server.security.domain.AdminPermission', -48),
  (-971, 'cn.globalph.openadmin.server.security.domain.AdminRole', -47),
  (-970, 'cn.globalph.openadmin.server.security.domain.AdminRole', -46),
  (-961, 'cn.globalph.common.config.domain.SystemProperty', -45),
  (-960, 'cn.globalph.common.config.domain.SystemProperty', -44),
  (-953, 'cn.globalph.common.sitemap.domain.SiteMapURLEntry', -43),
  (-952, 'cn.globalph.common.sitemap.domain.SiteMapURLEntry', -42),
  (-951, 'cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration', -43),
  (-950, 'cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration', -42),
  (-911, 'cn.globalph.common.i18n.domain.Translation', -41), (-910, 'cn.globalph.common.i18n.domain.Translation', -40),
  (-903, 'cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue', -39),
  (-902, 'cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue', -38),
  (-901, 'cn.globalph.common.enumeration.domain.DataDrivenEnumeration', -39),
  (-900, 'cn.globalph.common.enumeration.domain.DataDrivenEnumeration', -38),
  (-881, 'cn.globalph.common.config.domain.ModuleConfiguration', -37),
  (-880, 'cn.globalph.common.config.domain.ModuleConfiguration', -36),
  (-809, 'cn.globalph.b2c.search.domain.CategoryExcludedSearchFacet', -33),
  (-808, 'cn.globalph.b2c.search.domain.CategoryExcludedSearchFacet', -32),
  (-807, 'cn.globalph.b2c.search.domain.SearchFacetRange', -33),
  (-806, 'cn.globalph.b2c.search.domain.SearchFacetRange', -32),
  (-805, 'cn.globalph.b2c.search.domain.CategorySearchFacet', -33),
  (-804, 'cn.globalph.b2c.search.domain.CategorySearchFacet', -32), (-803, 'cn.globalph.b2c.search.domain.Field', -33),
  (-802, 'cn.globalph.b2c.search.domain.Field', -32), (-801, 'cn.globalph.b2c.search.domain.SearchFacet', -33),
  (-800, 'cn.globalph.b2c.search.domain.SearchFacet', -32),
  (-781, 'cn.globalph.b2c.search.redirect.domain.SearchRedirect', -31),
  (-780, 'cn.globalph.b2c.search.redirect.domain.SearchRedirect', -30),
  (-751, 'cn.globalph.cms.url.domain.URLHandler', -29), (-750, 'cn.globalph.cms.url.domain.URLHandler', -28),
  (-721, 'cn.globalph.openadmin.server.security.domain.AdminUser', -27),
  (-720, 'cn.globalph.openadmin.server.security.domain.AdminUser', -26),
  (-657, 'cn.globalph.cms.structure.domain.StructuredContentFieldTemplate', -201),
  (-655, 'cn.globalph.cms.structure.domain.StructuredContentItemCriteria', -25),
  (-654, 'cn.globalph.cms.structure.domain.StructuredContentItemCriteria', -24),
  (-653, 'cn.globalph.cms.structure.domain.StructuredContentType', -201),
  (-651, 'cn.globalph.cms.structure.domain.StructuredContent', -25),
  (-650, 'cn.globalph.cms.structure.domain.StructuredContent', -24),
  (-603, 'cn.globalph.cms.file.domain.StaticAssetFolder', -23),
  (-602, 'cn.globalph.cms.file.domain.StaticAssetFolder', -22), (-601, 'cn.globalph.cms.file.domain.StaticAsset', -23),
  (-600, 'cn.globalph.cms.file.domain.StaticAsset', -22), (-555, 'cn.globalph.cms.page.domain.PageItemCriteria', -21),
  (-554, 'cn.globalph.cms.page.domain.PageItemCriteria', -20), (-552, 'cn.globalph.cms.page.domain.PageTemplate', -200),
  (-551, 'cn.globalph.cms.page.domain.Page', -21), (-550, 'cn.globalph.cms.page.domain.Page', -20),
  (-514, 'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl', -19),
  (-513, 'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl', -18),
  (-512, 'cn.globalph.passport.domain.CustomerPhone', -19), (-511, 'cn.globalph.passport.domain.CustomerPhone', -18),
  (-510, 'cn.globalph.passport.domain.CustomerPayment', -19),
  (-509, 'cn.globalph.passport.domain.CustomerPayment', -18),
  (-508, 'cn.globalph.passport.domain.CustomerAddress', -19),
  (-507, 'cn.globalph.passport.domain.CustomerAddress', -18),
  (-506, 'cn.globalph.passport.domain.CustomerAttribute', -19),
  (-504, 'cn.globalph.passport.domain.CustomerAttribute', -18),
  (-503, 'cn.globalph.passport.domain.ChallengeQuestion', -19),
  (-502, 'cn.globalph.passport.domain.ChallengeQuestion', -18), (-501, 'cn.globalph.passport.domain.Customer', -19),
  (-500, 'cn.globalph.passport.domain.Customer', -18),
  (-461, 'cn.globalph.b2c.order.domain.BundleOrderItemFeePriceImpl', -17),
  (-460, 'cn.globalph.b2c.order.domain.BundleOrderItemFeePriceImpl', -16),
  (-459, 'cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl', -17),
  (-458, 'cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl', -16),
  (-457, 'cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustmentImpl', -17),
  (-456, 'cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustmentImpl', -16),
  (-455, 'cn.globalph.b2c.offer.domain.OrderItemAdjustment', -17),
  (-454, 'cn.globalph.b2c.offer.domain.OrderItemAdjustment', -16),
  (-453, 'cn.globalph.b2c.order.domain.DiscreteOrderItemFeePrice', -17),
  (-452, 'cn.globalph.b2c.order.domain.DiscreteOrderItemFeePrice', -16),
  (-451, 'cn.globalph.b2c.order.domain.OrderItem', -17), (-450, 'cn.globalph.b2c.order.domain.OrderItem', -16),
  (-407, 'cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl', -15),
  (-406, 'cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl', -14),
  (-405, 'cn.globalph.b2c.order.domain.FulfillmentGroupFeeImpl', -15),
  (-404, 'cn.globalph.b2c.order.domain.FulfillmentGroupFeeImpl', -14),
  (-403, 'cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment', -15),
  (-402, 'cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment', -14),
  (-401, 'cn.globalph.b2c.order.domain.FulfillmentGroup', -15),
  (-400, 'cn.globalph.b2c.order.domain.FulfillmentGroup', -14),
  (-370, 'cn.globalph.b2c.payment.domain.PaymentTransactionImpl', -13),
  (-369, 'cn.globalph.b2c.payment.domain.PaymentTransactionImpl', -12),
  (-368, 'org.broadleafcommerce.profile.core.domain.State', -13),
  (-367, 'org.broadleafcommerce.profile.core.domain.State', -12),
  (-366, 'cn.globalph.profile.core.domain.Country', -13), (-365, 'cn.globalph.profile.core.domain.Country', -12),
  (-361, 'cn.globalph.b2c.payment.domain.OrderPayment', -13),
  (-360, 'cn.globalph.b2c.payment.domain.OrderPayment', -12),
  (-356, 'cn.globalph.b2c.offer.domain.OrderAdjustment', -13),
  (-355, 'cn.globalph.b2c.offer.domain.OrderAdjustment', -12), (-351, 'cn.globalph.b2c.order.domain.Order', -13),
  (-350, 'cn.globalph.b2c.order.domain.Order', -12), (-307, 'cn.globalph.b2c.offer.domain.OfferTier', -11),
  (-306, 'cn.globalph.b2c.offer.domain.OfferTier', -10), (-305, 'cn.globalph.b2c.offer.domain.OfferCode', -11),
  (-304, 'cn.globalph.b2c.offer.domain.OfferCode', -10), (-303, 'cn.globalph.b2c.offer.domain.OfferItemCriteria', -11),
  (-302, 'cn.globalph.b2c.offer.domain.OfferItemCriteria', -10), (-301, 'cn.globalph.b2c.offer.domain.Offer', -11),
  (-300, 'cn.globalph.b2c.offer.domain.Offer', -10), (-251, 'cn.globalph.b2c.product.domain.Sku', -9),
  (-250, 'cn.globalph.b2c.product.domain.Sku', -8), (-205, 'cn.globalph.b2c.product.domain.ProductOptionXref', -7),
  (-204, 'cn.globalph.b2c.product.domain.ProductOptionXref', -6),
  (-203, 'cn.globalph.b2c.product.domain.ProductOptionValue', -7),
  (-202, 'cn.globalph.b2c.product.domain.ProductOptionValue', -6),
  (-201, 'cn.globalph.b2c.product.domain.ProductOption', -7),
  (-200, 'cn.globalph.b2c.product.domain.ProductOption', -6),
  (-107, 'cn.globalph.b2c.product.domain.SkuBundleItemImpl', -5),
  (-106, 'cn.globalph.b2c.product.domain.SkuBundleItemImpl', -4),
  (-105, 'cn.globalph.b2c.catalog.domain.UpSaleProductImpl', -5),
  (-104, 'cn.globalph.b2c.catalog.domain.UpSaleProductImpl', -4),
  (-103, 'cn.globalph.b2c.product.domain.ProductAttribute', -5),
  (-102, 'cn.globalph.b2c.product.domain.ProductAttribute', -4), (-101, 'cn.globalph.b2c.product.domain.Product', -5),
  (-100, 'cn.globalph.b2c.product.domain.Product', -4), (-14, 'cn.globalph.b2c.catalog.domain.UpSaleProductImpl', -3),
  (-13, 'cn.globalph.b2c.catalog.domain.UpSaleProductImpl', -2),
  (-12, 'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl', -3),
  (-11, 'cn.globalph.b2c.catalog.domain.CrossSaleProductImpl', -2),
  (-10, 'cn.globalph.b2c.catalog.domain.FeaturedProductImpl', -3),
  (-9, 'cn.globalph.b2c.catalog.domain.FeaturedProductImpl', -2),
  (-8, 'cn.globalph.b2c.catalog.domain.CategoryXrefImpl', -3),
  (-7, 'cn.globalph.b2c.catalog.domain.CategoryXrefImpl', -2),
  (-6, 'cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl', -3),
  (-5, 'cn.globalph.b2c.catalog.domain.CategoryProductXrefImpl', -2),
  (-4, 'cn.globalph.b2c.catalog.domain.CategoryAttribute', -3),
  (-3, 'cn.globalph.b2c.catalog.domain.CategoryAttribute', -2), (-2, 'cn.globalph.b2c.catalog.domain.Category', -3),
  (-1, 'cn.globalph.b2c.catalog.domain.Category', -2);

INSERT INTO `admin_permission_xref` (`CHILD_PERMISSION_ID`, `ADMIN_PERMISSION_ID`)
VALUES (-2, -100), (-4, -100), (-32, -100), (-3, -101), (-4, -101), (-32, -101), (-4, -102), (-6, -102), (-8, -102),
  (-34, -102), (-5, -103), (-6, -103), (-9, -103), (-34, -103), (-6, -104), (-32, -104), (-7, -105), (-32, -105),
  (-10, -106), (-11, -107), (-20, -108), (-200, -108), (-21, -109), (-200, -109), (-22, -110), (-23, -111), (-24, -112),
  (-201, -112), (-25, -113), (-201, -113), (-28, -114), (-29, -115), (-12, -116), (-16, -116), (-14, -116), (-10, -116),
  (-13, -117), (-16, -117), (-14, -117), (-10, -117), (-18, -118), (-19, -119), (-26, -120), (-46, -120), (-48, -120),
  (-27, -121), (-46, -120), (-48, -120), (-44, -122), (-45, -123), (-36, -126), (-37, -127), (-40, -130), (-41, -131),
  (-46, -140), (-48, -140), (-47, -141), (-48, -141), (-48, -150), (-49, -151);

INSERT INTO `admin_role` (`ADMIN_ROLE_ID`, `DESCRIPTION`, `NAME`)
VALUES (-7, '内容管理系统设计者', 'ROLE_CONTENT_DESIGNER'), (-6, '内容管理系统审批人', 'ROLE_CONTENT_APPROVER'),
  (-5, '内容管理系统编辑者', 'ROLE_CONTENT_EDITOR'), (-4, '用户和订单管理', 'ROLE_CUSTOMER_SERVICE_REP'),
  (-3, '促销管理员', 'ROLE_PROMOTION_MANAGER'), (-2, '商品管理员', 'ROLE_MERCHANDISE_MANAGER'),
  (-1, '超级管理员', 'ROLE_ADMIN');

INSERT INTO `admin_role_permission_xref` (`ADMIN_ROLE_ID`, `ADMIN_PERMISSION_ID`)
VALUES (-1, -207), (-1, -206), (-1, -205), (-1, -204), (-1, -203), (-1, -202), (-1, -141), (-5, -131), (-2, -131),
  (-1, -131), (-1, -127), (-1, -123), (-1, -121), (-4, -119), (-1, -119), (-4, -117), (-1, -117), (-1, -115),
  (-5, -113), (-1, -113), (-6, -111), (-5, -111), (-3, -111), (-2, -111), (-1, -111), (-6, -109), (-5, -109),
  (-1, -109), (-3, -107), (-1, -107), (-2, -105), (-1, -105), (-2, -103), (-1, -103), (-2, -101), (-1, -101);

INSERT INTO `admin_sandbox` (`SANDBOX_ID`, `AUTHOR`, `COLOR`, `DESCRIPTION`, `GO_LIVE_DATE`, `SANDBOX_NAME`, `SANDBOX_TYPE`, `PARENT_SANDBOX_ID`)
VALUES (1, NULL, '#0B9098', NULL, NULL, 'Default', 'DEFAULT', NULL), (2, -1, NULL, NULL, NULL, 'Default', 'USER', 1),
  (51, -2, NULL, NULL, NULL, 'Default', 'USER', 1), (52, -3, NULL, NULL, NULL, 'Default', 'USER', 1),
  (53, -4, NULL, NULL, NULL, 'Default', 'USER', 1), (54, -5, NULL, NULL, NULL, 'Default', 'USER', 1),
  (55, -6, NULL, NULL, NULL, 'Default', 'USER', 1);

INSERT INTO `admin_sandbox_mgmt` (`SANDBOX_MGMT_ID`, `SANDBOX_ID`)
VALUES (1, 1), (2, 2), (51, 51), (52, 52), (53, 53), (54, 54), (55, 55);

INSERT INTO `admin_sec_perm_xref`
VALUES (-1, -100), (-1, -101), (-2, -102), (-2, -103), (-3, -104), (-3, -105), (-4, -106), (-4, -107), (-5, -108),
  (-5, -109), (-6, -110), (-6, -111), (-7, -112), (-7, -113), (-8, -114), (-8, -115), (-9, -116), (-9, -117),
  (-10, -118), (-10, -119), (-11, -120), (-11, -121), (-12, -140), (-12, -141), (-13, -126), (-13, -127), (-15, -122),
  (-15, -123), (-16, -122), (-16, -123), (-17, -150), (-17, -151), (3, -208), (3, -209);

INSERT INTO `admin_section` VALUES
  (-17, 'cn.globalph.openadmin.server.security.domain.AdminPermission', NULL, 3000, 'Permission Management',
   'PermissionManagement', '/permission-management', NULL, -4),
  (-16, 'cn.globalph.common.config.domain.SystemProperty', NULL, 2000, 'System Properties', 'SystemProperties', '/system-properties', NULL, -5),
  (-15, 'cn.globalph.common.config.domain.SystemProperty', NULL, 3000, 'System Property Management', 'SystemPropertyManagement', '/system-properties-management', NULL, -5),
  (-13, 'cn.globalph.common.config.domain.ModuleConfiguration', NULL, 1000, 'Configuration Management', 'ConfigurationManagement', '/configuration-management', NULL, -5),
  (-12, 'cn.globalph.openadmin.server.security.domain.AdminRole', NULL, 2000, 'Role Management', 'RoleManagement', '/role-management', NULL, -4),
  (-11, 'cn.globalph.openadmin.server.security.domain.AdminUser', NULL, 1000, 'User Management', 'UserManagement', '/user-management', NULL, -4),
  (-10, 'cn.globalph.passport.domain.Customer', NULL, 1000, 'Customer', 'Customer', '/customer', NULL, -3),
  (-9, 'cn.globalph.b2c.order.domain.Order', NULL, 2000, 'Order', 'Order', '/order', NULL, -3),
  (-8, 'cn.globalph.cms.url.domain.URLHandler', NULL, 4000, 'Redirect URL', 'RedirectURL', '/redirect-url', NULL, -2),
  (-7, 'cn.globalph.cms.structure.domain.StructuredContent', NULL, 2000, 'Structured Content', 'StructuredContent', '/structured-content', NULL, -2),
  (-6, 'cn.globalph.cms.file.domain.StaticAsset', NULL, 3000, 'Assets', 'Assets', '/assets', NULL, -2),
  (-5, 'cn.globalph.cms.page.domain.Page', NULL, 1000, 'Pages', 'Pages', '/pages', NULL, -2),
  (-4, 'cn.globalph.b2c.offer.domain.Offer', NULL, 4000, 'Offer', 'Offer', '/offer', NULL, -1),
  (-3, 'cn.globalph.b2c.product.domain.ProductOption', NULL, 3000, 'Product Options', 'ProductOptions',
   '/product-options', NULL, -1),
  (-2, 'cn.globalph.b2c.product.domain.Product', NULL, 2000, 'Product', 'Product', '/product', NULL, -1),
  (-1, 'cn.globalph.b2c.catalog.domain.Category', NULL, 1000, 'Category', 'Category', '/category', NULL, -1),
  (1, 'cn.globalph.profile.core.domain.Community', NULL, 5000, 'Communities', 'Communities', '/community', NULL, 1),
  (2, 'cn.globalph.profile.core.domain.NetNode', NULL, 6000, 'Net Nodes', 'NetNodes', '/net-node', NULL, 1),
  (3, 'cn.globalph.b2c.product.domain.Provider', NULL, 2000, 'Provider', 'Provider Management', '/provider', NULL, 2);

INSERT INTO `admin_user` VALUES (-6, 1, 'cms_approver@yourdomain.com', 'cmsApprover', '内容管理系统审批人', 'admin{-6}', NULL),
  (-5, 1, 'cms_edit@yourdomain.com', 'cmsEditor', '内容管理系统编辑者', 'admin{-5}', NULL),
  (-4, 1, 'csr@yourdomain.com', 'csr', '用户和订单管理', 'admin{-4}', NULL),
  (-3, 1, 'promo@yourdomain.com', 'promo', '促销管理员', 'admin{-3}', NULL),
  (-2, 1, 'merchandise@yourdomain.com', 'merchandise', '商品管理员', 'admin{-2}', NULL),
  (-1, 1, 'admin@yourdomain.com', 'admin', '超级管理员', 'admin{-1}', NULL);

INSERT INTO `admin_user_role_xref` VALUES (-6, -6), (-5, -5), (-4, -4), (-3, -3), (-2, -2), (-1, -1);

INSERT INTO `cms_fld_define`
VALUES (-2, 0, '*', 1, 'BOOLEAN', 'Plain Text', 0, NULL, 'plainText', NULL, NULL, 0, NULL, NULL, NULL, -3),
  (-1, 0, '*', 0, 'HTML', 'File Contents', 0, NULL, 'body', NULL, NULL, 0, NULL, NULL, NULL, -3),
  (2, 0, '*', 1, 'HTML', 'Body', 0, NULL, 'body', NULL, NULL, 0, NULL, NULL, NULL, 1),
  (3, 0, '*', 0, 'STRING', 'Title', 0, NULL, 'title', NULL, NULL, 0, NULL, NULL, NULL, 1),
  (7, 0, '*', 0, 'STRING', 'Image URL', 0, 150, 'imageUrl', NULL, NULL, 0, NULL, NULL, NULL, 4),
  (8, 0, '*', 1, 'STRING', 'Target URL', 0, 150, 'targetUrl', NULL, NULL, 0, NULL, NULL, NULL, 4),
  (9, 0, '*', 0, 'STRING', 'Message Text', 0, 150, 'messageText', NULL, NULL, 0, NULL, NULL, NULL, 6),
  (10, 0, '*', 0, 'HTML', 'HTML Content', 0, NULL, 'htmlContent', NULL, NULL, 0, NULL, NULL, NULL, 5);

INSERT INTO `cms_fld_group`
VALUES (-3, 0, 'None'), (1, 0, 'Content'), (4, 0, 'Ad Fields'), (5, 0, 'HTML Fields'), (6, 0, 'Message Fields');

INSERT INTO `cms_img_static_asset`
VALUES (280, 940, 100150), (280, 940, 100151), (280, 940, 100200), (320, 240, 100250), (180, 180, 100251),
  (1182, 1210, 100252);

INSERT INTO `cms_page` VALUES (1, NULL, NULL, '2015-05-25 18:27:55', -6, '关于我们', NULL, '/about_us', NULL, NULL, 1),
  (2, NULL, NULL, '2014-10-07 15:09:20', -1, '商品分类', NULL, '/faq', NULL, NULL, 1),
  (3, NULL, NULL, NULL, NULL, 'New to Hot Sauce', NULL, '/new-to-hot-sauce', NULL, NULL, 1);

INSERT INTO `cms_page_fld` VALUES (1, NULL, NULL, NULL, NULL, 'body', NULL, 'test content'),
  (2, NULL, NULL, '2014-10-18 15:21:09', -1, 'title', NULL, NULL), (3, NULL, NULL, NULL, NULL, 'body', NULL,
                                                                    '<h2 style=\"text-align:center;\">This is an example of a content-managed page.</h2><h4 style=\"text-align:center;\"><a href=\"http://www.broadleafcommerce.com/features/content\">Click Here</a> to see more about Content Management in Broadleaf.</h4>'),
  (4, NULL, NULL, NULL, NULL, 'body', NULL,
   '<h2 style=\"text-align:center;\">This is an example of a content-managed page.</h2>'),
  (950, -1, '2014-10-07 15:09:20', '2014-10-07 15:09:20', NULL, 'title', NULL, NULL);

INSERT INTO `cms_page_fld_map`
VALUES (1, 1, 'body'), (1, 2, 'title'), (2, 3, 'body'), (3, 4, 'body'), (2, 950, 'title');

INSERT INTO `cms_page_tmplt` VALUES (-3, 'Outputs the body field.', 'NONE', 'NONE'),
  (1, 'Provides a basic layout with header and footer surrounding the content and title.', 'Default Template',
   '/content/default'),
  (2, 'This template provides a basic layout with header and footer surrounding the content and title.',
   'Basic Spanish Template', '/content/default');
INSERT INTO `cms_pgtmplt_fldgrp_xref` VALUES (-3, -3, 0), (1, 1, 0), (2, 1, 0);

INSERT INTO `cms_sc` VALUES (130, 1, '2014-06-08 14:56:21', '2015-05-13 19:39:57', -1, 'home page bar', 0, 3, 3),
  (950, -1, '2014-10-28 23:53:39', '2015-05-13 19:39:41', -1, 'home page bar', 0, 2, 3),
  (951, -1, '2014-10-28 23:55:34', '2015-05-13 19:39:12', -1, 'home page bar', 0, 1, 3),
  (1000, -1, '2015-05-13 17:51:32', '2015-05-13 17:51:32', NULL, 'hph banners', 0, 1, 1),
  (1050, -1, '2015-05-13 18:47:35', '2015-05-13 18:47:35', NULL, 'lph banners', 0, 1, 1),
  (1051, -1, '2015-05-13 18:48:35', '2015-05-13 18:48:35', NULL, 'gph banners', 0, 1, 1);

INSERT INTO `cms_sc_fld`
VALUES (11, 1, '2014-06-08 14:56:21', '2015-05-13 19:39:57', -1, 'messageText', NULL, '果品荟特选商品'),
  (950, -1, '2014-10-28 23:54:12', '2015-05-13 19:39:41', -1, 'messageText', NULL, '粮品荟特选商品'),
  (951, -1, '2014-10-28 23:55:57', '2015-05-13 19:38:38', -1, 'messageText', NULL, '海品荟特选商品'),
  (1000, -1, '2015-05-13 17:51:47', '2015-05-13 18:39:45', -1, 'imageUrl', NULL, '/cmsstatic/hph_banner_1.jpg'),
  (1001, -1, '2015-05-13 17:51:47', '2015-05-13 17:51:47', NULL, 'targetUrl', NULL, NULL),
  (1050, -1, '2015-05-13 18:47:57', '2015-05-13 18:47:57', NULL, 'imageUrl', NULL, '/cmsstatic/lph_banner_1.jpg'),
  (1051, -1, '2015-05-13 18:47:57', '2015-05-13 18:47:57', NULL, 'targetUrl', NULL, NULL),
  (1052, -1, '2015-05-13 18:48:57', '2015-05-13 18:58:58', -1, 'imageUrl', NULL, '/cmsstatic/gph_banner_1.jpg'),
  (1053, -1, '2015-05-13 18:48:57', '2015-05-13 18:48:57', NULL, 'targetUrl', NULL, NULL);
INSERT INTO `cms_sc_fld_map`
VALUES (130, 11, 'messageText'), (950, 950, 'messageText'), (951, 951, 'messageText'), (1000, 1000, 'imageUrl'),
  (1000, 1001, 'targetUrl'), (1050, 1050, 'imageUrl'), (1050, 1051, 'targetUrl'), (1051, 1052, 'imageUrl'),
  (1051, 1053, 'targetUrl');
INSERT INTO `cms_sc_fld_tmplt` VALUES (1, 'Ad Template'), (2, 'HTML Template'), (3, 'Message Template');
INSERT INTO `cms_sc_fldgrp_xref` VALUES (1, 4, 0), (2, 5, 0), (3, 6, 0);

INSERT INTO `cms_sc_type` VALUES (1, NULL, 'Homepage Banner Ad', 1), (2, NULL, 'Homepage Middle Promo Snippet', 2),
  (3, NULL, 'Homepage Featured Products Title', 3), (4, NULL, 'Right Hand Side Banner Ad', 1);

INSERT INTO `cms_static_asset` VALUES
  (100150, NULL, -1, '2015-05-13 17:56:05', '2015-05-13 17:56:05', NULL, 'jpg', 81205, '/lph_banner_1.jpg',
   'image/jpeg', 'lph_banner_1.jpg', 'FILESYSTEM', NULL),
  (100151, NULL, -1, '2015-05-13 17:58:06', '2015-05-13 17:58:06', NULL, 'jpg', 164871, '/hph_banner_1.jpg',
   'image/jpeg', 'hph_banner_1.jpg', 'FILESYSTEM', NULL),
  (100200, NULL, -1, '2015-05-13 19:04:13', '2015-05-13 19:04:13', NULL, 'jpg', 103588, '/gph_banner_1.jpg',
   'image/jpeg', 'gph_banner_1.jpg', 'FILESYSTEM', NULL);

INSERT INTO `cms_url_handler` VALUES (1, '/googlePerm', 'http://www.google.com', 'REDIRECT_PERM'),
  (2, '/googleTemp', 'http://www.google.com', 'REDIRECT_TEMP'),
  (3, '/insanity', '/hot-sauces/insanity_sauce', 'FORWARD'),
  (4, '/jalepeno', '/hot-sauces/hurtin_jalepeno_hot_sauce', 'REDIRECT_TEMP');

INSERT INTO `nph_bundle_sku` VALUES (10250, 0, 0, 0, 99);
INSERT INTO `nph_bundle_sku_item`
VALUES (-103, NULL, 1, 993, 2), (-102, NULL, 1, 993, 1), (-101, NULL, 1, 992, 2), (-100, NULL, 1, 992, 1),
  (1, '20.00000', 2, 10250, 2), (2, '2.00000', 2, 10250, 3);

INSERT INTO `nph_cat_search_facet_xref`
VALUES (1, '1.00', 2002, 1), (3, '3.00', 1, 3), (4, '1.00', 2003, 4), (5, '2.00', 2002, 5), (6, '3.00', 2002, 6);

INSERT INTO `nph_cat_site_map_gen_cfg` VALUES (1, 1, -4, 2);

INSERT INTO `nph_category` (`CATEGORY_ID`, `ACTIVE_END_DATE`, `ACTIVE_START_DATE`, `ARCHIVED`, `DESCRIPTION`, `DISPLAY_TEMPLATE`, `FULFILLMENT_TYPE`, `INVENTORY_TYPE`, `LONG_DESCRIPTION`, `NAME`, `URL`, `URL_KEY`, `DEFAULT_PARENT_CATEGORY_ID`)
VALUES (1, NULL, '2014-06-08 14:55:53', NULL, 'Root', NULL, NULL, NULL, NULL, '根栏目', NULL, NULL, NULL),
  (2, NULL, '2014-06-08 14:55:53', 'N', 'Primary Nav', NULL, NULL, 'ALWAYS_AVAILABLE', NULL, '首页导航', NULL, NULL, 1),
  (2001, NULL, '2014-06-08 14:55:53', 'N', 'Home', 'layout/home', NULL, NULL, NULL, '首页', '/', NULL, 2),
  (2002, NULL, '2014-06-08 14:55:53', 'N', 'Hot Sauces', 'layout/lphhome', NULL, NULL, NULL, '粮品荟', '/food', NULL, 2),
  (2003, NULL, '2014-06-08 14:55:53', 'N', 'Merchandise', 'layout/hphhome', NULL, NULL, NULL, '海品荟', '/sea', NULL, 2),
  (2004, NULL, '2014-06-08 14:55:53', 'N', 'Clearance', 'layout/gphhome', NULL, NULL, NULL, '果品荟', '/guo', NULL, 2),
  (10050, NULL, '2014-10-18 00:00:00', 'Y', NULL, NULL, NULL, NULL, NULL, '关于果品惠', '/about_us', NULL, NULL),
  (10100, NULL, '2014-10-30 00:00:00', 'N', NULL, NULL, 'PHYSICAL_PICKUP', NULL, NULL, '果品荟特选商品', NULL, NULL, 2001),
  (10101, NULL, '2014-10-30 00:00:00', 'N', NULL, NULL, NULL, NULL, NULL, '海品荟特选商品', NULL, NULL, 2001),
  (10102, NULL, '2014-10-30 00:00:00', 'N', NULL, NULL, NULL, NULL, NULL, '粮品荟特选商品', NULL, NULL, 2001);

INSERT INTO `nph_category_xref` (`CATEGORY_XREF_ID`, `DISPLAY_ORDER`, `CATEGORY_ID`, `SUB_CATEGORY_ID`)
VALUES (1, '1.000000', 1, 2), (2, '1.000000', 2, 2001), (3, '2.000000', 2, 2002), (4, '3.000000', 2, 2003),
  (5, '4.000000', 2, 2004), (1150, NULL, 2001, 10100), (1151, NULL, 2001, 10101), (1152, NULL, 2001, 10102);

INSERT INTO `nph_challenge_question`
VALUES (1, '您父亲叫什么?'), (2, '您小学的校名是什么?'),
  (3, '你最好的朋友叫什么?'), (4, '您最喜欢的一首歌是什么?'),
  (5, '您的配偶叫什么'), (6, 'What school did you attend for sixth grade?'),
  (7, 'Where does your nearest sibling live?'), (8, 'What is your youngest brother\'s birthday?'),
  (9, 'In what city or town was your first job?');

INSERT INTO `nph_offer_item_criteria` VALUES
  (1, 'MVEL.eval(\"toUpperCase()\",discreteOrderItem.category.name)==MVEL.eval(\"toUpperCase()\",\"merchandise\")', 1),
  (950, 'orderItem.?name!=null', 1), (1000, 'orderItem.?name!=null', 1), (1050, 'orderItem.?name!=null', 1),
  (1051, 'orderItem.?price.getAmount()>1', 1);
INSERT INTO `nph_offer_rule` VALUES (3, 'order.?subTotal.getAmount()>100');

INSERT INTO `nph_offer_rule_map` VALUES (1000, 3, 'ORDER');

INSERT INTO `nph_role` VALUES (1, 'ROLE_USER');

INSERT INTO `nph_search_facet`
VALUES (1, 1, '产地', NULL, 0, 0, 1), (2, 1, 'Heat Range', NULL, 0, 0, 2), (3, 1, '价格', NULL, 1, 1, 3),
  (4, 1, '颜色', NULL, 0, 1, 8), (5, 1, '品种', NULL, 2, 0, 9), (6, 1, '等级', NULL, 2, 0, 10);
INSERT INTO `nph_search_facet_range`
VALUES (1, '5.00000', '0.00000', 3), (2, '10.00000', '5.00000', 3), (3, '15.00000', '10.00000', 3),
  (4, NULL, '15.00000', 3);
INSERT INTO `nph_search_field` VALUES (1, 'mfg', 'PRODUCT', 's', 'manufacturer', 1, NULL),
  (2, 'heatRange', 'PRODUCT', 'i', 'productAttributes.heatRange', 0, NULL),
  (3, 'price', 'SKU', 'p', 'retailPrice', 0, NULL), (4, 'name', 'SKU', 's', 'name', 1, 1),
  (5, 'model', 'PRODUCT', 's', 'model', 1, NULL), (6, 'desc', 'SKU', NULL, 'description', 1, 1),
  (7, 'ldesc', 'SKU', NULL, 'longDescription', 1, 1), (8, 'color', 'PRODUCT', 's', 'productAttributes.color', 1, NULL),
  (9, 'breed', 'PRODUCT', 's', 'breed', 1, NULL), (10, 'grade', 'PRODUCT', 's', 'grade', 1, NULL);
INSERT INTO `nph_search_field_types` VALUES (1, 't'), (4, 't'), (5, 't'), (6, 't'), (7, 't');

INSERT INTO `nph_search_intercept` VALUES (1, NULL, NULL, 1, 'insanity', '/hot-sauces/insanity_sauce'),
  (2, '1999-10-15 21:28:36', '1992-10-15 14:28:36', -10, 'new york', '/search?q=pace picante');

INSERT INTO `nph_site_map_cfg` VALUES (NULL, NULL, NULL, NULL, -1);
INSERT INTO `nph_site_map_gen_cfg`
VALUES (-4, 'HOURLY', 0, 'CATEGORY', '0.5', -1), (-3, 'HOURLY', 0, 'PAGE', '0.5', -1),
  (-2, 'HOURLY', 0, 'PRODUCT', '0.5', -1), (-1, 'HOURLY', 0, 'CUSTOM', '0.5', -1);

INSERT INTO `nph_site_map_url_entry`
VALUES (-8, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/8', '0.5', -1),
  (-7, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/7', '0.5', -1),
  (-6, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/6', '0.5', -1),
  (-5, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/5', '0.5', -1),
  (-4, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/4', '0.5', -1),
  (-3, 'HOURLY', '2014-06-08 14:56:32', 'http://www.heatclinic.com/3', '0.5', -1),
  (-2, 'HOURLY', '2014-06-08 14:56:31', 'http://www.heatclinic.com/2', '0.5', -1),
  (-1, 'HOURLY', '2014-06-08 14:56:31', 'http://www.heatclinic.com/1', '0.5', -1);

INSERT INTO `nph_tar_crit_offer_xref` VALUES (1000, 1050);

INSERT INTO `sequence_generator`
VALUES ('AddressImpl', 1), ('CategoryExcludedSearchFacetImpl', 1000), ('CategoryImpl', 10250),
  ('CategoryProductImpl', 1000), ('CategoryProductXrefImpl', 1700), ('CategorySearchFacetImpl', 1000),
  ('CategoryXrefImpl', 1250), ('ChallengeQuestionImpl', 1000), ('CrossSaleProductImpl', 101),
  ('CustomerAddressImpl', 101), ('CustomerRoleImpl', 5251), ('EmailTrackingImpl', 4201), ('FeaturedProductImpl', 1000),
  ('FieldDefinitionImpl', 1000), ('FieldEnumerationImpl', 1000), ('FieldEnumerationItemImpl', 1000),
  ('FieldGroupImpl', 1000), ('FieldImpl', 1000), ('FulfillmentGroupImpl', 7701), ('FulfillmentGroupItemImpl', 8101),
  ('FulfillmentOptionImpl', 1000), ('MediaImpl', 100700), ('OfferAuditImpl', 451), ('OfferCodeImpl', 1050),
  ('OfferImpl', 1100), ('OfferItemCriteriaImpl', 1150), ('OfferRuleImpl', 101), ('OrderAdjustmentImpl', 701),
  ('OrderImpl', 1), ('OrderItemAttributeImpl', 101), ('OrderItemImpl', 8351),
  ('OrderItemPriceDetailAdjustmentImpl', 901), ('OrderItemPriceDetailImpl', 8101), ('OrderPaymentImpl', 3401),
  ('PageFieldImpl', 1050), ('PageImpl', 1000), ('PageTemplateImpl', 1000), ('PaymentTransactionImpl', 3201),
  ('PersonalMessageImpl', 201), ('PhoneImpl', 151), ('ProductAttributeImpl', 1050), ('ProductImpl', 10300),
  ('ProductOptionImpl', 1100), ('ProductOptionValueImpl', 1150), ('ProductOptionXrefImpl', 1550), ('ProviderImpl', 101),
  ('RatingDetailImpl', 201), ('RatingSummaryImpl', 201), ('ReviewDetailImpl', 201), ('RoleImpl', 1000),
  ('SandBoxImpl', 151), ('SandBoxManagementImpl', 151), ('SearchFacetImpl', 1000), ('SearchFacetRangeImpl', 1000),
  ('SearchInterceptImpl', 1000), ('SkuBundleItemImpl', 101), ('SkuImpl', 1), ('StaticAssetImpl', 100350),
  ('StructuredContentFieldImpl', 1150), ('StructuredContentImpl', 1150), ('StructuredContentRuleImpl', 1000),
  ('StructuredContentTypeImpl', 1000), ('URLHandlerImpl', 1000);

/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;