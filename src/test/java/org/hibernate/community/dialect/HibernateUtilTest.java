package org.hibernate.community.dialect;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HibernateUtilTest {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    public static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory created");
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null)
            sessionFactory.close();
        System.out.println("SessionFactory destroyed");
    }

    @Test
    public void testCreate() {
        System.out.println("Running testCreate...");

        // Ensure the session is opened before beginning the transaction
        Assertions.assertNotNull(session, "Session should not be null");

        session.beginTransaction();

        Contact contact = new Contact("smith", "smith@example.com");
        session.persist(contact);

        session.getTransaction().commit();

        Long id = contact.getId();

        Assertions.assertTrue(id > 0);
    }

    @Disabled
    @Test
    public void testUpdate() {
    }

    @Disabled
    @Test
    public void testGet() {
    }

    @Disabled
    @Test
    public void testList() {
    }

    @Disabled
    @Test
    public void testDelete() {
    }

    @BeforeEach
    public void openSession() {
        session = sessionFactory.openSession();
        System.out.println("Session created");
    }

    @AfterEach
    public void closeSession() {
        if (session != null)
            session.close();
        System.out.println("Session closed\n");
    }
}
