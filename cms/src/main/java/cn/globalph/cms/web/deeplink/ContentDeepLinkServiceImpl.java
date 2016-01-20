package cn.globalph.cms.web.deeplink;

import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.common.structure.dto.StructuredContentDTO;
import cn.globalph.common.web.deeplink.DeepLink;
import cn.globalph.common.web.deeplink.DeepLinkService;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides deep links for {@link StructuredContent} items.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class ContentDeepLinkServiceImpl extends DeepLinkService<StructuredContentDTO> {

    protected String structuredContentAdminPath;

    @Override
    protected List<DeepLink> getLinksInternal(StructuredContentDTO item) {
        List<DeepLink> links = new ArrayList<DeepLink>();

        links.add(new DeepLink()
            .withAdminBaseUrl(getAdminBaseUrl())
            .withUrlFragment(structuredContentAdminPath + item.getId())
            .withDisplayText("Edit")
            .withSourceObject(item));

        return links;
    }

    public String getStructuredContentAdminPath() {
        return structuredContentAdminPath;
    }

    public void setStructuredContentAdminPath(String structuredContentAdminPath) {
        this.structuredContentAdminPath = structuredContentAdminPath;
    }

}
