package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pageObjects.AuthAPI;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthAPITest {

    private static final String BASE_URL = "http://localhost:4000";
    private static AuthAPI authAPI;
    private static String testUser;
    private static String testPass;

    @BeforeAll
    static void setUp() {
        authAPI = new AuthAPI(BASE_URL);
        testUser = "user_" + System.currentTimeMillis();
        testPass = "testpass";
    }

    @Test
    @Order(1)
    void registerNewUser() {
        Response res = authAPI.register(testUser, testPass);
        assertEquals(200, res.statusCode());
        assertTrue(res.jsonPath().getBoolean("success"));
    }

    @Test
    @Order(2)
    void registerExistingUserFails() {
        Response res = authAPI.register(testUser, testPass);
        assertEquals(400, res.statusCode());
        assertEquals("User exists", res.jsonPath().getString("error"));
    }

    @Test
    @Order(3)
    void loginValidCredentials() {
        Response res = authAPI.login(testUser, testPass);
        assertEquals(200, res.statusCode());
        assertNotNull(res.jsonPath().getString("token"));
    }

    @Test
    @Order(4)
    void loginInvalidCredentials() {
        Response res = authAPI.login("nouser", "nopass");
        assertEquals(401, res.statusCode());
        assertEquals("Invalid credentials", res.jsonPath().getString("error"));
    }
}