package cn.globalph.common.jmx;

import javax.management.Descriptor;
import javax.management.JMException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

/**
 * The MetadataMBeanInfoAssembler provided by Spring does not allow the usage of JDK dynamic proxies. However, several
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
public class MetadataMBeanInfoAssembler extends org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler {

    protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {
        //do nothing
    }

    protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        return super.getNotificationInfo(managedBean, beanKey);
    }

    protected void populateMBeanDescriptor(Descriptor desc, Object managedBean, String beanKey) {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        super.populateMBeanDescriptor(desc, managedBean, beanKey);
    }

    protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey) throws JMException {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        return super.getAttributeInfo(managedBean, beanKey);
    }

    protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey) {
        managedBean = AspectUtil.exposeRootBean(managedBean);
        return super.getOperationInfo(managedBean, beanKey);
    }
}
