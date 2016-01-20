package cn.globalph.openadmin.server.service.artifact;

import cn.globalph.openadmin.server.service.artifact.image.Operation;

import java.io.InputStream;
import java.util.Map;

public interface OperationBuilder {

    public Operation buildOperation(Map<String, String> parameterMap, InputStream artifactStream, String mimeType);

}
