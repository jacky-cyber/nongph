package cn.globalph.b2c.search.dao;

import cn.globalph.b2c.search.domain.Field;

import java.util.List;

/**
 * DAO to facilitate interaction with Broadleaf fields.
 * 
 */
public interface FieldDao {

    /**
     * Given an abbreviation, returns the Field object that maps to this abbreviation.
     * Note that the default Broadleaf implementation of Field will enforce a uniqueness
     * constraint on the abbreviation field and this method will reliably return one field
     * 
     * @param abbreviation
     * @return the Field that has this abbreviation
     */
    public Field readFieldByAbbreviation(String abbreviation);

    /**
     * Reads all Field objects that are set to searchable. This is typically used to build an
     * index for searching. Note that the default Broadleaf implementation returns only fields that
     * have a FieldEntity equal to PRODUCT
     * 
     * @return the product Fields
     */
    public List<Field> readAllProductFields();

    /**
     * Persist an instance to the data layer.
     *
     * @param field the instance to persist
     * @return the instance after it has been persisted
     */
    public Field save(Field field);
}
