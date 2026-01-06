# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**RhenanenManager** ist ein umfassendes Mitgliederverwaltungssystem für deutsche Studentenverbindungen (Corps). Die Anwendung unterstützt das Management des Corps-Alltags mit Fokus auf Mitgliederdaten als Herzstück.

### Was ist ein Corps?
Ein Corps ist eine deutsche Studentenverbindung mit Mensur-Tradition (akademisches Fechten), hierarchischer Struktur und lebenslanger Mitgliedschaft. Mitglieder durchlaufen verschiedene Status:
- **Reception** - Aufnahme als Fuchs (neues Mitglied)
- **Acception** - Aufnahme als Bursche (vollwertiges Aktivmitglied)
- **Philistrierung** - Übergang zum Alten Herren (Alumni-Status)

### Technologie-Stack
- **Backend:** Spring Boot 3.4.1 REST API mit Java 21
- **Frontend:** Angular 19 SPA mit Angular Material
- **Datenbank:** MySQL/MariaDB mit umfangreichem Schema (70+ Tabellen)

### Herzstück: Die Mitgliederdatenbank
Die Datenbank bildet den gesamten Lebenszyklus eines Corps-Mitglieds ab:
- Persönliche Daten und Corps-Mitgliedschaft (Aufnahmedaten, Status, Ämter)
- Mensur-Tracking (Fechtpartien mit detailliertem Protokoll)
- Karriere und Lebensweg (Studium, Beruf, Kontakte)
- Engagement (Veranstaltungen, Übungsabende, Ämter)
- Kommunikation (Forum, E-Mail-Gruppen, Nachrichten)
- Dokumente (Satzungen, Protokolle, Artikel)

## Technology Stack

### Backend
- **Java 21** - Modern LTS Java version
- **Spring Boot 3.4.1** - Latest Spring Boot with Jakarta EE 10
- **Maven** - Build system
- **Spring Security** - JWT-based authentication (stateless)
- **Spring Data JPA** - Database access with Hibernate
- **MySQL/MariaDB** - Database (schema name: `rhenanenmanager` for development, `rhintern` for production)
- **Liquibase** - Database schema migration and version control
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
- Database `rhenanenmanager` created (see DATABASE_SETUP.md)

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

2. **Create development database** (if not exists):
   ```sql
   CREATE DATABASE rhenanenmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   **⚠️ IMPORTANT:** See `DATABASE_SETUP.md` for full setup instructions. The production database `rhintern` is separate and must NOT be modified!

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

## Current Implementation Status

**Implemented (Production Ready):**
- Authentication system with JWT (login, token validation, user management)
- User and role management (User, Role entities)
- Basic profile entity structure
- Security infrastructure (CORS, method-level security)
- API documentation (Swagger/OpenAPI)

**Partially Implemented:**
- Profile entity with all Corps-specific fields (not yet exposed via API)

**Not Yet Implemented:**
- Member/Profile CRUD operations
- Mensur management and tracking
- Event management and attendance
- Practice session tracking
- Document repository
- Forum and communication features
- Employment and career tracking
- Bed/accommodation booking system
- Spefuchs (recruitment) management

## Database

### ⚠️ Database Structure

**TWO SEPARATE DATABASES:**

1. **`rhintern`** (PRODUCTION - DO NOT TOUCH!)
   - Contains all existing production data
   - **NEVER MODIFIED** by development work
   - Old table structure preserved
   - Only used for read-only data migration

2. **`rhenanenmanager`** (DEVELOPMENT - New Structure)
   - New, improved table structure
   - Managed by Liquibase migrations
   - Can be safely dropped and recreated
   - Used for development and testing

**Connection (development):**
- URL: `jdbc:mysql://localhost:3306/rhenanenmanager`
- Username: `root`
- Password: `root` (configurable in `application-dev.yml`)

**📖 See `DATABASE_SETUP.md` for detailed setup instructions!**

**Schema Overview:**
Das Datenbankschema umfasst **70+ Tabellen** in der alten Struktur (`rhintern`). Die neue Struktur (`rhenanenmanager`) wird schrittweise mit Liquibase aufgebaut.

### Kern-Tabellen (Core Member Data)

**User & Authentication:**
- `user` - System-Zugänge (username, password, activation, account locking)
- `role` - Rollen mit granularen Berechtigungen
- `roles_permissions` - Berechtigungszuordnung

**Member Profiles:**
- `profile` - Mitgliederdaten (Name, Geburt, Kontakt, Corps-Daten)
  - Persönliche Daten: firstname, lastname, birth_date, birth_place, title
  - Corps-Mitgliedschaft: reception_date, acception_date, philistrierung_date
  - Status-Tracking: status, number, quit_date, quited
  - Karriere: honorary_appointments, life_stations, national_service
- `corps_data` - Erweiterte Corps-Daten (alternative Struktur)
- `profile_setting` - Mitgliedereinstellungen und Benachrichtigungspräferenzen

