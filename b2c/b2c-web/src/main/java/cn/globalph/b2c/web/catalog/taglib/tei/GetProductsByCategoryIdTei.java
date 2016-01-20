package cn.globalph.b2c.web.catalog.taglib.tei;

import cn.globalph.b2c.web.catalog.taglib.GetProductsByCategoryIdTag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import java.util.ArrayList;
import java.util.List;

public class GetProductsByCategoryIdTei extends TagExtraInfo {

    @Override
    public VariableInfo[] getVariableInfo(TagData tagData) {
        List<VariableInfo> infos = new ArrayList<VariableInfo>(2);

        String variableName = tagData.getAttributeString("var");
        infos.add(new VariableInfo(variableName, String.class.getName(), true, VariableInfo.NESTED));

        variableName = tagData.getAttributeString("categoryId");

        if (variableName != null) {
            variableName = GetProductsByCategoryIdTag.toVariableName(variableName);
            infos.add(new VariableInfo(variableName, String.class.getName(), true, VariableInfo.NESTED));
        }

        return infos.toArray(new VariableInfo[infos.size()]);
    }
}
