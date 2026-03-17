package com.hmt.Healix.Controller;

import com.hmt.Healix.Dtos.LoginRequestDto;
import com.hmt.Healix.Dtos.UserDto;
import com.hmt.Healix.ServiceImpl.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return authService.loginUser(loginRequestDto,response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        System.out.println("logout done");
        return authService.logout(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUser(@CookieValue(value = "refreshToken", required = false) String refreshToken){
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token found");
        }
        return authService.refreshExpiredToken(refreshToken);
    }
    @GetMapping("/currentuser")
    public ResponseEntity<UserDto> getCurrentUser(){
        return authService.getCurrentUser();
    }


}
