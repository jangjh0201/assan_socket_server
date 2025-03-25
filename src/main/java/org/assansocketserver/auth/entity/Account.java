package org.assansocketserver.auth.entity;

import org.assansocketserver.domain.ward.entity.Ward;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "manager_tel")
    private String managerTel;

    @Column(name = "manager_email")
    private String managerEmail;

    @OneToOne(mappedBy = "account")
    private Ward ward;

    // 비밀번호 변경
    public void updatePassword(String password) {
        this.password = password;
    }

}
