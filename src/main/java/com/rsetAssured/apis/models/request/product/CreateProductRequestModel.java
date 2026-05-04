package com.rsetAssured.apis.models.request.product;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateProductRequestModel {

    public String title;
    public Integer price;
    public String description;
    public Integer categoryId;
    public List<String> images;

    public CreateProductRequestModel() {}

    public CreateProductRequestModel(String title, Integer price, String description, Integer categoryId, List<String> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.images = images;
    }

}
