package com.hmt.Healix.Mapper;

import com.hmt.Healix.Dtos.RegisterUserDto;
import com.hmt.Healix.Dtos.UserDto;
import com.hmt.Healix.Entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public Users toUser(RegisterUserDto registerUserDto){
        Users user=new Users();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(registerUserDto.getPassword());

        return user;
    }

    public UserDto toDto(Users user){
        UserDto userDto=new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}
