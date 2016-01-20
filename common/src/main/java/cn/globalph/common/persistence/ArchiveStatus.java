package cn.globalph.common.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.sandbox.SandBoxNonProductionSkip;

@Embeddable
public class ArchiveStatus implements Serializable, SandBoxNonProductionSkip {

    @Column(name = "ARCHIVED")
    @AdminPresentation(friendlyName = "archived", visibility = VisibilityEnum.HIDDEN_ALL, group = "ArchiveStatus")
    protected Character archived = 'N';

    public Character getArchived() {
        return archived;
    }

    public void setArchived(Character archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArchiveStatus)) return false;

        ArchiveStatus that = (ArchiveStatus) o;

        if (archived != null ? !archived.equals(that.archived) : that.archived != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return archived != null ? archived.hashCode() : 0;
    }
}
