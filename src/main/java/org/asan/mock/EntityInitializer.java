package org.asan.mock;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asan.auth.entity.Account;
import org.asan.auth.repository.AccountRepository;
import org.asan.domain.notification.entity.Notification;
import org.asan.domain.notification.repository.NotificationRepository;
import org.asan.domain.patient.entity.NoContact;
import org.asan.domain.patient.entity.Patient;
import org.asan.domain.patient.entity.RestrictedArea;
import org.asan.domain.patient.enums.Gender;
import org.asan.domain.patient.repository.NoContactRepository;
import org.asan.domain.patient.repository.PatientRepository;
import org.asan.domain.patient.repository.RestrictedAreaRepository;
import org.asan.domain.post.dto.PostRequest;
import org.asan.domain.post.entity.Post;
import org.asan.domain.post.repository.PostRepository;
import org.asan.domain.post.service.PostService;
import org.asan.domain.risk.entity.Risk;
import org.asan.domain.risk.enums.Severity;
import org.asan.domain.risk.repository.RiskRepository;
import org.asan.domain.risk.entity.RiskType;
import org.asan.domain.risk.repository.RiskTypeRepository;
import org.asan.domain.riskgroup.entity.RiskGroup;
import org.asan.domain.riskgroup.repository.RiskGroupRepository;
import org.asan.domain.sector.entity.Sector;
import org.asan.domain.sector.enums.SectorType;
import org.asan.domain.sector.repository.SectorRepository;
import org.asan.domain.ward.entity.Ward;
import org.asan.domain.ward.repository.WardRepository;
import org.asan.domain.watch.entity.Watch;
import org.asan.domain.watch.repository.WatchRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@Profile({ "local", "dev" }) // 로컬, 개발 환경에서만 실행
@RequiredArgsConstructor
@Slf4j
public class EntityInitializer {

        // Account 관련
        private final AccountRepository accountRepository;
        private final PasswordEncoder passwordEncoder;

        // Notification 관련
        private final NotificationRepository notificationRepository;

        // 그 외 엔티티 관련
        private final WardRepository wardRepository;
        private final RiskGroupRepository riskGroupRepository;
        private final SectorRepository sectorRepository;
        private final RiskTypeRepository riskTypeRepository;
        private final RiskRepository riskRepository;
        private final WatchRepository watchRepository;
        private final PatientRepository patientRepository;
        private final NoContactRepository noContactRepository;
        private final RestrictedAreaRepository restrictedAreaRepository;
        private final PostRepository postRepository;

