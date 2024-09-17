package com.honley.fastcard.controller;


import com.honley.fastcard.response.Response;
import com.honley.fastcard.service.BusinessCardService;
import com.honley.fastcard.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Business card controller")
@RequestMapping("api/v1/business-cards")
@RestController
@RequiredArgsConstructor
public class BusinessCardController {

    private final BusinessCardService businessCardService;
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity getBusinessCard(@PathVariable String username) {
        try {
            return businessCardService.getBusinessCard(username);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/")
    public ResponseEntity updateBusinessCard(@RequestBody String html, @RequestBody String css) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return businessCardService.updateBusinessCard(username, html, css);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
