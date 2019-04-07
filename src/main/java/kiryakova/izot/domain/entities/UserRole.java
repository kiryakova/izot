package kiryakova.izot.domain.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole extends BaseEntity implements GrantedAuthority {
    //private String name;
    private String authority;

    public UserRole() {
    }

    public UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    @Column(name = "authority", nullable = false)
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

/*
    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Transient
    public String getAuthority() {
        return this.getName();
    }
*/
}
