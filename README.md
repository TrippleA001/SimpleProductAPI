# Simple Product Management API

This project is a simple REST API built with Spring Boot to manage products (Create, Read, Update, Delete) using an in-memory H2 database.

## Objective

To demonstrate proficiency in Java, Spring Boot (3.4.4), Spring Data JPA, REST API design, validation, error handling, and basic database interaction (H2).

## Technologies Used

* Java 21
* Spring Boot 3.4.4
* Spring Web
* Spring Data JPA
* H2 Database (In-Memory)
* Maven
* Lombok (for reduced boilerplate)
* Spring Validation
* Springdoc OpenAPI (for Swagger UI documentation)

## Setup Instructions

1.  **Clone the repository:**
    ```bash
    git clone <your-github-repo-url>
    cd product-api
    ```
    *(Replace `<your-github-repo-url>` with the actual URL of your GitHub repository)*

2.  **Build the project (optional, Spring Boot Maven plugin can run directly):**
    ```bash
    mvn clean package
    ```

3.  **Run the application:**
    * Using Maven:
        ```bash
        mvn spring-boot:run
        ```
    * Or run the `ProductApiApplication.java` main method directly from your IDE.

The application will start on `http://localhost:8080`.

## Accessing H2 Database Console

The H2 in-memory database console is enabled and accessible at:
`http://localhost:8080/h2-console`

**JDBC URL:** `jdbc:h2:mem:productdb`
**Username:** `admin`
**Password:** (leave blank)

Click "Connect" to view the database tables and data.

## Accessing API Documentation (Swagger UI)

Interactive API documentation is available via Swagger UI at:
`http://localhost:8080/swagger-ui/index.html`

## API Endpoints

The base URL is `http://localhost:8080/products`

| Method | Path           | Description              | Request Body Example                                     | Success Response Example                                                                   | Error Responses Example                                                                                                |
| :----- | :------------- | :----------------------- | :------------------------------------------------------- | :----------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------------------------------------------- |
| POST   | `/products`    | Add a new product        | `{"name": "Laptop Pro", "price": 1299.99, "quantity": 15}` | `201 Created`, `{"id": 1, "name": "Laptop Pro", "price": 1299.99, "quantity": 15}`       | `400 Bad Request`, `{"timestamp": "2025-04-04T07:00:00.000+00:00", "status": 400, "error": "Bad Request", "errors": {"name": "must not be blank"}}` |
| GET    | `/products`    | Get all products         | (None)                                                   | `200 OK`, `[{"id": 1, "name": "Laptop Pro", "price": 1299.99, "quantity": 15}, {"id": 2, "name": "Wireless Mouse", "price": 25.50, "quantity": 50}]` |                                                                                                                      |
| GET    | `/products/{id}` | Get a product by ID      | (None)                                                   | `200 OK`, `{"id": 1, "name": "Laptop Pro", "price": 1299.99, "quantity": 15}`             | `404 Not Found`, `{"timestamp": "2025-04-04T07:00:00.000+00:00", "status": 404, "error": "Not Found", "message": "Product with ID 99 not found", "path": "/products/99"}` |
| PUT    | `/products/{id}` | Update a product by ID   | `{"name": "Laptop Pro X", "price": 1349.00, "quantity": 10}` | `200 OK`, `{"id": 1, "name": "Laptop Pro X", "price": 1349.00, "quantity": 10}`         | `400 Bad Request`, `{"timestamp": "2025-04-04T07:00:00.000+00:00", "status": 400, "error": "Bad Request", "errors": {"price": "must be greater than 0"}}`, `404 Not Found`, `{"timestamp": "...", "status": 404, "error": "Not Found", ...}` |
| DELETE | `/products/{id}` | Delete a product by ID   | (None)                                                   | `204 No Content`                                                                         | `404 Not Found`, `{"timestamp": "2025-04-04T07:00:00.000+00:00", "status": 404, "error": "Not Found", "message": "Product with ID 99 not found", "path": "/products/99"}` |

### Example `POST /products` Request Body:

```json
{
  "name": "Wireless Mouse",
  "price": 25.50,
  "quantity": 50
}