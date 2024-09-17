package com.honley.fastcard.controller;

import com.honley.fastcard.DTO.JwtRequest;
import com.honley.fastcard.DTO.RegisterUserDTO;
import com.honley.fastcard.response.Response;
import com.honley.fastcard.service.AuthService;
import com.honley.fastcard.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "User controllers")
@RequestMapping("api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService createAuthToken;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody JwtRequest authRequest) {
        try {
            return createAuthToken.createAuthToken(authRequest);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterUserDTO registrationUserDto) {
        try {
            return userService.createNewUser(registrationUserDto);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/activate/{link}")
    public ResponseEntity activate(@PathVariable String link) {
        try {
            return userService.activateUser(link);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity getUser(@PathVariable String username) {
        try {
            return userService.getUserByUsername(username);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/update-password-request")
    public ResponseEntity updatePasswordRequest() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.generateResetToken(username);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity updatePassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            return userService.updatePassword(token, newPassword);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity deleteUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userService.deleteUser(username);
        } catch (Exception e) {
            Response response = new Response("bad request", 400);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
