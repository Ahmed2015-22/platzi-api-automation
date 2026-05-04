# Platzi Fake Store API Test Automation Framework

A robust, scalable, and maintainable API Test Automation Framework built using **Java 21**, **Rest Assured**, and **TestNG**. This framework is designed to test the [Platzi Fake Store API](https://fakeapi.platzi.com/), implementing industry-standard best practices such as the Service Object Model (SOM) and robust serialization/deserialization for API payloads.

## 🚀 Key Features

*   **Modern Tech Stack:** Developed with Java 21 features (Records, Switch Expressions, etc.).
*   **Rest Assured API:** Clean and fluent syntax for API request creation and validation.
*   **Data Serialization/Deserialization:** Leverages Jackson Databind and POJOs (Models) for mapping Java Objects to JSON payloads seamlessly.
*   **Test Runner:** TestNG configured for parallel execution (`thread-count="3"`) to reduce test execution time.
*   **Dynamic Data Generation:** Integration with **JavaFaker** to generate robust, randomized test data on the fly.
*   **Centralized Configuration:** Property files and JSON data readers for dynamic environment configurations and test data management.
*   **Comprehensive Logging:** Configured with **Log4j2** for detailed execution logging.
*   **Rich Reporting:** Integrated with **Allure Reports** to generate beautiful, detailed, and interactive HTML test reports.

## 📁 Project Architecture

The framework is highly modularized, following a layered architecture:

```text
src/
├── main/java/com/rsetAssured/
│   ├── apis/                  # Core API logic
│   │   ├── faker/             # JavaFaker data generation classes
│   │   ├── models/            # POJOs/Records for Serialization & Deserialization
│   │   │   ├── request/       # Request body models (Auth, Category, Product, User)
│   │   │   └── response/      # Response body models
│   │   ├── services/          # Request & Response Specification Builders (SpecBuilder)
│   │   └── utils/             # Service classes/endpoints (AuthUtils, ProductsUtils, etc.)
│   └── utils/                 # Framework utilities
│       ├── dataReader/        # JSON and Properties file readers
│       ├── logs/              # Log4j2 Managers
│       └── OSUtils.java       # Operating System utilities
├── main/resources/            # Configuration files (environment.properties, log4j2.properties)
└── test/java/com/rsetAssured/ # TestNG Test Classes (AuthTest, ProductsTest, etc.)
```
### Running via Maven (Command Line)
The `pom.xml` is configured with the `maven-surefire-plugin` to run the `testng.xml` suite automatically.

To execute the entire test suite:
```bash
mvn clean test -DsuiteXmlFile=testng.xml
```
## 🧪 Tested API Modules (Platzi Fake Store)

*   **Authentication (`AuthTest.java`)**: Login, Token Generation, Token Refreshing.
*   **Users (`UsersTest.java`)**: User creation, retrieving users, checking email availability.
*   **Products (`ProductsTest.java`)**: Creating products, fetching product lists.
*   **Categories (`CategoriesTest.java`)**: Creating and fetching product categories.
## 👨‍💻 Author
**Ahmed El-Sharkawi**  
*Junior Test Automation Engineer*

🔗 [LinkedIn Profile](https://www.linkedin.com/in/ahmed-el-sharkawi/)
🔗 [GitHub Profile](https://github.com/Ahmed2015-22)
