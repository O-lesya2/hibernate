package dao;

import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateUtilTestHelper;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private static final Logger logger = Logger.getLogger(UserDaoTest.class.getName());

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private UserDao userDao;
    private SessionFactory sessionFactory;
    private Long userId;

    @BeforeAll
    public void setUp() {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", postgresContainer.getJdbcUrl());
        props.setProperty("hibernate.connection.username", postgresContainer.getUsername());
        props.setProperty("hibernate.connection.password", postgresContainer.getPassword());
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.show_sql", "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(props);
        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();

        HibernateUtilTestHelper.setSessionFactory(sessionFactory);

        userDao = new UserDao(sessionFactory);
        logger.info("Testcontainers PostgreSQL и Hibernate инициализированы");
    }

    @AfterAll
    public void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("SessionFactory закрыт");
        }
    }

    @Test
    @Order(1)
    public void testCreate() {
        User user = new User("Test User", "test@example.com", 33);
        userId = userDao.create(user);
        assertNotNull(userId, "ID должен быть сгенерирован после создания");
    }

    @Test
    @Order(2)
    public void testRead() {
        assertNotNull(userId, "userId должен быть инициализирован в testCreate");
        User found = userDao.findById(userId);
        assertNotNull(found);
        assertEquals("Test User", found.getName());
    }

    @Test
    @Order(3)
    public void testUpdate() {
        assertNotNull(userId, "userId должен быть инициализирован в testCreate");
        User found = userDao.findById(userId);
        assertNotNull(found);

        found.setName("Updated User");
        boolean updated = userDao.update(found);
        assertTrue(updated);

        User updatedUser = userDao.findById(userId);
        assertEquals("Updated User", updatedUser.getName());
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertNotNull(userId, "userId должен быть инициализирован в testCreate");
        boolean deleted = userDao.delete(userId);
        assertTrue(deleted);

        User deletedUser = userDao.findById(userId);
        assertNull(deletedUser);
    }

    @Test
    @Order(5)
    public void testFindAll() {
        List<User> initialUsers = userDao.findAll();

        User user1 = new User("User One", "one@example.com", 20);
        User user2 = new User("User Two", "two@example.com", 25);

        userDao.create(user1);
        userDao.create(user2);

        List<User> users = userDao.findAll();
        assertTrue(users.size() >= initialUsers.size() + 2);
    }
}
