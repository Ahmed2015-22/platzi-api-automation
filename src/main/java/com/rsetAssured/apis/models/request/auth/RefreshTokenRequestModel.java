package com.rsetAssured.apis.models.request.auth;

public class RefreshTokenRequestModel {
    public String refreshToken;

    public RefreshTokenRequestModel() {}

    public RefreshTokenRequestModel(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
