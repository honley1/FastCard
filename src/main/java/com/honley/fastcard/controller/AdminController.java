package com.honley.fastcard.controller;

import com.honley.fastcard.response.ResponseWithMessage;
import com.honley.fastcard.service.BusinessCardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Admin controllers")
@RequestMapping("api/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final BusinessCardService businessCardService;

    @PostMapping("/activate-business-card")
    public ResponseEntity activateBusinessCard(@RequestParam String username) {
        try {
            return businessCardService.activateBusinessCard(username);
        } catch (Exception e) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Internal Server Error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/delete-business-card")
    public ResponseEntity deleteBusinessCard(@RequestParam String username) {
        try {
            return businessCardService.deleteBusinessCard(username);
        } catch (Exception e) {
            ResponseWithMessage response = new ResponseWithMessage(false, "Internal Server Error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
