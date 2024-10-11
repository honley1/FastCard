package com.honley.fastcard.controller;

import com.honley.fastcard.DTO.CreateApplicationDTO;
import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Application controller")
@RequestMapping("api/applications")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/")
    public ResponseEntity<?> createApplication(@RequestBody CreateApplicationDTO applicationDTO) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return applicationService.createApplication(username, applicationDTO);
        } catch (Exception e) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Internal Server Error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
