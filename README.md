# 🧑‍🏫 Tutor Support System — Backend (Spring Boot MVP)

This repository contains the **backend MVP** for the **Tutor Support System** project.

Implements:
- **UC-4** – Tutor Search (Browse + Filters + DTO responses)
- **UC-5** – Session Booking (uses hard-coded data, no DB)

> All data are stored **in memory** inside the `repository` package.  
> Responses use **DTOs** to avoid circular JSON (no nested loops).

---

## 🧰 Tech Stack

| Layer | Technology |
|-------|-------------|
| Language | Java 17 or higher |
| Framework | Spring Boot 3.x |
| Build Tool | Maven |
| Code Simplifier | Lombok |
| IDE | Visual Studio Code or IntelliJ IDEA |
| REST Testing | Browser / Postman / curl |

---

## 📂 Project Structure

```text
src/main/java/hcmut/edu/vn/tutor_support_system/
│
├── controller/          # REST controllers (TutorSearch, SessionBooking)
├── service/             # Business logic (search, booking)
├── repository/          # Hard-coded data (no database)
├── entity/              # Domain models
├── dto/                 # DTO classes for responses
├── mapper/              # DtoMapper → converts entities to DTOs
├── exception/           # Custom exceptions
└── TutorSupportSystemApplication.java  # Spring Boot entry point
```


## ▶️ Run the Project
Option 1: Run with VS Code Run button
1. Open the project folder in VS Code.
2. Open TutorSupportSystemApplication.java.
3. From the green Run button dropdown, choose "Run Java" (⚠️ not “Run Code”).
4. Wait for log lines such as:
"""
Started TutorSupportSystemApplication in ...
LivenessState changed to CORRECT
ReadinessState changed to ACCEPTING_TRAFFIC
"""
5. Visit: http://localhost:8080

Option 2: Run from Terminal (JAR file)
```text
mvn clean install && java -jar target/tutor-support-system-0.0.1-SNAPSHOT.jar
```

## 🧪 Quick API Tests
| Action                           | URL                                                                     |
| -------------------------------- | ----------------------------------------------------------------------- |
| List all tutors                  | http://localhost:8080/api/tutors/search                                 |
| Filter by course, campus, rating | http://localhost:8080/api/tutors/search?course=CO3001&minRating=4.5     |
| View a tutor profile             | http://localhost:8080/api/tutors/tutor-1                                |
| Get available slots              | http://localhost:8080/api/tutors/tutor-2/slots                          |

## ⚠️ Notes
No database required — repositories hold sample data.
All controllers return DTOs; entities are never exposed directly.
To reload data, just restart the server.
