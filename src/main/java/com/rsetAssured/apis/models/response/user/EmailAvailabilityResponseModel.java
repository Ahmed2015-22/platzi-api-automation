package com.rsetAssured.apis.models.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailAvailabilityResponseModel {

    @JsonProperty("isAvailable")
    public Boolean isAvailable;
}
