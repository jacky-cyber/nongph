package cn.globalph.openadmin.server.service.export;

import cn.globalph.openadmin.dto.Property;

import javax.servlet.ServletOutputStream;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Phillip Verheyden
 */
public interface AdminExporter {

    /**
     * The name of this exporter. Should be unique
     * 
     * @return
     */
    public String getName();

    /**
     * The friendly name of this exporter to display to the user to select from
     * 
     * @return
     */
    public String getFriendlyName();

    /**
     * <p>
     * The list of field names that are necessary for this exporter to function properly.
     * For instance, an Order exporter might request a start date and end date for criteria.
     * While this returns a list of Property, only a subset of the available fields on Property
     * are used to build the form.  Methods that should not return null:
     * <ul>
     *  <li>property.getName()</li>
     *  <li>property.getMetadata().getPresentationAttributes().getFriendlyName()</li>
     *  <li>property.getMetaData().getFieldType()</li>
     * </ul>
     * </p>
     * <p>
     * These methods can be null, but can contain values to further control display:
     * <ul>
     *  <li>property.getMetadata().getLength()</li>
     *  <li>property.getMetadata().getPresentationAttributes().isLargeEntry()</li>
     *  <li>property.getMetadata().getPresentationAttributes().getTooltip()</li>
     *  <li>property.getMetadata().getPresentationAttributes().getRequiredOverride()</li>
     * </ul>
     * <p>
     * For instance, this would be an example of creating start date and end date criteria
     * fields:
     * <pre>
     * {@code
     *  List<Property> criteria = new ArrayList<Property>();
     *  Property startDate = new Property();
     *  startDate.setName("startDate");
     *  startDate.getMetadata().getPresentationAttributes().setFriendlyName("Start Date");
     *  startDate.getMetadata().setFieldType(SupportedFieldType.DATE);
     *  criteria.add(startDate);
     *   
     *  Property endDate = new Property();
     *  endDate.setName("endDate");
     *  endDate.getMetadata().getPresentationAttributes().setFriendlyName("End Date");
     *  endDate.getMetadata().setFieldType(SupportedFieldType.DATE);
     *  criteria.add(endDate);
     *  return criteria;
     * }
     * </pre>
     * </p>
     * @return <b>null</b> if no additional criteria is needed
     */
    public List<Property> getCriteriaFields();

    /**
     * The type of this exporter
     * @return
     */
    public String getType();

    /**
     * The file name used in the Content-Disposition header for "attachment"
     * 
     * @return
     */
    public String getFileName();

    public void writeExport(ServletOutputStream out, Map<String, String> params) throws IOException;
    
}
