package com.honley.fastcard.controller;

import com.honley.fastcard.DTO.ApplicationDTO;
import com.honley.fastcard.DTO.CreateApplicationDTO;
import com.honley.fastcard.response.Response;
import com.honley.fastcard.service.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Application controller")
@RequestMapping("api/v1/applications")
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
            Response response = new Response("Internal Server Error", 500);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
