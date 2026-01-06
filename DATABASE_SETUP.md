# Datenbank Setup

## ⚠️ WICHTIG: Datenbank-Struktur

Wir verwenden **zwei separate Datenbanken**:

### 1. `rhintern` (PRODUKTIV - NICHT ANFASSEN!)
- Enthält **alle bestehenden Produktivdaten**
- **WIRD NICHT VERÄNDERT**
- Alte Tabellenstruktur bleibt erhalten
- Nur lesend für Datenmigration verwenden

### 2. `rhenanenmanager` (ENTWICKLUNG - Neue Struktur)
- Neue, verbesserte Tabellenstruktur
- Wird von Liquibase verwaltet
- Kann gefahrlos gelöscht und neu erstellt werden
- Für Entwicklung und Tests

---

## Setup für Entwicklung

### Schritt 1: Neue Entwicklungsdatenbank erstellen

Verbinde dich mit MySQL und führe aus:

```sql
-- Neue Datenbank für Entwicklung erstellen
CREATE DATABASE rhenanenmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Optional: Separaten User für Entwicklung erstellen
CREATE USER 'rhenanenmanager'@'localhost' IDENTIFIED BY 'dev_password';
GRANT ALL PRIVILEGES ON rhenanenmanager.* TO 'rhenanenmanager'@'localhost';
FLUSH PRIVILEGES;
```

### Schritt 2: Backend starten

```bash
cd rhenanenmanager-backend
./mvnw spring-boot:run
```

Liquibase erstellt automatisch alle Tabellen in `rhenanenmanager` beim ersten Start!

### Schritt 3: Überprüfen

Verbinde dich mit der neuen Datenbank:

```sql
USE rhenanenmanager;
SHOW TABLES;

-- Sollte zeigen:
-- +---------------------------+
-- | Tables_in_rhenanenmanager |
-- +---------------------------+
-- | address                   |
-- | contact                   |
-- | corps_member_data         |
-- | employer                  |
-- | honorary_appointment      |
-- | life_station              |
-- | military_service          |
-- | profile                   |
-- | role                      |
-- | status                    |
-- | user                      |
-- | user_activity_tracking    |
-- | DATABASECHANGELOG         |
-- | DATABASECHANGELOGLOCK     |
-- +---------------------------+
```

---

## Umgebungen

### Development (`application-dev.yml`)
- Datenbank: `rhenanenmanager`
- URL: `jdbc:mysql://localhost:3306/rhenanenmanager`
- Liquibase: enabled
- Hibernate DDL: validate

### Production (`application-prod.yml`)
- Datenbank: `rhintern` (wird später zu `rhenanenmanager` umgestellt)
- URL: Über Umgebungsvariable `DATABASE_URL`
- Liquibase: enabled
- Hibernate DDL: validate

---

## Migration bestehender Daten (später)

Wenn die neue Struktur fertig ist, erstellen wir ein separates Migrations-Skript:

```sql
-- Beispiel für User-Migration (noch NICHT ausführen!)
INSERT INTO rhenanenmanager.user (username, email, password, firstname, lastname, ...)
SELECT username, email, password, firstname, lastname, ...
FROM rhintern.user;

-- Beispiel für Profile-Migration
INSERT INTO rhenanenmanager.profile (user_id, firstname, lastname, ...)
SELECT u.id, p.firstname, p.lastname, ...
FROM rhintern.profile p
JOIN rhenanenmanager.user u ON p.user_id = u.id;
```

**Status:** ⏸️ Migrations-Skript wird später erstellt, wenn alle Tabellen fertig sind

---

## Datenbank zurücksetzen (nur Development!)

Falls du die Entwicklungsdatenbank neu aufsetzen möchtest:

```sql
-- ACHTUNG: Löscht alle Daten in rhenanenmanager!
DROP DATABASE rhenanenmanager;
CREATE DATABASE rhenanenmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Dann Backend neu starten, und Liquibase erstellt alles neu.

---

## Liquibase Changelog-Tabellen

Liquibase erstellt automatisch zwei Management-Tabellen:

- `DATABASECHANGELOG` - Historie aller ausgeführten Changesets
- `DATABASECHANGELOGLOCK` - Verhindert parallele Migrations-Konflikte

**Diese Tabellen NICHT manuell ändern!**

---

## Troubleshooting

### Problem: "Table 'rhintern.user' doesn't exist"
**Lösung:** Du verwendest noch die alte DB-Konfiguration. Stelle sicher, dass `application-dev.yml` auf `rhenanenmanager` zeigt.

### Problem: Liquibase läuft nicht
**Lösung:** Prüfe in den Logs:
```
Liquibase: Executing changeset: db/changelog/changesets/v1.0/001-create-role-table.xml
```

### Problem: "Access denied for user 'root'@'localhost'"
**Lösung:** Prüfe MySQL-Credentials in `application-dev.yml`

---

## Sicherheits-Checkliste

- ✅ `rhintern` wird **niemals** von Liquibase verändert
- ✅ Development verwendet `rhenanenmanager`
- ✅ `drop-first: false` in allen Environments
- ✅ Produktivdatenbank wird vor Migration gesichert
- ✅ Migrations-Skript wird separat getestet
