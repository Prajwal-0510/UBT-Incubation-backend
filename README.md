# UBT TECHNOLOGY — Spring Boot Backend

Full REST API backend for the UBT Technology Incubation platform.

---

## Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Framework    | Spring Boot 3.2.5                 |
| Language     | Java 17                           |
| Build        | Maven 3.9+                        |
| Database     | MySQL 8.x                         |
| ORM          | Hibernate (via Spring Data JPA)   |
| Security     | Spring Security + JWT (JJWT 0.11) |
| Packaging    | JAR                               |

---

## Project Structure

```
ubt-backend/
├── pom.xml
├── schema.sql                          ← Manual SQL schema (optional)
├── frontend-api-service/
│   ├── api.js                          ← Drop into src/services/api.js
│   └── AdminContext.jsx                ← Replace existing AdminContext.jsx
└── src/
    └── main/
        ├── java/com/ubt/backend/
        │   ├── UbtBackendApplication.java
        │   ├── config/
        │   │   ├── SecurityConfig.java
        │   │   └── DataSeeder.java
        │   ├── controller/
        │   │   ├── AuthController.java
        │   │   ├── GalleryController.java
        │   │   ├── ProjectController.java
        │   │   ├── UpdateController.java
        │   │   ├── ContactController.java
        │   │   ├── TestimonialController.java
        │   │   └── AdminDashboardController.java
        │   ├── dto/                    ← Request + Response objects
        │   ├── entity/                 ← JPA Hibernate entities
        │   ├── exception/              ← Custom exceptions + global handler
        │   ├── repository/             ← Spring Data JPA repos
        │   ├── security/
        │   │   ├── JwtUtils.java
        │   │   └── JwtAuthenticationFilter.java
        │   └── service/
        │       ├── *Service.java       ← Interfaces
        │       └── impl/               ← Implementations
        └── resources/
            └── application.properties
```

---

## ── STEP 1: Prerequisites ──

- Java 17 installed  
  Check: `java -version`
- Maven 3.9+ installed  
  Check: `mvn -version`
- MySQL 8 running locally

---

## ── STEP 2: Database Setup ──

Open MySQL CLI or MySQL Workbench and run:

```sql
CREATE DATABASE IF NOT EXISTS ubt_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

Hibernate will auto-create all tables on first run
(`spring.jpa.hibernate.ddl-auto=update`).

Alternatively, run `schema.sql` manually for full control.

---

## ── STEP 3: Configure application.properties ──

Edit `src/main/resources/application.properties`:

```properties
# ── REQUIRED — change these ──
spring.datasource.url=jdbc:mysql://localhost:3306/ubt_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Admin panel password (must match frontend)
app.admin.password=ubtech@admin2025

# JWT secret (keep secret, min 32 chars)
app.jwt.secret=UBTTechnologyIncubationSecretKey2025@Nagpur@Secure

# ── OPTIONAL — email notifications ──
spring.mail.username=your_gmail@gmail.com
spring.mail.password=your_gmail_app_password
app.mail.to=info@ubtorg.com
```

> **Gmail App Password:** Go to Google Account → Security → App Passwords
> to generate one. Do NOT use your main Gmail password.

---

## ── STEP 4: Build & Run ──

### Option A — VS Code / Terminal

```bash
# Navigate to project folder
cd ubt-backend

# Clean build
mvn clean install -DskipTests

