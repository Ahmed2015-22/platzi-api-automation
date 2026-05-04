package com.rsetAssured.apis.models.response.category;

import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "slug",
        "image",
        "creationAt",
        "updatedAt"
})
@Generated("jsonschema2pojo")
public class GetCategoriesResponseModel {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("slug")
    public String slug;
    @JsonProperty("image")
    public String image;
    @JsonProperty("creationAt")
    public String creationAt;
    @JsonProperty("updatedAt")
    public String updatedAt;

}
