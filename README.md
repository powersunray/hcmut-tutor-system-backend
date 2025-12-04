# 🧑‍🏫 HCMUT Tutor Support System — Backend

**Version**: 1.0.0
**Framework**: Spring Boot 3.5.7
**Database**: PostgreSQL 15
**Java Version**: 17+

---

## 📋 Overview

The **HCMUT Tutor Support System** is a comprehensive Spring Boot application that connects students with tutors for academic support. The system provides tutor search, session booking, feedback management, material sharing, and analytics reporting.

### Key Features

✅ **Tutor Search & Booking** - Browse and filter tutors, book sessions
✅ **User Management** - Students, tutors, and staff with role-based access
✅ **Feedback System** - Students rate and review tutoring sessions
✅ **Material Management** - Upload and share session materials
✅ **Analytics & Reports** - Performance metrics for staff roles
✅ **Notifications** - Async email notifications for system events
✅ **Enrollment Tracking** - Course enrollments with grade tracking

---

## 🧰 Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
| Framework | Spring Boot 3.5.7 |
| Database | PostgreSQL 15 |
| ORM | Spring Data JPA + Hibernate |
| Build Tool | Maven |
| Code Simplification | Lombok |
| Containerization | Docker + Docker Compose |
| Code Formatting | Google Java Format (Spotless) |

---

## 📂 Project Structure

```text
backend/
├── src/main/java/hcmut/edu/vn/tutor_support_system/
│   ├── controller/         # REST API endpoints
│   ├── service/           # Business logic layer
│   ├── repository/        # JPA repositories
│   ├── entity/            # JPA entities
│   ├── dto/               # Data Transfer Objects
│   ├── mapper/            # Entity-to-DTO mappers
│   ├── exception/         # Custom exceptions & handlers
│   ├── config/            # Configuration classes
│   └── util/              # Utility classes
├── src/main/resources/
│   └── application.properties  # Application configuration
├── docs/                  # Documentation
├── postgres_schema_jpa.sql     # Database schema
├── postgres_dummy_data_jpa.sql # Sample data
├── docker-compose.yml     # Docker configuration
├── setup_postgres_db.sh   # Database setup script
└── pom.xml               # Maven configuration
```

---

## 🚀 Getting Started

### Prerequisites

Before running the application, ensure you have:

