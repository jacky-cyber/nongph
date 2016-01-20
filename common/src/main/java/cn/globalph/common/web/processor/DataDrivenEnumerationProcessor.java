package cn.globalph.common.web.processor;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.enumeration.domain.DataDrivenEnumeration;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue;
import cn.globalph.common.enumeration.service.DataDrivenEnumerationService;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;


/**
 * Processor that returns a list of {@link DataDriveEnumerationValue}s for a particular key
 *  Parameters:
 *  <ul>
 *      <li>key - key for the {@link DataDrivenEnumeration} that the {@link DataDrivenEnumerationValue}s should be apart of</li>
 *      <li>sort - 'ASCENDING' or 'DESCENDING' if the resulting values should be sorted by not. The sort will be on the
 *          display value of the {@link DataDrivenEnumerationValue}</li>
 *  </ul>
 *  
 *  This will add a new variable on the model called 'enumValues'
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public class DataDrivenEnumerationProcessor extends AbstractModelVariableModifierProcessor {

    @Resource(name = "blDataDrivenEnumerationService")
    protected DataDrivenEnumerationService enumService;
    
    /**
     * @param elementName
     */
    public DataDrivenEnumerationProcessor() {
        super("enumeration");
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String key = element.getAttributeValue("key");
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("No 'key' parameter was passed to find enumeration values");
        }
        
        DataDrivenEnumeration ddEnum = enumService.findEnumByKey(key);
        if (ddEnum == null) {
            throw new IllegalArgumentException("Could not find a data driven enumeration keyed by " + key);
        }
        List<DataDrivenEnumerationValue> enumValues = new ArrayList<DataDrivenEnumerationValue>(ddEnum.getEnumValues());
        
        final String sort = element.getAttributeValue("sort");
        if (StringUtils.isNotEmpty(sort)) {
            Collections.sort(enumValues, new Comparator<DataDrivenEnumerationValue>() {

                @Override
                public int compare(DataDrivenEnumerationValue arg0, DataDrivenEnumerationValue arg1) {
                    if (sort.equals("ASCENDING")) {
                        return arg0.getDisplay().compareTo(arg1.getDisplay());
                    } else {
                        return arg1.getDisplay().compareTo(arg0.getDisplay());
                    }
                }
            });
        }
        
        addToModel(arguments, "enumValues", enumValues);
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

}
