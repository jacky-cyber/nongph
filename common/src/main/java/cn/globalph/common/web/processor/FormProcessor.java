package cn.globalph.common.web.processor;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.service.ExploitProtectionService;

import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that adds a CSRF token to forms that are not going to be submitted
 * via GET
 * 
 * @author felix.wu
 */
@Component("blFormProcessor")
public class FormProcessor extends AbstractElementProcessor {
    
    @Resource(name = "blExploitProtectionService")
    protected ExploitProtectionService eps;
    
    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public FormProcessor() {
        super("form");
    }
    
    /**
     * We need this replacement to execute as early as possible to allow subsequent processors to act
     * on this element as if it were a normal form instead of a blc:form
     */
    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        // If the form will be not be submitted with a GET, we must add the CSRF token
        // We do this instead of checking for a POST because post is default if nothing is specified
        if (!"GET".equalsIgnoreCase(element.getAttributeValueFromNormalizedName("method"))) {
            try {
                String csrfToken = eps.getCSRFToken();

                //detect multipart form
                if ("multipart/form-data".equalsIgnoreCase(element.getAttributeValueFromNormalizedName("enctype"))) {
                    Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                            .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValueFromNormalizedName("th:action"));
                    String action = (String) expression.execute(arguments.getConfiguration(), arguments);
                    String csrfQueryParameter = "?" + eps.getCsrfTokenParameter() + "=" + csrfToken;
                    element.removeAttribute("th:action");
                    element.setAttribute("action", action + csrfQueryParameter);
                } else {
                    Element csrfNode = new Element("input");
                    csrfNode.setAttribute("type", "hidden");
                    csrfNode.setAttribute("name", eps.getCsrfTokenParameter());
                    csrfNode.setAttribute("value", csrfToken);
                    element.addChild(csrfNode);
                }

            } catch (ServiceException e) {
                throw new RuntimeException("Could not get a CSRF token for this session", e);
            }
        }
        
        // Convert the <blc:form> node to a normal <form> node
        Element newElement = element.cloneElementNodeWithNewName(element.getParent(), "form", false);
        newElement.setRecomputeProcessorsImmediately(true);
        element.getParent().insertAfter(element, newElement);
        element.getParent().removeChild(element);
        
        return ProcessorResult.OK;
    }
    
}
