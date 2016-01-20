package cn.globalph.cms.url.domain;

import cn.globalph.cms.url.type.URLRedirectType;
import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CMS_URL_HANDLER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "URLHandlerImpl_friendyName")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class URLHandlerImpl implements URLHandler, Serializable, AdminMainEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "URLHandlerID")
    @GenericGenerator(
        name="URLHandlerID",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="URLHandlerImpl"),
            @Parameter(name="entity_name", value="cn.globalph.cms.url.domain.URLHandlerImpl")
        }
    )
    @Column(name = "URL_HANDLER_ID")
    @AdminPresentation(friendlyName = "URLHandlerImpl_ID", order = 1, group = "URLHandlerImpl_friendyName", groupOrder = 1, visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @AdminPresentation(friendlyName = "URLHandlerImpl_incomingURL", order = 1, group = "URLHandlerImpl_friendyName", prominent = true, groupOrder = 1)
    @Column(name = "INCOMING_URL", nullable = false)
    @Index(name="INCOMING_URL_INDEX", columnNames={"INCOMING_URL"})
    protected String incomingURL;

    @Column(name = "NEW_URL", nullable = false)
    @AdminPresentation(friendlyName = "URLHandlerImpl_newURL", order = 1, group = "URLHandlerImpl_friendyName", prominent = true, groupOrder = 1)
    protected String newURL;

    @Column(name = "URL_REDIRECT_TYPE")
    @AdminPresentation(friendlyName = "URLHandlerImpl_redirectType", order = 4, group = "URLHandlerImpl_friendyName", fieldType = SupportedFieldType.BROADLEAF_ENUMERATION, broadleafEnumeration = "cn.globalph.cms.url.type.URLRedirectType", groupOrder = 2, prominent = true)
    protected String urlRedirectType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getIncomingURL() {
        return incomingURL;
    }

    @Override
    public void setIncomingURL(String incomingURL) {
        this.incomingURL = incomingURL;
    }

    @Override
    public String getNewURL() {
        return newURL;
    }

    @Override
    public void setNewURL(String newURL) {
        this.newURL = newURL;
    }

    @Override
    public URLRedirectType getUrlRedirectType() {
        return URLRedirectType.getInstance(urlRedirectType);
    }

    @Override
    public void setUrlRedirectType(URLRedirectType redirectType) {
        this.urlRedirectType = redirectType.getType();
    }

    @Override
    public String getMainEntityName() {
        return getIncomingURL();
    }

}
