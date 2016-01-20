package cn.globalph.passport.domain;

import cn.globalph.common.time.domain.TemporalTimestampListener;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@EntityListeners(value = { TemporalTimestampListener.class })
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_ROLE")
public class RoleImpl implements Role {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "RoleId")
    @GenericGenerator(
        name="RoleId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="RoleImpl"),
            @Parameter(name="entity_name", value="cn.globalph.passport.domain.RoleImpl")
        }
    )
    @Column(name = "ROLE_ID")
    protected Long id;

    @Column(name = "ROLE_NAME", nullable = false)
    @Index(name="ROLE_NAME_INDEX", columnNames={"ROLE_NAME"})
    protected String roleName;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getRoleName() {
        return roleName;
    }

    @Override
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
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
        RoleImpl other = (RoleImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (roleName == null) {
            if (other.roleName != null)
                return false;
        } else if (!roleName.equals(other.roleName))
            return false;
        return true;
    }

}
