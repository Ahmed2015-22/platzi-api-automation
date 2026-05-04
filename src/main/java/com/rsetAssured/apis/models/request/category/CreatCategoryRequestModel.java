package com.rsetAssured.apis.models.request.category;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatCategoryRequestModel {
    public String name;
    public String image;
    public CreatCategoryRequestModel()
    {

    }
    public CreatCategoryRequestModel(String name ,String image)
    {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
