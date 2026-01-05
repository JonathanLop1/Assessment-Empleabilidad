# 📚 Technical Documentation & API Reference

This document provides a technical overview of the Project & Task Management System, including instructions for accessing the Swagger UI and testing endpoints via cURL.

## 🚀 Quick Start

Ensure the backend is running (default port: `8080`).

```bash
# Run with Maven
cd backend
export DB_PORT=5432
mvn spring-boot:run
```

## 📖 Swagger UI

The API documentation is automatically generated via OpenAPI/Swagger.

- **URL**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **JSON Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Use the Swagger UI to interactively test endpoints. Authorize requests by clicking the "Authorize" button and entering your JWT token (Bearer <token>).

## 🛠️ API Endpoints & cURL Examples

### 1. Authentication

#### Register a new user
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "techuser",
    "email": "tech@example.com",
    "password": "securepassword"
  }'
```

#### Login (Get JWT Token)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "tech@example.com",
    "password": "securepassword"
  }'
```
> **Note**: Copy the `token` from the response for subsequent requests.

### 2. Projects

**Set your token variable:**
```bash
export TOKEN="YOUR_JWT_TOKEN_HERE"
```

#### Create a Project
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "API Integration Project",
    "description": "Testing endpoints via cURL"
  }'
```

#### List Projects
```bash
curl -X GET http://localhost:8080/api/projects \
  -H "Authorization: Bearer $TOKEN"
```

#### Activate Project
```bash
# Replace {projectId} with actual UUID
curl -X PATCH http://localhost:8080/api/projects/{projectId}/activate \
  -H "Authorization: Bearer $TOKEN"
```

### 3. Tasks

#### Create a Task
```bash
# Replace {projectId} with actual UUID
curl -X POST http://localhost:8080/api/projects/{projectId}/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implement Webhooks",
    "description": "Setup outbound webhooks for events",
    "dueDate": "2026-12-31T23:59:59"
  }'
```

#### Complete a Task
```bash
# Replace {taskId} with actual UUID
curl -X PATCH http://localhost:8080/api/tasks/{taskId}/complete \
  -H "Authorization: Bearer $TOKEN"
```

#### Delete a Task
```bash
# Replace {taskId} with actual UUID
curl -X DELETE http://localhost:8080/api/tasks/{taskId} \
  -H "Authorization: Bearer $TOKEN"
```

## 🏗️ Architecture Overview

- **Architecture**: Hexagonal (Ports & Adapters)
- **Framework**: Spring Boot 3
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **Build Tool**: Maven

### Key Directories
- `domain`: Core business logic and models (independent of framework).
- `application`: Use cases and services.
- `infrastructure`: Adapters for persistence, security, and configuration.
- `presentation`: REST controllers and DTOs.
