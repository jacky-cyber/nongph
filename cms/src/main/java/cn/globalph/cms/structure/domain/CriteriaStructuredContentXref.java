package cn.globalph.cms.structure.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The purpose of this entity is to simply enforce the not null constraints on the two
 * columns in this join table. Otherwise, during DDL creation, Hibernate was
 * creating incompatible SQL for MS SQLServer.
 */
@Entity
@Table(name = "CMS_QUAL_CRIT_SC_XREF")
@Inheritance(strategy=InheritanceType.JOINED)
public class CriteriaStructuredContentXref {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The category id. */
    @EmbeddedId
    CriteriaStructuredContentXrefPK criteriaStructuredContentXrefPK = new CriteriaStructuredContentXrefPK();

    public CriteriaStructuredContentXrefPK getCriteriaStructuredContentXrefPK() {
        return criteriaStructuredContentXrefPK;
    }

    public void setCriteriaStructuredContentXrefPK(final CriteriaStructuredContentXrefPK criteriaStructuredContentXrefPK) {
        this.criteriaStructuredContentXrefPK = criteriaStructuredContentXrefPK;
    }

    public static class CriteriaStructuredContentXrefPK implements Serializable {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @ManyToOne(targetEntity = StructuredContentImpl.class, optional=false)
        @JoinColumn(name = "SC_ID")
        protected StructuredContent structuredContent = new StructuredContentImpl();
        
        @ManyToOne(targetEntity = StructuredContentItemCriteriaImpl.class, optional=false)
        @JoinColumn(name = "SC_ITEM_CRITERIA_ID")
        protected StructuredContentItemCriteria structuredContentItemCriteria = new StructuredContentItemCriteriaImpl();

        public StructuredContent getStructuredContent() {
            return structuredContent;
        }

        public void setStructuredContent(StructuredContent structuredContent) {
            this.structuredContent = structuredContent;
        }

        public StructuredContentItemCriteria getStructuredContentItemCriteria() {
            return structuredContentItemCriteria;
        }

        public void setStructuredContentItemCriteria(StructuredContentItemCriteria structuredContentItemCriteria) {
            this.structuredContentItemCriteria = structuredContentItemCriteria;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((structuredContent == null) ? 0 : structuredContent.hashCode());
            result = prime * result + ((structuredContentItemCriteria == null) ? 0 : structuredContentItemCriteria.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CriteriaStructuredContentXrefPK other = (CriteriaStructuredContentXrefPK) obj;
            if (structuredContent == null) {
                if (other.structuredContent != null)
                    return false;
            } else if (!structuredContent.equals(other.structuredContent))
                return false;
            if (structuredContentItemCriteria == null) {
                if (other.structuredContentItemCriteria != null)
                    return false;
            } else if (!structuredContentItemCriteria.equals(other.structuredContentItemCriteria))
                return false;
            return true;
        }

    }
}
