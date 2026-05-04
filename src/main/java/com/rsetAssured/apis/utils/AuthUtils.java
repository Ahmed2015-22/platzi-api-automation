package com.rsetAssured.apis.utils;

import com.rsetAssured.apis.models.request.auth.LoginRequestModel;
import com.rsetAssured.apis.models.request.auth.RefreshTokenRequestModel;
import com.rsetAssured.apis.models.response.auth.LoginResponseModel;
import com.rsetAssured.apis.models.response.user.GetUsersResponseModel;
import com.rsetAssured.utils.dataReader.PropertyReader;
import io.restassured.RestAssured;

import static com.rsetAssured.apis.services.SpecBuilder.getReqSpec;
import static com.rsetAssured.apis.services.SpecBuilder.postRequest;

public class AuthUtils {

    private static String getAuthEndpoint() {
        return PropertyReader.getProperty("AuthEP");
    }

    /**
     * Login with email and password to get JWT tokens
     */
    public static LoginResponseModel login(LoginRequestModel body) {
        return RestAssured.given()
                .spec(postRequest(getAuthEndpoint()))
                .body(body)
                .when()
                .post("/login")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().as(LoginResponseModel.class);
    }

    /**
     * Login expecting a specific status code (for negative tests)
     */
    public static <T> T loginWithDifferentResponse(LoginRequestModel body, Class<T> responseModel, int expectedStatusCode) {
        return RestAssured.given()
                .spec(postRequest(getAuthEndpoint()))
                .body(body)
                .when()
                .post("/login")
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModel);
    }

    /**
     * Get user profile using Bearer token
     */
    public static GetUsersResponseModel getProfile(String accessToken) {
        return RestAssured.given()
                .spec(getReqSpec(getAuthEndpoint()))
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/profile")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetUsersResponseModel.class);
    }

    /**
     * Get profile expecting a specific status code (for negative tests)
     */
    public static <T> T getProfileWithDifferentResponse(String accessToken, Class<T> responseModel, int expectedStatusCode) {
        return RestAssured.given()
                .spec(getReqSpec(getAuthEndpoint()))
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/profile")
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModel);
    }

    /**
     * Refresh access token using refresh token
     */
    public static LoginResponseModel refreshToken(RefreshTokenRequestModel body) {
        return RestAssured.given()
                .spec(postRequest(getAuthEndpoint()))
                .body(body)
                .when()
                .post("/refresh-token")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().as(LoginResponseModel.class);
    }

    /**
     * Refresh token expecting a specific status code (for negative tests)
     */
    public static <T> T refreshTokenWithDifferentResponse(RefreshTokenRequestModel body, Class<T> responseModel, int expectedStatusCode) {
        return RestAssured.given()
                .spec(postRequest(getAuthEndpoint()))
                .body(body)
                .when()
                .post("/refresh-token")
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModel);
    }
}
