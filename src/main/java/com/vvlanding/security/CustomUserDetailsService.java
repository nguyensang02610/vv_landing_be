package com.vvlanding.security;

import com.vvlanding.exception.ResourceNotFoundException;
import com.vvlanding.repo.RepoUser;
import com.vvlanding.table.ShopUserRole;
import com.vvlanding.table.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    RepoUser repoUser;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = repoUser.findByUsername(usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                );
        Set<ShopUserRole> roles = user.getShopUserRoles();
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        for (ShopUserRole s:
             roles) {
            GrantedAuthority authority = new SimpleGrantedAuthority(s.getRole());
            grantList.add(authority);
        }
        return UserPrincipal.create(user,grantList);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = repoUser.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        Set<ShopUserRole> roles = user.getShopUserRoles();
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        for (ShopUserRole s:
                roles) {
            GrantedAuthority authority = new SimpleGrantedAuthority(s.getRole());
            grantList.add(authority);
        }
        return UserPrincipal.create(user,grantList);
    }
}
