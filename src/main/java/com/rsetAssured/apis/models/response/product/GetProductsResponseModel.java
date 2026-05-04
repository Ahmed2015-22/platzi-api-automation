//This Class for Desirelizations used in Assertions

package com.rsetAssured.apis.models.response.product;
import java.util.List;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "slug",
        "price",
        "description",
        "category",
        "images",
        "creationAt",
        "updatedAt"
})
@Generated("jsonschema2pojo")
public class GetProductsResponseModel {

    @JsonProperty("id")
    public int id;
    @JsonProperty("title")
    public String title;
    @JsonProperty("slug")
    public String slug;
    @JsonProperty("price")
    public int price;
    @JsonProperty("description")
    public String description;
    @JsonProperty("category")
    public Category category;
    @JsonProperty("images")
    public List<String> images;
    @JsonProperty("creationAt")
    public String creationAt;
    @JsonProperty("updatedAt")
    public String updatedAt;


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
    public static class Category {

        @JsonProperty("id")
        public int id;
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
}

