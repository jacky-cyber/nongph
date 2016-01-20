package cn.globalph.b2c.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.PropertyType;

@Entity
@Table(name = "PH_REFUND_MEDIA")
@AdminPresentationMergeOverrides(
	    {
	        @AdminPresentationMergeOverride(name = "", mergeEntries =
	            @AdminPresentationMergeEntry(propertyType = PropertyType.AdminPresentation.READONLY,
	                                            booleanOverrideValue = true))
	    }
	)
	@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "上传图片")
	@DirectCopyTransform({
	        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class RefundMediaImpl implements RefundMedia {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REFUND_MEDIA_ID")
	@GeneratedValue(generator = "RefundMediaId")
    @GenericGenerator(
            name = "RefundMediaId",
            strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name = "segment_value", value = "RefundMediaImpl"),
                @Parameter(name = "entity_name", value = "cn.globalph.b2c.order.domain.RefundMediaImpl")
            }
        )
	private Long id;
	
	@OneToOne(targetEntity = RefundImpl.class)
	@JoinColumn(name = "REFUND_ID")
	private Refund refund;
	
	@Column(name = "MEDIA_URL")
    @AdminPresentation(friendlyName = "URL", prominent = true)
	private String mediaUrl;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Refund getRefund() {
		return this.refund;
	}

	@Override
	public void setRefund(Refund refund) {
		this.refund = refund;
	}

	@Override
	public String getMediaUrl() {
		return this.mediaUrl;
	}

	@Override
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
}
