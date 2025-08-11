package service;

import dao.UserDao;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userDao);
    }

    @Test
    void testCreateUser() {
        when(userDao.create(any(User.class))).thenReturn(1L);

        Long id = userService.create("Olesya", "olesya@example.com", 25);

        assertEquals(1L, id);
        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void testGetUserById() {
        User user = new User("Alexander", "Alexander@example.com", 40);
        when(userDao.findById(2L)).thenReturn(user);

        User result = userService.getById(2L);

        assertNotNull(result);
        assertEquals("Alexander", result.getName());
        verify(userDao, times(1)).findById(2L);
    }

    @Test
    void testGetAllUsers() {
        when(userDao.findAll()).thenReturn(Arrays.asList(
                new User("U1", "u1@mail.com", 20),
                new User("U2", "u2@mail.com", 22)
        ));

        assertEquals(2, userService.getAllUsers().size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        User user = new User("Old", "old@mail.com", 33);
        when(userDao.findById(3L)).thenReturn(user);
        when(userDao.update(any(User.class))).thenReturn(true);

        boolean updated = userService.update(3L, "New", "new@mail.com", 35);

        assertTrue(updated);
        verify(userDao, times(1)).update(any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userDao.delete(4L)).thenReturn(true);

        boolean deleted = userService.delete(4L);

        assertTrue(deleted);
        verify(userDao, times(1)).delete(4L);
    }
}
