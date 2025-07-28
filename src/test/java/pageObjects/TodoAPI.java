package pageObjects;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TodoAPI {
    private final String baseUrl;

    public TodoAPI(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response getTodos(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseUrl + "/api/todos");
    }

    public Response addTodo(String token, String text) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{\"text\":\"" + text + "\"}")
                .when()
                .post(baseUrl + "/api/todos");
    }

    public Response updateTodo(String token, long id, String text, boolean done) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body("{\"text\":\"" + text + "\",\"done\":" + done + "}")
                .when()
                .put(baseUrl + "/api/todos/" + id);
    }

    public Response deleteTodo(String token, long id) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseUrl + "/api/todos/" + id);
    }
}