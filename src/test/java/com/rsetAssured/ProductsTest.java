package com.rsetAssured;

import com.rsetAssured.apis.faker.RequestDataGenerator;
import com.rsetAssured.apis.models.request.product.CreateProductRequestModel;
import com.rsetAssured.apis.models.response.ErrorResponseModel;
import com.rsetAssured.apis.models.response.product.GetProductsResponseModel;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;
import java.util.List;

import static com.rsetAssured.apis.utils.ProductsUtils.*;

public class ProductsTest {

    private CreateProductRequestModel requestPayload;
    private CreateProductRequestModel duplicatePayload;
    private GetProductsResponseModel createdProductResp;
    private GetProductsResponseModel fetchedProductResp;
    private ErrorResponseModel errorCreatResponse;

    // ========================================
    // CRUD Tests
    // ========================================

    @Description("Create Product with Unique Title")
    @Test(priority = 0)
    public void createProduct() {
        requestPayload = new CreateProductRequestModel(
                RequestDataGenerator.getProductTitle(),
                RequestDataGenerator.getProductPrice(),
                RequestDataGenerator.getProductDescription(),
                RequestDataGenerator.getCategoryId(),
                RequestDataGenerator.getProductImages());

        createdProductResp = createUniqueProduct(requestPayload);
        Assert.assertTrue(createdProductResp.id > 0, "Product ID should be greater than 0");
        System.out.println("Product ID: " + createdProductResp.id);
    }

    @Description("Create Product With Duplicate Title with a generic Response")
    @Test(priority = 1, dependsOnMethods = "createProduct")
    public void testCreateProductWithDuplicateTitle() {
        Assert.assertNotNull(requestPayload, "Original Product was not created!");

        String duplicateTitle = requestPayload.title;
        duplicatePayload = new CreateProductRequestModel(
                duplicateTitle,
                RequestDataGenerator.getProductPrice(),
                RequestDataGenerator.getProductDescription(),
                RequestDataGenerator.getCategoryId(),
                RequestDataGenerator.getProductImages());
        errorCreatResponse = createProductWithDifferentResponse(duplicatePayload, ErrorResponseModel.class, 400);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(errorCreatResponse.name, "QueryFailedError", "Duplicated Error Name");
        soft.assertEquals(errorCreatResponse.code, "SQLITE_CONSTRAINT_UNIQUE", "Code not Match");
        soft.assertTrue(errorCreatResponse.getMessageAsString().contains("UNIQUE constraint failed: product.slug"),
                "Message not Match");
        soft.assertNotNull(errorCreatResponse.timestamp, "Timestamp not Founded");
        soft.assertAll();
    }

    @Description("Retrieve one Product and Assert its attributes")
    @Test(priority = 2, dependsOnMethods = "createProduct")
    public void getSpecificProduct() {
        Assert.assertNotNull(createdProductResp, "Created Product Response should not be null");
        fetchedProductResp = getOneProduct(createdProductResp.id);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(fetchedProductResp.title, createdProductResp.title, "Product Title not as Expected");
        soft.assertEquals(fetchedProductResp.price, createdProductResp.price, "Product Price not as Expected");
        soft.assertAll();
    }

    

    @Description("Attempt to retrieve a product with a valid ID and verify the Valid Response")
    @Test(priority = 4, dependsOnMethods = "createProduct")
    public void getValidProductAndVerifyResponse() {
        Assert.assertNotNull(createdProductResp, "Created Product is null");

        GetProductsResponseModel getOneProduct = getOneProductWithDifferentResponse(createdProductResp.id,
                GetProductsResponseModel.class, 200);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(getOneProduct.title, createdProductResp.title);
        soft.assertEquals(getOneProduct.price, createdProductResp.price);
        soft.assertAll();
    }

    @Description("Delete a Specific Product")
    @Test(priority = 20, dependsOnMethods = "createProduct")
    public void deleteSpecificProduct() {
        Assert.assertNotNull(createdProductResp, "Created Product Response should not be null");

        boolean isDeleted = deleteProduct(createdProductResp.id);

        Assert.assertTrue(isDeleted, "Cannot delete Product");
    }

    @Description("Retrieve All Products and Assert the list is not empty")
    @Test(priority = 5)
    public void getAllProduct() {
        List<GetProductsResponseModel> model = getAllProducts();
        Assert.assertNotNull(model, "Products list is null");
        Assert.assertFalse(model.isEmpty(), "Products list is empty");
    }

