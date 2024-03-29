package cn.globalph.b2c.product.dao;

import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuFee;

import java.util.List;

/**
 * {@code SkuDao} provides persistence access to {@code Sku} instances
 *
 */
public interface SkuDao {

    /**
     * Retrieve a {@code Sku} instance by its primary key
     *
     * @param skuId the primary key of the sku
     * @return the sku at the primary key
     */
    public Sku readSkuById(Long skuId);

    /**
     * Persist a {@code Sku} instance to the datastore
     *
     * @param sku the sku to persist
     * @return the saved state of the passed in sku
     */
    public Sku save(Sku sku);
    
    public SkuFee saveSkuFee(SkuFee fee);

    /**
     * Retrieve the {@code Sku} instance whose primary key is the smallest
     * of all skus in the datastore
     *
     * @return the sku with the smallest primary key
     */
    public Sku readFirstSku();

    /**
     * Retrieve all {@code Sku} instances from the datastore
     *
     * @return the list of all skus
     */
    public List<Sku> readAllSkus();

    /**
     * Find all the {@code Sku} instances whose primary key matches
     * one of the values from the passed in list
     *
     * @param ids the list of primary key values
     * @return the list of skus that match the list of primary key values
     */
    public List<Sku> readSkusById(List<Long> ids);

    /**
     * Remove the {@code Sku} instance from the datastore
     *
     * @param sku the sku to remove
     */
    public void delete(Sku sku);    

    /**
     * Create a new {@code Sku} instance. The system will use the configuration in
     * {@code /BroadleafCommerce/core/BroadleafCommerceFramework/src/main/resources/bl-framework-applicationContext-entity.xml}
     * to determine which polymorphic version of {@code Sku} to instantiate. To make Broadleaf instantiate your
     * extension of {@code Sku} by default, include an entity configuration bean in your application context xml similar to:
     * <p>
     * {@code
     *     <bean id="blEntityConfiguration" class="cn.globalph.common.persistence.EntityConfiguration">
     *          <property name="entityContexts">
     *              <list>
     *                  <value>classpath:myCompany-applicationContext-entity.xml</value>
     *              </list>
     *          </property>
     *      </bean>
     * }
     * </p>
     * Declare the same key for your desired entity in your entity xml that is used in the Broadleaf entity xml, but change the value to the fully
     * qualified classname of your entity extension.
     *
     * @return a {@code Sku} instance based on the Broadleaf entity configuration.
     */
    public Sku create();
    
    /**
     * 根据ID和产品URI查询SKU
     */
    public Sku findSkuByProductURIAndId(String productURI, Long skuId);
}
