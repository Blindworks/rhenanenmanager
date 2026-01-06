# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

RhenanenManager is a full-stack member management system for fraternities/corps with:
- `rhenanenmanager-backend/` - Spring Boot 3.4.1 REST API with Java 21
- `rhenanenmanager-frontend/` - Angular 19 SPA with Angular Material

## Technology Stack

### Backend
- **Java 21** - Modern LTS Java version
- **Spring Boot 3.4.1** - Latest Spring Boot with Jakarta EE 10
- **Maven** - Build system
- **Spring Security** - JWT-based authentication (stateless)
- **Spring Data JPA** - Database access with Hibernate
- **MySQL/MariaDB** - Database (schema name: `rhintern`)
- **Lombok** - Reduces boilerplate code
- **SpringDoc OpenAPI** - API documentation (Swagger UI)
- **Spring Boot DevTools** - Hot reload during development

### Frontend
- **Angular 19** - Modern Angular with standalone components
- **Angular Material** - Material Design UI components
- **TypeScript** - Type-safe development
- **RxJS** - Reactive programming
- **Proxy Configuration** - Proxies `/api` requests to backend during development

## Build & Run Commands

### Backend

**Prerequisites:**
- Java 21 JDK installed
- Maven 3.9+ (or use included Maven wrapper)
- MySQL/MariaDB running on localhost:3306
- Database `rhintern` created

**Build:**
```bash
cd rhenanenmanager-backend
./mvnw clean install
# Windows: mvnw.cmd clean install
```

**Run (development mode):**
```bash
./mvnw spring-boot:run
# Backend runs on http://localhost:8080
```

**Run tests:**
```bash
./mvnw test
```

**Package for production:**
```bash
./mvnw clean package -DskipTests
# JAR: target/rhenanenmanager-backend-0.0.1-SNAPSHOT.jar
```

### Frontend

**Prerequisites:**
- Node.js 20+ installed
- npm 10+ installed

**Install dependencies:**
```bash
cd rhenanenmanager-frontend
npm install
```

**Run (development mode):**
```bash
npm start
# Frontend runs on http://localhost:4200
# Proxies /api requests to http://localhost:8080
```

**Build for production:**
```bash
npm run build
# Output: dist/ directory
```

**Run tests:**
```bash
npm test
```

### Full Development Setup

