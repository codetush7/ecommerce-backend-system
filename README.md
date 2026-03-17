# 🛒 E-Commerce Backend System

A **production-grade backend system** for an **E-Commerce platform** developed using **Java, Spring Boot, and MySQL**.  
The system manages **users, products, carts, orders, and payments** while following **industry-standard layered architecture**.

This project demonstrates professional backend development practices including:

- RESTful API development
- JWT authentication
- Pagination and filtering
- Exception handling
- Clean layered architecture
- Unit testing using JUnit & Mockito

---

# 📌 Project Overview

The goal of this project is to build a **scalable backend system for an online shopping platform**.

The backend provides APIs for:

- User registration and authentication
- Product catalog management
- Shopping cart functionality
- Order checkout and payment processing
- Product filtering and category search

The system is designed following **enterprise backend development practices** using **Spring Boot and REST APIs**.

---

# 🏗️ System Architecture

The project follows a **Layered Architecture**.

Client (Postman / Frontend)  
↓  
Controller Layer (REST APIs)  
↓  
Service Layer (Business Logic)  
↓  
Repository Layer (JPA / Hibernate)  
↓  
MySQL Database

### Controller Layer
Handles HTTP requests and responses.

### Service Layer
Contains business logic and validations.

### Repository Layer
Handles database operations using **Spring Data JPA**.

### Entity Layer
Maps Java objects to database tables.

---

# 🛠️ Tech Stack

| Technology | Purpose |
|------------|--------|
| Java 21 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Data JPA | ORM |
| Hibernate | Persistence |
| MySQL | Database |
| JWT | Authentication |
| JUnit 5 | Unit Testing |
| Mockito | Mocking Framework |
| Maven | Dependency Management |
| Postman | API Testing |
| Swagger | API Documentation |

---
# 📂 Project Structure

```
src/main/java/com/ecommerce
│
├── controller
│       AuthController
│       UserController
│       ProductController
│       CartController
│       OrderController
│
├── service
│       UserService
│       ProductService
│       CartService
│       OrderService
|       EmailService
│
├── repository
│       UserRepository
│       ProductRepository
│       CartRepository
│       CartItemRepository
│       OrderRepository
|       OrderItemRepository
│
├── entity
│       User
│       Product
│       Cart
│       CartItem
│       Order
|       OrderItem
│
├── enums
│       ProductCategory
│       Role
│       PaymentMode
│       OrderStatus
│
├── utils
│       JWTUtil
│
├── exception
│       ResourceNotFoundException
│       ErrorResponse
│       GlobalExceptionHandler
│
├── config
│       ModelMapperConfig
│       SecurityConfig
│       
├── security
│       JWTAuthFilter
|
└── EcommerceApplication
```
---

