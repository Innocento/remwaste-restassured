package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pageObjects.AuthAPI;
import pageObjects.TodoAPI;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoAPITest {

    private static final String BASE_URL = "http://localhost:4000";
    private static AuthAPI authAPI;
    private static TodoAPI todoAPI;
    private static String testUser;
    private static String testPass;
    private static String token;
    private static long todoId;

    @BeforeAll
    static void setUp() {
        authAPI = new AuthAPI(BASE_URL);
        todoAPI = new TodoAPI(BASE_URL);
        testUser = "todo_" + System.currentTimeMillis();
        testPass = "todopass";
        authAPI.register(testUser, testPass);
        Response loginRes = authAPI.login(testUser, testPass);
        token = loginRes.jsonPath().getString("token");
    }

    @Test
    @Order(1)
    void addTodo() {
        Response res = todoAPI.addTodo(token, "Test ToDo");
        assertEquals(200, res.statusCode());
        assertEquals("Test ToDo", res.jsonPath().getString("text"));
        assertFalse(res.jsonPath().getBoolean("done"));
        todoId = res.jsonPath().getLong("id");
    }

    @Test
    @Order(2)
    void getTodos() {
        Response res = todoAPI.getTodos(token);
        assertEquals(200, res.statusCode());
        assertTrue(res.jsonPath().getList("$").size() > 0);
        assertTrue(res.jsonPath().getList("text").contains("Test ToDo"));
    }

    @Test
    @Order(3)
    void updateTodo() {
        Response res = todoAPI.updateTodo(token, todoId, "Updated ToDo", true);
        assertEquals(200, res.statusCode());
        Response todosRes = todoAPI.getTodos(token);
        assertTrue(todosRes.jsonPath().getList("text").contains("Updated ToDo"));
        // Optional: check done status
        boolean updatedDone = false;
        for (Object t : todosRes.jsonPath().getList("$")) {
            if (((java.util.Map<?,?>)t).get("text").equals("Updated ToDo")) {
                updatedDone = Boolean.TRUE.equals(((java.util.Map<?,?>)t).get("done"));
                break;
            }
        }
        assertTrue(updatedDone);
    }

    @Test
    @Order(4)
    void deleteTodo() {
        Response res = todoAPI.deleteTodo(token, todoId);
        assertEquals(200, res.statusCode());
        Response todosRes = todoAPI.getTodos(token);
        assertFalse(todosRes.jsonPath().getList("text").contains("Updated ToDo"));
    }
}