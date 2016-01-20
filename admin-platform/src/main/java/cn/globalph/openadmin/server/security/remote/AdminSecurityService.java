package cn.globalph.openadmin.server.security.remote;

import cn.globalph.common.exception.ServiceException;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminSecurityService {

    public AdminUser getAdminUser() throws ServiceException;
    
}
