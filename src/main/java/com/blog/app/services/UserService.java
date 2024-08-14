package com.blog.app.services;

import com.blog.app.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerNewUser(UserDto userDto);
    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUser();
    UserDto getUserById(Integer userId);
    UserDto updateUser(UserDto userDto, Integer userId);
    void deleteUser(Integer userId);

}
