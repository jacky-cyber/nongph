package cn.globalph.openadmin.server.security.remote;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.security.domain.AdminUser;

/**
 * @author Jeff Fischer
 */
public interface SecurityVerifier {

    AdminUser getPersistentAdminUser();

    void securityCheck(String ceilingEntityFullyQualifiedName, EntityOperationType operationType) throws ServiceException;

    void securityCheck(PersistencePackage persistencePackage, EntityOperationType operationType) throws ServiceException;

}
