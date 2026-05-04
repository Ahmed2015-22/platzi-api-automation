package com.rsetAssured.apis.faker;

import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.List;

public class RequestDataGenerator {
    private static final Faker faker = new Faker();

    public static String getProductTitle() {
        return faker.commerce().productName();
    }
    public static String getCategoryName() {
        return faker.commerce().department();
    }

    public static String getCategoryImage() {

        return faker.internet().image(640, 480, false, null);
    }

    public static Integer getProductPrice() {
        return faker.number().numberBetween(10, 500);
    }

    public static String getProductDescription() {
        return faker.lorem().sentence(10);
    }

    public static Integer getCategoryId() {

        return faker.number().numberBetween(1, 5);
    }

    public static List<String> getProductImages() {


        return Collections.singletonList("https://placehold.co/600x400");
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }
    public static String getLastName() {
        return faker.name().lastName();
    }
    public static String getAvatar() {
        return faker.internet().avatar();
    }
    public static String getFullName() {
        return faker.name().fullName();
    }

    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    public static String getPassword() {
        return faker.internet().password(6, 8, true, false, true);
    }

    public static String getJobTitle() {
        return faker.job().title();
    }
}
