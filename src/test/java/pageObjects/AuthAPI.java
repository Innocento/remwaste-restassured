package pageObjects;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthAPI {
    private final String baseUrl;

    public AuthAPI(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response register(String username, String password) {
        return given()
                .contentType("application/json")
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post(baseUrl + "/api/register");
    }

    public Response login(String username, String password) {
        return given()
                .contentType("application/json")
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post(baseUrl + "/api/login");
    }
}