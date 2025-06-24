# 👩‍🍳 Recipe Management API

A **Java Spring Boot**-based **REST API** designed for managing cooking recipes. It supports full CRUD functionality and advanced filtering capabilities.

---

## 📋 Features

- Create, view, update, delete recipes
- Filter by:
  - Vegetarian status
  - Number of servings
  - Included/excluded ingredients (case-insensitive partial match)
  - Instruction content (case-insensitive partial match)

---

## 🛠️ Technical Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL 14.18**
- **Liquibase** for database change management
- **Project Lombok** to reduce boilerplate
- **Maven** for build automation
- **Swagger** for interactive API documentation
- **JUnit 5 + Mockito** for unit testing
- **Environment Variables** for configuration

---

## ⚙️ Setup Instructions

### 1. Prerequisites

- Java 17  
- IntelliJ IDEA (recommended)
- PostgreSQL 14  
- Git  

### 2. Create the Database

```sql
CREATE DATABASE recipe_main;
```

### 3. Clone and Open Project

```bash
git clone https://github.com/your-username/recipe-api.git
cd recipe-api
```

Use **main** branch. Open the project in IntelliJ. It should auto-import as a Maven project.

### 4. Configure and Run

- Use Java 17  
- Main class: `com.api.recipe.RecipeApplication`  
- Set environment variables (manually or from a `.env` file):

```env
DB_HOSTNAME=localhost
DB_PORT=5432
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

Run the application.

---

### 📘 API Documentation (Swagger UI)

Once the application is running, you can access the interactive API documentation via Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui/index.html)

---

### 🔄 Generating Liquibase Changelog

Liquibase is used to generate changelogs by comparing entity definitions with the current database schema.

To generate a new changelog:

```bash
# 1. Package the app (without running tests)
mvn clean package -DskipTests

# 2. Run Liquibase diff using the dev profile
mvn liquibase:diff -Pdev
```
---

### 🗂️ Project Structure

- `controller/` – Handles incoming HTTP requests  
- `service/` – Contains business logic  
- `repository/` – Interfaces for data access using Spring Data JPA  
- `entity/` – JPA entity classes mapped to database tables  
- `dto/` – Data Transfer Objects for decoupling external requests/responses  
- `common/` – Shared utilities, exceptions, and constants  

---

### 🧱 Database Design

- `recipe` table stores core details like `title`, `description`, `instruction`, `vegetarian`, and `servings`  
- Uses `UUID` as a unique, secure identifier for update, delete, and fetch operations.
- `ingredient` table is separate and linked to recipe via a many-to-one relation to enable normalization and efficient ingredient-based searches.
- Separate `ingredient` table for normalized structure
- `BaseEntity` includes common fields like `id`, `createdAt`, `updatedAt`, and `modifiedBy`  

---

### ✅ Best Practices

- `Layered architecture` for separation of concerns  
- `DTO pattern` to decouple persistence models from API contracts
- Used `Projection` in read operations to fetch only required fields and improve query performance
- Centralized `exception handling` for cleaner controllers  
- Externalized configuration using `environment variables`  
- `Pagination` and `filtering` supported in search endpoints
- Centralized `message handling` for consistent and localizable validations and errors
- `Liquibase` is used to track and version schema changes, ensuring the database stays in sync with entity definitions during development

---

### 🧪 Testing

- Used `JUnit 5` and `Mockito` for unit testing  
- Focused on service layer logic and edge case validations  
- API responses are tested using `MockMvc` in controller tests  

---
