package es.ntwk.filemaker.hibernate.dialect.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.TypedQuery;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HibernateUtilTest {

    private static SessionFactory sessionFactory;
    private Session session;
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtilTest.class);

    @BeforeAll
    public static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        // logger.info("SessionFactory created at BeforeAll");

        logger.info("SessionFactory created at BeforeAll");

    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null)
            sessionFactory.close();
        logger.info("SessionFactory destroyed at AfterAll");
    }

    //@Disabled
    @Test
    @Order(1)
    public void testJdbcDriverVersion() {
        logger.info("Running testJdbcDriverVersion...");
        String driverVersion = null;

        try {
            driverVersion = java.sql.DriverManager.getDriver("jdbc:filemaker://").getMajorVersion() + "." +
                    java.sql.DriverManager.getDriver("jdbc:filemaker://").getMinorVersion();

        } catch (SQLException e) {
            System.err.println("Error retrieving JDBC driver version: " + e.getMessage());
        }
        logger.info("FMJDBC Driver Version: " + driverVersion);

    }

    //@Disabled
    @Test
    @Order(4)
    public void testDirectCreate() {
        logger.info("test4 - Running testDirectCreate...");
        Assertions.assertNotNull(session, "Session should not be null");
        Contact contact = new Contact("smithww33", "smith@example.com");
        session.beginTransaction();
        try {
            session.persist(contact);
            logger.info("test4 - done testDirectCreate...: ");
        } catch (Exception e) {
            logger.info("test4 - Error persisting contact: " + e.getMessage());
        }
    }

    //@Disabled
    @Test
    @Order(5)
    public void testCreate() {
        logger.info("test5 - testCreate");

        Assertions.assertNotNull(session, "Session should not be null");
        session.beginTransaction();
        Contact contact = new Contact("abcddd", "abc@example.com");

        try {
            session.persist(contact);
        } catch (Exception e) {
            logger.info("test5 - Error persisting contact: " + e.getMessage());
        }

        session.getTransaction().commit();
        Long id = contact.getId();
        Assertions.assertTrue(id > 0);
        logger.info("test5 - new id is: " + contact.getId());
    }

    @Disabled
    @Test
    @Order(6)
    public void testUpdate() {
        logger.info("test6 - testUpdate");

        Long id = 5L;
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName("pepe");
        contact.setEmail("smith@example.com");

        session.beginTransaction();
        session.merge(contact);
        session.getTransaction().commit();
        Contact updatedContact = session.find(Contact.class, id);

        assertEquals("pepe", updatedContact.getName());

    }

    @Disabled
    @Test
    @Order(7)
    public void testGet() {
        logger.info("test7 - testGet by ID");
        Long id = 5L;
        Contact contact = session.find(Contact.class, id);
        assertEquals("pepe", contact.getName());
    }

    @Disabled
    @Test
    @Order(9)
    public void testDelete() {
        logger.info("test9 - testDelete");

        Long id = 255L;
        Contact contact = session.find(Contact.class, id);
        session.beginTransaction();
        session.remove(contact);
        session.getTransaction().commit();
        Contact deletedContact = session.find(Contact.class, id);

        Assertions.assertNull(deletedContact);
    }

   

    @Disabled
    @Test
    @Order(11)
    public void testUnpagedList() {
        logger.info("test11 - testUnpagedList");
        TypedQuery<Contact> query = session.createQuery("from Contact", Contact.class);
        List<Contact> resultList = query.getResultList();
        Assertions.assertFalse(resultList.isEmpty());
        logger.info("test11 - got results: " + resultList.size());
    }

    @Disabled
    @Test
    @Order(12)
    public void testOffsetList() {
        logger.info("test12 - testOffsetList");

        // Define pagination parameters
        int pageSize = 5; // Number of contacts per page

        // Begin a transaction
        session.beginTransaction();

        // Create a query to fetch contacts with pagination
        Query<Contact> query = session.createQuery("FROM Contact", Contact.class);
        query.setMaxResults(pageSize); // Set the maximum number of results

        // Execute the query and get the result list
        List<Contact> contacts = query.getResultList();

        // Commit the transaction
        session.getTransaction().commit();

        // Assertions
        Assertions.assertNotNull(contacts, "Contact list should not be null");
        Assertions.assertTrue(contacts.size() <= pageSize,
                "Contact list size should be less than or equal to page size");
        logger.info("Retrieved {} contacts.", contacts.size());

        // Optionally, print the contacts for verification
        contacts.forEach(contact -> logger.info("Contact: {}, Email: {}", contact.getName(), contact.getEmail()));
    }


    @Disabled
    @Test
    @Order(10)
    public void testNativePagedList() {
        logger.info("### testNativePagedList");

        // Try with native SQL first to verify
        try {
            String nativeSql = "SELECT id, email, name FROM contact OFFSET 10 ROWS FETCH FIRST 5 ROWS ONLY";
            List<Contact> contacts = session.createNativeQuery(nativeSql, Contact.class).getResultList();
            logger.info("Native SQL test succeeded with " + contacts.size() + " results\n");
            // Optionally, print the contacts for verification
            contacts.forEach(contact -> logger.info("Contact: {}, Email: {}", contact.getName(), contact.getEmail()));
        } catch (Exception e) {
            System.err.println("Native SQL test failed: " + e.getMessage());
        }

         
    }


    @Disabled
    @Test
    @Order(13)
    public void testPagedList() {
        logger.info("### testPagedList");

        // Define pagination parameters
        int pageNumber = 1; // First page
        int pageSize = 5; // Number of contacts per page

        // Begin a transaction
        session.beginTransaction();

        // Create a query to fetch contacts
        Query<Contact> query = session.createQuery("FROM Contact", Contact.class);
        query.setMaxResults(pageSize);
        query.setFirstResult(pageNumber * pageSize);

        // Log the query and parameters for debugging
        logger.debug("Executing query: {}", query.getQueryString());
        logger.debug("With parameters: offset = {}, limit = {}", pageNumber * pageSize, pageSize);

        // Execute the query and get the result list
        List<Contact> contacts = query.getResultList();

        // Commit the transaction
        session.getTransaction().commit();

        // Assertions
        Assertions.assertNotNull(contacts, "Contact list should not be null");
        Assertions.assertTrue(contacts.size() <= pageSize,
                "Contact list size should be less than or equal to page size");
        logger.info("Retrieved {} contacts.", contacts.size());

        // Optionally, print the contacts for verification
        contacts.forEach(contact -> logger.info("Contact: {}, Email: {}", contact.getName(), contact.getEmail()));
    }
    
    @Disabled
    @Test
    public void testSimplePagination() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query<Contact> query = session.createQuery("FROM Contact", Contact.class);
        //query.setMaxResults(5);
        query.setFirstResult(0); // Adjust as needed

        List<Contact> results = query.getResultList();
        assertNotNull(results);
        session.getTransaction().commit();
        session.close();
    }

    @BeforeEach
    public void openSession() {
        session = sessionFactory.openSession();
        logger.info("Session created");
    }

    @AfterEach
    public void closeSession() {
        if (session != null)
            session.close();
        logger.info("Session closed\n");
    }

}
