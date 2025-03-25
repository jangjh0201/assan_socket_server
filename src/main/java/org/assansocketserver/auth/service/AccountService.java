package org.assansocketserver.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.assansocketserver.auth.dto.AccountDTO;
import org.assansocketserver.auth.entity.Account;
import org.assansocketserver.auth.repository.AccountRepository;
import org.assansocketserver.auth.repository.AccountSpecification;
import org.assansocketserver.domain.ward.service.WardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WardService wardService;

    public void create(AccountDTO accountDTO) {

        String username = accountDTO.getUsername();
        String password = bCryptPasswordEncoder.encode("ROH1234!");

        if (accountRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 username입니다.");
        }

        if (accountDTO.getRoleName() == null) {
            throw new RuntimeException("RoleName을 입력해주세요.");
        }

        Account.AccountBuilder builder = Account.builder()
                .username(username)
                .password(password)
                .roleName(accountDTO.getRoleName())
                .hospitalName(accountDTO.getHospitalName())
                .wardName(accountDTO.getWardName())
                .managerName(accountDTO.getManagerName())
                .managerTel(accountDTO.getManagerTel())
                .managerEmail(accountDTO.getManagerEmail());

        if ("ROLE_USER".equals(accountDTO.getRoleName())) {
            builder.name(accountDTO.getWardName());
            accountRepository.save(builder.build());
            wardService.create(accountDTO);
        } else if ("ROLE_ADMIN".equals(accountDTO.getRoleName()) || "ROLE_SUPER".equals(accountDTO.getRoleName())) {
            builder.name(accountDTO.getHospitalName());
            accountRepository.save(builder.build());
        }

    }

    /**
     * 현재 로그인한 사용자의 프로필 정보 조회
     */
    public AccountDTO getProfile(UserDetails userDetails) {
        Account account = getCurrentAccount(userDetails);

        return AccountDTO.builder()
                .username(account.getUsername())
                .managerName(account.getManagerName())
                .managerTel(account.getManagerTel())
                .managerEmail(account.getManagerEmail())
                .build();
    }

    /**
     * 현재 로그인한 사용자의 비밀번호 확인
     */
    public boolean verifyPassword(UserDetails userDetails, String password) {
        Account account = getCurrentAccount(userDetails);
        return bCryptPasswordEncoder.matches(password, account.getPassword());
    }

    /**
     * 현재 로그인한 사용자의 비밀번호 변경
     */
    public void updatePassword(UserDetails userDetails, String newPassword) {
        Account account = getCurrentAccount(userDetails);
        String password = bCryptPasswordEncoder.encode(newPassword);
        account.updatePassword(password);
        accountRepository.save(account);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getRoleName().equals("ROLE_USER"))
                .collect(Collectors.toList());
    }

    public Page<Account> getAccounts(Pageable pageable) {
        // 기본 전체 조회 시에도 wardName이 있는 계정만 조회
        Specification<Account> spec = AccountSpecification.hasWardName();
        return accountRepository.findAll(spec, pageable);
    }

    public Page<Account> getAccountsByName(String keyword, Pageable pageable) {
        // 이름 검색과 wardName 조건을 모두 적용
        Specification<Account> spec = Specification
                .where(AccountSpecification.nameContains(keyword))
                .and(AccountSpecification.hasWardName());
        return accountRepository.findAll(spec, pageable);
    }

    public Account getAccount(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * 현재 로그인한 사용자의 Account 정보 가져오기
     */
    public Account getCurrentAccount(UserDetails userDetails) {
        return accountRepository.findByUsername(userDetails.getUsername());
    }

}