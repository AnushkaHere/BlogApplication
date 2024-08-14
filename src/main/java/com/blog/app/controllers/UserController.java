package com.blog.app.controllers;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name="User Controller", description = "This API works on User Service")
public class UserController {

    @Autowired
    private UserService userService;

    //getAll
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(this.userService.getAllUser());
    }

    //get
    @GetMapping("/{user_id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("user_id") Integer userId) {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }
    //create
    @Hidden
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createUserDto=this.userService.createUser(userDto);
        return new ResponseEntity(createUserDto, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{user_id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("user_id") Integer userId, @Valid @RequestBody UserDto userDto) {
        UserDto updateUser = this.userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updateUser);
    }

    //delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("user_id") Integer userId){
        this.userService.deleteUser(userId);
        return new ResponseEntity(new ApiResponse("User deleted successfully", true), HttpStatus.OK);
    }

}
