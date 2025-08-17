package springboot.service;

import org.springframework.stereotype.Service;
import springboot.dao.UserRepository;
import springboot.dto.UserDto;
import springboot.mapper.UserMapper;
import springboot.model.User;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Long create(String name, String email, int age) {
        User user = new User(name, email, age);
        User saved = userRepository.save(user);
        logger.info("Пользователь успешно создан с id=" + saved.getId());
        return saved.getId();
    }

    public UserDto getById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("Найден пользователь: " + user.get());
            return userMapper.toDto(user.get());
        } else {
            logger.warning("Пользователь с id=" + id + " не найден");
            return null;
        }
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("Получено пользователей: " + users.size());
        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public boolean update(long id, String name, String email, int age) {
        return userRepository.findById(id).map(user -> {
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            userRepository.save(user);
            logger.info("Пользователь обновлён: id=" + id);
            return true;
        }).orElseGet(() -> {
            logger.warning("Пользователь с id=" + id + " не найден");
            return false;
        });
    }

    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.info("Пользователь удалён: id=" + id);
            return true;
        } else {
            logger.warning("Не удалось удалить пользователя с id=" + id);
            return false;
        }
    }
}
