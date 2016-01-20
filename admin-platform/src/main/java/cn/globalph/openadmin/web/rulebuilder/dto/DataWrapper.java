package cn.globalph.openadmin.web.rulebuilder.dto;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * An example of the Serialized JSON:
 * (This is an example of a complex Item Rule)
 *
 * {'data': [
 *      {'id':'100',
 *      'quantity':'1',
 *      'groupOperator':'AND',
 *      'groups':[
 *          {'id':null,
 *          'quantity':null,
 *          'groupOperator':null,
 *          'groups':null,
 *          'name':'name',
 *          'operator':'IEQUALS',
 *          'value':'merchandise'}]},
 *      {'id':'200',
 *      'quantity':'2',
 *      'groupOperator':'AND',
 *      'groups':[
 *          {'id':null,
 *          'quantity':null,
 *          'groupOperator':null,
 *          'groups':null,
 *          'name':'retailPrice',
 *          'operator':'GREATER_THAN',
 *          'value':'20.00'}]}
 * ]}
 *
 */
public class DataWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    protected ArrayList<DataDTO> data = new ArrayList<DataDTO>();

    protected String error;
    protected String rawMvel;

    public ArrayList<DataDTO> getData() {
        return data;
    }

    public void setData(ArrayList<DataDTO> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRawMvel() {
        return rawMvel;
    }

    public void setRawMvel(String rawMvel) {
        this.rawMvel = rawMvel;
    }

    public String serialize() throws JsonGenerationException, JsonMappingException, IOException {
        return new ObjectMapper().writeValueAsString(this);
    }
}
