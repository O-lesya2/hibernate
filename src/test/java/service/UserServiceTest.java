package service;

import springboot.dao.UserRepository;
import springboot.dto.UserDto;
import springboot.mapper.UserMapper;
import springboot.model.User;
import springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        User savedUser = new User("Olesya", "olesya@example.com", 25);
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Long id = userService.create("Olesya", "olesya@example.com", 25);

        assertEquals(1L, id);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User("Alexander", "alex@example.com", 40);
        user.setId(2L);
        UserDto dto = new UserDto(2L, "Alexander", "alex@example.com", 40);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.getById(2L);

        assertNotNull(result);
        assertEquals("Alexander", result.getName());
        verify(userRepository, times(1)).findById(2L);
        verify(userMapper, times(1)).toDto(user);
    }


    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UserDto result = userService.getById(99L);

        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                new User("U1", "u1@mail.com", 20),
                new User("U2", "u2@mail.com", 22)
        ));

        List<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testGetAllUsersEmpty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> users = userService.getAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Old", "old@mail.com", 33);
        user.setId(3L);
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean updated = userService.update(3L, "New", "new@mail.com", 35);

        assertTrue(updated);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        boolean updated = userService.update(404L, "Ghost", "ghost@mail.com", 99);

        assertFalse(updated);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(4L)).thenReturn(true);

        boolean deleted = userService.delete(4L);

        assertTrue(deleted);
        verify(userRepository, times(1)).deleteById(4L);
    }

    @Test
    void testDeleteUserFailure() {
        when(userRepository.existsById(5L)).thenReturn(false);

        boolean deleted = userService.delete(5L);

        assertFalse(deleted);
        verify(userRepository, never()).deleteById(5L);
    }
}
