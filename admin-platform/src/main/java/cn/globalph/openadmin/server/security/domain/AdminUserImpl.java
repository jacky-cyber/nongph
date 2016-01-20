package cn.globalph.openadmin.server.security.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.AdminPresentationOperationTypes;
import cn.globalph.common.presentation.ConfigurationItem;
import cn.globalph.common.presentation.ValidationConfiguration;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxImpl;
import cn.globalph.openadmin.server.service.type.ContextType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ADMIN_USER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@AdminPresentationClass(friendlyName = "AdminUserImpl_baseAdminUser")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_ADMINUSER)
})
public class AdminUserImpl implements AdminUser, AdminMainEntity {

    private static final Log LOG = LogFactory.getLog(AdminUserImpl.class);
    private static final long serialVersionUID = 1L;
    protected static final String LAST_USED_SANDBOX = "LAST_USED_SANDBOX";

    @Id
    @GeneratedValue(generator = "AdminUserId")
    @GenericGenerator(
        name="AdminUserId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminUserImpl"),
            @Parameter(name="entity_name", value="cn.globalph.openadmin.server.security.domain.AdminUserImpl")
        }
    )
    @Column(name = "ADMIN_USER_ID")
    @AdminPresentation(friendlyName = "AdminUserImpl_Admin_User_ID", group = "AdminUserImpl_Primary_Key", visibility = VisibilityEnum.HIDDEN_ALL)
    private Long id;

    @Column(name = "NAME", nullable=false)
    @Index(name="ADMINUSER_NAME_INDEX", columnNames={"NAME"})
    @AdminPresentation(friendlyName = "AdminUserImpl_Admin_Name", gridOrder = 1000, order = 1000,
            group = "AdminUserImpl_User", prominent = true)
    protected String name;

    @Column(name = "LOGIN", nullable=false)
    @AdminPresentation(friendlyName = "AdminUserImpl_Admin_Login", gridOrder = 2000, order = 2000,
            group = "AdminUserImpl_User", prominent = true)
    protected String login;

    @Column(name = "PASSWORD")
    @AdminPresentation(
            friendlyName = "AdminUserImpl_Admin_Password", order = 6000,
            group = "AdminUserImpl_User",
            fieldType = SupportedFieldType.PASSWORD,
            validationConfigurations = { @ValidationConfiguration(
                    validationImplementation = "cn.globalph.openadmin.server.service.persistence.validation.MatchesFieldValidator",
                    configurationItems = {
                            @ConfigurationItem(itemName = ConfigurationItem.ERROR_MESSAGE, itemValue = "passwordNotMatchError"),
                            @ConfigurationItem(itemName = "otherField", itemValue = "passwordConfirm")
                    }
                    )
            })
    protected String password;

    @Column(name = "EMAIL", nullable=false)
    @Index(name="ADMINPERM_EMAIL_INDEX", columnNames={"EMAIL"})
    @AdminPresentation(friendlyName = "AdminUserImpl_Admin_Email_Address", order = 3000, group = "AdminUserImpl_User")
    protected String email;

    @Column(name = "PHONE_NUMBER")
    @AdminPresentation(friendlyName = "AdminUserImpl_Phone_Number", order = 5000, group = "AdminUserImpl_User")
    protected String phoneNumber;

    @Column(name = "ACTIVE_STATUS_FLAG")
    @AdminPresentation(friendlyName = "AdminUserImpl_Active_Status",
            order = 4000, group = "AdminUserImpl_User")
    protected Boolean activeStatusFlag = Boolean.TRUE;

    /** All roles that this user has */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRoleImpl.class)
    @JoinTable(name = "ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    @AdminPresentationCollection(addType = AddMethodType.LOOKUP, friendlyName = "roleListTitle", manyToField = "allUsers",
                operationTypes = @AdminPresentationOperationTypes(removeType = OperationScope.NONDESTRUCTIVEREMOVE))
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermissionImpl.class)
    @JoinTable(name = "ADMIN_USER_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    @BatchSize(size = 50)
    @AdminPresentationCollection(addType = AddMethodType.LOOKUP,
            friendlyName = "permissionListTitle",
            customCriteria = "includeFriendlyOnly",
            manyToField = "allUsers",
            operationTypes = @AdminPresentationOperationTypes(removeType = OperationScope.NONDESTRUCTIVEREMOVE))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    @Transient
    protected String unencodedPassword;
    
    @Override
    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    @ManyToOne(targetEntity = SandBoxImpl.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "ADMIN_USER_SANDBOX", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "SANDBOX_ID", referencedColumnName = "SANDBOX_ID"))
    @AdminPresentation(excluded = true)
    protected SandBox overrideSandBox;

    @ElementCollection
    @CollectionTable(name = "ADMIN_USER_ADDTL_FIELDS", joinColumns = @JoinColumn(name="ADMIN_USER_ID"))
    @MapKeyColumn(name = "FIELD_NAME")
    @Column(name = "FIELD_VALUE")
    @BatchSize(size = 50)
    protected Map<String, String> additionalFields = new HashMap<String, String>();

    @Override
    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

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
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Boolean getActiveStatusFlag() {
        return activeStatusFlag;
    }

    @Override
    public void setActiveStatusFlag(Boolean activeStatusFlag) {
        this.activeStatusFlag = activeStatusFlag;
    }

    @Override
    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    @Override
    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    @Override
    public SandBox getOverrideSandBox() {
        return overrideSandBox;
    }

    @Override
    public void setOverrideSandBox(SandBox overrideSandBox) {
        this.overrideSandBox = overrideSandBox;
    }

    @Override
    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    @Override
    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    @Override
    public ContextType getContextType() {
        return ContextType.GLOBAL;
    }

    @Override
    public void setContextType(ContextType contextType) {
        //do nothing
    }

    @Override
    public String getContextKey() {
        return null;
    }

    @Override
    public void setContextKey(String contextKey) {
        //do nothing
    }
    
    @Override
    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    @Override
    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }
    
    @Override
    public Long getLastUsedSandBoxId() {
        String sandBox = getAdditionalFields().get(LAST_USED_SANDBOX);
        if (StringUtils.isNotBlank(sandBox)) {
            return Long.parseLong(sandBox);
        }
        return null;
    }
    
    @Override
    public void setLastUsedSandBoxId(Long sandBoxId) {
        getAdditionalFields().put(LAST_USED_SANDBOX, String.valueOf(sandBoxId));
    }

    @Override
    public String getMainEntityName() {
        return getName();
    }

}
