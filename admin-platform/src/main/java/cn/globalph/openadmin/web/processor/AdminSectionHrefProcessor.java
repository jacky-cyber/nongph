package cn.globalph.openadmin.web.processor;

import cn.globalph.openadmin.server.security.domain.AdminSection;

import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.spring3.context.SpringWebContext;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * A Thymeleaf processor that will generate the HREF of a given Admin Section.
 * This is useful in constructing the left navigation menu for the admin console.
 *
 * @author elbertbautista
 */
@Component("blAdminSectionHrefProcessor")
public class AdminSectionHrefProcessor extends AbstractAttributeModifierAttrProcessor {

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public AdminSectionHrefProcessor() {
        super("admin_section_href");
    }

    @Override
    public int getPrecedence() {
        return 10002;
    }

    @Override
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
        String href = "#";
        
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        AdminSection section = (AdminSection) expression.execute(arguments.getConfiguration(), arguments);
        if (section != null) {
            HttpServletRequest request = ((SpringWebContext) arguments.getContext()).getHttpServletRequest();

            href = request.getContextPath() + section.getUrl();
        }
        
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put("href", href);
        return attrs;
    }

    @Override
    protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
        return false;
    }

}
