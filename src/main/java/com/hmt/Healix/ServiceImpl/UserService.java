package com.hmt.Healix.ServiceImpl;

import com.hmt.Healix.Dtos.RegisterUserDto;
import com.hmt.Healix.Entities.Role;
import com.hmt.Healix.Entities.Users;
import com.hmt.Healix.Mapper.UserMapper;
import com.hmt.Healix.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerUser(RegisterUserDto registerUserDto){
        if(userRepository.existsByEmail(registerUserDto.getEmail())){
            return ResponseEntity.badRequest().body(
                    Map.of("email","email already registered")
            );
        }
        if(userRepository.existsByUsername(registerUserDto.getUsername())){
            return ResponseEntity.badRequest().body(
                    Map.of("Username","Username is already registered")
            );
        }

        Users user=userMapper.toUser(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.PATIENT);

        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
}
