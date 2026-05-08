# Trevora Web API

Spring Boot API for Trevora. Current scope: registration and login.

## Run

Set the Supabase database environment variables from `.env.example`, then run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Supabase

Use the direct Postgres connection string from Supabase project settings. The JDBC URL should look like:

```text
jdbc:postgresql://YOUR-SUPABASE-HOST:5432/postgres?sslmode=require
```

Hibernate is currently set to `update`, so it will create/update the `app_users` table during development.
