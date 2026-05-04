package com.rsetAssured.apis.models.response.user;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "email",
        "password",
        "name",
        "role",
        "avatar",
        "creationAt",
        "updatedAt"
})
@Generated("jsonschema2pojo")
public class GetUsersResponseModel {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
    @JsonProperty("name")
    public String name;
    @JsonProperty("role")
    public String role;
    @JsonProperty("avatar")
    public String avatar;
    @JsonProperty("creationAt")
    public String creationAt;
    @JsonProperty("updatedAt")
    public String updatedAt;
}