    // ========================================
    // NEW: Pagination Tests
    // ========================================

    @Description("Retrieve products with pagination (offset=0, limit=5) and verify count")
    @Test(priority = 7)
    public void getProductsWithPaginationFirstPage() {
        List<GetProductsResponseModel> products = getProductsWithPagination(0, 5);
        Assert.assertNotNull(products, "Paginated products list is null");
        Assert.assertTrue(products.size() <= 5, "Products count should be at most 5, but got " + products.size());
        Assert.assertFalse(products.isEmpty(), "Products list should not be empty for first page");
    }

    @Description("Retrieve products with pagination (offset=5, limit=5) second page")
    @Test(priority = 8)
    public void getProductsWithPaginationSecondPage() {
        List<GetProductsResponseModel> firstPage = getProductsWithPagination(0, 5);
        List<GetProductsResponseModel> secondPage = getProductsWithPagination(5, 5);

        Assert.assertNotNull(secondPage, "Second page products list is null");

        // Verify pages contain different products (if both have data)
        if (!firstPage.isEmpty() && !secondPage.isEmpty()) {
            Assert.assertNotEquals(firstPage.get(0).id, secondPage.get(0).id,
                    "First product on page 1 and page 2 should be different");
        }
    }

    @Description("Retrieve products with very large offset returns empty list")
    @Test(priority = 9)
    public void getProductsWithPaginationLargeOffset() {
        List<GetProductsResponseModel> products = getProductsWithPagination(99999, 5);
        Assert.assertNotNull(products, "Products list is null");
        Assert.assertTrue(products.isEmpty(), "Products list should be empty for very large offset");
    }

    // ========================================
    // NEW: Negative / Edge Case Tests
    // ========================================

    @Description("Create product with negative price should return error")
    @Test(priority = 11)
    public void createProductWithNegativePrice() {
        CreateProductRequestModel invalidPayload = new CreateProductRequestModel(
                RequestDataGenerator.getProductTitle(),
                -100,
                RequestDataGenerator.getProductDescription(),
                RequestDataGenerator.getCategoryId(),
                RequestDataGenerator.getProductImages());

        ErrorResponseModel errorResponse = createProductWithDifferentResponse(invalidPayload, ErrorResponseModel.class,
                400);

        Assert.assertNotNull(errorResponse.message, "Error response should contain a message for negative price");
    }

    @Description("Create product with invalid categoryId should return error")
    @Test(priority = 12)
    public void createProductWithInvalidCategoryId() {
        CreateProductRequestModel invalidPayload = new CreateProductRequestModel(
                RequestDataGenerator.getProductTitle(),
                RequestDataGenerator.getProductPrice(),
                RequestDataGenerator.getProductDescription(),
                99999,
                RequestDataGenerator.getProductImages());

        ErrorResponseModel errorResponse = createProductWithDifferentResponse(invalidPayload, ErrorResponseModel.class,
                400);

        Assert.assertNotNull(errorResponse.message, "Error response should contain a message for invalid category");
    }

    @Description("Verify product creation returns correct category object nested inside product")
    @Test(priority = 13, dependsOnMethods = "createProduct")
    public void verifyProductCategoryNesting() {
        Assert.assertNotNull(createdProductResp, "Created Product Response should not be null");

        GetProductsResponseModel product = getOneProduct(createdProductResp.id);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(product.category, "Category should not be null in product response");
        soft.assertTrue(product.category.id > 0, "Category ID should be valid");
        soft.assertNotNull(product.category.name, "Category name should not be null");
        soft.assertAll();
    }

    @Description("Verify all products have required fields populated")
    @Test(priority = 14)
    public void verifyAllProductsHaveRequiredFields() {
        List<GetProductsResponseModel> products = getProductsWithPagination(0, 10);
        Assert.assertFalse(products.isEmpty(), "Products list should not be empty");

        SoftAssert soft = new SoftAssert();
        for (GetProductsResponseModel product : products) {
            soft.assertTrue(product.id > 0, "Product ID should be positive for: " + product.title);
            soft.assertNotNull(product.title, "Product title should not be null for ID: " + product.id);
            soft.assertTrue(product.price >= 0, "Product price should be non-negative for: " + product.title);
            soft.assertNotNull(product.category, "Product category should not be null for: " + product.title);
        }
        soft.assertAll();
    }
}