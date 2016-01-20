package cn.globalph.cms.file.service.operation;

import java.util.Map;

public interface NamedOperationManager {

    Map<String, String> applyNamedParameters(Map<String, String> parameterMap);

}
