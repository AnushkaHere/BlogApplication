package com.blog.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password");
        userDto.setAbout("About John Doe");

        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAbout("About John Doe");
    }

    @Test
    void createUserTest() {
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto createdUserDto = userService.createUser(userDto);

        assertNotNull(createdUserDto);
        assertEquals(userDto.getName(), createdUserDto.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUserTest() {
        List<User> users = List.of(user);
        List<UserDto> userDtos = List.of(userDto);

        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        List<UserDto> allUserDtos = userService.getAllUser();

        assertNotNull(allUserDtos);
        assertEquals(1, allUserDtos.size());
        assertEquals(userDto.getName(), allUserDtos.get(0).getName());
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto foundUserDto = userService.getUserById(1);

        assertNotNull(foundUserDto);
        assertEquals(userDto.getName(), foundUserDto.getName());
    }

    @Test
    void getUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1);
        });

        assertEquals("User not found with id : 1", thrown.getMessage());
    }




    @Test
    public void updateUserNotFound() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(userDto, userId),
                "Expected updateUser to throw ResourceNotFoundException"
        );

        assertEquals("User not found with  Id  : " + userId, thrown.getMessage());
    }



    @Test
    void deleteUserTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteUserNotFound() {
        // Arrange
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(userId),
                "Expected deleteUser to throw ResourceNotFoundException"
        );

        assertEquals("User not found with  Id  : " + userId, thrown.getMessage());
    }
}
