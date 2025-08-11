package service;

import dao.UserDao;
import model.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Long create(String name, String email, int age) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        Long id = userDao.create(user);
        if (id != null) {
            logger.info("Пользователь успешно создан с id=" + id);
        } else {
            logger.warning("Не удалось создать пользователя");
        }
        return id;
    }

    public User getById(long id) {
        User user = userDao.findById(id);
        if (user != null) {
            logger.info("Найден пользователь: " + user);
        } else {
            logger.warning("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userDao.findAll();
            logger.info("Получено пользователей: " + users.size());
            return users;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при получении списка пользователей", e);
            return List.of();
        }
    }

    public boolean update(long id, String name, String email, int age) {
        User user = userDao.findById(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            boolean success = userDao.update(user);
            if (success) {
                logger.info("Пользователь обновлён: id=" + id);
            } else {
                logger.warning("Не удалось обновить пользователя с id=" + id);
            }
            return success;
        } else {
            logger.warning("Пользователь с id=" + id + " не найден");
            return false;
        }
    }

    public boolean delete(Long id) {
        boolean success = userDao.delete(id);
        if (success) {
            logger.info("Пользователь удалён: id=" + id);
        } else {
            logger.warning("Не удалось удалить пользователя с id=" + id);
        }
        return success;
    }
}
