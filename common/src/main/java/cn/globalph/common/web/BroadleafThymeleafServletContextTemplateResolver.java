package cn.globalph.common.web;

import cn.globalph.common.site.domain.Theme;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.util.Validate;

/**
 * Overrides the Thymeleaf ContextTemplateResolver and appends the cn.globalph.common.web.Theme path to the url
 * if it exists.
 */
public class BroadleafThymeleafServletContextTemplateResolver extends ServletContextTemplateResolver {    
    
    protected String templateFolder = "";

    @Override
    protected String computeResourceName(final TemplateProcessingParameters templateProcessingParameters) {
        String themePath = null;
    
        Theme theme = WebRequestContext.getWebRequestContext().getTheme();
        if (theme != null && theme.getPath() != null) {
            themePath = theme.getPath();
        }             

        checkInitialized();

        final String templateName = templateProcessingParameters.getTemplateName();

        Validate.notNull(templateName, "Template name cannot be null");

        String unaliasedName = this.getTemplateAliases().get(templateName);
        if (unaliasedName == null) {
            unaliasedName = templateName;
        }

        final StringBuilder resourceName = new StringBuilder();
        String prefix = this.getPrefix();
        if (prefix != null && ! prefix.trim().equals("")) {
           
            if (themePath != null) {        
                resourceName.append(prefix).append(themePath).append('/').append(templateFolder);
            }
        }
        resourceName.append(unaliasedName);
        String suffix = this.getSuffix();
        if (suffix != null && ! suffix.trim().equals("")) {
            resourceName.append(suffix);
        }

        return resourceName.toString();
    }
    
    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }
    
}


