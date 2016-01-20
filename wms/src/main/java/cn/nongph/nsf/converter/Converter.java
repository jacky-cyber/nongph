package com.felix.nsf.converter;

import javax.faces.convert.ConverterException;

public interface Converter {
    /**
     * <p>Convert the specified string value into a model data object that
     * is appropriate for being stored during the <em>Apply Request
     * Values</em> phase of the request processing lifecycle.</p>
     *
     * @param value     String value to be converted (may be <code>null</code>)
     * @return <code>null</code> if the value to convert is <code>null</code>,
     *         otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully
     */
    public Object getAsObject(String value);


    /**
     * <p>Convert the specified model object value into a String that is suitable
     * for being included in the response generated during the
     * <em>Render Response</em> phase of the request processing
     * lifeycle.</p>
     * @param value     Model object value to be converted
     *                  (may be <code>null</code>)
     * @return a zero-length String if value is <code>null</code>,
     *         otherwise the result of the conversion
     * @throws ConverterException   if conversion cannot be successfully
     *                              performed
     */
    public String getAsString(Object value);
}
