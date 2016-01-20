package cn.globalph.openadmin.web.rulebuilder.service;

import java.util.List;

/**
 * Factory class that returns the appropriate RuleBuilderFieldService
 * given the service name. The services are injected into the factory defined in applicationContext-servlet-open-admin.xml
 * @see cn.globalph.openadmin.web.rulebuilder.service.RuleBuilderFieldService
 *
 * @author Jeff Fischer
 */
public interface RuleBuilderFieldServiceFactory {

    RuleBuilderFieldService createInstance(String name);

    List<RuleBuilderFieldService> getFieldServices();

    void setFieldServices(List<RuleBuilderFieldService> fieldServices);
}
