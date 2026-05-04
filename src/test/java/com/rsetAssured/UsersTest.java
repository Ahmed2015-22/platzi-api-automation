package com.rsetAssured;

import com.rsetAssured.apis.faker.RequestDataGenerator;
import com.rsetAssured.apis.models.request.user.CreateUserRequestModel;
import com.rsetAssured.apis.models.request.user.EmailAvailabilityRequestModel;
import com.rsetAssured.apis.models.response.ErrorResponseModel;
import com.rsetAssured.apis.models.response.user.EmailAvailabilityResponseModel;
import com.rsetAssured.apis.models.response.user.GetUsersResponseModel;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.rsetAssured.apis.utils.UsersUtils.*;

public class UsersTest {
    //For sending body request
    private CreateUserRequestModel requestPayload;
    //Saving Response of Request
    private GetUsersResponseModel createdUserResp;
    //Saving Response of Get
    private GetUsersResponseModel fetchedUserResp;

    // ========================================
    // CRUD Tests
    // ========================================

    @Description("Retrieve All Users and verify the first user")
    @Test(priority = 0)
    public void getAllUsersTest()
    {
        List<GetUsersResponseModel> users = getAllUsers();
        Assert.assertNotNull(users, "Users list should not be null");
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");
        Assert.assertEquals(users.get(0).id, Integer.valueOf(1), "First User ID should be 1");
        Assert.assertEquals(users.get(0).name, "Jhon", "First User Name should be Jhon");
    }

    @Description("Create a new User with valid data")
    @Test(priority = 1)
    public void createUser()
    {
        requestPayload = new CreateUserRequestModel(
                RequestDataGenerator.getFirstName(),
                RequestDataGenerator.getEmail(),
                RequestDataGenerator.getPassword(),
                RequestDataGenerator.getAvatar()
        );
        createdUserResp = createUniqueUser(requestPayload, 201);
        Assert.assertTrue(createdUserResp.id > 0, "User ID should be greater than 0");
        System.out.println("User ID: " + createdUserResp.id);

    }

    @Description("Create User With Duplicate Data and verify it's accepted by the API")
    @Test(priority = 2, dependsOnMethods = "createUser")
    public void testCreateUserWithDuplicateData()
    {
        Assert.assertNotNull(requestPayload, "Original User was not created!");

        createdUserResp = createUniqueUser(requestPayload, 201);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(createdUserResp.name, requestPayload.name , "User Name not as Expected");
        soft.assertEquals(createdUserResp.email, requestPayload.email, "User Email not as Expected");
        soft.assertNotNull(createdUserResp.avatar, "User Avatar should not be null");
        soft.assertAll();
    }

    @Description("Retrieve a specific User and Assert its attributes")
    @Test(priority = 3, dependsOnMethods = "createUser")
    public void getSpecificUser()
    {
        Assert.assertNotNull(createdUserResp, "Created User Response should not be null");
        fetchedUserResp = getOneUser(createdUserResp.id);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(fetchedUserResp.name , createdUserResp.name, "User Name not as Expected");
        soft.assertEquals(fetchedUserResp.email , createdUserResp.email, "User Email not as Expected");
        soft.assertAll();
    }

    @Description("Update a specific User's name and email")
    @Test(priority = 4, dependsOnMethods = "createUser")
    public void updateSpecificUser()
    {
        CreateUserRequestModel updatePayload = new CreateUserRequestModel(
                RequestDataGenerator.getEmail(),
                RequestDataGenerator.getFirstName()
        );
        Assert.assertNotNull(createdUserResp, "Created User Response should not be null");
        fetchedUserResp = updateUserDataById(createdUserResp.id , updatePayload);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(fetchedUserResp.name , updatePayload.name, "User Name not as Expected");
        soft.assertEquals(fetchedUserResp.email , updatePayload.email, "User Email not as Expected");
        soft.assertAll();
    }

    // ========================================
    // NEW: Delete User Test
    // ========================================

    @Description("Delete a specific user and verify deletion")
    @Test(priority = 20, dependsOnMethods = "createUser")
    public void deleteSpecificUser()
    {
        Assert.assertNotNull(createdUserResp, "Created User Response should not be null");
        boolean isDeleted = deleteUser(createdUserResp.id);
        Assert.assertTrue(isDeleted, "Cannot delete User");
    }

    // ========================================
    // NEW: Email Availability Tests
    // ========================================

    @Description("Check email availability for an existing email returns false")
    @Test(priority = 5)
    public void checkExistingEmailAvailability()
    {
        EmailAvailabilityRequestModel request = new EmailAvailabilityRequestModel("john@mail.com");
        EmailAvailabilityResponseModel response = checkEmailAvailability(request);

        Assert.assertNotNull(response.isAvailable, "isAvailable should not be null");
        Assert.assertFalse(response.isAvailable, "Existing email should NOT be available");
    }

    @Description("Check email availability for a new unique email and verify response structure")
    @Test(priority = 6)
    public void checkNewEmailAvailability()
    {
        // Use UUID to guarantee a truly unique email that doesn't exist in the system
        String uniqueEmail = "test_" + java.util.UUID.randomUUID() + "@uniquetest.com";
        EmailAvailabilityRequestModel request = new EmailAvailabilityRequestModel(uniqueEmail);
        EmailAvailabilityResponseModel response = checkEmailAvailability(request);

        // Verify the endpoint returns a valid boolean response
        Assert.assertNotNull(response.isAvailable, "isAvailable field should not be null");
    }

    // ========================================
    // NEW: Negative Tests
    // ========================================

    @Description("Retrieve an invalid user ID and verify the error response")
    @Test(priority = 7)
    public void getInvalidUserAndVerifyError()
    {
        ErrorResponseModel errorResponse = getOneUserWithDifferentResponse(99999, ErrorResponseModel.class, 400);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(errorResponse.message, "Error message should not be null");
        soft.assertEquals(errorResponse.name, "EntityNotFoundError", "Error name should be EntityNotFoundError");
        soft.assertAll();
    }

    @Description("Create user with missing required fields should return error")
    @Test(priority = 8)
    public void createUserWithMissingFields()
    {
        CreateUserRequestModel invalidPayload = new CreateUserRequestModel();
        invalidPayload.name = RequestDataGenerator.getFirstName();
        // Missing email, password, avatar

        ErrorResponseModel errorResponse = createUserWithDifferentResponse(invalidPayload, ErrorResponseModel.class, 400);

        Assert.assertNotNull(errorResponse.message, "Error response should contain a message");
    }

    @Description("Verify all users in the list have valid IDs and emails")
    @Test(priority = 9)
    public void verifyAllUsersHaveRequiredFields()
    {
        List<GetUsersResponseModel> users = getAllUsers();
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");

        SoftAssert soft = new SoftAssert();
        for (GetUsersResponseModel user : users) {
            soft.assertTrue(user.id > 0, "User ID should be positive for: " + user.name);
            soft.assertNotNull(user.email, "User email should not be null for ID: " + user.id);
            soft.assertNotNull(user.name, "User name should not be null for ID: " + user.id);
            soft.assertNotNull(user.role, "User role should not be null for ID: " + user.id);
        }
        soft.assertAll();
    }

}
