package org.assansocketserver.auth.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.assansocketserver.auth.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Account account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return account.getRoleName();
            }
        });

        return collection;
    }

    public Long getId() {
        return account.getId();
    }

    @Override
    public String getPassword() {

        return account.getPassword();
    }

    @Override
    public String getUsername() {

        return account.getUsername();
    }

    public String getRole() {

        return account.getRoleName();
    }

    public String getName() {

        return account.getName();
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

        return true;
    }

}