1. **Start MySQL database** (ensure it's running on localhost:3306)

2. **Create database** (if not exists):
   ```sql
   CREATE DATABASE rhintern CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Start backend:**
   ```bash
   cd rhenanenmanager-backend
   ./mvnw spring-boot:run
   ```

4. **Start frontend** (in new terminal):
   ```bash
   cd rhenanenmanager-frontend
   npm start
   ```

5. **Access application:**
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/swagger-ui.html

## Architecture

### Backend Architecture (Layered)

```
Controller Layer (REST endpoints)
    ↓
Service Layer (Business logic)
    ↓
Repository Layer (Data access)
    ↓
Entity Layer (JPA entities → Database)
```

**Key Packages:**
- `config/` - Spring configuration classes (Security, CORS, OpenAPI)
- `controller/` - REST API endpoints
- `service/` & `service/impl/` - Business logic
- `domain/entity/` - JPA entities mapping to database tables
- `domain/repository/` - Spring Data JPA repositories
- `domain/dto/request/` - Request DTOs
- `domain/dto/response/` - Response DTOs
- `security/` - JWT provider, authentication filter, UserDetailsService
- `exception/` - Global exception handling

### Frontend Architecture

```
Component (UI)
    ↓
Service (Business Logic)
    ↓
HTTP Interceptor (Add JWT token)
    ↓
HttpClient (API calls)
    ↓
Backend REST API
```

**Key Directories:**
- `src/app/core/` - Singleton services, guards, interceptors, models
  - `models/` - TypeScript interfaces
  - `services/` - Business logic services (AuthService, etc.)
  - `interceptors/` - HTTP interceptors (auth token injection)
  - `guards/` - Route guards (authentication)
- `src/app/features/` - Feature modules (lazy-loaded)
  - `auth/` - Authentication features (login)
  - `dashboard/` - Main dashboard
- `src/app/shared/` - Shared components, directives, pipes
- `src/environments/` - Environment configurations

## Authentication Flow

1. User enters credentials in login form (frontend)
2. LoginComponent calls AuthService.login()
3. AuthService sends POST to `/api/auth/login`
4. Backend authenticates via Spring Security
5. JwtTokenProvider generates JWT token
6. Backend returns AuthResponse with token
7. Frontend stores token in localStorage
8. AuthInterceptor adds token to all subsequent API requests
9. Backend JwtAuthenticationFilter validates token and sets SecurityContext
10. Protected routes use AuthGuard to check authentication

## API Endpoints

**Authentication:**
- POST `/api/auth/login` - User login (returns JWT token)
- GET `/api/auth/health` - Health check

**Protected endpoints** require `Authorization: Bearer <token>` header.

## Database

**Connection (development):**
- URL: `jdbc:mysql://localhost:3306/rhintern`
- Username: `root`
- Password: `root` (configurable in `application-dev.yml`)

**Schema:**
- Existing schema from `database_ddl.txt`
- Main tables: `user`, `role`, `profile` (and 70+ others)
- **Important:** Current schema lacks PRIMARY KEY constraints - add them before first run

**Recommended database fixes:**
```sql
ALTER TABLE `user` ADD PRIMARY KEY (`id`);
ALTER TABLE `user` MODIFY `id` BIGINT(20) AUTO_INCREMENT;

ALTER TABLE `role` ADD PRIMARY KEY (`id`);
ALTER TABLE `role` MODIFY `id` BIGINT(20) AUTO_INCREMENT;

ALTER TABLE `profile` ADD PRIMARY KEY (`id`);
ALTER TABLE `profile` MODIFY `id` BIGINT(20) AUTO_INCREMENT;
```

## Development Workflow

### Backend Development

**Profiles:**
- `dev` - Development (verbose logging, Swagger enabled, MySQL localhost)
- `test` - Testing (H2 in-memory database)
- `prod` - Production (minimal logging, Swagger disabled, environment variables)

**Set profile:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Hot reload:** Spring Boot DevTools automatically restarts on code changes

### Frontend Development

**Proxy Configuration:**
- `proxy.conf.json` proxies `/api` to `http://localhost:8080`
- Avoids CORS issues during development
- Automatically used by `npm start`

**Component Generation:**
```bash
ng generate component features/your-feature/your-component --standalone
```

**Service Generation:**
```bash
ng generate service core/services/your-service
```

## Security

**Backend:**
- JWT tokens expire in 24 hours (configurable)
- Passwords hashed with BCrypt
- CORS configured for `http://localhost:4200`
- Public endpoints: `/api/auth/**`, `/actuator/health`, `/swagger-ui/**`
- All other endpoints require authentication

**Frontend:**
- JWT token stored in localStorage
- AuthGuard protects routes
- AuthInterceptor adds token to requests
- Automatic redirect to login on 401

## Common Tasks

**Add new entity:**
1. Create entity class in `domain/entity/`
2. Create repository in `domain/repository/`
3. Create DTOs in `domain/dto/`
4. Create service interface and implementation
5. Create controller
6. Add to Swagger documentation

**Add new frontend feature:**
1. Generate component: `ng g c features/feature-name/component-name --standalone`
2. Create route file: `feature-name.routes.ts`
3. Add lazy route in `app.routes.ts`
4. Create services if needed
5. Add to navigation

## Repository Information

- Remote: https://github.com/Blindworks/rhenanenmanager
- Main branch: `main`

## Troubleshooting

**Backend won't start:**
- Check MySQL is running on port 3306
- Verify database `rhintern` exists
- Check `application-dev.yml` credentials
- Ensure Java 21 is installed

**Frontend won't start:**
- Run `npm install` to install dependencies
- Check Node.js version (20+)
- Clear `node_modules` and reinstall if issues persist

**Login fails:**
- Ensure backend is running
- Check browser console for errors
- Verify proxy is working (requests to `/api` should go to port 8080)
- Check database has user records with valid passwords
