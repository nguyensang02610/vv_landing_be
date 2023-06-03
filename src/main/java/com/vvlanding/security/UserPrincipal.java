package com.vvlanding.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.vvlanding.table.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Boolean active;

    private String image;

    private String title;

    private String phone;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String password, Boolean active,
                         String email, String phone, String image, String title,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.phone = phone;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user,Collection<? extends GrantedAuthority> authorities) {
        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getActive(),
                user.getEmail(),
                user.getPhone(),
                user.getImage(),
                user.getTitle(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Boolean getActive() {
        return active;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (active == true)
            return true;
        else
            return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
