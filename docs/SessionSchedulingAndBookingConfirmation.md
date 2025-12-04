# Session Scheduling & Booking API Documentation

## Overview
This API manages tutoring session booking, scheduling, and status updates. It provides endpoints for viewing available time slots, creating bookings, managing session lifecycle (confirmation, cancellation, rescheduling), and retrieving session history for both tutors and students.

---

## Base URL
```
http://localhost:8080/api
```

---

## Data Models

### Session Response
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-20T09:00:00",
  "endTime": "2025-01-20T10:00:00",
  "mode": "HYBRID",
  "status": "CONFIRMED",
  "locationOrLink": "Room C6-403 & https://zoom.us/meeting"
}
```

### Availability Slot
```json
{
  "availabilityId": "slot-1",
  "tutorId": "tutor-1",
  "dayOfWeek": "MONDAY",
  "startTime": "09:00:00",
  "endTime": "10:00:00",
  "status": "AVAILABLE"
}
```

### Enums

**Session Status:**
- `PENDING` – Awaiting tutor confirmation
- `PENDING_TUTOR_APPROVAL` – Requires explicit tutor approval
- `CONFIRMED` – Tutor has confirmed the booking
- `CANCELLED` – Session cancelled by tutor or student
- `COMPLETED` – Session has been completed

**Session Mode:**
- `ONLINE` – Virtual session via video call
- `OFFLINE` – In-person meeting
- `HYBRID` – Flexible (student can choose online or offline)

---

## Endpoints

### 1. Get Available Slots
**GET** `/tutors/{tutorId}/slots`

Retrieve all available time slots for a specific tutor.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `tutorId` | string | path | Yes | Tutor identifier (e.g., `tutor-1`) |

**Success Response (200):**
```json
[
  {
    "availabilityId": "slot-1",
    "tutorId": "tutor-1",
    "dayOfWeek": "MONDAY",
    "startTime": "09:00:00",
    "endTime": "10:00:00",
    "status": "AVAILABLE"
  }
]
```

**Error Responses:**
- `404 Not Found` – Tutor not found

**Example:**
```bash
curl -X GET http://localhost:8080/api/tutors/tutor-1/slots
```

---

### 2. Book a Session
**POST** `/tutors/{tutorId}/sessions`

Create a new session booking from an available slot.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `tutorId` | string | path | Yes | Tutor identifier |

**Request Body:**
```json
{
  "studentId": "2110001",
  "availabilityId": "slot-2",
  "preferredMode": "HYBRID"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `studentId` | string | Yes | Student identifier |
| `availabilityId` | string | Yes | Availability slot ID to book |
| `preferredMode` | string | Yes | `ONLINE`, `OFFLINE`, or `HYBRID` |

**Success Response (200):**
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-20T09:00:00",
  "endTime": "2025-01-20T10:00:00",
  "mode": "HYBRID",
  "status": "CONFIRMED",
  "locationOrLink": "Room C6-403 & https://zoom.us/meeting"
}
```

**Error Responses:**
- `400 Bad Request` – Invalid session details or availability ID
- `404 Not Found` – Student, tutor, or availability slot not found
- `409 Conflict` – Slot already booked (booking conflict)

**Example:**
```bash
curl -X POST http://localhost:8080/api/tutors/tutor-1/sessions \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2110001",
    "availabilityId": "slot-2",
    "preferredMode": "ONLINE"
  }'
```

---

### 3. Get Session by ID
**GET** `/sessions/{sessionId}`

Retrieve detailed information about a specific session.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `sessionId` | string | path | Yes | Session UUID or session ID string |

**Success Response (200):**
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-20T09:00:00",
  "endTime": "2025-01-20T10:00:00",
  "mode": "HYBRID",
  "status": "PENDING",
  "locationOrLink": "Room C6-403 & https://zoom.us/meeting"
}
```

**Error Responses:**
- `404 Not Found` – Session not found

**Example:**
```bash
curl -X GET http://localhost:8080/api/sessions/14141414-1414-1414-1414-141414141414
```

---

### 4. Get Sessions by Tutor
**GET** `/sessions/tutor/{tutorId}`

Retrieve all sessions for a specific tutor.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `tutorId` | string | path | Yes | Tutor identifier |

**Success Response (200):**
```json
[
  {
    "sessionId": "14141414-1414-1414-1414-141414141414",
    "tutorId": "tutor-1",
    "studentId": "2110001",
    "startTime": "2025-01-20T09:00:00",
    "endTime": "2025-01-20T10:00:00",
    "mode": "HYBRID",
    "status": "CONFIRMED",
    "locationOrLink": "Room C6-403"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/sessions/tutor/tutor-1
```

---

### 5. Get Sessions by Student
**GET** `/sessions/student/{studentId}`

Retrieve all sessions for a specific student.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `studentId` | string | path | Yes | Student identifier |

**Success Response (200):**
```json
[
  {
    "sessionId": "14141414-1414-1414-1414-141414141414",
    "tutorId": "tutor-1",
    "studentId": "2110001",
    "startTime": "2025-01-20T09:00:00",
    "endTime": "2025-01-20T10:00:00",
    "mode": "HYBRID",
    "status": "CONFIRMED",
    "locationOrLink": "Room C6-403"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/sessions/student/2110001
```

---

### 6. Update Session Status
**PUT** `/sessions/{sessionId}/status`

Update the status of a session (e.g., confirm, cancel, complete).

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `sessionId` | string | path | Yes | Session UUID |
| `status` | string | query | Yes | `PENDING`, `CONFIRMED`, `CANCELLED`, `COMPLETED` |

