# Complete E-commerce Backend — Spring Boot

Backend-only REST API for an e-commerce application.

## Technology

- Java 17+ (JDK 23 also works)
- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- JWT authentication
- MySQL
- Maven
- Bean Validation

## Features

- User registration and login
- BCrypt password hashing
- JWT authentication
- USER and ADMIN roles
- Product listing, search and category filtering
- Admin product CRUD
- Shopping cart
- Cart total
- Checkout
- Stock reduction after checkout
- Order history
- Order cancellation
- Admin order management
- CORS
- Validation and global error handling
- Demo data on first run

## 1. Create MySQL database

You can simply start MySQL. The application uses:

Database: `ecommerce_db`
Username: `root`
Password: `root`

The database is created automatically because `createDatabaseIfNotExist=true`.

If your MySQL password is different, edit:

`src/main/resources/application.properties`

Change:
```
spring.datasource.username=root
spring.datasource.password=root
```

## 2. Run in IntelliJ

1. Open the `ecommerce-backend` folder.
2. Open `pom.xml` as a Maven project.
3. Select JDK 17 or newer. JDK 23 is fine.
4. Wait for Maven to download dependencies.
5. Run:
   `EcommerceApplication.java`

Server:
`http://localhost:8080`

## 3. Demo accounts

Admin:
- Email: `admin@ecommerce.com`
- Password: `Admin@123`

User:
- Email: `user@ecommerce.com`
- Password: `User@123`

Change these credentials before using the project outside local development.

## API endpoints

### Authentication

POST `/api/auth/register`

```json
{
  "name": "Raj",
  "email": "raj@example.com",
  "password": "Raj@123"
}
```

POST `/api/auth/login`

```json
{
  "email": "user@ecommerce.com",
  "password": "User@123"
}
```

The response contains a JWT token.

For protected endpoints add:

`Authorization: Bearer YOUR_TOKEN`

### Products

GET `/api/products`

GET `/api/products?category=Electronics`

GET `/api/products?search=phone`

GET `/api/products/1`

POST `/api/products` — ADMIN

PUT `/api/products/1` — ADMIN

DELETE `/api/products/1` — ADMIN

Product JSON:
```json
{
  "name": "Gaming Laptop",
  "description": "High performance laptop",
  "price": 79999,
  "stock": 10,
  "category": "Electronics",
  "imageUrl": "https://example.com/laptop.jpg"
}
```

### User

GET `/api/users/me`

Requires login.

### Cart

GET `/api/cart`

GET `/api/cart/total`

POST `/api/cart`

```json
{
  "productId": 1,
  "quantity": 2
}
```

PUT `/api/cart/{itemId}`

```json
{
  "quantity": 3
}
```

DELETE `/api/cart/{itemId}`

DELETE `/api/cart`

### Orders

POST `/api/orders/checkout`

```json
{
  "shippingAddress": "Pune, Maharashtra, India"
}
```

GET `/api/orders`

GET `/api/orders/{id}`

POST `/api/orders/{id}/cancel`

### Admin orders

GET `/api/admin/orders`

PUT `/api/admin/orders/{id}/status`

```json
{
  "status": "SHIPPED"
}
```

Allowed statuses:
- PLACED
- CONFIRMED
- SHIPPED
- DELIVERED
- CANCELLED

## Suggested Postman testing order

1. Login as user.
2. Copy JWT token.
3. GET products.
4. Add a product to cart.
5. GET cart.
6. Checkout.
7. GET orders.
8. Login as admin.
9. Create/update/delete products.
10. View and update orders.

## Project structure

```
src/main/java/com/example/ecommerce
├── config
├── controller
├── dto
├── entity
├── repository
├── security
└── service
```

This is a backend API. No HTML/CSS/JavaScript frontend is included.
