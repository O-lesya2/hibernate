package springboot.controller;

import springboot.dto.UserDto;
import springboot.model.User;
import springboot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody User user) {
        Long id = userService.create(user.getName(), user.getEmail(), user.getAge());
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getById(id);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {
        boolean success = userService.update(id, updatedUser.getName(), updatedUser.getEmail(), updatedUser.getAge());
        return success
                ? ResponseEntity.ok("Пользователь обновлён")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.delete(id);
        return success
                ? ResponseEntity.ok("Пользователь удалён")
                : ResponseEntity.notFound().build();
    }
}
