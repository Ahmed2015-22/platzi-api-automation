package com.rsetAssured;

import com.rsetAssured.apis.faker.RequestDataGenerator;
import com.rsetAssured.apis.models.request.category.CreatCategoryRequestModel;
import com.rsetAssured.apis.models.response.ErrorResponseModel;
import com.rsetAssured.apis.models.response.category.GetCategoriesResponseModel;
import com.rsetAssured.apis.models.response.product.GetProductsResponseModel;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.rsetAssured.apis.utils.CategoriesUtils.*;

public class CategoriesTest {
    private CreatCategoryRequestModel requestPayload;
    private CreatCategoryRequestModel duplicatePayload;
    private GetCategoriesResponseModel createdCategoryResp;
    private GetCategoriesResponseModel fetchedCategoryResp;
    private ErrorResponseModel errorCreatResponse;

    // ========================================
    // CRUD Tests
    // ========================================

    @Description("Retrieve All Categories and Assert the list is not empty, Id")
    @Test(priority = 1)
    public void getAllCategoriesTest()
    {
        List<GetCategoriesResponseModel> cat = getAllCategories();
        Assert.assertNotNull(cat, "Categories list is null");
        Assert.assertFalse(cat.isEmpty(), "Categories list is empty");
        Assert.assertEquals(cat.get(0).id, Integer.valueOf(1), "First Category ID is not as Expected");
    }

    @Description("Create Category with Unique Name")
    @Test(priority = 2)
    public void createCategory()
    {
        requestPayload  = new CreatCategoryRequestModel(
                RequestDataGenerator.getCategoryName(),
                RequestDataGenerator.getCategoryImage()
        );

        createdCategoryResp = createUniqueCategory(requestPayload);
        Assert.assertTrue(createdCategoryResp.id > 0, "Category ID should be greater than 0");
        System.out.println("Category ID: " + createdCategoryResp.id);
    }

    @Description("Create Category With Duplicate Name with a generic Response")
    @Test(priority = 3, dependsOnMethods = "createCategory")
    public void testCreateCategoryWithDuplicateName()
    {
        Assert.assertNotNull(requestPayload, "Original Category was not created!");

        String duplicateName = requestPayload.getName();
        duplicatePayload  = new CreatCategoryRequestModel(
                duplicateName,
                RequestDataGenerator.getCategoryImage()
        );
        errorCreatResponse = createInvalidCategory(duplicatePayload,  400);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(errorCreatResponse.name, "QueryFailedError", "Duplicated Error Name");
        soft.assertEquals(errorCreatResponse.code, "SQLITE_CONSTRAINT_UNIQUE", "Code not Match");
        soft.assertNotNull(errorCreatResponse.timestamp, "Timestamp not Found");
        soft.assertAll();
    }

    @Description("Retrieve one Category and Assert its attributes")
    @Test(priority = 4, dependsOnMethods = "createCategory")
    public void getSpecificCategory()
    {
        Assert.assertNotNull(createdCategoryResp, "Created Category Response should not be null");
        fetchedCategoryResp = getOneCategory(createdCategoryResp.id);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(fetchedCategoryResp.name , createdCategoryResp.name, "Category Name not as Expected");
        soft.assertEquals(fetchedCategoryResp.image , createdCategoryResp.image, "Category Image not as Expected");
        soft.assertAll();
    }

    @Description("Attempt to retrieve a Category with an invalid ID and verify the Error Response")
    @Test(priority = 5)
    public void getInvalidCategoryAndVerifyError()
    {
        ErrorResponseModel errorResponse = getInvalidCategory(9999, 400);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(errorResponse.getMessageAsString().contains("Could not find any entity of type \"Category\" matching"), "Error message mismatch");
        soft.assertEquals(errorResponse.name , "EntityNotFoundError" , "Error Name not as Expected");
        soft.assertAll();
    }

