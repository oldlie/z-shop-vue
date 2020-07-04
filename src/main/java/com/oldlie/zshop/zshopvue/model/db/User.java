package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "t_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String nickname;
    @Column(name = "pwd")
    private String password;
    /**
     * 用户支付密码，可以明文存储，仅6位数字
     */
    @Column(columnDefinition = "char(6) default '888888'")
    private String payPassword;
    @Column(columnDefinition = "BIT default 1")
    private boolean isAccountNonExpired;
    @Column(columnDefinition = "BIT default 1")
    private boolean isAccountNonLocked;
    @Column(columnDefinition = "BIT default 1")
    private boolean isCredentialsNonExpired;
    @Column(columnDefinition = "BIT default 1")
    private boolean isEnabled;
    @ManyToMany
    @JoinTable(name = "t_user_join_role")
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> _list = new ArrayList<>();
        this.roles.forEach(r -> _list.add(new SimpleGrantedAuthority(r.getName())));
        return _list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
