package cn.globalph.common.site.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="NPH_CATALOG")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITEMARKER)
})
public class CatalogImpl implements Catalog {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(CatalogImpl.class);

    @Id
    @GeneratedValue(generator= "CatalogId")
    @GenericGenerator(
        name="CatalogId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CatalogImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.site.domain.CatalogImpl")
        }
    )
    @Column(name = "CATALOG_ID")
    protected Long id;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "Catalog_Name", order=1, prominent = true)
    protected String name;

    @ManyToMany(targetEntity = SiteImpl.class)
    @JoinTable(name = "NPH_SITE_CATALOG", joinColumns = @JoinColumn(name = "CATALOG_ID"), inverseJoinColumns = @JoinColumn(name = "SITE_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    protected List<Site> sites = new ArrayList<Site>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Site> getSites() {
        return sites;
    }

    @Override
    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public void checkCloneable(Catalog catalog) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = catalog.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") && !catalog.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone.");
        }
    }

    @Override
    public Catalog clone() {
        Catalog clone;
        try {
            clone = (Catalog) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clone);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " + clone.getClass().getName(), e);
            }
            clone.setId(id);
            clone.setName(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clone;
    }

}