**Success Response (200):**
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-20T09:00:00",
  "endTime": "2025-01-20T10:00:00",
  "mode": "HYBRID",
  "status": "CONFIRMED",
  "locationOrLink": "Room C6-403"
}
```

**Error Responses:**
- `400 Bad Request` – Invalid session ID format or invalid status value
- `404 Not Found` – Session not found

**Example:**
```bash
curl -X PUT "http://localhost:8080/api/sessions/14141414-1414-1414-1414-141414141414/status?status=CONFIRMED"
```

---

### 7. Update Session Details
**PUT** `/sessions/{sessionId}`

Update session mode or location/link details.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `sessionId` | string | path | Yes | Session UUID |

**Request Body:**
```json
{
  "mode": "OFFLINE",
  "locationOrLink": "Room C6-403 & https://zoom.us/meeting"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `mode` | string | No | `ONLINE`, `OFFLINE`, or `HYBRID` |
| `locationOrLink` | string | No | Meeting location or video call link |

**Success Response (200):**
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-20T09:00:00",
  "endTime": "2025-01-20T10:00:00",
  "mode": "OFFLINE",
  "status": "CONFIRMED",
  "locationOrLink": "Room C6-403 & https://zoom.us/meeting"
}
```

**Error Responses:**
- `400 Bad Request` – Invalid session ID format
- `404 Not Found` – Session not found

**Example:**
```bash
curl -X PUT http://localhost:8080/api/sessions/14141414-1414-1414-1414-141414141414 \
  -H "Content-Type: application/json" \
  -d '{
    "mode": "OFFLINE",
    "locationOrLink": "Room C6-403"
  }'
```

---

### 8. Reschedule Session
**PUT** `/sessions/{sessionId}/reschedule`

Move a session to a different available time slot.

**Parameters:**
| Name | Type | Location | Required | Description |
|------|------|----------|----------|-------------|
| `sessionId` | string | path | Yes | Session UUID |

**Request Body:**
```json
{
  "availabilityId": "slot-3"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `availabilityId` | string | Yes | New availability slot ID |

**Success Response (200):**
```json
{
  "sessionId": "14141414-1414-1414-1414-141414141414",
  "tutorId": "tutor-1",
  "studentId": "2110001",
  "startTime": "2025-01-21T10:00:00",
  "endTime": "2025-01-21T11:00:00",
  "mode": "HYBRID",
  "status": "PENDING",
  "locationOrLink": "Room C6-403"
}
```

**Error Responses:**
- `400 Bad Request` – Invalid UUID format
- `404 Not Found` – Session or availability slot not found
- `409 Conflict` – New slot is unavailable

**Example:**
```bash
curl -X PUT http://localhost:8080/api/sessions/14141414-1414-1414-1414-141414141414/reschedule \
  -H "Content-Type: application/json" \
  -d '{
    "availabilityId": "slot-3"
  }'
```

---

## Common Workflows

### Complete Booking Flow

1. **Student views available slots**
```bash
   GET /tutors/tutor-1/slots
```

2. **Student books a session**
```bash
   POST /tutors/tutor-1/sessions
   Body: {
     "studentId": "2110001",
     "availabilityId": "slot-2",
     "preferredMode": "HYBRID"
   }
```

3. **Check session status (if pending approval)**
```bash
   GET /sessions/{sessionId}
```

4. **Tutor confirms the booking**
```bash
   PUT /sessions/{sessionId}/status?status=CONFIRMED
```

5. **Update session details (optional)**
```bash
   PUT /sessions/{sessionId}
   Body: {
     "mode": "OFFLINE",
     "locationOrLink": "Room C6-403"
   }
```

6. **View all sessions**
```bash
   GET /sessions/tutor/tutor-1
   GET /sessions/student/2110001
```

### Cancellation & Rescheduling Flow

1. **Cancel session**
```bash
   PUT /sessions/{sessionId}/status?status=CANCELLED
```

2. **Reschedule to new slot**
```bash
   PUT /sessions/{sessionId}/reschedule
   Body: { "availabilityId": "slot-5" }
```

---

## Error Handling

All error responses follow this format:
```json
{
  "status": 404,
  "message": "Session not found: 14141414-1414-1414-1414-141414141414",
  "timestamp": "2025-01-20T14:30:00",
  "path": "/sessions/14141414-1414-1414-1414-141414141414"
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| `200 OK` | Request successful |
| `400 Bad Request` | Invalid input, format, or business rule violation |
| `404 Not Found` | Resource not found (session, tutor, student, availability) |
| `409 Conflict` | Booking conflict or slot unavailable |

---

## Notes & Assumptions

- **Session IDs:** The API accepts both UUID format and custom session ID strings. UUIDs are preferred for internal operations.
- **Authentication:** Assumes JWT-based authentication is handled by upstream security configuration. Controllers expect authenticated context.
- **Hybrid Mode:** When a tutor offers `HYBRID` mode, students must specify their preferred mode (`ONLINE` or `OFFLINE`) when booking.
- **Pending Approval:** Some tutors require explicit confirmation. Sessions may be created with status `PENDING` or `PENDING_TUTOR_APPROVAL`.
- **Slot Availability:** Once booked, slots are marked unavailable. Attempting to book the same slot again will result in a `409 Conflict`.

---

## Testing

Use the provided `requests.http` file with REST Client extensions (VS Code, IntelliJ) or import into Postman for comprehensive API testing covering normal flows, alternative flows, and exception cases.