package cn.globalph.cms.admin.server.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.RequestDTOImpl;
import cn.globalph.openadmin.dto.PersistencePackage;

import org.springframework.stereotype.Component;

@Component("blRequestDTOCustomPersistenceHandler")
public class RequestDTOCustomPersistenceHandler extends TimeDTOCustomPersistenceHandler {

    private static final Log LOG = LogFactory.getLog(RequestDTOCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return RequestDTOImpl.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }
}
