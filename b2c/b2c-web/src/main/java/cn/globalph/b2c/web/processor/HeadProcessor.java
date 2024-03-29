package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.web.processor.extension.HeadProcessorExtensionListener;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.element.AbstractFragmentHandlingElementProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.attr.StandardFragmentAttrProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will include the standard head element. It will also set the
 * following variables for use by the head fragment.
 * 
 * <ul>
 *  <li><b>pageTitle</b> - The title of the page</li>
 *  <li><b>additionalCss</b> - An additional, page specific CSS file to include</li>
 *  <li><b>metaDescription</b> - Optional, Content for the Meta-Description tag</li>
 *  <li><b>metaKeywords</b> - Optional, Content for the Meta-Keywords tag</li>
 *  <li><b>metaRobot</b> - Optional, Content for the Meta-Robots tag</li>
 * </ul>
 * 
 * @author apazzolini
 *
 * @deprecated
 *
 * The entire FragmentAndTarget class has been deprecated in favor of a completely new system in Thymeleaf 2.1
 * The referenced issue can be found at https://github.com/thymeleaf/thymeleaf/issues/205
 *
 * Use th:include or th:replace within the head tag and include the variables to replicate the behaviour.
 *
 * Examples:
 *
 * <head th:include="/layout/partials/head (pageTitle='My Page Title')"></head>
 * <head th:include="/layout/partials/head (twovar=${value2},onevar=${value1})">...</head>
 *
 */
@Deprecated
public class HeadProcessor extends AbstractFragmentHandlingElementProcessor {

    @Resource(name = "blHeadProcessorExtensionManager")
    protected HeadProcessorExtensionListener extensionManager;

    public static final String FRAGMENT_ATTR_NAME = StandardFragmentAttrProcessor.ATTR_NAME;
    protected String HEAD_PARTIAL_PATH = "layout/partials/head";
    
    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public HeadProcessor() {
        super("head");
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

    @Override
    protected boolean getRemoveHostNode(final Arguments arguments, final Element element) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<Node> computeFragment(final Arguments arguments, final Element element) {
        // The pageTitle attribute could be an expression that needs to be evaluated. Try to evaluate, but fall back
        // to its text value if the expression wasn't able to be processed. This will allow things like
        // pageTitle="Hello this is a string"
        // as well as expressions like
        // pageTitle="${'Hello this is a ' + product.name}"
        
        String pageTitle = element.getAttributeValue("pageTitle");
        try {
            Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                    .parseExpression(arguments.getConfiguration(), arguments, pageTitle);
            pageTitle = (String) expression.execute(arguments.getConfiguration(), arguments);
        } catch (TemplateProcessingException e) {
            // Do nothing.
        }
        ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put("pageTitle", pageTitle);
        ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put("additionalCss", element.getAttributeValue("additionalCss"));

        extensionManager.processAttributeValues(arguments, element);
        
        //the commit at https://github.com/thymeleaf/thymeleaf/commit/b214d9b5660369c41538e023d4b8d7223ebcbc22 along with
        //the referenced issue at https://github.com/thymeleaf/thymeleaf/issues/205
        
        
//        return new FragmentAndTarget(HEAD_PARTIAL_PATH, WholeFragmentSpec.INSTANCE);
        return new ArrayList<Node>();
    }

}
