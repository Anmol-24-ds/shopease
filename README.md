# ShopEase — E-Commerce Order Management REST API

A backend REST API for an e-commerce order management system, built to demonstrate real-world Spring Boot practices: authentication, role-based authorization, transactional business logic, and layered architecture.

## Tech Stack

- **Java 24**, **Spring Boot 4.1**
- **Spring Data JPA** + **Hibernate** (MySQL)
- **Spring Security** with **JWT** (JSON Web Tokens) for stateless authentication
- **BCrypt** for password encryption
- **Maven** for dependency management
- **Lombok** for boilerplate reduction

## Features

- User registration and login with JWT-based authentication
- Role-based access control (`CUSTOMER` / `ADMIN`)
- Product catalog (create, view, update, delete)
- Shopping cart with stock validation and automatic quantity merging
- Order placement with:
  - Real-time stock validation and reduction
  - Price locking at time of purchase (`priceAtPurchase`), so historical orders stay accurate even if product prices change later
  - Full transactional safety (`@Transactional`) — if any part of order placement fails, all changes roll back
- Order history per user
- Admin-only endpoints for viewing all orders and updating order status (`PLACED` → `SHIPPED` → `DELIVERED`)
- Environment-variable based configuration — no secrets committed to source control

## Architecture

The project follows a standard layered architecture:

```
Controller → Service → Repository → Database
```

- **Controller layer**: REST endpoints, request/response handling
- **Service layer**: business logic (stock checks, order calculations, authentication)
- **Repository layer**: Spring Data JPA interfaces for database access
- **Security layer**: JWT generation/validation, custom `UserDetailsService`, and a security filter chain enforcing role-based access

## Entity Relationships

- `User` → `Cart` (one-to-one)
- `Cart` → `CartItem` (one-to-many)
- `CartItem` → `Product` (many-to-one)
- `User` → `Order` (one-to-many)
- `Order` → `OrderItem` (one-to-many)
- `OrderItem` → `Product` (many-to-one)

## Getting Started

### Prerequisites
- Java 24+
- Maven
- MySQL running locally (or a remote instance)

### Setup

1. Clone the repository:
   ```
   git clone <your-repo-link>
   cd shopease
   ```

2. Set the following environment variables (do not hardcode credentials in `application.properties`):
   ```
   DB_URL=jdbc:mysql://localhost:3306/shopease_db
   DB_USERNAME=root
   DB_PASSWORD=<your-mysql-password>
   ```

3. Create the database:
   ```sql
   CREATE DATABASE shopease_db;
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

   The app will start on `http://localhost:8080`. Hibernate automatically creates all required tables on first run.

## API Overview

| Endpoint | Method | Access | Description |
|---|---|---|---|
| `/api/users/register` | POST | Public | Register a new user |
| `/api/auth/login` | POST | Public | Login, returns JWT token |
| `/api/products` | GET/POST | Public | View / add products |
| `/api/products/{id}` | GET/PUT/DELETE | Public | View / update / delete a product |
| `/api/cart/add` | POST | Authenticated | Add item to cart |
| `/api/cart` | GET | Authenticated | View current cart |
| `/api/orders/place` | POST | Authenticated | Place an order from cart |
| `/api/orders/my` | GET | Authenticated | View own order history |
| `/api/orders/all` | GET | Admin only | View all orders |
| `/api/orders/{id}/status` | PUT | Admin only | Update order status |

All authenticated endpoints require a `Bearer <token>` in the `Authorization` header, obtained from `/api/auth/login`.

## Notable Design Decisions

- **Price locking**: `OrderItem` stores `priceAtPurchase` separately from `Product.price`, ensuring past orders remain accurate even after price changes.
- **Transactional order placement**: Stock reduction, order creation, and cart clearing all happen within a single `@Transactional` method, preventing partial/inconsistent state if something fails mid-operation.
- **JWT over session-based auth**: Chosen for statelessness, which scales better for REST APIs and avoids server-side session storage.
- **Environment-based secrets**: Database credentials are never hardcoded, following standard security practice for deployable applications.

## Author

Anmol Gupta — [GitHub](https://github.com/Anmol-24-ds)
