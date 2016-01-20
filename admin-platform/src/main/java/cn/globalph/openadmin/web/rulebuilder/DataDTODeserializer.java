package cn.globalph.openadmin.web.rulebuilder;

import cn.globalph.openadmin.web.rulebuilder.dto.DataDTO;
import cn.globalph.openadmin.web.rulebuilder.dto.ExpressionDTO;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class DataDTODeserializer extends StdDeserializer<DataDTO> {

    public DataDTODeserializer() {
        super(DataDTO.class);
    }

    @Override
    public DataDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = (ObjectNode) mapper.readTree(jp);
        Iterator<Map.Entry<String, JsonNode>> elementsIterator =
                root.getFields();
        DataDTO dataDTO = new DataDTO();
        ExpressionDTO expressionDTO = new ExpressionDTO();
        boolean isExpression = false;
        while (elementsIterator.hasNext()) {
            Map.Entry<String, JsonNode> element=elementsIterator.next();
            String name = element.getKey();
            if ("name".equals(name)) {
                expressionDTO.setName(element.getValue().asText());
                isExpression = true;
            }
            if ("operator".equals(name)) {
                expressionDTO.setOperator(element.getValue().asText());
                isExpression = true;
            }
            if ("value".equals(name)) {
                expressionDTO.setValue(element.getValue().asText());
                isExpression = true;
            }
            if ("start".equals(name)) {
                expressionDTO.setStart(element.getValue().asText());
                isExpression = true;
            }
            if ("end".equals(name)) {
                expressionDTO.setEnd(element.getValue().asText());
                isExpression = true;
            }
            if ("id".equals(name)) {
                if ("null".equals(element.getValue().asText())) {
                    dataDTO.setId(null);
                } else {
                    dataDTO.setId(element.getValue().asLong());
                }
            }
            if ("quantity".equals(name)) {
                if ("null".equals(element.getValue().asText())) {
                    dataDTO.setQuantity(null);
                } else {
                    dataDTO.setQuantity(element.getValue().asInt());
                }
            }
            if ("groupOperator".equals(name)) {
                dataDTO.setGroupOperator(element.getValue().asText());
            }
            if ("groups".equals(name)){
                dataDTO.setGroups((ArrayList<DataDTO>)mapper.readValue(element.getValue(), new TypeReference<ArrayList<DataDTO>>(){}));
            }


        }

        if (isExpression) {
            return expressionDTO;
        } else {
            return dataDTO;
        }
    }

}
