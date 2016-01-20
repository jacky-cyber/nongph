package cn.globalph.common.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * The MetadataNamingStrategy provided by Spring does not allow the usage of JDK dynamic proxies. However, several
 * of our services are AOP proxied for the sake of transactions, and the default behavior is to use JDK dynamic proxies for this.
 * It is possible to cause Spring to use CGLIB proxies instead via configuration, but this causes problems when it is desireable
 * or necessary to use constructor injection for the service definition, since CGLIB proxies require a default, no argument
 * constructor.
 * 
 * This class enhances the behavior of the Spring implementation to retrieve the rootId object inside the proxy for the sake of
 * metadata retrieval, thereby working around these shortcomings.
 * 
 * @felix.wu
 *
 */
public class MetadataNamingStrategy extends org.springframework.jmx.export.naming.MetadataNamingStrategy {

    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        return super.getObjectName(managedBean, beanKey);
    }

}
