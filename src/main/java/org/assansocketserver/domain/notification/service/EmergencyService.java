package org.assansocketserver.domain.notification.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.assansocketserver.domain.notification.dto.EmergencyDTO;
import org.assansocketserver.domain.notification.entity.Notification;
import org.assansocketserver.domain.notification.repository.NotificationRepository;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.ward.entity.Ward;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmergencyService {

        private final MongoTemplate mongoTemplate;
        private final PatientRepository patientRepository;

        /**
         * MongoDB의 Notification 데이터를 startdate, enddate, sort, page, keyword를 반영하여
         * 조회합니다.
         *
         * @param ward      현재 병동
         * @param startdate 조회 시작 날짜 (yyMMdd 형식, 예: "000101")
         * @param enddate   조회 종료 날짜 (yyMMdd 형식, 예: "991231")
         * @param sort      정렬 기준 ("date", "name", "number", "sector", "watch")
         * @param sortby    정렬 순서 ("asc", "desc")
         * @param page      페이지 번호 (1부터 시작)
         * @param keyword   검색어 (emergency 이름 또는 환자 이름)
         * @return 페이징 처리된 응급 알림 데이터와 총 건수 정보
         */
        public Map<String, Object> getEmergencies(Ward ward, String startdate, String enddate,
                        String sort, String sortby, Integer page, String keyword) {
                // 1. 해당 병동에 속한 환자의 ID Set 구성 (JPA를 통해 조회)
                Set<Long> wardPatientIds = patientRepository.findAllByWard(ward).stream()
                                .map(Patient::getId)
                                .collect(Collectors.toSet());

                // 2. 날짜 변환: 클라이언트의 "yyMMdd" 값을 ISO 형식으로 변환
                String startIso = LocalDate.parse(startdate, DateTimeFormatter.ofPattern("yyMMdd"))
                                .atStartOfDay()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String endIso = LocalDate.parse(enddate, DateTimeFormatter.ofPattern("yyMMdd"))
                                .atTime(23, 59, 59, 999000000)
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // 3. 동적 쿼리 구성: 기본 조건 - category가 "risk"이고, 환자 ID가 해당 ward에 포함하며, timestamp 범위
                // 조건 적용
                Criteria criteria = Criteria.where("category").is("risk")
                                .and("data.patient_id").in(wardPatientIds)
                                .and("data.timestamp").gte(startIso).lte(endIso);

                // 4. keyword 필터: patient_room_name(sector_name) 또는 patient name(patient_name)에
                // keyword가 포함되는지
                if (keyword != null && !keyword.trim().isEmpty()) {
                        Criteria keywordCriteria = new Criteria().orOperator(
                                        Criteria.where("data.sector_name").regex(keyword, "i"),
                                        Criteria.where("data.patient_name").regex(keyword, "i"));
                        criteria = new Criteria().andOperator(criteria, keywordCriteria);
                }

                Query query = new Query(criteria);

                // 5. 정렬: sort 파라미터에 따라 정렬 필드 결정 (내림차순)
                String sortField = "data.timestamp"; // 기본은 date
                if ("name".equalsIgnoreCase(sort)) {
                        sortField = "data.risk_name";
                } else if ("sector".equalsIgnoreCase(sort)) {
                        sortField = "data.sector_name";
                } else if ("number".equalsIgnoreCase(sort)) {
                        sortField = "data.patient_number";
                }

                if ("asc".equalsIgnoreCase(sortby)) {
                        query.with(Sort.by(Sort.Direction.ASC, sortField));
                } else {
                        query.with(Sort.by(Sort.Direction.DESC, sortField));
                }
                // 6. 페이징: 페이지 번호는 1부터 시작, 페이지 당 10건
                int pageSize = 10;
                int skip = (page - 1) * pageSize;
                query.skip(skip).limit(pageSize);

                // 7. 전체 건수 조회 (페이징 조건 없이 count)
                Query countQuery = new Query(criteria); // 페이징 조건을 제외한 쿼리로 count를 수행
                long total = mongoTemplate.count(countQuery, Notification.class);

                // 8. 쿼리 실행하여 결과 Notification 리스트를 가져옴
                List<Notification> notifications = mongoTemplate.find(query, Notification.class);

                // 9. Notification을 EmergencyDTO로 매핑
                List<EmergencyDTO> emergencyList = notifications.stream().map(notification -> {
                        Map<String, Object> data = notification.getData();
                        Long emergencyId = (Long) data.get("risk_id");
                        String emergencyName = (String) data.get("risk_name");
                        Long patientId = (Long) data.get("patient_id");
                        String patientName = (String) data.get("patient_name");
                        Patient patient = patientRepository.findById(patientId).orElse(null);
                        String patientNumber = (patient != null) ? patient.getNumber() : null;
                        String sectorName = (String) data.get("sector_name");
                        String currentSectorName = (String) data.get("position");
                        String timestamp = (String) data.get("timestamp");
                        return EmergencyDTO.builder()
                                        .id(emergencyId)
                                        .name(emergencyName)
                                        .patientId(patientId)
                                        .patientName(patientName)
                                        .patientNumber(patientNumber)
                                        .patientRoomName(sectorName)
                                        .currentSectorName(currentSectorName)
                                        .duration(5)
                                        .timestamp(timestamp)
                                        .build();
                }).collect(Collectors.toList());

                int totalPages = (int) Math.ceil((double) total / pageSize);
                return Map.of(
                                "total_count", total,
                                "total_pages", totalPages,
                                "current_page", page,
                                "per_page", pageSize,
                                "emergencies", emergencyList);
        }
}