- ☑️ **Java 17 or higher** ([Download](https://adoptium.net/))
- ☑️ **Maven 3.6+** (or use included Maven wrapper `./mvnw`)
- ☑️ **Docker & Docker Compose** ([Download](https://www.docker.com/))
- ☑️ **Git** (for version control)

### Verify Prerequisites

```bash
# Check Java version
java -version  # Should show version 17 or higher

# Check Maven version
mvn -version   # Should show version 3.6 or higher

# Check Docker
docker --version && docker-compose --version
```

---

## ▶️ Running the Application

### Step 1: Start the Database

The application requires PostgreSQL. Use Docker Compose to start the database with schema and sample data:

```bash
# Option 1: Use the setup script (recommended)
./setup_postgres_db.sh

# Option 2: Use docker-compose directly
docker-compose up -d
```

**What happens:**
- PostgreSQL 15 container starts on port `5432`
- Database `tutoring_system_db` is created
- Schema is initialized from `postgres_schema_jpa.sql`
- Sample data is loaded from `postgres_dummy_data_jpa.sql`
- Data persists in Docker volume `postgres_data`

**Verify database is running:**
```bash
docker-compose ps

# You should see:
# NAME                           STATUS
# tutoring_system_postgres       Up X seconds
```

**Database Connection Details:**
- Host: `localhost`
- Port: `5432`
- Database: `tutoring_system_db`
- Username: `tutoring_user`
- Password: `secure_password123`

---

### Step 2: Build the Application

```bash
# Clean build with Maven
mvn clean install

# Or use Maven wrapper (if Maven not installed)
./mvnw clean install
```

**What happens:**
- Dependencies are downloaded
- Code is compiled
- Tests are executed
- JAR file is created in `target/` directory
- Google Java Format is applied (via Spotless plugin)

---

### Step 3: Run the Backend

**Option 1: Run with Maven**
```bash
mvn spring-boot:run
```

**Option 2: Run from JAR file**
```bash
java -jar target/tutor-support-system-0.0.1-SNAPSHOT.jar
```

**Option 3: Run with VS Code**
1. Open `TutorSupportSystemApplication.java`
2. Click the green ▶️ Run button
3. Choose "Run Java" (NOT "Run Code")

**Verify backend is running:**

Look for these log messages:
```
Started TutorSupportSystemApplication in X.XXX seconds
LivenessState changed to CORRECT
ReadinessState changed to ACCEPTING_TRAFFIC
```

The application will be available at: **http://localhost:8080**

---

## 🧪 Testing the API

### Quick Health Check

Open your browser and visit:
```
http://localhost:8080
```

You should see: **"Hello World!"**

### API Endpoints

#### **Tutor Search**
```bash
# Search all tutors
curl http://localhost:8080/api/tutors/search

# Filter by course and rating
curl "http://localhost:8080/api/tutors/search?course=CO3001&minRating=4.5"

# Get tutor profile
curl http://localhost:8080/api/tutors/tutor-1
```

#### **User Management**
```bash
# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/{userId}
```

#### **Feedback**
```bash
# Submit feedback
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{"sessionId":"SESSION-123","rating":5,"comment":"Great session!"}'

# Get tutor feedback
curl http://localhost:8080/api/feedback/tutor/tutor-1
```

#### **Analytics**
```bash
# Tutor performance report
curl http://localhost:8080/api/analytics/reports/tutor-performance

# Student engagement report
curl http://localhost:8080/api/analytics/reports/student-engagement

# Comprehensive report
curl http://localhost:8080/api/analytics/reports/comprehensive
```

### Complete API Documentation

For detailed API documentation with all endpoints, request/response formats, and examples:

📖 **[API Documentation](docs/API_DOCUMENTATION.md)**

---

## 🛠️ Development Commands

### Building & Running

```bash
# Clean build
mvn clean install

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=FeedbackServiceTest

# Run specific test method
mvn test -Dtest=FeedbackServiceTest#testSubmitFeedback

# Run application
mvn spring-boot:run

# Build without tests
mvn clean install -DskipTests
```

### Code Formatting

This project uses **Google Java Format** via the Spotless Maven plugin.

```bash
# Format all Java files
mvn spotless:apply

# Check formatting (fails if violations found)
mvn spotless:check
```

**Pre-commit Hook**: A Git pre-commit hook automatically formats Java files before each commit. The hook is located at `.git/hooks/pre-commit`.

### Database Management

```bash
# Start database
docker-compose up -d

# Stop database (preserves data)
docker-compose down

# Stop database and remove all data
docker-compose down -v

# View database logs
docker-compose logs postgres

# Access PostgreSQL CLI
docker-compose exec postgres psql -U tutoring_user -d tutoring_system_db
```

### Useful SQL Queries

```sql
-- Check tables
\dt

-- Count users
SELECT user_type, COUNT(*) FROM users GROUP BY user_type;

-- Count sessions
SELECT COUNT(*) FROM tutoring_sessions;

-- Count feedback
SELECT COUNT(*) FROM feedback;

-- Count materials
SELECT COUNT(*) FROM session_materials;
```

---

## 🏗️ Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│         REST Controllers            │  ← HTTP endpoints
├─────────────────────────────────────┤
│         Service Layer               │  ← Business logic
├─────────────────────────────────────┤
│         Repository Layer            │  ← Data access (JPA)
├─────────────────────────────────────┤
│         Database (PostgreSQL)       │  ← Data persistence
└─────────────────────────────────────┘
```

### Key Design Patterns

- **DTO Pattern**: Controllers return DTOs, never entities (prevents circular JSON)
- **Repository Pattern**: Spring Data JPA repositories for data access
- **Service Layer**: All business logic in `@Service` classes
- **Exception Handling**: Centralized via `@ControllerAdvice`
- **Builder Pattern**: DTOs use Lombok `@Builder`
- **Single Table Inheritance**: User hierarchy (User → Student/Tutor/Staff)

### Database Schema

- **Primary Keys**: UUIDs with `uuid_generate_v4()`
- **Inheritance**: Single table inheritance for User types
- **Relationships**: Proper foreign key constraints
- **Timestamps**: Automatic `created_at`, `updated_at`
- **Tables**: users, profiles, subjects, availabilities, tutoring_sessions, feedback, evaluations, session_materials, meeting_notes, enrollments, support_needs

---

## 🔧 Configuration

### Application Properties

Key configurations in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/tutoring_system_db
spring.datasource.username=tutoring_user
spring.datasource.password=secure_password123

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Connection Pool (HikariCP)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=30000
```

### Security

**Current Status**: Spring Security is **disabled** for MVP development.

When enabled, security will require:
- JWT-based authentication
- Role-based access control (STUDENT, TUTOR, STAFF)
- Password encryption

---

## ❗ Troubleshooting

### Database Connection Failed

**Problem**: Application cannot connect to PostgreSQL

**Solutions**:
```bash
# Check if database is running
docker-compose ps

# Check database logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres

# Verify port (should be 5432)
docker-compose ps
```

### Port Already in Use

**Problem**: Port 8080 or 5432 is already in use

**Solutions**:
```bash
# Find process using port 8080
lsof -i :8080
# or
netstat -ano | findstr :8080

# Kill the process (replace PID)
kill -9 <PID>

# For PostgreSQL (port 5432)
docker-compose down
```

### Lombok Not Working

**Problem**: "Cannot find symbol" errors for getters/setters

**Solutions**:
1. Install Lombok plugin for your IDE
   - VS Code: Search "Lombok" in Extensions
   - IntelliJ: File → Settings → Plugins → Search "Lombok"
2. Enable annotation processing in IDE settings
3. Rebuild project: `mvn clean install`

### Tests Failing

**Problem**: Tests fail during build

**Solutions**:
```bash
# Run tests with verbose output
mvn test -X

# Skip tests temporarily
mvn clean install -DskipTests

# Run specific test
mvn test -Dtest=ClassName
```

### Pre-commit Hook Fails

**Problem**: Commit fails with formatting errors

**Solutions**:
```bash
# Manually format code
mvn spotless:apply

# Check formatting
mvn spotless:check

# If hook is missing
chmod +x .git/hooks/pre-commit
```

---

## 🧑‍💻 Development Workflow

### Adding a New Feature

1. **Create feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Update database schema** (if needed)
   - Modify `postgres_schema_jpa.sql`
   - Add entity classes in `entity/` package

3. **Create repository**
   - Add repository interface extending `JpaRepository`

4. **Implement service**
   - Add service class with `@Service`
   - Use `@Transactional` for write operations

5. **Create controller**
   - Add REST controller with `@RestController`
   - Return DTOs, never entities

6. **Write tests**
   - Unit tests for services
   - Integration tests for repositories
   - Controller tests with `@WebMvcTest`

7. **Format code**
   ```bash
   mvn spotless:apply
   ```

8. **Commit changes**
   ```bash
   git add .
   git commit -m "feat: your feature description"
   # Pre-commit hook will auto-format
   ```

---

## 🤝 Contributing

### Code Style

- Follow Google Java Format (enforced by Spotless)
- Use Lombok annotations (`@Getter`, `@Setter`, `@Builder`)
- Write JavaDoc for public APIs
- Keep methods short and focused

### Commit Messages

Use conventional commit format:
```
feat: add new feature
fix: fix bug in existing feature
docs: update documentation
refactor: refactor code
test: add or update tests
chore: maintenance tasks
```

---

## 📞 Support

For questions or issues:
- **Technical Issues**: Check [Troubleshooting](#-troubleshooting) section

---

## 📄 License

Internal use only - HCMUT Tutor Support System

---

**Last Updated**: December 4, 2025
