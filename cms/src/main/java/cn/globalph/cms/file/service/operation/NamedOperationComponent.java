package cn.globalph.cms.file.service.operation;

import java.util.Map;

/**
 * @author felix.wu
 */
public interface NamedOperationComponent {

    Map<String, String> setOperationValues(Map<String, String> originalParameters);

}