        @PostConstruct
        public void init() {
                // **기존 데이터 삭제 (종속 관계 고려하여 순서 삭제)**
                notificationRepository.deleteAll();
                postRepository.deleteAll();
                restrictedAreaRepository.deleteAll();
                noContactRepository.deleteAll();
                patientRepository.deleteAll();
                watchRepository.deleteAll();
                riskRepository.deleteAll();
                riskTypeRepository.deleteAll();
                riskGroupRepository.deleteAll();
                sectorRepository.deleteAll();
                wardRepository.deleteAll();
                accountRepository.deleteAll(); // ward보다 나중에 account 테이블 삭제

                // **1. Account 생성**
                List<Account> accounts = List.of(
                                Account.builder()
                                                .name("174병동")
                                                .roleName("ROLE_USER")
                                                .hospitalName("연중병원")
                                                .wardName("174병동")
                                                .managerName("임하민")
                                                .managerTel("01012345678")
                                                .managerEmail("user@mail.com")
                                                .username("user123")
                                                .password(passwordEncoder.encode("user123123"))
                                                .build(),
                                Account.builder()
                                                .name("연중병원")
                                                .roleName("ROLE_ADMIN")
                                                .hospitalName("연중병원")
                                                .managerName("신항식")
                                                .managerTel("01012345678")
                                                .managerEmail("admin@mail.com")
                                                .username("admin123")
                                                .password(passwordEncoder.encode("admin123123"))
                                                .build(),
                                Account.builder()
                                                .name("아산병원")
                                                .roleName("ROLE_SUPER")
                                                .hospitalName("아산병원")
                                                .managerName("이연진")
                                                .managerTel("01012345678")
                                                .managerEmail("super@mail.com")
                                                .username("super123")
                                                .password(passwordEncoder.encode("super123123"))
                                                .build());
                accountRepository.saveAll(accounts);
                log.info("Account 초기화 완료");

                // **2. Ward 생성**
                List<Ward> wards = List.of(
                                Ward.builder()
                                                .name("174병동")
                                                .account(accounts.get(0))
                                                .build());
                wardRepository.saveAll(wards);
                log.info("Ward 초기화 완료");

                // **3. Sector 생성**
                List<Sector> sectors = List.of(
                                Sector.builder()
                                                .name("17401호")
                                                .startX(BigDecimal.valueOf(175.0))
                                                .startY(BigDecimal.valueOf(75.0))
                                                .endX(BigDecimal.valueOf(346.0))
                                                .endY(BigDecimal.valueOf(321.0))
                                                .sectorType(SectorType.MALE)
                                                .ward(wards.get(0))
                                                .build(),
                                Sector.builder()
                                                .name("17402호")
                                                .startX(BigDecimal.valueOf(987.0))
                                                .startY(BigDecimal.valueOf(413.0))
                                                .endX(BigDecimal.valueOf(1414.0))
                                                .endY(BigDecimal.valueOf(757.0))
                                                .sectorType(SectorType.RESTRICTED)
                                                .ward(wards.get(0))
                                                .build(),
                                Sector.builder()
                                                .name("17403호")
                                                .startX(BigDecimal.valueOf(588.0))
                                                .startY(BigDecimal.valueOf(1020.0))
                                                .endX(BigDecimal.valueOf(714.0))
                                                .endY(BigDecimal.valueOf(1360.0))
                                                .sectorType(SectorType.PUBLIC)
                                                .ward(wards.get(0))
                                                .build(),
                                Sector.builder()
                                                .name("17404호")
                                                .startX(BigDecimal.valueOf(1360.0))
                                                .startY(BigDecimal.valueOf(1020.0))
                                                .endX(BigDecimal.valueOf(1567.0))
                                                .endY(BigDecimal.valueOf(1400.0))
                                                .sectorType(SectorType.FEMALE)
                                                .ward(wards.get(0))
                                                .build());
                sectorRepository.saveAll(sectors);
                log.info("Sector 초기화 완료");

                // **4. Riskgroup 생성**
                List<RiskGroup> riskGroups = List.of(
                                RiskGroup.builder()
                                                .name("자살")
                                                .ward(wards.get(0))
                                                .build(),
                                RiskGroup.builder()
                                                .name("자해")
                                                .ward(wards.get(0))
                                                .build(),
                                RiskGroup.builder()
                                                .name("낙상")
                                                .ward(wards.get(0))
                                                .build(),
                                RiskGroup.builder()
                                                .name("욕창")
                                                .ward(wards.get(0))
                                                .build());
                riskGroupRepository.saveAll(riskGroups);
                log.info("RiskGroup 초기화 완료");

                // **5. RiskType 생성**
                List<RiskType> riskTypes = List.of(
                                RiskType.builder()
                                                .name("워치 탈착")
                                                .build(),
                                RiskType.builder()
                                                .name("고심박")
                                                .build(),
                                RiskType.builder()
                                                .name("저심박")
                                                .build());
                riskTypeRepository.saveAll(riskTypes);
                log.info("RiskType 초기화 완료");

                // **6. Risk 생성 (Ward, RiskType 연관)**
                List<Risk> risks = List.of(
                                Risk.builder()
                                                .availability(true)
                                                .severity(Severity.HIGH)
                                                .ward(wards.get(0))
                                                .riskType(riskTypes.get(0))
                                                .build(),
                                Risk.builder()
                                                .availability(false)
                                                .severity(Severity.MID)
                                                .ward(wards.get(0))
                                                .riskType(riskTypes.get(1))
                                                .build(),
                                Risk.builder()
                                                .availability(true)
                                                .severity(Severity.LOW)
                                                .ward(wards.get(0))
                                                .riskType(riskTypes.get(2))
                                                .build());
                riskRepository.saveAll(risks);
                log.info("Risk 초기화 완료");

                // **7. Watch 생성**
                List<Watch> watches = List.of(
                                Watch.builder()
                                                .uuid("watch-uuid-001")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-002")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-003")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-004")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-005")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-006")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-007")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-008")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-009")
                                                .build(),
                                Watch.builder()
                                                .uuid("watch-uuid-0010")
                                                .build());
                watchRepository.saveAll(watches);
                log.info("Watch 초기화 완료");

                // **8. Patient 생성 (Watch, Sector, RiskGroup 연관)**
                List<Patient> patients = List.of(
                                Patient.builder()
                                                .name("곽철용")
                                                .number("16")
                                                .gender(Gender.MALE)
                                                .minHeartRate(60)
                                                .maxHeartRate(100)
                                                .watch(watches.get(0))
                                                .sector(sectors.get(0))
                                                .riskGroup(riskGroups.get(0))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("김당식")
                                                .number("24")
                                                .gender(Gender.MALE)
                                                .minHeartRate(55)
                                                .maxHeartRate(95)
                                                .watch(watches.get(1))
                                                .sector(sectors.get(0))
                                                .riskGroup(riskGroups.get(1))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("이햄식")
                                                .number("5")
                                                .gender(Gender.FEMALE)
                                                .minHeartRate(70)
                                                .maxHeartRate(90)
                                                .watch(watches.get(2))
                                                .sector(sectors.get(2))
                                                .riskGroup(riskGroups.get(0))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("임재호")
                                                .number("3")
                                                .gender(Gender.MALE)
                                                .minHeartRate(80)
                                                .maxHeartRate(110)
                                                .watch(watches.get(3))
                                                .sector(sectors.get(0))
                                                .riskGroup(riskGroups.get(2))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("장연정")
                                                .number("72")
                                                .gender(Gender.FEMALE)
                                                .minHeartRate(58)
                                                .maxHeartRate(89)
                                                .watch(watches.get(4))
                                                .sector(sectors.get(3))
                                                .riskGroup(riskGroups.get(1))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("이준형")
                                                .number("92")
                                                .gender(Gender.MALE)
                                                .minHeartRate(60)
                                                .maxHeartRate(80)
                                                .watch(watches.get(5))
                                                .sector(sectors.get(1))
                                                .riskGroup(riskGroups.get(2))
                                                .ward(wards.get(0))
                                                .build(),
                                Patient.builder()
                                                .name("박연진")
                                                .number("54")
                                                .gender(Gender.FEMALE)
                                                .minHeartRate(60)
                                                .maxHeartRate(80)
                                                .watch(watches.get(6))
                                                .sector(sectors.get(3))
                                                .riskGroup(riskGroups.get(2))
                                                .ward(wards.get(0))
                                                .build());

                patientRepository.saveAll(patients);
                log.info("Patient 초기화 완료");

                // **10. NoContact 생성 (환자 간 관계)**
                List<NoContact> noContacts = List.of(
                                NoContact.builder()
                                                .patient(patients.get(0))
                                                .noContactPatient(patients.get(1))
                                                .build());
                noContactRepository.saveAll(noContacts);
                log.info("NoContact 초기화 완료");

                // **11. RestrictedArea 생성 (환자와 섹터 연관)**
                List<RestrictedArea> restrictedAreas = List.of(
                                RestrictedArea.builder()
                                                .patient(patients.get(0))
                                                .sector(sectors.get(0))
                                                .build(),
                                RestrictedArea.builder()
                                                .patient(patients.get(0))
                                                .sector(sectors.get(2))
                                                .build(),
                                RestrictedArea.builder()
                                                .patient(patients.get(1))
                                                .sector(sectors.get(0))
                                                .build(),
                                RestrictedArea.builder()
                                                .patient(patients.get(1))
                                                .sector(sectors.get(2))
                                                .build());
                restrictedAreaRepository.saveAll(restrictedAreas);
                log.info("RestrictedArea 초기화 완료");

                // **13. Post 생성**
                PostService postService = new PostService(postRepository);
                Random random = new Random();
                for (int i = 1; i < 14; i++) {
                        postService.createPost(accounts.get(2), PostRequest.builder()
                                        .title("연중병원 공지사항" + i)
                                        .content("연중병원 공지사항입니다. -이연진-")
                                        .notification(random.nextBoolean()) // true 또는 false 랜덤 설정
                                        .build());
                }
                log.info("Post 초기화 완료");

                // **14. Notification 생성**
                ZonedDateTime kstTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

                List<Notification> notifications = List.of(
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(0).getId(),
                                                                "risk_name", risks.get(0).getRiskType().getName(),
                                                                "patient_id", patients.get(0).getId(),
                                                                "patient_name", patients.get(0).getName(),
                                                                "sector_id", sectors.get(0).getId(),
                                                                "sector_name", sectors.get(0).getName(),
                                                                "position", sectors.get(2).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(0).getName(),
                                                                                patients.get(0).getSector().getName(),
                                                                                risks.get(0).getRiskType().getName()),
                                                                "timestamp", kstTime.format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("info")
                                                .data(Map.of(
                                                                "info_id", 1,
                                                                "info_name", "워치 충전 필요",
                                                                "message", String.format("%s(%s)님 워치(ID : %s) 충전 필요",
                                                                                patients.get(6).getName(),
                                                                                patients.get(6).getSector().getName(),
                                                                                patients.get(6).getWatch().getId()),
                                                                "timestamp", kstTime.plusSeconds(1).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("notice")
                                                .data(Map.of(
                                                                "notice_id", 1,
                                                                "notice_title", "[공지사항] 연중병원 공지사항",
                                                                "notice_author", accounts.get(2).getName(),
                                                                "message", "[공지사항] 연중병원 공지사항 by 총괄관리자",
                                                                "timestamp", kstTime.plusSeconds(2).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(1).getId(),
                                                                "risk_name", risks.get(1).getRiskType().getName(),
                                                                "patient_id", patients.get(1).getId(),
                                                                "patient_name", patients.get(1).getName(),
                                                                "sector_id", sectors.get(0).getId(),
                                                                "sector_name", sectors.get(0).getName(),
                                                                "position", sectors.get(2).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(1).getName(),
                                                                                sectors.get(0).getName(),
                                                                                risks.get(1).getRiskType().getName()),
                                                                "timestamp", kstTime.plusSeconds(3).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(2).getId(),
                                                                "risk_name", risks.get(2).getRiskType().getName(),
                                                                "patient_id", patients.get(2).getId(),
                                                                "patient_name", patients.get(2).getName(),
                                                                "sector_id", sectors.get(2).getId(),
                                                                "sector_name", sectors.get(2).getName(),
                                                                "position", sectors.get(1).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(2).getName(),
                                                                                sectors.get(2).getName(),
                                                                                risks.get(2).getRiskType().getName()),
                                                                "timestamp", kstTime.plusSeconds(4).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(0).getId(),
                                                                "risk_name", risks.get(0).getRiskType().getName(),
                                                                "patient_id", patients.get(0).getId(),
                                                                "patient_name", patients.get(0).getName(),
                                                                "sector_id", sectors.get(0).getId(),
                                                                "sector_name", sectors.get(0).getName(),
                                                                "position", sectors.get(2).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(0).getName(),
                                                                                sectors.get(0).getName(),
                                                                                risks.get(0).getRiskType().getName()),
                                                                "timestamp", kstTime.plusSeconds(5).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(1).getId(),
                                                                "risk_name", risks.get(1).getRiskType().getName(),
                                                                "patient_id", patients.get(1).getId(),
                                                                "patient_name", patients.get(1).getName(),
                                                                "sector_id", sectors.get(0).getId(),
                                                                "sector_name", sectors.get(0).getName(),
                                                                "position", sectors.get(0).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(1).getName(),
                                                                                sectors.get(0).getName(),
                                                                                risks.get(1).getRiskType().getName()),
                                                                "timestamp", kstTime.minusDays(1).format(formatter)))
                                                .isRead(false)
                                                .build(),
                                Notification.builder()
                                                .category("risk")
                                                .data(Map.of(
                                                                "risk_id", risks.get(2).getId(),
                                                                "risk_name", risks.get(2).getRiskType().getName(),
                                                                "patient_id", patients.get(5).getId(),
                                                                "patient_name", patients.get(5).getName(),
                                                                "sector_id", sectors.get(2).getId(),
                                                                "sector_name", sectors.get(2).getName(),
                                                                "position", sectors.get(1).getName(),
                                                                "message", String.format("%s(%s)님 %s 발생",
                                                                                patients.get(5).getName(),
                                                                                sectors.get(2).getName(),
                                                                                risks.get(2).getRiskType().getName()),
                                                                "timestamp", kstTime.minusDays(2).format(formatter)))
                                                .isRead(false)
                                                .build());

                notificationRepository.saveAll(notifications);
                log.info("Notification 초기화 완료");

                log.info("모든 엔티티 초기화 완료");
        }
}