### Corps-spezifische Tabellen

**Mensur (Akademisches Fechten):**
- `mensur` - Mensur-Übersicht (Datum, Typ, Ergebnis, Anzahl Gänge)
- `mensur_day` - Mensur-Veranstaltungen (Pauktage mit Location, Ärzten)
- `match_protocol` - Detailliertes Fechtprotokoll (50+ Spalten)
  - Technik: terz, hoch_quart, horizontal_quart, prim, quart_parade
  - Leistung: geschwindigkeit, haerte, technik, kondition
  - Ergebnis: gaenge, blutige, nadeln, finish, abstimmung

**Ämter und Organisation:**
- `amt` - Corps-Ämter/Positionen (z.B. Senior, Consenior, Fuchsmajor)
- `amt_history` - Amtszeiten-Historie (wer hatte wann welches Amt)
- `fraternity` - Verbindungs-Stammdaten (Name, Stadt, Dachverband)
- `ahsc` - Altherrenschaft (regionale Organisation)

**Veranstaltungen:**
- `event` - Events (Name, Datum, Typ, Pflicht/Optional, Wiederholung)
- `attendance` - Zusagen mit Reminder-System
- `external_attendance` - Gäste (Partner, Familie)
- `guest` - Gast-Tracking

**Übungsbetrieb:**
- `planned_practice_session` - Geplante Übungsabende (wöchentlich)
- `occured_practice_session` - Tatsächlich stattgefundene Übungen
- `occured_practice_session_attendance` - Anwesenheit mit Entschuldigungen
- `penalty` - Strafen bei unentschuldigtem Fehlen

### Kommunikation & Content

**Forum & Messaging:**
- `forum_message` - Forum-Beiträge (mit Voting, Views, Threading)
- `forum_message_voting` - Bewertungen von Beiträgen
- `mail_group` - E-Mail-Verteiler (z.B. Aktivitas, Altherrenschaft)
- `mail_group_message` - Nachrichten an Verteiler
- `profile_mail_group` - Mitgliedschaft in Verteilern

**Dokumente & News:**
- `document` - Dokumente (Satzungen, Protokolle, PDFs)
- `news` - Neuigkeiten mit Kategorien und Gültigkeit
- `article_entry` - Artikel-Archiv (Rhenanenruf, Publikationen)
- `information_card` - Info-Karten für Dashboard
- `manuals` - Systemhandbücher

### Weitere wichtige Tabellen

**Karriere & Kontakte:**
- `job_history` - Beruflicher Werdegang
- `employer` - Arbeitgeber mit Abteilung
- `job` - Stellenangebote im Netzwerk
- `contact` - Kontaktdaten (E-Mail, Telefon, Skype)
- `address` - Adressen mit Geokoordinaten

**Nachwuchsgewinnung:**
- `spefuchs` - Interessenten/Kandidaten
- `spefuchs_activity` - Aktivitäten mit Kandidaten
- `spefuchs_comment` - Kommentare zu Kandidaten
- `spefuchs_connection` - Verbindungen zu aktiven Mitgliedern

**Unterkunft & Finanzen:**
- `bed` - Betten/Zimmer im Verbindungshaus
- `bed_booking` - Buchungen für Übernachtungen
- `charge` - Chargen (Mitgliedsbeiträge)
- `fee` - Gebühren und Strafen

**System & Support:**
- `status` - Mitglieder-Status (Fuchs, Bursche, Inaktiver, Alter Herr)
- `connection` - Beziehungen zwischen Mitgliedern (Leibbursch-Leibfuchs)
- `semester` - Semester-Zeiträume
- `system_event` - Audit-Log für Änderungen
- `system_setting` - Systemeinstellungen

**Database Migration with Liquibase:**

Die neue Datenbank-Struktur wird vollständig von Liquibase verwaltet:
- Changelogs in `src/main/resources/db/changelog/`
- Automatische Schema-Erstellung beim Backend-Start
- Versionierung aller Datenbank-Änderungen
- Alle Tabellen mit PRIMARY KEYs, FOREIGN KEYs und Indexes

**Migration Status (v1.0):**
- ✅ `role`, `status` - Basis-Tabellen mit Default-Werten
- ✅ `address`, `contact`, `employer` - Support-Tabellen
- ✅ `user` - Authentifizierung (improved structure)
- ✅ `profile` - Persönliche Daten (OHNE Corps-Felder)
- ✅ `corps_member_data` - Corps-spezifische Daten (separiert)
- ✅ `honorary_appointment`, `life_station`, `military_service` - Strukturierte Daten
- ✅ `user_activity_tracking` - UI-Tracking (separiert)

**Noch zu migrieren:** 60+ weitere Tabellen (event, mensur, forum, etc.)

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

## Planned Corps-Specific Features