# ⚙️ How to Run Locally

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-repository/ecommerce-backend.git
```

---

## 2️⃣ Open the Project

Open the project using:

- IntelliJ IDEA
- Eclipse
- VS Code

---

## 3️⃣ Create MySQL Database

Run the following command in MySQL:

```sql
CREATE DATABASE ecommerce_db;
```

---

## 4️⃣ Configure Database

Update the file:

```
src/main/resources/application.properties
```

Add the following configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 5️⃣ Install Dependencies

```bash
mvn clean install
```

---

## 6️⃣ Run the Application

Run the main class:

```
EcommerceApplication.java
```

or run using Maven:

```bash
mvn spring-boot:run
```

---

## 7️⃣ Server Runs At

```
http://localhost:8080
```
---

# 📚 API Documentation

---

## 1️⃣ Authentication API

### Login User

**Endpoint**

**Description**

Authenticates the user and returns a **JWT token**.

---

### Query Parameters

| Parameter | Type   | Description |
|-----------|--------|-------------|
| email     | String | User email |
| password  | String | User password |

---

### Example Request
POST /api/auth/login?email=tusharmaharana10@gmail.com&password=123456

---

### Response

Returns a **JWT Token**

Example:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

---
---

---

## 2️⃣ User API

---

### 1️⃣ Register User

**Endpoint**

```http
POST /api/users/register
```

**Description**

Registers a new user in the system.

---

### Request Body

```json
{
  "name": "Tushar Maharana",
  "email": "tusharmaharana10@gmail.com",
  "password": "123456",
  "role": "CUSTOMER"
}
```

---

### Allowed Roles

- **ADMIN**
- **CUSTOMER**

---

### Response Example

```json
{
  "id": 1,
  "name": "Tushar Maharana",
  "email": "tusharmaharana10@gmail.com",
  "role": "CUSTOMER"
}
```

---

### 2️⃣ Get User By ID

**Endpoint**

```http
GET /api/users/{id}
```

**Description**

Fetch user details.

---

### Path Parameter

| Parameter | Type | Description |
|----------|------|-------------|
| id | Long | User ID |

---

### Response

```json
{
  "id": 1,
  "name": "Tushar Maharana",
  "email": "tusharmaharana10@gmail.com",
  "role": "CUSTOMER"
}
```

---

### 3️⃣ Update User

**Endpoint**

```http
PUT /api/users/{id}
```

**Description**

Updates user information.

---

### Request Body

```json
{
  "name": "Tushar Maharana",
  "email": "tusharmaharana10@gmail.com"
}
```

---

### 4️⃣ Delete User (Admin Only)

**Endpoint**

```http
DELETE /api/users/{id}
```

**Description**

Deletes a user from the system. Only **ADMIN** can perform this action.

---

### 5️⃣ Change Password

**Endpoint**

```http
PUT /api/users/change-password
```

**Description**

Allows a user to change their password.

---

### Request Body

```json
{
  "oldPassword": "123456",
  "newPassword": "newpassword123"
}
```

---

---

## 3️⃣ Product API

---

### 1️⃣ Add Product (Admin)

**Endpoint**

```http
POST /api/products
```

**Description**

Adds a new product to the system. Only **ADMIN** can perform this operation.

---

### Request Body

```json
{
  "name": "Gaming Laptop",
  "description": "High performance gaming laptop",
  "price": 75000,
  "stock": 20,
  "category": "ELECTRONICS",
  "imageUrl": "laptop.jpg",
  "rating": 4.5
}
```

---

### Allowed Categories

- **ELECTRONICS**
- **CLOTHING**
- **BOOKS**
- **HOME_APPLIANCES**
- **BEAUTY**
- **SPORTS**
- **TOYS**
- **GROCERY**

---

### 2️⃣ Get All Products

**Endpoint**

```http
GET /api/products
```

**Description**

Fetches all products with pagination support.

---

### Example Request

```http
GET /api/products?page=0&size=5
```

---

### Response

```json
{
  "content": [
    {
      "id": 1,
      "name": "Gaming Laptop",
      "price": 75000,
      "stock": 20,
      "category": "ELECTRONICS"
    }
  ],
  "pageNumber": 0,
  "pageSize": 5,
  "totalElements": 10,
  "totalPages": 2,
  "last": false
}
```

---

### 3️⃣ Get Product By ID

**Endpoint**

```http
GET /api/products/{id}
```

**Example**

```http
GET /api/products/1
```

---

### 4️⃣ Update Product (Admin)

**Endpoint**

```http
PUT /api/products/{id}
```

**Description**

Updates product details. Only **ADMIN** can update products.

---

### 5️⃣ Delete Product

**Endpoint**

```http
DELETE /api/products/{id}
```

---

### 6️⃣ Filter Products By Category

**Endpoint**

```http
GET /api/products/category/{category}
```

---

### Example

```http
GET /api/products/category/ELECTRONICS
```

---

### 7️⃣ Filter Products By Price Range

**Endpoint**

```http
GET /api/products/price
```

---

### Example

```http
GET /api/products/price?minPrice=1000&maxPrice=50000
```

---

# 🛒 Cart API

---

### 1️⃣ Add Product to Cart

**Endpoint**

```http
POST /api/cart/add/{productId}
```

---

### Query Parameter

| Parameter | Type | Description |
|----------|------|-------------|
| quantity | Integer | Number of products to add |

---

### Example

```http
POST /api/cart/add/1?quantity=2
```

---

### 2️⃣ Update Cart Item

**Endpoint**

```http
PUT /api/cart/update/{productId}
```

---

### Example

```http
PUT /api/cart/update/1?quantity=3
```

---

### 3️⃣ Remove Product From Cart

**Endpoint**

```http
DELETE /api/cart/remove/{productId}
```

---

### Example

```http
DELETE /api/cart/remove/1
```

---

### 4️⃣ Get Cart

**Endpoint**

```http
GET /api/cart
```

---

### Response Example

```json
{
  "cartId": 1,
  "totalPrice": 150000,
  "items": [
    {
      "productId": 1,
      "productName": "Gaming Laptop",
      "price": 75000,
      "quantity": 2
    }
  ]
}
```

---
---

## 📦 Order API

---

### 1️⃣ Checkout Order

**Endpoint**

```http
POST /api/orders/checkout
```

**Description**

Places an order for the products in the user's cart.

---

### Query Parameter

| Parameter | Type | Description |
|----------|------|-------------|
| paymentMode | String | Payment method for the order |

---

### Allowed Payment Modes

- **COD**
- **UPI**
- **CARD**

---

### Example

```http
POST /api/orders/checkout?paymentMode=UPI
```

---

### Response

```json
{
  "id": 1,
  "total_amount": 150000,
  "paymentStatus": "PENDING",
  "orderStatus": "PLACED"
}
```

---

### 2️⃣ Get All Orders

**Endpoint**

```http
GET /api/orders
```

**Description**

Returns all orders for the logged-in user.

---

### 3️⃣ Get Order By ID

**Endpoint**

```http
GET /api/orders/{id}
```

---

### Example

```http
GET /api/orders/1
```

---

### 4️⃣ Update Order Status (Admin)

**Endpoint**

```http
PUT /api/orders/{id}/status
```

---

### Example

```http
PUT /api/orders/1/status?status=SHIPPED
```

---

### Allowed Status

- **PENDING**
- **PLACED**
- **SHIPPED**
- **DELIVERED**
- **CANCELLED**

---

### 5️⃣ Process Payment

**Endpoint**

```http
POST /api/orders/{id}/payment
```

---

### Example

```http
POST /api/orders/1/payment?success=true
```

---

# 📧 Email Features

The system automatically sends emails for the following events:

### Welcome Email
Sent when a new user registers.

### Order Confirmation Email
Sent after successful order placement.

---

# ⚙️ Running the Project

Follow the steps below to run the project locally.

---

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/ecommerce-backend.git
```

---

### 2️⃣ Navigate to the Project Directory

```bash
cd ecommerce-backend
```

---

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

---

### 4️⃣ Access the Application

The application will start on:

```
http://localhost:8080
```

---

---

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

---

## 🧑‍💻 Author

**Tushar Maharana**

📧 Email: maharana.tushar19@gmail.com

---
