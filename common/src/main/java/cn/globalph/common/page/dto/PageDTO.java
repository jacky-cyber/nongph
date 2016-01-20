package cn.globalph.common.page.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.globalph.common.structure.dto.ItemCriteriaDTO;

/**
 * Page fields must be pre-processed (for example to fix image paths).
 * This DTO allows us to process the PageFields once and then cache
 * the results.
 *
 * Created by bpolster.
 */
public class PageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String description;
    protected String localeCode;
    protected String templatePath;
    protected String url;
    protected Integer priority;
    protected Map<String, String> pageFields = new HashMap<String,String>();
    protected String ruleExpression;
    protected List<ItemCriteriaDTO> itemCriteriaDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getPageFields() {
        return pageFields;
    }

    public void setPageFields(Map<String, String> pageFields) {
        this.pageFields = pageFields;
    }
    
    public String getRuleExpression() {
        return ruleExpression;
    }

    public void setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
    }

    public List<ItemCriteriaDTO> getItemCriteriaDTOList() {
        return itemCriteriaDTOList;
    }

    public void setItemCriteriaDTOList(List<ItemCriteriaDTO> itemCriteriaDTOList) {
        this.itemCriteriaDTOList = itemCriteriaDTOList;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }   
    
}

