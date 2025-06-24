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

