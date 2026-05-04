package com.rsetAssured.apis.models.request.auth;

public class LoginRequestModel {
    public String email;
    public String password;

    public LoginRequestModel() {}

    public LoginRequestModel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
