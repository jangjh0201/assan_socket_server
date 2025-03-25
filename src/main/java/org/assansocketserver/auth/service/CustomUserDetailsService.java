package org.assansocketserver.auth.service;

import org.assansocketserver.auth.dto.CustomUserDetails;
import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.auth.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다 : " + username);
        }
        return new CustomUserDetails(account);
    }
    
}
