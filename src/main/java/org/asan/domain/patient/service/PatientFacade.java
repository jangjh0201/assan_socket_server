package org.asan.domain.patient.service;

import lombok.RequiredArgsConstructor;

import org.asan.domain.patient.dto.PatientDTO;
import org.asan.domain.patient.dto.PatientRequest;
import org.asan.domain.patient.dto.PatientResponse;
import org.asan.domain.patient.entity.Patient;
import org.asan.domain.patient.enums.Gender;
import org.asan.domain.patient.repository.PatientRepository;
import org.asan.domain.riskgroup.repository.RiskGroupRepository;
import org.asan.domain.sector.entity.Sector;
import org.asan.domain.sector.repository.SectorRepository;
import org.asan.domain.ward.entity.Ward;
import org.asan.domain.sector.dto.SectorDTO;
import org.asan.domain.watch.repository.WatchRepository;
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
                                .Gender(patient.getGender().name())
                                .sectorId(patient.getSector().getId())
                                .sectorName(patient.getSector().getName())
                                .riskgroupId(patient.getRiskGroup() != null ? patient.getRiskGroup().getId() : null)
                                .riskgroupName(patient.getRiskGroup() != null ? patient.getRiskGroup().getName() : null)
                                .heartRateMin(patient.getMinHeartRate())
                                .heartRateMax(patient.getMaxHeartRate())
                                // noContact로 지정된 환자 id, name
                                .noContact(patient.getNoContacts().stream()
                                                .map(noContact -> patientRepository
                                                                .findById(noContact.getNoContactPatient().getId())
                                                                .map(noContactPatient -> PatientDTO.builder()
                                                                                .id(noContactPatient.getId())
                                                                                .name(noContactPatient.getName())
                                                                                .build()))
                                                .map(Optional::get)
                                                .collect(Collectors.toList()))
                                .restrictedArea(patient.getRestrictedAreas().stream()
                                                .map(restrictedArea -> sectorRepository
                                                                .findById(restrictedArea.getSector().getId())
                                                                .map(sector -> SectorDTO.builder()
                                                                                .id(sector.getId())
                                                                                .name(sector.getName())
                                                                                .build()))
                                                .map(Optional::get)
                                                .collect(Collectors.toList()))
                                .watchId(patient.getWatch().getId())
                                .build();
        }

        @Transactional
        public void createPatient(Ward ward, PatientRequest request) {
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
                                // riskGroup 없을 시(빈문자열) null로 처리
                                .riskGroup(request.getRiskgroupId() == null ? null
                                                : riskGroupRepository.findById(request.getRiskgroupId())
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                                "RiskGroup not found")))
                                .ward(ward)
                                .build();

                // NoContact 등록
                List<PatientDTO> noContacts = request.getNoContact();
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
                List<SectorDTO> restrictedAreas = request.getRestrictedArea();
                if (restrictedAreas != null) {
                        for (SectorDTO restrictedArea : restrictedAreas) {
                                Long restrictedAreaId = restrictedArea.getId();
                                Optional<Sector> optionalSector = sectorRepository.findById(restrictedAreaId);
                                if (optionalSector.isPresent()) {
                                        patient.addRestrictedArea(optionalSector.get());
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

                // 기존 연관관계 제거
                patient.removeNoContacts();
                patient.removeRestrictedAreas();

                // patient 객체를 기준으로 연관관계 추가
                List<PatientDTO> noContacts = request.getNoContact();
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

                List<SectorDTO> restrictedAreas = request.getRestrictedArea();
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
                                // 추후 getId로 가져오는 방식으로 변경
                                .riskGroup(request.getRiskgroupName() == null ? null
                                                : riskGroupRepository.findByName(request.getRiskgroupName())
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                                "RiskGroup not found")))
                                .build());
        }
}