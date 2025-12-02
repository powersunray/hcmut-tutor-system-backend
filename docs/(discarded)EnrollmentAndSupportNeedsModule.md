# Enrollment & Support Needs Module

## Overview
This module manages student program enrollments, course registrations, support need declarations, and subject metadata. It is designed as a self-contained vertical slice that communicates with other modules only via shared IDs (e.g., `studentId`).

## Entities
- **Student**: student profile with academic attributes and audit fields.
- **Enrollment**: program/course enrollment with status tracking (`ACTIVE`, `COMPLETED`, `DROPPED`).
- **SupportNeed**: declared support needs linked to a `SupportType` and priority (`LOW`, `MEDIUM`, `HIGH`).
- **SupportType**: catalog of support categories.
- **Subject**: subject catalog with prerequisites.

```
Student 1---* Enrollment
Student 1---* SupportNeed *---1 SupportType
Subject (prerequisites by code)
```

## Endpoints
- `POST /api/enrollments` – create an enrollment.
- `GET /api/enrollments/student/{studentId}` – list enrollments.
- `PUT /api/enrollments/{id}/status?status=COMPLETED` – update status.
- `DELETE /api/enrollments/{id}` – cancel enrollment.
- `POST /api/support-needs` – declare support need.
- `PUT /api/support-needs/{id}` – update support need.
- `GET /api/support-needs/student/{studentId}?priority=HIGH` – list support needs.
- `DELETE /api/support-needs/{id}` – delete support need.
- `DELETE /api/support-needs/archive?threshold=2024-01-01` – archive old needs.
- `POST /api/subjects` – create subject.
- `GET /api/subjects` – list subjects with optional filters.
- `GET /api/subjects/{id}` – get subject detail.
- `PUT /api/subjects/{id}` – update subject.
- `DELETE /api/subjects/{id}` – delete subject.

Example:
```bash
curl -X POST http://localhost:8080/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"programName":"CS","courseCode":"CS101","semester":"2025A"}'
```

## Setup
- Uses Spring Boot 3, JPA, and H2 (runtime) with auditing enabled.
- Ensure database credentials (if not using H2) are configured via `application.properties`.

## Testing
Run:
```bash
mvn test
```

## Assumptions
- Authentication is handled by a shared security configuration; controllers expect JWT-protected context.
- Student validation currently relies on repository lookups by `studentId`.