    @Description("Delete a Specific Category and Assert the deletion was successful")
    @Test(priority = 20, dependsOnMethods = "createCategory")
    public void deleteSpecificCategory() {
        Assert.assertNotNull(createdCategoryResp, "Created Category Response should not be null");

        boolean isDeleted = deleteCategory(createdCategoryResp.id);

        Assert.assertTrue(isDeleted, "Cannot delete Category");
    }

    // ========================================
    // NEW: Update Category Tests
    // ========================================

    @Description("Update a category's name and image and verify the changes")
    @Test(priority = 6, dependsOnMethods = "createCategory")
    public void updateCategoryNameAndImage()
    {
        Assert.assertNotNull(createdCategoryResp, "Created Category Response should not be null");

        String newName = RequestDataGenerator.getCategoryName();
        String newImage = RequestDataGenerator.getCategoryImage();

        CreatCategoryRequestModel updatePayload = new CreatCategoryRequestModel(newName, newImage);

        GetCategoriesResponseModel updatedCategory = updateCategory(createdCategoryResp.id, updatePayload);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updatedCategory.id, createdCategoryResp.id, "Category ID should remain the same");
        soft.assertEquals(updatedCategory.name, newName, "Category Name was not updated");
        soft.assertAll();
    }

    // ========================================
    // NEW: Get Products by Category
    // ========================================

    @Description("Retrieve all products belonging to category 1 and verify the list")
    @Test(priority = 7)
    public void getProductsByCategoryId()
    {
        List<GetProductsResponseModel> products = getProductsByCategory(1);
        Assert.assertNotNull(products, "Products by category list is null");

        // Verify all returned products belong to the requested category
        if (!products.isEmpty()) {
            SoftAssert soft = new SoftAssert();
            for (GetProductsResponseModel product : products) {
                soft.assertNotNull(product.category, "Product category should not be null for product: " + product.title);
                soft.assertEquals(product.category.id, 1, "Product should belong to category 1, but product '" + product.title + "' belongs to category " + product.category.id);
            }
            soft.assertAll();
        }
    }

    // ========================================
    // NEW: Negative / Edge Case Tests
    // ========================================

    @Description("Create category with empty name should return error")
    @Test(priority = 8)
    public void createCategoryWithEmptyName()
    {
        CreatCategoryRequestModel invalidPayload = new CreatCategoryRequestModel("", RequestDataGenerator.getCategoryImage());
        ErrorResponseModel errorResponse = createInvalidCategory(invalidPayload, 400);

        Assert.assertNotNull(errorResponse.message, "Error response should contain a message");
    }

    @Description("Create category with missing image should return error")
    @Test(priority = 9)
    public void createCategoryWithMissingImage()
    {
        CreatCategoryRequestModel invalidPayload = new CreatCategoryRequestModel(RequestDataGenerator.getCategoryName(), null);
        ErrorResponseModel errorResponse = createInvalidCategory(invalidPayload, 400);

        Assert.assertNotNull(errorResponse.message, "Error response should contain a message for missing image");
    }

    @Description("Verify all categories have valid IDs, names, and images")
    @Test(priority = 10)
    public void verifyAllCategoriesHaveRequiredFields()
    {
        List<GetCategoriesResponseModel> categories = getAllCategories();
        Assert.assertFalse(categories.isEmpty(), "Categories list should not be empty");

        SoftAssert soft = new SoftAssert();
        for (GetCategoriesResponseModel category : categories) {
            soft.assertTrue(category.id > 0, "Category ID should be positive for: " + category.name);
            soft.assertNotNull(category.name, "Category name should not be null for ID: " + category.id);
            soft.assertNotNull(category.image, "Category image should not be null for: " + category.name);
        }
        soft.assertAll();
    }

    @Description("Verify category slug is generated correctly from name")
    @Test(priority = 11, dependsOnMethods = "createCategory")
    public void verifyCategorySlugGeneration()
    {
        Assert.assertNotNull(createdCategoryResp, "Created Category Response should not be null");
        Assert.assertNotNull(createdCategoryResp.slug, "Category slug should not be null");
        Assert.assertFalse(createdCategoryResp.slug.isEmpty(), "Category slug should not be empty");
    }

}
