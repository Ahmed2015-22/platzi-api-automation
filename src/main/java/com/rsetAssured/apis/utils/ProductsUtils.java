package com.rsetAssured.apis.utils;

import com.rsetAssured.apis.models.request.product.CreateProductRequestModel;
import com.rsetAssured.apis.models.response.product.GetProductsResponseModel;
import com.rsetAssured.apis.services.SpecBuilder;
import com.rsetAssured.utils.dataReader.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

import java.util.List;

import static com.rsetAssured.apis.services.SpecBuilder.getReqSpec;
import static com.rsetAssured.apis.services.SpecBuilder.postRequest;

public class ProductsUtils {

    //End Points
    private static String getProductsEndpoint() {
        return PropertyReader.getProperty("ProductsEP");
    }



    public static <T> T getOneProductWithDifferentResponse(int id, Class<T> responseModule, int statusCode) {
        return RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .when()
                .get("/" + id)
                .then().log().all()
                .assertThat().statusCode(statusCode)
                .extract().as(responseModule);
    }

    public static <T> T createProductWithDifferentResponse(CreateProductRequestModel body, Class<T> responseModule, int expectedStatusCode) {
        return RestAssured.given()
                .spec(postRequest(getProductsEndpoint()))
                .body(body)
                .when()
                .post()
                .then().log().all()
                .assertThat().statusCode(expectedStatusCode)
                .extract().as(responseModule);
    }


    public static GetProductsResponseModel getOneProduct(int id) {
        return getOneProductWithDifferentResponse(id, GetProductsResponseModel.class, 200);
    }

    public static GetProductsResponseModel createUniqueProduct(CreateProductRequestModel body) {
        return createProductWithDifferentResponse(body, GetProductsResponseModel.class, 201);
    }


    public static List<GetProductsResponseModel> getAllProducts() {
        return RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetProductsResponseModel>>() {});
    }

    public static boolean deleteProduct(int id) {
        String responseText = RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .when()
                .delete("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().asString();

        return responseText != null && responseText.trim().equalsIgnoreCase("true");
    }

    /**
     * Update a product by ID with partial or full body
     */
    public static GetProductsResponseModel updateProduct(int id, CreateProductRequestModel body) {
        return RestAssured.given()
                .spec(postRequest(getProductsEndpoint()))
                .body(body)
                .when()
                .put("/" + id)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetProductsResponseModel.class);
    }

    /**
     * Get products with pagination
     */
    public static List<GetProductsResponseModel> getProductsWithPagination(int offset, int limit) {
        return RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get()
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetProductsResponseModel>>() {});
    }

    /**
     * Get a product by its slug
     */
    public static GetProductsResponseModel getProductBySlug(String slug) {
        return RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .when()
                .get("/slug/" + slug)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(GetProductsResponseModel.class);
    }

    /**
     * Get related products by product ID
     */
    public static List<GetProductsResponseModel> getRelatedProducts(int id) {
        return RestAssured.given()
                .spec(getReqSpec(getProductsEndpoint()))
                .when()
                .get("/" + id + "/related")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().as(new TypeRef<List<GetProductsResponseModel>>() {});
    }
}