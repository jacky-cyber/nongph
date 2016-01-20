package cn.globalph.openadmin.server.security.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.presentation.AdminPresentation;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ADMIN_PERMISSION_ENTITY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
public class AdminPermissionQualifiedEntityImpl implements AdminPermissionQualifiedEntity, Serializable {

    private static final Log LOG = LogFactory.getLog(AdminPermissionQualifiedEntityImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminPermissionEntityId")
    @GenericGenerator(
        name="AdminPermissionEntityId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminPermissionEntityImpl"),
            @Parameter(name="entity_name", value="cn.globalph.openadmin.server.security.domain.AdminPermissionQualifiedEntityImpl")
        }
    )
    @Column(name = "ADMIN_PERMISSION_ENTITY_ID")
    protected Long id;

    @Column(name = "CEILING_ENTITY", nullable=false)
    @AdminPresentation(friendlyName = "AdminPermissionQualifiedEntityImpl_Ceiling_Entity_Name", order=1, group = "AdminPermissionQualifiedEntityImpl_Permission", prominent=true)
    protected String ceilingEntityFullyQualifiedName;

    @ManyToOne(targetEntity = AdminPermissionImpl.class)
    @JoinColumn(name = "ADMIN_PERMISSION_ID")
    protected AdminPermission adminPermission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCeilingEntityFullyQualifiedName() {
        return ceilingEntityFullyQualifiedName;
    }

    @Override
    public void setCeilingEntityFullyQualifiedName(String ceilingEntityFullyQualifiedName) {
        this.ceilingEntityFullyQualifiedName = ceilingEntityFullyQualifiedName;
    }

    @Override
    public AdminPermission getAdminPermission() {
        return adminPermission;
    }

    @Override
    public void setAdminPermission(AdminPermission adminPermission) {
        this.adminPermission = adminPermission;
    }

    public void checkCloneable(AdminPermissionQualifiedEntity qualifiedEntity) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = qualifiedEntity.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") && !qualifiedEntity.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone.");
        }
    }

    @Override
    public AdminPermissionQualifiedEntity clone() {
        AdminPermissionQualifiedEntity clone;
        try {
            clone = (AdminPermissionQualifiedEntity) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clone);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " + clone.getClass().getName(), e);
            }
            clone.setId(id);
            clone.setCeilingEntityFullyQualifiedName(ceilingEntityFullyQualifiedName);

            //don't clone the AdminPermission, as it would cause a recursion
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}
