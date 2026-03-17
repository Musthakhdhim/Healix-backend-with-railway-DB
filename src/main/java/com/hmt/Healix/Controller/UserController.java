package com.hmt.Healix.Controller;

import com.hmt.Healix.Dtos.RegisterUserDto;
import com.hmt.Healix.ServiceImpl.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto){
        return userService.registerUser(registerUserDto);
    }
}
