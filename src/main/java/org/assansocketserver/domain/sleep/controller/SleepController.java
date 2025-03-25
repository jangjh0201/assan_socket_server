package org.assansocketserver.domain.sleep.controller;

import lombok.RequiredArgsConstructor;

import org.assansocketserver.domain.sleep.service.SleepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSleepPredictions() {
        List<Map<String, Object>> predictions = sleepService.getSleepPredictions();
        return ResponseEntity.ok(predictions);
    }
}
