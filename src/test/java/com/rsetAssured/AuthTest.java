package com.rsetAssured;

import com.rsetAssured.apis.faker.RequestDataGenerator;
import com.rsetAssured.apis.models.request.auth.LoginRequestModel;
import com.rsetAssured.apis.models.request.auth.RefreshTokenRequestModel;
import com.rsetAssured.apis.models.request.user.CreateUserRequestModel;
import com.rsetAssured.apis.models.response.ErrorResponseModel;
import com.rsetAssured.apis.models.response.auth.LoginResponseModel;
import com.rsetAssured.apis.models.response.user.GetUsersResponseModel;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.rsetAssured.apis.utils.AuthUtils.*;
import static com.rsetAssured.apis.utils.UsersUtils.createUniqueUser;

public class AuthTest {

    private LoginResponseModel loginResponse;
    private GetUsersResponseModel createdUserForAuth;
    private String testEmail;
    private String testPassword;

    // ========================================
    // Setup: Create a user for auth tests
    // ========================================

    @Description("Create a fresh user for authentication tests")
    @Test(priority = 0)
    public void setupUserForAuth() {
        testEmail = RequestDataGenerator.getEmail();
        testPassword = RequestDataGenerator.getPassword();

        CreateUserRequestModel userPayload = new CreateUserRequestModel(
                RequestDataGenerator.getFirstName(),
                testEmail,
                testPassword,
                RequestDataGenerator.getAvatar()
        );

        createdUserForAuth = createUniqueUser(userPayload, 201);
        Assert.assertTrue(createdUserForAuth.id > 0, "User should be created for auth tests");
    }

    // ========================================
    // Login Tests
    // ========================================

    @Description("Login with valid credentials and verify tokens are returned")
    @Test(priority = 1, dependsOnMethods = "setupUserForAuth")
    public void loginWithValidCredentials() {
        LoginRequestModel loginPayload = new LoginRequestModel(testEmail, testPassword);

        loginResponse = login(loginPayload);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(loginResponse.accessToken, "Access token should not be null");
        soft.assertNotNull(loginResponse.refreshToken, "Refresh token should not be null");
        soft.assertFalse(loginResponse.accessToken.isEmpty(), "Access token should not be empty");
        soft.assertFalse(loginResponse.refreshToken.isEmpty(), "Refresh token should not be empty");
        soft.assertAll();
    }

    @Description("Login with invalid email should return 401 error")
    @Test(priority = 2)
    public void loginWithInvalidEmail() {
        LoginRequestModel loginPayload = new LoginRequestModel("nonexistent@fake.com", "wrongpassword");

        ErrorResponseModel errorResponse = loginWithDifferentResponse(loginPayload, ErrorResponseModel.class, 401);

        Assert.assertNotNull(errorResponse.message, "Error message should not be null");
    }

    @Description("Login with correct email but wrong password should return 401")
    @Test(priority = 3, dependsOnMethods = "setupUserForAuth")
    public void loginWithWrongPassword() {
        LoginRequestModel loginPayload = new LoginRequestModel(testEmail, "completely_wrong_password");

        ErrorResponseModel errorResponse = loginWithDifferentResponse(loginPayload, ErrorResponseModel.class, 401);

        Assert.assertNotNull(errorResponse.message, "Error message should not be null for wrong password");
    }

    // ========================================
    // Profile Tests
    // ========================================

    @Description("Get user profile with valid access token")
    @Test(priority = 4, dependsOnMethods = "loginWithValidCredentials")
    public void getProfileWithValidToken() {
        Assert.assertNotNull(loginResponse, "Login response should not be null");

        GetUsersResponseModel profile = getProfile(loginResponse.accessToken);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(profile.id, "Profile ID should not be null");
        soft.assertEquals(profile.email, testEmail, "Profile email should match login email");
        soft.assertNotNull(profile.name, "Profile name should not be null");
        soft.assertNotNull(profile.role, "Profile role should not be null");
        soft.assertAll();
    }

    @Description("Get user profile with invalid token should return 401")
    @Test(priority = 5)
    public void getProfileWithInvalidToken() {
        ErrorResponseModel errorResponse = getProfileWithDifferentResponse(
                "invalid.jwt.token",
                ErrorResponseModel.class,
                401
        );

        Assert.assertNotNull(errorResponse.message, "Error message should not be null for invalid token");
    }

    // ========================================
    // Refresh Token Tests
    // ========================================

    @Description("Refresh access token with valid refresh token")
    @Test(priority = 6, dependsOnMethods = "loginWithValidCredentials")
    public void refreshTokenWithValidToken() {
        Assert.assertNotNull(loginResponse, "Login response should not be null");

        RefreshTokenRequestModel refreshPayload = new RefreshTokenRequestModel(loginResponse.refreshToken);
        LoginResponseModel refreshedTokens = refreshToken(refreshPayload);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(refreshedTokens.accessToken, "New access token should not be null");
        soft.assertNotNull(refreshedTokens.refreshToken, "New refresh token should not be null");
        soft.assertFalse(refreshedTokens.accessToken.isEmpty(), "New access token should not be empty");
        soft.assertAll();
    }

    @Description("Refresh token with invalid refresh token should return error")
    @Test(priority = 7)
    public void refreshTokenWithInvalidToken() {
        RefreshTokenRequestModel invalidRefreshPayload = new RefreshTokenRequestModel("invalid.refresh.token");

        ErrorResponseModel errorResponse = refreshTokenWithDifferentResponse(
                invalidRefreshPayload,
                ErrorResponseModel.class,
                401
        );

        Assert.assertNotNull(errorResponse.message, "Error message should not be null for invalid refresh token");
    }

    // ========================================
    // Login with default user (from API docs)
    // ========================================

    @Description("Login with the default Platzi user (john@mail.com / changeme)")
    @Test(priority = 8)
    public void loginWithDefaultPlatziUser() {
        LoginRequestModel loginPayload = new LoginRequestModel("john@mail.com", "changeme");

        LoginResponseModel defaultLoginResp = login(loginPayload);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(defaultLoginResp.accessToken, "Access token should not be null for default user");
        soft.assertNotNull(defaultLoginResp.refreshToken, "Refresh token should not be null for default user");
        soft.assertAll();
    }
}
