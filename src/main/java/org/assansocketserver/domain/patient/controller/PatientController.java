package org.assansocketserver.domain.patient.controller;

import java.util.Map;

import org.assansocketserver.auth.dto.CustomUserDetails;
import org.assansocketserver.domain.patient.dto.PatientRequest;
import org.assansocketserver.domain.patient.dto.PatientResponse;
import org.assansocketserver.domain.patient.service.PatientService;
import org.assansocketserver.domain.ward.service.WardService;
import org.assansocketserver.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/patients")
@RestController
public class PatientController {

        private final WardService wardService;
        private final PatientService patientService;

        // 환자 목록 조회
        @GetMapping("")
        public ResponseEntity<RestResponse<Map<String, Object>>> getPatients(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(required = false, defaultValue = "number", value = "sort") String sort,
                        @RequestParam(required = false, defaultValue = "asc", value = "sortby") String sortby,
                        @RequestParam(required = false, defaultValue = "1", value = "page") Integer page,
                        @RequestParam(required = false, defaultValue = "", value = "gender") String gender,
                        @RequestParam(required = false, defaultValue = "", value = "search") String keyword) {
                Map<String, Object> response = patientService.getPatients(wardService.getCurrentWard(userDetails),
                                sort, sortby, page, gender, keyword);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @PostMapping("")
        public ResponseEntity<RestResponse<Void>> createPatient(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestBody PatientRequest request) {
                patientService.createPatient(wardService.getCurrentWard(userDetails), request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(RestResponse.CREATED());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<RestResponse<Void>> deletePatient(@PathVariable("id") Long id) {
                patientService.deletePatient(id);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }

        // 환자 목록 약식 조회(id, name)
        @GetMapping("/list")
        public ResponseEntity<RestResponse<Map<String, Object>>> getPatientsList(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @RequestParam(required = false, defaultValue = "", value = "search") String keyword) {
                Map<String, Object> response = patientService.getPatientsList(keyword,
                                wardService.getCurrentWard(userDetails));

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));

        }

        @GetMapping("/{id}")
        public ResponseEntity<RestResponse<PatientResponse>> getPatient(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable("id") Long id) {
                PatientResponse response = patientService.getPatient(wardService.getCurrentWard(userDetails), id);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK(response));
        }

        @PatchMapping("/{id}")
        public ResponseEntity<RestResponse<Void>> updatePatient(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable("id") Long id,
                        @RequestBody PatientRequest request) {
                patientService.updatePatient(wardService.getCurrentWard(userDetails), id, request);

                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }

        // 특정 환자 호출
        @GetMapping("/{id}/call")
        public ResponseEntity<RestResponse<Void>> callPatient(
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long id) {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(RestResponse.OK());
        }
}
