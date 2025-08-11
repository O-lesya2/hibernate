package utils;

import org.hibernate.SessionFactory;

public class HibernateUtilTestHelper {

    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory sf) {
        sessionFactory = sf;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory не инициализирован");
        }
        return sessionFactory;
    }
}
