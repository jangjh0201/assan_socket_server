package org.assansocketserver.domain.hospital.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assansocketserver.global.common.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hospitals")
@RestController
public class HospitalController {

    @GetMapping("")
    public ResponseEntity<RestResponse<Map<String, Object>>> getHospital() {

        List<Map<String, Object>> hospitals = new ArrayList<>();
        hospitals.add(
                Map.of(
                        "hospital_id", 1,
                        "hospital_name", "아산병원"));

        Map<String, Object> response = Map.of(
                "total_count", hospitals.size(),
                "hospitals", hospitals);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(RestResponse.OK(response));
    }
}
