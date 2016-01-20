package cn.globalph.openadmin.server.service;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.service.persistence.PersistenceResponse;

import org.springframework.security.access.annotation.Secured;

/**
 * 通用数据访问层
 * @felix.wu
 *
 */
public interface DynamicEntityService {
    
    @Secured("PERMISSION_OTHER_DEFAULT")
    public PersistenceResponse inspect(PersistencePackage persistencePackage) throws ServiceException;
      
     /**
      * 查询
    * @param persistencePackage
    * @param cto 查询条件对象
    * @return
    * @throws ServiceException
      */
    @Secured("PERMISSION_OTHER_DEFAULT")
    public PersistenceResponse fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;
    
    /**
     * 增加
    * @param persistencePackage
    * @return
    * @throws ServiceException
     */
    @Secured("PERMISSION_OTHER_DEFAULT")
    public PersistenceResponse add(PersistencePackage persistencePackage) throws ServiceException;
    
     /**
      * 修改
    * @param persistencePackage
    * @return
    * @throws ServiceException
      */
    @Secured("PERMISSION_OTHER_DEFAULT")
    public PersistenceResponse update(PersistencePackage persistencePackage) throws ServiceException;
     
     /**
      * 删除
    * @param persistencePackage
    * @return
    * @throws ServiceException
      */
    @Secured("PERMISSION_OTHER_DEFAULT")
    public PersistenceResponse remove(PersistencePackage persistencePackage) throws ServiceException;
    
}
