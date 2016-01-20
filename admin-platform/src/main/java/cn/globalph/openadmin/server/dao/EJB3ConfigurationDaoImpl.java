package cn.globalph.openadmin.server.dao;

import org.hibernate.ejb.Ejb3Configuration;

import java.util.HashMap;

import javax.persistence.spi.PersistenceUnitInfo;

/**
 * 
 * @felix.wu
 *
 */
public class EJB3ConfigurationDaoImpl implements EJB3ConfigurationDao {

    private Ejb3Configuration configuration = null;

    protected PersistenceUnitInfo persistenceUnitInfo;

    public Ejb3Configuration getConfiguration() {
        synchronized(this) {
            if (configuration == null) {
                Ejb3Configuration temp = new Ejb3Configuration();
                String previousValue = persistenceUnitInfo.getProperties().getProperty("hibernate.hbm2ddl.auto");
                persistenceUnitInfo.getProperties().setProperty("hibernate.hbm2ddl.auto", "none");
                configuration = temp.configure(persistenceUnitInfo, new HashMap());
                configuration.getHibernateConfiguration().buildSessionFactory();
                if (previousValue != null) {
                    persistenceUnitInfo.getProperties().setProperty("hibernate.hbm2ddl.auto", previousValue);
                }
            }
        }
        return configuration;
    }

    public PersistenceUnitInfo getPersistenceUnitInfo() {
        return persistenceUnitInfo;
    }

    public void setPersistenceUnitInfo(PersistenceUnitInfo persistenceUnitInfo) {
        this.persistenceUnitInfo = persistenceUnitInfo;
    }
    
}
