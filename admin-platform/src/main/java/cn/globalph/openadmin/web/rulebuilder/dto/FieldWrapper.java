package cn.globalph.openadmin.web.rulebuilder.dto;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Elbert Bautista (elbertbautista)
 *
 * An example of the Serialized JSON:
 *
 * {'fields': [
 *      {'label':'Order Item - name',
 *      'name':'name',
 *      'operators':blcOperators_Text,
 *      'options':[]},
 *      {'label':'Order Item - Retail Price',
 *      'name':'retailPrice',
 *      'operators':blcOperators_Numeric,
 *      'options':[]},
 *      {'label':'Product - is Featured Product',
 *      'name':'sku.product.isFeaturedProduct',
 *      'operators':blcOperators_Boolean,
 *      'options':[]},
 *      {'label':'Sku - Active End Date',
 *      'name':'sku.activeEndDate',
 *      'operators':blcOperators_Date,
 *      'options':[]},
 *      {'label':'Category - Fulfillment Type',
 *      'name':'category.fulfillmentType',
 *      'operators':blcOperators_Enumeration,
 *      'options':blcOptions_FulfillmentType}
 * ]}
 *
 */
public class FieldWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ArrayList<FieldDTO> fields = new ArrayList<FieldDTO>();

    public ArrayList<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(ArrayList<FieldDTO> fields) {
        this.fields = fields;
    }
    
    public String serialize() throws JsonGenerationException, JsonMappingException, IOException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
