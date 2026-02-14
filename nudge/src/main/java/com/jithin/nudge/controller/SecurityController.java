package com.jithin.nudge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jithin.nudge.dto.UserRegistrationDTO;
import com.jithin.nudge.dto.UserSuccessResponseDTO;
import com.jithin.nudge.service.UserService;
import com.jithin.nudge.util.ResponseWrapper;
import com.jithin.nudge.util.WrappedResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class SecurityController {

    private UserService userService;

    SecurityController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<WrappedResponse<String>> login(HttpServletRequest request) {
        String credentials = request.getHeader("credentrials");
        System.out.println(credentials);
        return ResponseWrapper.ok("Login successful", "Token placeholder");
    }

    @PostMapping("/register")
    public ResponseEntity<WrappedResponse<UserSuccessResponseDTO>> register(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) {
        UserSuccessResponseDTO successResponse = this.userService.registerUser(userRegistrationDTO);
        return ResponseWrapper.created("User registered successfully", successResponse);
    }
}
