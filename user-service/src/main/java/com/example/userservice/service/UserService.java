package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    List<UserDto> getUserByAll();
}
