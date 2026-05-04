package com.rsetAssured.apis.services;

import com.rsetAssured.utils.dataReader.PropertyReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SpecBuilder {

    // Core generic method for all requests
    private static RequestSpecBuilder getBaseSpecBuilder(String basePath) {
        return new RequestSpecBuilder()
                .setBaseUri(PropertyReader.getProperty("BaseURI"))
                .setBasePath(basePath)
                .log(LogDetail.ALL);
    }

    // For requests without a body (GET, DELETE)
    public static RequestSpecification getReqSpec(String basePath) {
        return getBaseSpecBuilder(basePath).build();
    }


    public static RequestSpecification postRequest(String basePath) {
        return getBaseSpecBuilder(basePath)
                .setContentType(ContentType.JSON)
                .build();
    }

    //For requests with a body & with AUTHENTICATION
    public static RequestSpecification postRequest(String basePath, String token) {
        return getBaseSpecBuilder(basePath)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }



}