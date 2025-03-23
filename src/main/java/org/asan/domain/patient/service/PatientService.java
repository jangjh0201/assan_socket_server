package org.asan.domain.patient.service;

import org.asan.domain.patient.dto.PatientDTO;
import org.asan.domain.patient.dto.PatientRequest;
import org.asan.domain.patient.dto.PatientResponse;
import org.asan.domain.patient.entity.Patient;
import org.asan.domain.patient.repository.PatientRepository;
import org.asan.domain.patient.repository.PatientSpecification;
import org.asan.domain.ward.entity.Ward;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class PatientService {

        private final PatientRepository patientRepository;
        private final PatientFacade patientFacade;

        /**
         * 환자 목록 조회 (Specification 활용)
         * 
         * @param ward    병동
         * @param sort    정렬 기준 (number, name, sector, watch)
         * @param sortby  정렬 순서 (asc, desc)
         * @param page    페이지 번호 (1부터 시작)
         * @param gender  성별 (FEMALE, MALE)
         * @param keyword 이름 검색어
         * @return 환자 목록 및 페이징 정보를 담은 Map
         */
        public Map<String, Object> getPatients(Ward ward, String sort, String sortby,
                        Integer pageNo, String gender, String keyword) {

                // Pageable 생성: 클라이언트 페이지 번호는 1부터 시작하므로 -1 처리
                Pageable pageable = PageRequest.of(pageNo - 1, 10,
                                Sort.by(Sort.Direction.fromString(sortby.toUpperCase()), sort));

                // Specification 조건 조합: 병동, 성별, 이름 검색 조건을 모두 적용
                Specification<Patient> spec = Specification.where(PatientSpecification.wardIs(ward))
                                .and(PatientSpecification.genderIs(gender))
                                .and(PatientSpecification.searchByKeyword(keyword));

                // 조건과 Pageable을 적용하여 환자 목록 조회
                Page<Patient> patientsPage = patientRepository.findAll(spec, pageable);

                List<PatientDTO> patientList = IntStream.range(0, patientsPage.getContent().size())
                                .mapToObj(i -> {
                                        Patient patient = patientsPage.getContent().get(i);
                                        PatientDTO.PatientDTOBuilder builder = PatientDTO.builder()
                                                        .id(patient.getId())
                                                        .number(patient.getNumber())
                                                        .name(patient.getName())
                                                        .gender(patient.getGender())
                                                        .sectorId(patient.getSector().getId())
                                                        .sectorName(patient.getSector().getName())
                                                        .watchId(patient.getWatch().getId());

                                        if (i == 0) {
                                                builder.watchStatus(2) // 예시: 다른 값 적용
                                                                .watchBattery(90) // 예시: 다른 값 적용
                                                                .watchCharging(true); // 예시: 다른 값 적용
                                        } else if (i == 1) {
                                                builder.watchStatus(0) // 예시: 다른 값 적용
                                                                .watchBattery(0) // 예시: 다른 값 적용
                                                                .watchCharging(false); // 예시: 다른 값 적용
                                        } else if (i == 2) {
                                                builder.watchStatus(2) // 예시: 다른 값 적용
                                                                .watchBattery(30) // 예시: 다른 값 적용
                                                                .watchCharging(false); // 예시: 다른 값 적용
                                        } else if (i == 5) {
                                                builder.watchStatus(1) // 예시: 다른 값 적용
                                                                .watchBattery(70) // 예시: 다른 값 적용
                                                                .watchCharging(false); // 예시: 다른 값 적용
                                        } else {
                                                builder.watchStatus(2)
                                                                .watchBattery(50)
                                                                .watchCharging(true);
                                        }

                                        return builder.build();
                                })
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", patientsPage.getTotalElements(),
                                "total_pages", patientsPage.getTotalPages(),
                                "current_page", pageNo,
                                "per_page", pageable.getPageSize(),
                                "patients", patientList);
        }

        public Map<String, Object> getPatientsList(String keyword, Ward ward) {
                List<PatientDTO> patientList = patientRepository.findAllByWardAndNameContaining(ward, keyword).stream()
                                .map(patient -> PatientDTO.builder()
                                                .id(patient.getId())
                                                .name(patient.getName())
                                                .build())
                                .collect(Collectors.toList());

                return Map.of(
                                "total_count", patientList.size(),
                                "patients", patientList);
        }

        /**
         * (웹소켓) 환자 목록 및 위치 조회
         * 
         * @return 환자, 워치, 위치, 상태
         */
        public List<PatientDTO> getAllPatients() {
                List<Patient> patients = patientRepository.findAll();
                return patients.stream()
                                .map(patient -> PatientDTO.builder()
                                                .id(patient.getId())
                                                .number(patient.getNumber())
                                                .name(patient.getName())
                                                .sectorId(patient.getSector().getId())
                                                .sectorName(patient.getSector().getName())
                                                /**
                                                 * watchStatus, watchBattery, watchCharging은 임의로 설정
                                                 */
                                                .watchStatus(0)
                                                .watchBattery(90)
                                                .watchCharging(false)
                                                .build())
                                .collect(Collectors.toList());
        }

        public PatientResponse getPatient(Ward ward, Long id) {
                return patientFacade.readPatient(ward, id);
        }

        public Patient getPatient(Long id) {
                return patientRepository.findById(id).orElseThrow();
        }

        public void createPatient(Ward ward, PatientRequest request) {
                patientFacade.createPatient(ward, request);
        }

        public void updatePatient(Ward ward, Long id, PatientRequest request) {
                patientFacade.updatePatient(ward, id, request);
        }
}
