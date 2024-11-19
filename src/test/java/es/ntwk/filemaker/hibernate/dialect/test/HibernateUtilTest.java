package es.ntwk.filemaker.hibernate.dialect.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import jakarta.persistence.TypedQuery;
import org.hibernate.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HibernateUtilTest {

    private static SessionFactory sessionFactory;
    private Session session;
    private static final Logger logger = LogManager.getLogger(HibernateUtilTest.class);

 
    @BeforeAll
    public static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        //System.out.println("SessionFactory created at BeforeAll");
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
        System.out.println("Running testJdbcDriverVersion...");
        String driverVersion = null;
        
        try {
            driverVersion = java.sql.DriverManager.getDriver("jdbc:filemaker://").getMajorVersion() + "." +
                           java.sql.DriverManager.getDriver("jdbc:filemaker://").getMinorVersion();
            
        } catch (SQLException e) {
            System.err.println("Error retrieving JDBC driver version: " + e.getMessage());
        }
        System.out.println("FMJDBC Driver Version: " + driverVersion);

    }

    //@Disabled
    @Test
    @Order(4)
    public void testDirectCreate(){
        System.out.println("test4 - Running testDirectCreate...");
        Assertions.assertNotNull(session, "Session should not be null");
        Contact contact = new Contact("smithww", "smith@example.com");
        session.beginTransaction();
        try {
            session.persist(contact);
            System.out.println("test4 - done testDirectCreate...: " );
        } catch (Exception e) {
            System.err.println("test4 - Error persisting contact: " + e.getMessage());
        }
    }


    //@Disabled
    @Test
    @Order(5)
    public void testCreate() {
        System.out.println("test5 - testCreate");

        Assertions.assertNotNull(session, "Session should not be null");
        session.beginTransaction();
        Contact contact = new Contact("abc", "abc@example.com");

        try {
            session.persist(contact);
        } catch (Exception e) {
            System.err.println("test5 - Error persisting contact: " + e.getMessage());
        }

        session.getTransaction().commit();
        Long id = contact.getId();
        Assertions.assertTrue(id > 0);
        System.out.println("test5 - new id is: " + contact.getId());
    }

    //@Disabled
    @Test
    @Order(6)
    public void testUpdate() {
        System.out.println("test6 - testUpdate");

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

    //@Disabled
    @Test
    @Order(7)
    public void testGet() {
        System.out.println("test7 - testGet by ID");
        Long id = 5L;
        Contact contact = session.find(Contact.class, id);
        assertEquals("pepe", contact.getName());
    }

   

    @Disabled
    @Test
    @Order(9)
    public void testDelete() {
        System.out.println("test9 - testDelete");

        Long id = 404L;
        Contact contact = session.find(Contact.class, id);
        session.beginTransaction();
        session.remove(contact);
        session.getTransaction().commit();
        Contact deletedContact = session.find(Contact.class, id);

        Assertions.assertNull(deletedContact);
    }

    //@Disabled
    @Test
    @Order(10)
    public void testNativePagination() {
        System.out.println("test10 - testPagination");
        
        // Try with native SQL first to verify
        try {
            String nativeSql = "SELECT id, email, name FROM contact OFFSET 0 ROW FETCH FIRST 5 ROW ONLY";
            List<?> result = session.createNativeQuery(nativeSql, Object[].class).getResultList();
            System.out.println("Native SQL test succeeded with " + result.size() + " results");
        } catch (Exception e) {
            System.err.println("Native SQL test failed: " + e.getMessage());
        }
    }
     //@Disabled
     @Test
     @Order(11)
     public void testUnpagedList() {
         System.out.println("test11 - testUnpagedList");
         TypedQuery<Contact> query = session.createQuery("from Contact", Contact.class);
         List<Contact> resultList = query.getResultList();
         Assertions.assertFalse(resultList.isEmpty());
         System.out.println("test11 - got results: " + resultList.size() );
     }

    //@Disabled
    @Test
    @Order(12)
    public void testPagedList() {
        logger.info("test12 - testPagedList");

        // Define pagination parameters
        int pageNumber = 0; // First page
        int pageSize = 5;   // Number of contacts per page

        // Begin a transaction
        session.beginTransaction();
        
        // Create a query to fetch contacts with pagination
        Query<Contact> query = session.createQuery("FROM Contact", Contact.class);
        query.setFirstResult(pageNumber * pageSize); // Set the starting point
        query.setMaxResults(pageSize);               // Set the maximum number of results

        // Execute the query and get the result list
        List<Contact> contacts = query.getResultList();

        // Commit the transaction
        session.getTransaction().commit();

        // Assertions
        Assertions.assertNotNull(contacts, "Contact list should not be null");
        Assertions.assertTrue(contacts.size() <= pageSize, "Contact list size should be less than or equal to page size");
        logger.info("Retrieved {} contacts.", contacts.size());
        
        // Optionally, print the contacts for verification
        contacts.forEach(contact -> logger.info("Contact: {}, Email: {}", contact.getName(), contact.getEmail()));
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
