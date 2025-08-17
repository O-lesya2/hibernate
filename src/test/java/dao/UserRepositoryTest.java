package dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import springboot.Main;
import springboot.dao.UserRepository;
import springboot.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
@Testcontainers
class UserRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Test
    void testCreateAndFindById() {
        User user = new User("Olesya", "olesya@example.com", 25);
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Olesya", found.get().getName());
    }

    @Test
    void testUpdate() {
        User user = new User("Alexander", "alexander@example.com", 30);
        User saved = userRepository.save(user);

        saved.setName("Vasily");
        userRepository.save(saved);

        Optional<User> updated = userRepository.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Vasily", updated.get().getName());
    }

    @Test
    void testDelete() {
        User user = new User("Lydia", "lydia@example.com", 28);
        User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());

        Optional<User> deleted = userRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindAll() {
        userRepository.save(new User("User1", "u1@example.com", 20));
        userRepository.save(new User("User2", "u2@example.com", 22));

        List<User> users = userRepository.findAll();
        assertTrue(users.size() >= 2);
    }
}
