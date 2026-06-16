# ThoughtSync – Blog Management System

A modern blogging platform built with Jakarta EE 11, JSF Facelets, JPA (EclipseLink), and PostgreSQL, deployed on Payara Server 7.

---

## Tech Stack

- Java 17
- Jakarta EE 11 (JSF, JPA, Servlet, CDI)
- PostgreSQL
- Payara Server 7
- Maven

---

## Setup Instructions

### 1. Database
```sql
-- Run schema
psql -U postgres -f database/schema.sql

-- Run migrations (if upgrading)
psql -U postgres -d blogdb -f database/migrate.sql
```

### 2. Configure Secrets

These files are gitignored — you must create them manually:

**`src/main/webapp/WEB-INF/payara-resources.xml`**
```bash
cp src/main/webapp/WEB-INF/payara-resources.xml.template src/main/webapp/WEB-INF/payara-resources.xml
# Edit and fill in your DB credentials
```

**`src/main/java/com/mycompany/blogmanagement/util/OAuthConstants.java`**
```bash
cp src/main/java/com/mycompany/blogmanagement/util/OAuthConstants.java.template src/main/java/com/mycompany/blogmanagement/util/OAuthConstants.java
# Edit and fill in your Google OAuth credentials
```

### 3. Build & Deploy
```bash
mvn clean package
# Deploy BlogManagement-1.0-SNAPSHOT.war to Payara
```

### 4. Access
- App: http://localhost:8080/BlogManagement/
- Admin login: `admin` / `admin123`

---

## Default Credentials

| Role  | Username | Password  |
|-------|----------|-----------|
| Admin | admin    | admin123  |

---

## Project Structure

```
src/main/
├── java/
│   └── com/mycompany/blogmanagement/
│       ├── dao/          # Data Access Objects
│       ├── entity/       # JPA Entities
│       ├── servlet/      # HTTP Servlets
│       ├── service/      # Business Logic
│       └── util/         # Utilities
├── resources/
│   └── META-INF/persistence.xml
└── webapp/
    ├── css/
    ├── WEB-INF/
    │   ├── views/        # JSF Facelets pages
    │   ├── includes/     # layout, header, footer
    │   └── web.xml
    └── index.xhtml
```