# Run
mvn spring-boot:run
```

### Option B — IntelliJ IDEA

1. Open IntelliJ → **File → Open** → select the `ubt-backend` folder
2. IntelliJ will auto-detect the Maven project and download dependencies
3. Open `UbtBackendApplication.java`
4. Click the green ▶ **Run** button next to `main()`

### Option C — Run the JAR directly

```bash
mvn clean package -DskipTests
java -jar target/ubt-backend-1.0.0.jar
```

Server starts at: **http://localhost:8080**

---

## ── STEP 5: Connect React Frontend ──

1. Copy `frontend-api-service/api.js` → `src/services/api.js` in your React project
2. Copy `frontend-api-service/AdminContext.jsx` → `src/context/AdminContext.jsx`
3. Add to your React project's `.env` (create if missing):

```env
VITE_API_URL=http://localhost:8080/api
```

---

## API Endpoints Reference

All endpoints are prefixed with `/api`

### Auth
| Method | Endpoint        | Auth     | Description          |
|--------|-----------------|----------|----------------------|
| POST   | /auth/login     | Public   | Admin login → JWT    |
| GET    | /auth/verify    | JWT      | Verify token valid   |

### Gallery
| Method | Endpoint          | Auth     | Description                        |
|--------|-------------------|----------|------------------------------------|
| GET    | /gallery          | Public   | Get all (optional ?category=...)   |
| POST   | /gallery          | ADMIN    | Add gallery item                   |
| DELETE | /gallery/{id}     | ADMIN    | Soft-delete gallery item           |

### Projects
| Method | Endpoint          | Auth     | Description                        |
|--------|-------------------|----------|------------------------------------|
| GET    | /projects         | Public   | Get all (optional ?level=...)      |
| POST   | /projects         | ADMIN    | Add project                        |
| DELETE | /projects/{id}    | ADMIN    | Soft-delete project                |

### Updates
| Method | Endpoint             | Auth     | Description               |
|--------|----------------------|----------|---------------------------|
| GET    | /updates             | Public   | Get all (pinned first)    |
| POST   | /updates             | ADMIN    | Post new update           |
| DELETE | /updates/{id}        | ADMIN    | Soft-delete update        |
| PATCH  | /updates/{id}/pin    | ADMIN    | Toggle pinned status      |

### Contact
| Method | Endpoint                          | Auth     | Description             |
|--------|-----------------------------------|----------|-------------------------|
| POST   | /contact                          | Public   | Submit inquiry form     |
| GET    | /contact/inquiries                | ADMIN    | Get all inquiries       |
| PATCH  | /contact/inquiries/{id}/status    | ADMIN    | Update inquiry status   |
| DELETE | /contact/inquiries/{id}           | ADMIN    | Delete inquiry          |

### Testimonials
| Method | Endpoint             | Auth     | Description             |
|--------|----------------------|----------|-------------------------|
| GET    | /testimonials        | Public   | Get all testimonials    |
| POST   | /testimonials        | ADMIN    | Add testimonial         |
| DELETE | /testimonials/{id}   | ADMIN    | Soft-delete testimonial |

### Admin Dashboard
| Method | Endpoint        | Auth     | Description          |
|--------|-----------------|----------|----------------------|
| GET    | /admin/stats    | ADMIN    | Get dashboard stats  |

---

## Authentication Flow

```
Frontend                           Backend
   │                                  │
   │  POST /api/auth/login            │
   │  { "password": "ubtech@..." }    │
   │ ─────────────────────────────►   │
   │                                  │  Validate password
   │  { token: "eyJhbGci..." }        │  Generate JWT
   │ ◄─────────────────────────────   │
   │                                  │
   │  Store token in localStorage     │
   │                                  │
   │  GET /api/gallery                │
   │  Authorization: Bearer eyJhbGci │
   │ ─────────────────────────────►   │
   │                                  │  Validate JWT
   │  { success: true, data: [...] }  │  Return data
   │ ◄─────────────────────────────   │
```

---

## Common Errors & Fixes

| Error | Fix |
|-------|-----|
| `Communications link failure` | MySQL not running. Start MySQL service. |
| `Access denied for user 'root'` | Wrong password in `application.properties` |
| `Port 8080 already in use` | Change `server.port=8081` or kill the process |
| `401 Unauthorized` on admin endpoints | Token missing/expired — login again |
| `403 Forbidden` | Token valid but not ADMIN role |
| `CORS error` in browser | Add your React dev URL to `app.cors.allowed-origins` |

---

## Production Checklist

- [ ] Change `app.admin.password` to a strong password
- [ ] Change `app.jwt.secret` to a random 64-char string
- [ ] Set `spring.jpa.show-sql=false`
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate` (never `update` in prod)
- [ ] Use environment variables for all secrets (never commit passwords)
- [ ] Enable HTTPS / SSL termination
- [ ] Set `app.cors.allowed-origins` to your production domain only

---

*UBT TECHNOLOGY Incubation Backend — v1.0.0*
