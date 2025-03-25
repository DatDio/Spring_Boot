package com.example.learn_spring_boot.controllers;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.auths.SignUpRequest;
import com.example.learn_spring_boot.dtos.auths.SigninRequest;
import com.example.learn_spring_boot.sercurity.auth.JwtAuhenticationResponse;
import com.example.learn_spring_boot.services.auths.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/singup")
    public ApiResponse<String> singup(@Valid @RequestBody SignUpRequest requets)  {
        return authenticationService.signUp(requets);
    }

    @PostMapping("/singin")
    public ApiResponse<JwtAuhenticationResponse> singin(@RequestBody SigninRequest requets) {
        return authenticationService.signIn(requets);
    }
}
