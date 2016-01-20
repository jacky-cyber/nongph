
package cn.globalph.openadmin.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.server.service.EntityAdminService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @see SearchFieldResolver
 * @author Andre Azzolini (apazzolini)
 */
@Service("blSearchFieldResolver")
public class SearchFieldResolverImpl implements SearchFieldResolver {
    protected static final Log LOG = LogFactory.getLog(SearchFieldResolverImpl.class);

    @Resource(name = "blAdminEntityService")
    protected EntityAdminService service;
    
    @Override
    public String resolveField(String className) throws ServiceException {
        PersistencePackageRequest ppr = PersistencePackageRequest.standard()
                .withRootEntityClassname(className);
        ClassMetadata md = service.getPersistenceResponse(ppr).getDynamicResultSet().getClassMetaData();
        
        if (md.getPMap().containsKey("name")) {
            return "name";
        }

        if (md.getPMap().containsKey("friendlyName")) {
            return "friendlyName";
        }

        if (md.getPMap().containsKey("templateName")) {
            return "templateName";
        }
        
        return "id";
    }


}
