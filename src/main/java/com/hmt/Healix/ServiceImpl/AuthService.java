package com.hmt.Healix.ServiceImpl;

import com.hmt.Healix.Dtos.JwtResponse;
import com.hmt.Healix.Dtos.LoginRequestDto;
import com.hmt.Healix.Dtos.UserDto;
import com.hmt.Healix.Entities.Users;
import com.hmt.Healix.Mapper.UserMapper;
import com.hmt.Healix.Repository.UserRepository;
import com.hmt.Healix.config.JwtConfig;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

    public ResponseEntity<?> loginUser(LoginRequestDto loginRequestDto,
                                       HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        Users user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> {
            throw new UsernameNotFoundException("User with " + loginRequestDto.getEmail() + " not found");
        });

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        System.out.println("accesstoken :-"+ accessToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) //  Set to false for HTTP
                .path("/")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .sameSite("Lax")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        System.out.println("cookie set "+cookie.toString());
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }


    public ResponseEntity<?> refreshExpiredToken(String refreshToken) {

        if(!jwtService.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users user=userRepository.findById(jwtService.getIdFromToken(refreshToken)).orElseThrow();
        String accessToken=jwtService.generateToken(user);
        System.out.println("new refresh token "+accessToken);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    public ResponseEntity<UserDto> getCurrentUser(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(long)authentication.getPrincipal();

        Users user=userRepository.findById(userId).orElseThrow();

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok().build();
    }

}
































//    public ResponseEntity<?> loginUser(LoginRequestDto loginRequestDto,
//                                       HttpServletResponse response){
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequestDto.getEmail(),
//                        loginRequestDto.getPassword()
//                )
//        );
//
//        Users user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()->{
//                    throw new UsernameNotFoundException("user with "+loginRequestDto.getEmail()+" not found");
//                }
//        );
//
//        String accessToken=jwtService.generateToken(user);
//        String refreshToken= jwtService.generateRefreshToken(user);
//
//        Cookie cookie=new Cookie("refreshToken",refreshToken);
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
//        cookie.setPath("/");
//        cookie.setSecure(false);
//
//        response.addCookie(cookie);
//
//        return ResponseEntity.ok(new JwtResponse(accessToken));
//    }


//public ResponseEntity<?> logout(HttpServletResponse response) {
//    // Clear the refresh token cookie
//    Cookie cookie = new Cookie("refreshToken", null);
//    cookie.setHttpOnly(true);
//    cookie.setSecure(true);
//    cookie.setPath("/");
//    cookie.setMaxAge(0); // Delete cookie immediately
//
//    response.addCookie(cookie);
//
//    return ResponseEntity.ok("Logged out successfully");
//}