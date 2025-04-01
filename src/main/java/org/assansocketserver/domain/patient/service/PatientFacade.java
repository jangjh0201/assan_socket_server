package org.assansocketserver.domain.patient.service;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.patient.dto.PatientDTO;
import org.assansocketserver.domain.patient.dto.PatientRequest;
import org.assansocketserver.domain.patient.dto.PatientResponse;
import org.assansocketserver.domain.patient.entity.Patient;
import org.assansocketserver.domain.patient.enums.Gender;
import org.assansocketserver.domain.patient.repository.PatientRepository;
import org.assansocketserver.domain.riskgroup.dto.RiskGroupDTO;
import org.assansocketserver.domain.riskgroup.entity.RiskGroup;
import org.assansocketserver.domain.riskgroup.repository.RiskGroupRepository;
import org.assansocketserver.domain.sector.dto.SectorDTO;
import org.assansocketserver.domain.sector.entity.Sector;
import org.assansocketserver.domain.sector.repository.SectorRepository;
import org.assansocketserver.domain.ward.entity.Ward;
import org.assansocketserver.domain.watch.repository.WatchRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientFacade {

        private final PatientRepository patientRepository;
        private final SectorRepository sectorRepository;
        private final RiskGroupRepository riskGroupRepository;
        private final WatchRepository watchRepository;

        @Transactional(readOnly = true)
        public PatientResponse readPatient(Ward ward, Long id) {
                // 환자 존재 여부 및 병동 일치 여부 확인
                Patient patient = patientRepository.findById(id)
                                .filter(p -> p.getWard().equals(ward))
                                .orElseThrow(() -> new NoSuchElementException("해당 환자를 찾을 수 없습니다. id: " + id));
                return PatientResponse.builder()
                                .id(patient.getId())
                                .name(patient.getName())
                                .number(patient.getNumber().toString())
                                .gender(patient.getGender().name())
                                .sectorId(patient.getSector().getId())
                                .sectorName(patient.getSector().getName())
                                .heartRateMin(patient.getMinHeartRate())
                                .heartRateMax(patient.getMaxHeartRate())
                                // noContact로 지정된 환자 id, name
                                .noContacts(patient.getNoContacts().stream()
                                                .map(noContact -> patientRepository
                                                                .findById(noContact.getNoContactPatient().getId())
                                                                .map(noContactPatient -> PatientDTO.builder()
                                                                                .id(noContactPatient.getId())
                                                                                .name(noContactPatient.getName())
                                                                                .build()))
                                                .map(Optional::get)
                                                .collect(Collectors.toList()))
                                .restrictedAreas(patient.getRestrictedAreas().stream()
                                                .map(restrictedArea -> sectorRepository
                                                                .findById(restrictedArea.getSector().getId())
                                                                .map(sector -> SectorDTO.builder()
                                                                                .id(sector.getId())
                                                                                .name(sector.getName())
                                                                                .build()))
                                                .map(Optional::get)
                                                .collect(Collectors.toList()))
                                .highRiskGroups(patient.getHighRiskGroups().stream()
                                                .map(highRiskGroup -> riskGroupRepository
                                                                .findById(highRiskGroup.getRiskGroup().getId())
                                                                .map(riskGroup -> RiskGroupDTO.builder()
                                                                                .id(riskGroup.getId())
                                                                                .name(riskGroup.getName())
                                                                                .build()))
                                                .map(Optional::get)
                                                .collect(Collectors.toList()))
                                .watchId(patient.getWatch().getId())
                                .build();
        }

        @Transactional
        public void createPatient(Ward ward, PatientRequest request) {
                // 환자 번호 중복 체크
                if (patientRepository.existsByNumber(request.getNumber())) {
                        throw new IllegalArgumentException("이미 존재하는 환자 번호입니다. : " + request.getNumber());
                }

                // Patient 엔티티 생성
                Patient patient = Patient.builder()
                                .name(request.getName())
                                .number(request.getNumber())
                                .gender(Gender.valueOf(request.getGender()))
                                .minHeartRate(request.getHeartRateMin())
                                .maxHeartRate(request.getHeartRateMax())
                                .watch(watchRepository.findById(request.getWatchId())
                                                .orElseThrow(() -> new EntityNotFoundException("Watch not found")))
                                .sector(sectorRepository.findByName(request.getSectorName())
                                                .orElseThrow(() -> new EntityNotFoundException("Sector not found")))
                                .ward(ward)
                                .build();

                // NoContact 등록
                List<PatientDTO> noContacts = request.getNoContacts();
                if (noContacts != null) {
                        for (PatientDTO noContact : noContacts) {
                                Long noContactId = noContact.getId();
                                Optional<Patient> optionalPatient = patientRepository.findById(noContactId);
                                if (optionalPatient.isPresent()) {
                                        patient.addNoContact(optionalPatient.get());
                                }
                        }
                }

                // RestrictedArea 등록
                List<SectorDTO> restrictedAreas = request.getRestrictedAreas();
                if (restrictedAreas != null) {
                        for (SectorDTO restrictedArea : restrictedAreas) {
                                Long restrictedAreaId = restrictedArea.getId();
                                Optional<Sector> optionalSector = sectorRepository.findById(restrictedAreaId);
                                if (optionalSector.isPresent()) {
                                        patient.addRestrictedArea(optionalSector.get());
                                }
                        }
                }

                // RiskGroup 등록
                List<RiskGroupDTO> highRiskGroups = request.getRiskGroups();
                if (highRiskGroups != null) {
                        for (RiskGroupDTO highRiskGroup : highRiskGroups) {
                                Long highRiskGroupId = highRiskGroup.getId();
                                Optional<RiskGroup> optionalRiskGroup = riskGroupRepository.findById(highRiskGroupId);
                                if (optionalRiskGroup.isPresent()) {
                                        patient.addHighRiskGroup(optionalRiskGroup.get());
                                }
                        }
                }

                patientRepository.save(patient);
        }

        @Transactional
        public void updatePatient(Ward ward, Long id, PatientRequest request) {
                Patient patient = patientRepository.findById(id)
                                .filter(p -> p.getWard().equals(ward))
                                .orElseThrow(() -> new NoSuchElementException("해당 환자를 찾을 수 없습니다. id: " + id));
                // 환자 번호 중복 체크(자기 번호 제외)
                if (patientRepository.existsByNumber(request.getNumber()) && !patient.getNumber().equals(request.getNumber())) {
                        throw new IllegalArgumentException("이미 존재하는 환자 번호입니다. : " + request.getNumber());
                }

                // 기존 연관관계 제거
                patient.removeNoContacts();
                patient.removeRestrictedAreas();
                patient.removeHighRiskGroups();

                // patient 객체를 기준으로 연관관계 추가
                List<PatientDTO> noContacts = request.getNoContacts();
                if (noContacts != null) {
                        for (PatientDTO noContact : noContacts) {
                                Long noContactId = noContact.getId();
                                Optional<Patient> optionalPatient = patientRepository.findById(noContactId);
                                if (optionalPatient.isPresent()) {
                                        patient.addNoContact(optionalPatient.get());
                                }
                        }
                } else {
                        patient.removeNoContacts();
                }

                List<SectorDTO> restrictedAreas = request.getRestrictedAreas();
                if (restrictedAreas != null) {
                        for (SectorDTO restrictedArea : restrictedAreas) {
                                Long restrictedAreaId = restrictedArea.getId();
                                Optional<Sector> optionalSector = sectorRepository.findById(restrictedAreaId);
                                if (optionalSector.isPresent()) {
                                        patient.addRestrictedArea(optionalSector.get());
                                }
                        }
                } else {
                        patient.removeRestrictedAreas();
                }

                List<RiskGroupDTO> highRiskGroups = request.getRiskGroups();
                if (highRiskGroups != null) {
                        for (RiskGroupDTO highRiskGroup : highRiskGroups) {
                                Long highRiskGroupId = highRiskGroup.getId();
                                Optional<RiskGroup> optionalRiskGroup = riskGroupRepository.findById(highRiskGroupId);
                                if (optionalRiskGroup.isPresent()) {
                                        patient.addHighRiskGroup(optionalRiskGroup.get());
                                }
                        }
                } else {
                        patient.removeHighRiskGroups();
                }

                // 그 외 업데이트
                patient.update(Patient.builder()
                                .name(request.getName())
                                .number(request.getNumber())
                                .gender(Gender.valueOf(request.getGender()))
                                .minHeartRate(request.getHeartRateMin())
                                .maxHeartRate(request.getHeartRateMax())
                                .watch(watchRepository.findById(request.getWatchId())
                                                .orElseThrow(() -> new EntityNotFoundException("Watch not found")))
                                // 추후 getId로 가져오는 방식으로 변경
                                .sector(sectorRepository.findByName(request.getSectorName())
                                                .orElseThrow(() -> new EntityNotFoundException("Sector not found")))
                                .build());
        }
}