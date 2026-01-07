# Test Users for Development

The application automatically creates test users when running in **development mode** (`dev` profile).

## Available Test Accounts

### Admin User
- **Username:** `admin`
- **Password:** `password`
- **Email:** admin@rhenanenmanager.de
- **Role:** ROLE_ADMIN (Administrator with full access)
- **Name:** Max Mustermann

### Member User
- **Username:** `member`
- **Password:** `password`
- **Email:** member@rhenanenmanager.de
- **Role:** ROLE_MEMBER (Regular member with basic access)
- **Name:** Hans Schmidt

## How It Works

The `DataInitializer` class automatically creates these users on application startup if they don't already exist in the database. This only happens when running with the `dev` profile active.

## Usage

1. Start the backend application with the `dev` profile:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. The console will show logs like:
   ```
   Created test admin user: username=admin, password=password
   Created test member user: username=member, password=password
   Development data initialization completed!
   ```

3. You can now login with either account:
   - Go to http://localhost:4200
   - Click "Login"
   - Enter username and password
   - Access the dashboard

## Security Note

⚠️ **These are test accounts with weak passwords!**
- Only use in development environment
- Never use in production
- Change passwords immediately in production deployments
