package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public void create(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Создан пользователь: ID=" + user.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Ошибка при создании пользователя", e);
        }
    }

    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("Получен пользователь: ID=" + id);
            } else {
                logger.warning("Пользователь с ID=" + id + " не найден");
            }
            return user;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при получении пользователя по ID=" + id, e);
            return null;
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("FROM User", User.class).list();
            logger.info("Получено пользователей: " + users.size());
            return users;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при получении всех пользователей", e);
            return List.of();
        }
    }

    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Обновлён пользователь: ID=" + user.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Ошибка при обновлении пользователя", e);
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("Удалён пользователь: ID=" + id);
            } else {
                logger.warning("Не найден пользователь для удаления: ID=" + id);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            logger.log(Level.SEVERE, "Ошибка при удалении пользователя", e);
        }
    }
}
