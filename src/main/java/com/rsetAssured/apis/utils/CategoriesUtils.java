package com.rsetAssured.apis.utils;

import com.rsetAssured.apis.models.request.category.CreatCategoryRequestModel;
import com.rsetAssured.apis.models.response.ErrorResponseModel;
import com.rsetAssured.apis.models.response.category.GetCategoriesResponseModel;
import com.rsetAssured.apis.models.response.product.GetProductsResponseModel;
import com.rsetAssured.apis.services.SpecBuilder;
import com.rsetAssured.utils.dataReader.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

import java.util.List;

public class CategoriesUtils {

    private static String getCategoriesEndpoint() {
        return PropertyReader.getProperty("CategoriesEP");
    }


    public static <T> T getOneCategoryWithDifferentResponse(int id, Class<T> responseModule, int statusCode) {
        return RestAssured.given()
                .spec(SpecBuilder.getReqSpec(getCategoriesEndpoint()))
                .when()
                .get("/" + id)
                .then().log().all()
                .assertThat().statusCode(statusCode)
                .extract().as(responseModule);
    }

    public static <T> T createCategoryWithDifferentResponse(Object body, Class<T> responseModule, int expectedStatusCode) {
        return RestAssured.given()
                .spec(SpecBuilder.postRequest(getCategoriesEndpoint()))
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModule);
    }



    public static GetCategoriesResponseModel getOneCategory(int id) {
        return getOneCategoryWithDifferentResponse(id, GetCategoriesResponseModel.class, 200);
    }

    public static ErrorResponseModel getInvalidCategory(int invalidId, int expectedStatusCode) {
        return getOneCategoryWithDifferentResponse(invalidId, ErrorResponseModel.class, expectedStatusCode);
    }

    public static GetCategoriesResponseModel createUniqueCategory(CreatCategoryRequestModel body) {
        return createCategoryWithDifferentResponse(body, GetCategoriesResponseModel.class, 201);
    }


    public static ErrorResponseModel createInvalidCategory(Object body, int expectedStatusCode) {
        return createCategoryWithDifferentResponse(body, ErrorResponseModel.class, expectedStatusCode);
    }


    public static List<GetCategoriesResponseModel> getAllCategories() {
        return RestAssured.given()
                .spec(SpecBuilder.getReqSpec(getCategoriesEndpoint()))
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetCategoriesResponseModel>>() {});
    }

    public static boolean deleteCategory(int id) {
        String responseText = RestAssured.given()
                .spec(SpecBuilder.getReqSpec(getCategoriesEndpoint()))
                .when()
                .delete("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().asString();

        return responseText != null && responseText.trim().equalsIgnoreCase("true");
    }

    /**
     * Update a category by ID
     */
    public static GetCategoriesResponseModel updateCategory(int id, CreatCategoryRequestModel body) {
        return RestAssured.given()
                .spec(SpecBuilder.postRequest(getCategoriesEndpoint()))
                .body(body)
                .when()
                .put("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetCategoriesResponseModel.class);
    }

    /**
     * Get a category by its slug
     */
    public static GetCategoriesResponseModel getCategoryBySlug(String slug) {
        return RestAssured.given()
                .spec(SpecBuilder.getReqSpec(getCategoriesEndpoint()))
                .when()
                .get("/slug/" + slug)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetCategoriesResponseModel.class);
    }

    /**
     * Get all products belonging to a specific category
     */
    public static List<GetProductsResponseModel> getProductsByCategory(int categoryId) {
        return RestAssured.given()
                .spec(SpecBuilder.getReqSpec(getCategoriesEndpoint()))
                .when()
                .get("/" + categoryId + "/products")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetProductsResponseModel>>() {});
    }

}