Die folgenden Features sind durch das Datenbankschema vorbereitet und warten auf Implementierung:

### 1. Mitgliederverwaltung (Core Feature)
- **Mitglieder-CRUD**: Anlegen, Bearbeiten, Anzeigen von Profilen
- **Corps-Lifecycle**: Tracking von Reception → Acception → Philistrierung
- **Leibbursch-System**: Zuordnung von Leibburschen zu Leibfüchsen
- **Status-Management**: Fuchs, Bursche, Inaktiver Bursche, Alter Herr, Ehrenmitglied
- **Mitglieder-Suche**: Nach Name, Charge, Status, Aufnahmedatum

### 2. Mensur-Management (Fechtsport)
- **Mensur-Planung**: Pauktage anlegen mit Location, Datum, Ärzten
- **Paukprotokoll**: Detailliertes Protokoll mit 50+ technischen Metriken
  - Technik-Bewertung (Terz, Quart, Prim, Paraden)
  - Leistungs-Tracking (Geschwindigkeit, Härte, Kondition)
  - Ergebnis-Dokumentation (Gänge, Blutige, Nadeln)
- **Mensur-Historie**: Übersicht aller Mensuren pro Mitglied
- **Statistiken**: Mensuren-Anzahl, Erfolgsquote, Technik-Entwicklung

### 3. Veranstaltungsmanagement
- **Event-Kalender**: Pflicht- und optionale Veranstaltungen
- **RSVP-System**: Zusagen mit automatischen Reminders
- **Gäste-Management**: Partner und externe Gäste verwalten
- **Wiederkehrende Events**: Wöchentliche/monatliche Termine (Kneipen)
- **Anwesenheits-Tracking**: Wer war bei welcher Veranstaltung

### 4. Übungsbetrieb & Strafen
- **Übungsplan**: Wöchentliche Paukübungen planen
- **Anwesenheitsliste**: Teilnahme erfassen mit Verspätungen
- **Entschuldigungen**: Entschuldigte/unentschuldigte Abwesenheit
- **Strafen-System**: Automatische Strafen bei Nichterscheinen
- **Nachpauken**: Tracking von nachgeholten Übungen

### 5. Ämter-Verwaltung
- **Amts-Hierarchie**: Senior, Consenior, Fuchsmajor, etc.
- **Amtszeiten**: Historische Übersicht wer wann welches Amt hatte
- **Gremien**: Zuordnung zu Aktivitas, AH-Verband, Ehrenrat
- **Nachfolge-Planung**: Vorschläge für Amtsnachfolger

### 6. Kommunikations-Plattform
- **Internes Forum**: Diskussionen mit Voting und Threading
- **E-Mail-Verteiler**: Gruppenmails an Aktivitas, AH, Gesamtverbindung
- **News-System**: Neuigkeiten mit Kategorien und Gültigkeit
- **Benachrichtigungen**: E-Mail-Reminder für Events, News, Forum

### 7. Dokumenten-Archiv
- **Dokumente**: Satzungen, Protokolle, Historische Dokumente
- **Artikel-Bibliothek**: Rhenanenruf-Artikel, Publikationen
- **Download-Tracking**: Wer hat was wann heruntergeladen
- **Kategorisierung**: Nach Typ, Datum, Autor

### 8. Karriere-Netzwerk
- **Beruflicher Werdegang**: CV-Tracking der Mitglieder
- **Arbeitgeber-Datenbank**: Netzwerk von Firmen
- **Stellenangebote**: Interne Job-Börse
- **Kontakt-Verwaltung**: Geschäftliche und private Kontakte

### 9. Nachwuchsgewinnung (Spefuchs-System)
- **Kandidaten-Verwaltung**: Interessenten tracken
- **Aktivitäten-Log**: Kontakte und Veranstaltungen dokumentieren
- **Bewertung**: Chance, Fitting, Kategorie bewerten
- **Zuordnung**: Kontaktperson aus Aktivitas zuweisen

### 10. Unterkunft & Finanzen
- **Betten-Buchung**: Zimmer im Verbindungshaus reservieren
- **Chargen-Verwaltung**: Mitgliedsbeiträge tracken
- **Gebühren**: Strafen und Sondergebühren verwalten

## Repository Information

- Remote: https://github.com/Blindworks/rhenanenmanager
- Main branch: `main`

## Troubleshooting

**Backend won't start:**
- Check MySQL is running on port 3306
- Verify database `rhenanenmanager` exists (NOT `rhintern`!)
- Check `application-dev.yml` credentials
- Ensure Java 21 is installed
- Check Liquibase logs for migration errors

**Frontend won't start:**
- Run `npm install` to install dependencies
- Check Node.js version (20+)
- Clear `node_modules` and reinstall if issues persist

**Login fails:**
- Ensure backend is running
- Check browser console for errors
- Verify proxy is working (requests to `/api` should go to port 8080)
- Check database has user records with valid passwords
