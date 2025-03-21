package com.example.learn_spring_boot.services.auths;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.dtos.auths.ChangePassword;
import com.example.learn_spring_boot.dtos.auths.ResetPassword;
import com.example.learn_spring_boot.dtos.auths.SignUpRequest;
import com.example.learn_spring_boot.dtos.auths.SigninRequest;
import com.example.learn_spring_boot.entities.Role;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.repositories.roles.RoleRepository;
import com.example.learn_spring_boot.repositories.users.UserRepository;
import com.example.learn_spring_boot.sercurity.auth.JwtAuhenticationResponse;
import com.example.learn_spring_boot.sercurity.token.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.learn_spring_boot.SystemContants.RoleConstants;

import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    // ✅ Đăng ký tài khoản
    @Override
    public ApiResponse<String> signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ApiResponse<>(false, "Email đã tồn tại", null);
        }
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            return new ApiResponse<>(false, "Username đã tồn tại", null);
        }
        Role customerRole = roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Users user = Users.builder()
                .userName(signUpRequest.getUserName())
                .email(signUpRequest.getEmail())
                .passWord(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Set.of(customerRole))
                .build();

        userRepository.save(user);

        return new ApiResponse<>(true, "Đăng ký thành công", "User created");
    }

    // ✅ Đăng nhập tài khoản
    @Override
    public ApiResponse<JwtAuhenticationResponse> singIn(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        String token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        return new ApiResponse<>(true, "Đăng nhập thành công",
                JwtAuhenticationResponse.builder()
                        .refreshToken(refreshToken)
                        .token(token)
                        .build());
    }

    // ✅ Đặt lại mật khẩu
    @Override
    public ApiResponse<String> resetPassword(ResetPassword resetPassword) {
//        Users user = userRepository.findByEmail(resetPassword.getEmail())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));
//
//        user.setPassWord(passwordEncoder.encode(resetPassword.getNewPassword()));
//        userRepository.save(user);

        return new ApiResponse<>(true, "Đặt lại mật khẩu thành công", "Password updated");
    }

    // ✅ Đổi mật khẩu
    @Override
    public ApiResponse<String> changePassword(ChangePassword changePassword) {
//        Users user = userRepository.findByEmail(changePassword.getEmail())
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));
//
//        if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
//            return new ApiResponse<>(false, "Mật khẩu cũ không đúng", null);
//        }
//
//        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
//        userRepository.save(user);
//
       return new ApiResponse<>(true, "Đổi mật khẩu thành công", "Password changed");
    }
}
