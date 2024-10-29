package org.hibernate.community.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import jakarta.persistence.TypedQuery;

public class HibernateUtilTest {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    public static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory created at BeforeAll");
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null)
            sessionFactory.close();
        System.out.println("SessionFactory destroyed at AfterAll");
    }

    @Test
    public void testCreate() {
        System.out.println("Running testCreate...");

        Assertions.assertNotNull(session, "Session should not be null");
        session.beginTransaction();
        Contact contact = new Contact("smith", "smith@example.com");

        try {
            session.persist(contact);
        } catch (Exception e) {
            System.err.println("Error persisting contact: " + e.getMessage());
        }

        session.getTransaction().commit();
        Long id = contact.getId();
        Assertions.assertTrue(id > 0);
    }

    @Disabled
    @Test
    public void testUpdate() {
        System.out.println("Running testUpdate...");

        Long id = 309L;
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName("smith");
        contact.setEmail("smith@example.com");

        session.beginTransaction();
        session.merge(contact);
        session.getTransaction().commit();
        Contact updatedContact = session.find(Contact.class, id);

        assertEquals("smith", updatedContact.getName());

    }

    @Disabled
    @Test
    public void testGet() {
        System.out.println("Running testGet...");
        Long id = 309L;
        Contact contact = session.find(Contact.class, id);
        assertEquals("smith@example.com", contact.getEmail());
    }

    @Disabled
    @Test
    public void testList() {
        System.out.println("Running testList...");
        TypedQuery<Contact> query = session.createQuery("from Contact", Contact.class);
        List<Contact> resultList = query.getResultList();
        Assertions.assertFalse(resultList.isEmpty());
    }

    @Disabled
    @Test
    public void testDelete() {
        System.out.println("Running testDelete...");

        Long id = 328L;
        Contact product = session.find(Contact.class, id);
        session.beginTransaction();
        session.remove(product);
        session.getTransaction().commit();
        Contact deletedContact = session.find(Contact.class, id);

        Assertions.assertNull(deletedContact);
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
