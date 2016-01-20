package cn.globalph.openadmin.dto;

import java.io.Serializable;

/**
 * @author Jeff Fischer
 */
public class SectionCrumb implements Serializable {

	 private static final long serialVersionUID = -7861144066647102753L;
	 
	 protected String sectionIdentifier;
    protected String sectionId;

    public String getSectionIdentifier() {
        return sectionIdentifier;
    }

    public void setSectionIdentifier(String sectionIdentifier) {
        this.sectionIdentifier = sectionIdentifier;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SectionCrumb{");
        sb.append("sectionIdentifier='").append(sectionIdentifier).append('\'');
        sb.append(", sectionId='").append(sectionId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SectionCrumb)) return false;

        SectionCrumb that = (SectionCrumb) o;

        if (sectionId != null ? !sectionId.equals(that.sectionId) : that.sectionId != null) return false;
        if (sectionIdentifier != null ? !sectionIdentifier.equals(that.sectionIdentifier) : that.sectionIdentifier !=
                null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sectionIdentifier != null ? sectionIdentifier.hashCode() : 0;
        result = 31 * result + (sectionId != null ? sectionId.hashCode() : 0);
        return result;
    }
}
