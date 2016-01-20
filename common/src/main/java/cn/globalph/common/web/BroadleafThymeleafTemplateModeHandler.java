package cn.globalph.common.web;

import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templateparser.ITemplateParser;
import org.thymeleaf.templatewriter.AbstractGeneralTemplateWriter;
import org.thymeleaf.templatewriter.CacheAwareGeneralTemplateWriter;
import org.thymeleaf.templatewriter.ITemplateWriter;

/**
 * Overrides the Thymeleaf ContextTemplateResolver and appends the cn.globalph.common.web.Theme path to the url
 * if it exists.
 */
public class BroadleafThymeleafTemplateModeHandler implements ITemplateModeHandler {

    private ITemplateModeHandler handler;
    private CacheAwareGeneralTemplateWriter writer;

    public BroadleafThymeleafTemplateModeHandler(ITemplateModeHandler handler) {
        super();
        this.handler = handler;
    }

    public String getTemplateModeName() {
        return handler.getTemplateModeName();
    }

    public ITemplateParser getTemplateParser() {
        return handler.getTemplateParser();
    }

    public ITemplateWriter getTemplateWriter() {
        if (handler.getTemplateWriter() instanceof AbstractGeneralTemplateWriter) {
            if (writer == null) {
                writer = new CacheAwareGeneralTemplateWriter((AbstractGeneralTemplateWriter) handler.getTemplateWriter());
            }
            return writer;
        } else {
            return handler.getTemplateWriter();
        }
    }
}


