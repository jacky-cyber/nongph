package cn.globalph.common.site.service;

import cn.globalph.common.site.dao.SiteDao;
import cn.globalph.common.site.domain.Catalog;
import cn.globalph.common.site.domain.Site;
import cn.globalph.common.util.StreamCapableTransactionalOperationAdapter;
import cn.globalph.common.util.StreamingTransactionCapableUtil;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service("blSiteService")
public class SiteServiceImpl implements SiteService {

    @Resource(name="blStreamingTransactionCapableUtil")
    protected StreamingTransactionCapableUtil transUtil;

    @Resource(name = "blSiteDao")
    protected SiteDao siteDao;

    @Override
    public Site createSite() {
        return siteDao.create();
    }

    @Override
    public Site retrieveSiteById(final Long id) {
        //Since the methods on this class are frequently called during regular page requests and transactions are expensive,
        //only run the operation under a transaction if there is not already an entity manager in the view
        final Site[] response = new Site[1];
        transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
            @Override
            public void execute() throws Throwable {
                Site temp = siteDao.retrieve(id);
                if (temp != null) {
                    temp = temp.clone();
                }
                response[0] = temp;
            }
        }, RuntimeException.class, !TransactionSynchronizationManager.hasResource(((JpaTransactionManager) transUtil.getTransactionManager()).getEntityManagerFactory()));

        return response[0];
    }

    @Override
    public Site retrieveSiteByDomainName(final String domainName) {
        //Since the methods on this class are frequently called during regular page requests and transactions are expensive,
        //only run the operation under a transaction if there is not already an entity manager in the view
        final Site[] response = new Site[1];
        transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
            @Override
            public void execute() throws Throwable {
                String domainPrefix = null;
                if (domainName != null) {
                    int pos = domainName.indexOf('.');
                    if (pos >= 0) {
                        domainPrefix = domainName.substring(0, pos);
                    } else {
                        domainPrefix = domainName;
                    }
                }

                Site temp = siteDao.retrieveSiteByDomainOrDomainPrefix(domainName, domainPrefix);
                if (temp != null) {
                    temp = temp.clone();
                }
                response[0] = temp;
            }
        }, RuntimeException.class, !TransactionSynchronizationManager.hasResource(((JpaTransactionManager) transUtil.getTransactionManager()).getEntityManagerFactory()));

        return response[0];
    }

    @Override
    @Transactional("blTransactionManager")
    public Site save(Site site) {
        return siteDao.save(site).clone();
    }

    @Override
    public Catalog findCatalogById(Long id) {
        return siteDao.retrieveCatalog(id);
    }

    @Override
    public Site retrieveDefaultSite() {
        //Since the methods on this class are frequently called during regular page requests and transactions are expensive,
        //only run the operation under a transaction if there is not already an entity manager in the view
        final Site[] response = new Site[1];
        transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
            @Override
            public void execute() throws Throwable {
                response[0] = siteDao.retrieveDefaultSite().clone();
            }
        }, RuntimeException.class, !TransactionSynchronizationManager.hasResource(((JpaTransactionManager) transUtil.getTransactionManager()).getEntityManagerFactory()));

        return response[0];
    }
    
    @Override
    public List<Site> findAllActiveSites() {
        //Since the methods on this class are frequently called during regular page requests and transactions are expensive,
        //only run the operation under a transaction if there is not already an entity manager in the view
        final List<Site> response = new ArrayList<Site>();
        transUtil.runOptionalTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
            @Override
            public void execute() throws Throwable {
                List<Site> sites = siteDao.readAllActiveSites();
                for (Site site : sites) {
                    response.add(site.clone());
                }
            }
        }, RuntimeException.class, !TransactionSynchronizationManager.hasResource(((JpaTransactionManager) transUtil.getTransactionManager()).getEntityManagerFactory()));

        return response;
    }

}
