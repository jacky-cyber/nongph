package cn.globalph.b2c.web.processor.extension;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Ocanas (jocanas)
 */
public class HeadProcessorExtensionManager implements HeadProcessorExtensionListener {

    protected List<HeadProcessorExtensionListener> listeners;

    @Override
    public void processAttributeValues(Arguments arguments, Element element) {
        if(listeners == null) {
            listeners = new ArrayList<HeadProcessorExtensionListener>();
        }
        for(HeadProcessorExtensionListener listener : listeners){
            listener.processAttributeValues(arguments, element);
        }
    }

    public List<HeadProcessorExtensionListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<HeadProcessorExtensionListener> listeners) {
        this.listeners = listeners;
    }
}
