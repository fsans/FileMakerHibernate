package com.filemaker.hibernate.dialect;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.*;

import jakarta.persistence.*;
//import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



public class FileMakerDialectTest {

    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeAll
    public static void setUp() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "com.filemaker.hibernate.dialect.FileMakerDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.filemaker.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:filemaker://192.168.0.24/Contacts");
        configuration.setProperty("hibernate.connection.username", "admin");
        configuration.setProperty("hibernate.connection.password", "wakawaka");

        configuration.setProperty("hibernate.connection.initial_pool_size", "1");
        configuration.setProperty("hibernate.connection.min_pool_size", "1");
        configuration.setProperty("hibernate.connection.pool_size", "5");
        configuration.setProperty("hibernate.connection.pool_validation_interval", "30");
        configuration.setProperty("hibernate.connection.autocommit", "false"); 

        // CRITICAL, add the following "tested" settings
        configuration.setProperty("hibernate.hbm2ddl.auto", "none");
        //configuration.setProperty("id.new_generator_mappings", "true");
        //configuration.setProperty("hibernate.connection.provider_disables_autocommit", "true");


        configuration.addAnnotatedClass(Contact.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

    
        System.out.println("\n\nSetting up session factory...");
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        System.out.println("\n\nSession factory initialized: " + (sessionFactory != null));
    }

    @BeforeEach
    public void openSession() {
        System.out.println("Opening session...");
        if (sessionFactory == null) {
            throw new IllegalStateException("SessionFactory is not initialized.");
        }
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        System.out.println("\n\nSession opened: " + (session != null));
    }

    @AfterEach
    public void closeSession() {
        if (transaction != null) {
            transaction.rollback();
        }
        if (session != null) {
            session.close();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testBasicCRUDOperations() {

        if (session == null) {
            throw new IllegalStateException("Session is null. Cannot perform operations.");
        }

        // Create
        Contact contact = new Contact("John Doe", "john@example.com");
        session.persist(contact);
        transaction.commit();

        // Read
        session.clear();
        Contact retrievedContact = session.get(Contact.class, contact.getId());
        assertThat(retrievedContact).isNotNull();
        assertThat(retrievedContact.getName()).isEqualTo("John Doe");
        assertThat(retrievedContact.getEmail()).isEqualTo("john@example.com");

        // Update
        retrievedContact.setName("Jane Doe");
        session.merge(retrievedContact);
        transaction.commit();
        session.clear();

        Contact updatedContact = session.get(Contact.class, contact.getId());
        assertThat(updatedContact.getName()).isEqualTo("Jane Doe");

        // Delete
        session.remove(updatedContact);
        transaction.commit();
        session.clear();

        Contact deletedContact = session.get(Contact.class, contact.getId());
        assertThat(deletedContact).isNull();
    }

/*     @Test
    public void testQueryWithLimitAndOffset() {
        // Insert test data
        for (int i = 0; i < 20; i++) {
            Contact contact = new Contact("Contact " + i, "contact" + i + "@example.com");
            session.persist(contact);
        }
        transaction.commit();
        session.clear();

        // Query with limit and offset
        String hql = "FROM Contact ORDER BY name";
        List<Contact> contacts = session.createQuery(hql, Contact.class)
                .setFirstResult(5)
                .setMaxResults(10)
                .getResultList();

        assertThat(contacts).hasSize(10);
        assertThat(contacts.get(0).getName()).isEqualTo("Contact 5");
        assertThat(contacts.get(9).getName()).isEqualTo("Contact 9");
    }
 */






    @Entity
    @Table(name = "contact")
    public static class Contact {
        // add GenerationType.AUTO since fmjdbc does not support identity columns
        // and it cannot be overriden in the dialect class in H6
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String email;

        public Contact() {
        }

        public Contact(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Getters and setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
