package cn.globalph.openadmin.web.form.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import cn.globalph.openadmin.web.form.component.ListGrid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * tab控件
 * @author felix.wu
 *
 */
public class Tab {

    protected String title;
    protected Integer order;
    protected String tabClass;

    Set<FieldGroup> fieldGroups = new TreeSet<FieldGroup>(new Comparator<FieldGroup>() {
        @Override
        public int compare(FieldGroup o1, FieldGroup o2) {
            return new CompareToBuilder()
                    .append(o1.getOrder(), o2.getOrder())
                    .append(o1.getTitle(), o2.getTitle())
                    .toComparison();
        }
    });

    Set<ListGrid> listGrids = new TreeSet<ListGrid>(new Comparator<ListGrid>() {
        @Override
        public int compare(ListGrid o1, ListGrid o2) {
            return new CompareToBuilder()
                    .append(o1.getOrder(), o2.getOrder())
                    .append(o1.getSubCollectionFieldName(), o2.getSubCollectionFieldName())
                    .toComparison();
        }
    });
    
    public Boolean getIsVisible() {
        if (listGrids.size() > 0) {
            return true;
        }

        for (FieldGroup fg : fieldGroups) {
            if (fg.getIsVisible()) {
                return true;
            }
        }

        return false;
    }

    public FieldGroup findGroup(String groupTitle) {
        for (FieldGroup fg : fieldGroups) {
            if (fg.getTitle() != null && fg.getTitle().equals(groupTitle)) {
                return fg;
            }
        }
        return null;
    }
    
    public List<Field> getFields() {
        List<Field> fields = new ArrayList<Field>();
        for (FieldGroup fg : getFieldGroups()) {
            fields.addAll(fg.getFields());
        }
        return fields;
    }
    
    public void removeListGrid(ListGrid listGrid) {
        listGrids.remove(listGrid);
    }

    public String getTabClass() {
        return StringUtils.isBlank(tabClass) ? "" : " " + tabClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<FieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    public void setFieldGroups(Set<FieldGroup> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    public Set<ListGrid> getListGrids() {
        return listGrids;
    }

    public void setListGrids(Set<ListGrid> listGrids) {
        this.listGrids = listGrids;
    }

    public void setTabClass(String tabClass) {
        this.tabClass = tabClass;
    }
    
}


