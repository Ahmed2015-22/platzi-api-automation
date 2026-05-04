package com.rsetAssured.apis.utils;

import com.rsetAssured.apis.models.request.user.CreateUserRequestModel;
import com.rsetAssured.apis.models.request.user.EmailAvailabilityRequestModel;
import com.rsetAssured.apis.models.response.user.EmailAvailabilityResponseModel;
import com.rsetAssured.apis.models.response.user.GetUsersResponseModel;
import com.rsetAssured.utils.dataReader.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

import java.util.List;

import static com.rsetAssured.apis.services.SpecBuilder.getReqSpec;
import static com.rsetAssured.apis.services.SpecBuilder.postRequest;

public class UsersUtils {

    private static String getUsersEndpoint()
    {
        return PropertyReader.getProperty("UsersEP");
    }

    public static List<GetUsersResponseModel> getAllUsers()
    {
        return RestAssured.given()
                .spec(getReqSpec(getUsersEndpoint()))
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetUsersResponseModel>>() {});
    }

    public static GetUsersResponseModel createUniqueUser(CreateUserRequestModel body , int statusCode)
    {
        return RestAssured.given()
                .spec(postRequest(getUsersEndpoint()))
                .when()
                .body(body)
                .post()
                .then().log().all()
                .assertThat().statusCode(statusCode)
                .extract().as(GetUsersResponseModel.class);
    }

    /**
     * Create user with generic response type (for negative tests)
     */
    public static <T> T createUserWithDifferentResponse(CreateUserRequestModel body, Class<T> responseModel, int expectedStatusCode)
    {
        return RestAssured.given()
                .spec(postRequest(getUsersEndpoint()))
                .when()
                .body(body)
                .post()
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModel);
    }

    public static GetUsersResponseModel updateUserDataById(int id, CreateUserRequestModel body)
    {
        return RestAssured.given()
                .spec(postRequest(getUsersEndpoint()))
                .pathParam("id", id)
                .when()
                .body(body)
                .put("/{id}")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetUsersResponseModel.class);
    }

    public static GetUsersResponseModel getOneUser(int id)
    {
        return RestAssured.given()
                .spec(getReqSpec(getUsersEndpoint()))
                .when()
                .get("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetUsersResponseModel.class);
    }

    /**
     * Get one user with generic response (for negative tests)
     */
    public static <T> T getOneUserWithDifferentResponse(int id, Class<T> responseModel, int expectedStatusCode)
    {
        return RestAssured.given()
                .spec(getReqSpec(getUsersEndpoint()))
                .when()
                .get("/" + id)
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModel);
    }

    /**
     * Delete a user by ID
     */
    public static boolean deleteUser(int id) {
        String responseText = RestAssured.given()
                .spec(getReqSpec(getUsersEndpoint()))
                .when()
                .delete("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().asString();

        return responseText != null && responseText.trim().equalsIgnoreCase("true");
    }

    /**
     * Check if an email is available for registration
     */
    public static EmailAvailabilityResponseModel checkEmailAvailability(EmailAvailabilityRequestModel body) {
        return RestAssured.given()
                .spec(postRequest(getUsersEndpoint()))
                .body(body)
                .when()
                .post("/is-available")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().as(EmailAvailabilityResponseModel.class);
    }
}
