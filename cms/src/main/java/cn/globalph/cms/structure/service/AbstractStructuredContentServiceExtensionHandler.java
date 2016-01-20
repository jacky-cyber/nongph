package cn.globalph.cms.structure.service;

import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.structure.dto.StructuredContentDTO;

import java.util.List;

/**
 * Extension handler for the {@link StructuredContentService}
 *
 * @author bpolster
 */
public class AbstractStructuredContentServiceExtensionHandler extends AbstractExtensionHandler
        implements StructuredContentServiceExtensionHandler {

    public ExtensionResultStatusType populateAdditionalStructuredContentFields(StructuredContent sc,
            StructuredContentDTO dto, boolean secure) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }


    public ExtensionResultStatusType modifyStructuredContentDtoList(List<StructuredContentDTO> structuredContentList,
            ExtensionResultHolder resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
