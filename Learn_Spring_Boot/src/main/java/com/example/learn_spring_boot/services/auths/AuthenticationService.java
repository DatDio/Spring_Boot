package com.example.learn_spring_boot.services.auths;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.auths.ChangePassword;
import com.example.learn_spring_boot.dtos.auths.ResetPassword;
import com.example.learn_spring_boot.dtos.auths.SigninRequest;
import com.example.learn_spring_boot.dtos.auths.SignUpRequest;
import com.example.learn_spring_boot.sercurity.auth.JwtAuhenticationResponse;

public interface AuthenticationService {

    ApiResponse<String> signUp(SignUpRequest signUpRequest);

    ApiResponse<JwtAuhenticationResponse> signIn(SigninRequest request);




}