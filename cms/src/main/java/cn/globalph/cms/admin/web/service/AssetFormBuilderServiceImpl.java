package cn.globalph.cms.admin.web.service;

import cn.globalph.cms.file.service.StaticAssetService;
import cn.globalph.cms.file.service.operation.StaticMapNamedOperationComponent;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.form.component.ListGridRecord;
import cn.globalph.openadmin.web.form.component.MediaField;
import cn.globalph.openadmin.web.form.entity.Field;
import cn.globalph.openadmin.web.service.DynamicFormBuilder;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("blAssetFormBuilderService")
public class AssetFormBuilderServiceImpl implements AssetFormBuilderService {
    
    @Resource(name = "blFormBuilderService")
    protected DynamicFormBuilder formBuilderService;
    
    @Resource(name = "cmsStaticAssetService")
    protected StaticAssetService staticAssetService;
    
    @Resource(name = "blStaticMapNamedOperationComponent")
    protected StaticMapNamedOperationComponent operationMap;
 
    @Override
    public void addImageThumbnailField(ListGrid listGrid, String urlField) {
        listGrid.getHeaderFields().add(new Field()
            .withName("thumbnail")
            .withFriendlyName("Asset_thumbnail")
            .withFieldType(SupportedFieldType.STRING.toString())
            .withOrder(Integer.MIN_VALUE)
            .withColumnWidth("50px")
            .withFilterSortDisabled(true));
        
        for (ListGridRecord record : listGrid.getRecords()) {
            // Get the value of the URL
            String imageUrl = record.getField(urlField).getValue();
            
            // Prepend the static asset url prefix if necessary
            String staticAssetUrlPrefix = staticAssetService.getStaticAssetUrlPrefix();
            if (staticAssetUrlPrefix != null && !staticAssetUrlPrefix.startsWith("/")) {
                staticAssetUrlPrefix = "/" + staticAssetUrlPrefix;
            }
            if (staticAssetUrlPrefix == null) {
                staticAssetUrlPrefix = "";
            } else {
                imageUrl = staticAssetUrlPrefix + imageUrl;
            }
            
            MediaField mf = (MediaField) new MediaField()
                .withName("thumbnail")
                .withFriendlyName("Asset_thumbnail")
                .withFieldType(SupportedFieldType.IMAGE.toString())
                .withOrder(Integer.MIN_VALUE)
                .withValue(imageUrl);
            
            // Add a hidden field for the large thumbnail path
            record.getHiddenFields().add(new Field()
                .withName("cmsUrlPrefix")
                .withValue(staticAssetUrlPrefix));
            
            record.getHiddenFields().add(new Field()
                .withName("thumbnailKey")
                .withValue("?smallAdminThumbnail"));
            
            record.getHiddenFields().add(new Field()
                .withName("servletContext")
                .withValue(WebRequestContext.getWebRequestContext().getRequest().getContextPath()));
            
            // Set the height value on this field
            mf.setHeight(operationMap.getNamedOperations().get("smallAdminThumbnail").get("resize-height-amount"));
            record.getFields().add(mf);
            
            // Since we've added a new field, we need to clear the cached map to ensure it will display
            record.clearFieldMap();
        }
    }
}
