package cn.globalph.b2c.web.processor.extension;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

/**
 * @author Jerry Ocanas (jocanas)
 */
public interface HeadProcessorExtensionListener {

    public void processAttributeValues(Arguments arguments, Element element);

}
