# API Reference for Frontend Developers

This document provides an overview of all API endpoints available in the Student Tutoring System Backend. It includes information about required parameters, expected request/response formats, and status codes.

## Base URL

All endpoints are relative to: `http://localhost:8080`

## Authentication

Authentication is not required for any endpoints in this MVP version.

---

## Enrollment Management

### Get All Enrollments for a Student

```
GET /api/students/{studentId}/enrollments
```

#### Parameters

- `studentId` (path, required): Student's ID (7-digit number)
- `semester` (query, optional): Semester code in YYS format (e.g., 241, 242, 251)

#### Response

- `200 OK`: Returns an array of enrollment objects
- `400 Bad Request`: Invalid student ID format
- `404 Not Found`: Student not found

#### Example Request

```
GET /api/students/2110001/enrollments
```

---

### Get All Enrollments by Filters

```
GET /api/enrollments
```

#### Parameters

- `semester` (query, optional): Semester code in YYS format
- `status` (query, optional): Enrollment status (ACTIVE, COMPLETED, DROPPED)

#### Response

- `200 OK`: Returns an array of enrollment objects
- `400 Bad Request`: Invalid semester or status format

#### Example Request

```
GET /api/enrollments?semester=251&status=ACTIVE
```

---

### Create New Enrollment

```
POST /api/students/{studentId}/enrollments
```

#### Parameters

- `studentId` (path, required): Student's ID (7-digit number)
- `subjectCode` (query, required): Subject code (e.g., CO3001)
- `semester` (query, required): Semester code in YYS format

#### Response

- `201 Created`: New enrollment object
- `400 Bad Request`: Invalid parameters
- `409 Conflict`: Duplicate enrollment

#### Example Request

```
POST /api/students/2110001/enrollments?subjectCode=CO3001&semester=251
```

---

### Update Enrollment Status

```
PUT /api/enrollments/{enrollmentId}/status
```

#### Parameters

- `enrollmentId` (path, required): UUID of the enrollment
- `status` (query, required): New status (ACTIVE, COMPLETED, DROPPED)

#### Response

- `200 OK`: Updated enrollment object
- `400 Bad Request`: Invalid status
- `404 Not Found`: Enrollment not found

#### Example Request

```
PUT /api/enrollments/50505050-5050-5050-5050-505050505050/status?status=DROPPED
```

---

### Update Enrollment Grade

```
PUT /api/enrollments/{enrollmentId}/grade
```

#### Parameters

- `enrollmentId` (path, required): UUID of the enrollment
- `grade` (query, required): New grade (e.g., A, A-, B+, B, etc.)

#### Response

- `200 OK`: Updated enrollment object
- `400 Bad Request`: Invalid grade format
- `404 Not Found`: Enrollment not found

#### Example Request

```
PUT /api/enrollments/52525252-5252-5252-5252-525252525252/grade?grade=A-
```

---

### Delete Enrollment

```
DELETE /api/enrollments/{enrollmentId}
```

#### Parameters

- `enrollmentId` (path, required): UUID of the enrollment

#### Response

- `204 No Content`: Successfully deleted
- `404 Not Found`: Enrollment not found

#### Example Request

```
DELETE /api/enrollments/52525252-5252-5252-5252-525252525252
```

---

## Support Needs Management

### Get All Support Needs for a Student

```
GET /api/students/{studentId}/support-needs
```

#### Parameters

- `studentId` (path, required): Student's ID (7-digit number)

#### Response

- `200 OK`: Returns an array of support need objects
- `400 Bad Request`: Invalid student ID format
- `404 Not Found`: Student not found

#### Example Request

```
GET /api/students/2110001/support-needs
```

---

### Get Support Needs by Filters

```
GET /api/support-needs
```

#### Parameters

- `type` (query, optional): Support type (SCHOLARSHIP, ACADEMIC_HELP, ADVISING)
- `status` (query, optional): Support need status (PENDING, FULFILLED, CANCELLED)

#### Response

- `200 OK`: Returns an array of support need objects
- `400 Bad Request`: Invalid type or status

#### Example Request

```
GET /api/support-needs?type=ACADEMIC_HELP&status=PENDING
```

---

### Create New Support Need

```
POST /api/students/{studentId}/support-needs
```

#### Parameters

- `studentId` (path, required): Student's ID (7-digit number)
- `supportType` (query, required): Support type (SCHOLARSHIP, ACADEMIC_HELP, ADVISING)
- `description` (query, optional): Description of the support need

#### Response

- `201 Created`: New support need object with PENDING status
- `400 Bad Request`: Invalid support type

#### Example Request

```
POST /api/students/2110001/support-needs?supportType=ACADEMIC_HELP&description=Need%20help%20with%20final%20project
```

---

### Update Support Need Status

```
PUT /api/support-needs/{supportNeedId}/status
```

#### Parameters

- `supportNeedId` (path, required): UUID of the support need
- `status` (query, required): New status (PENDING, FULFILLED, CANCELLED)

#### Response

- `200 OK`: Updated support need object
- `400 Bad Request`: Invalid status
- `404 Not Found`: Support need not found

#### Example Request

```
PUT /api/support-needs/70707070-7070-7070-7070-707070707070/status?status=FULFILLED
```

---

### Update Support Need Description

```
PUT /api/support-needs/{supportNeedId}/description
```

#### Parameters

- `supportNeedId` (path, required): UUID of the support need
- `description` (query, required): New description

#### Response

- `200 OK`: Updated support need object
- `400 Bad Request`: Invalid request
- `404 Not Found`: Support need not found

#### Example Request

```
PUT /api/support-needs/70707070-7070-7070-7070-707070707070/description?description=Updated:%20Found%20a%20great%20tutor
```

---

### Get Support Needs Statistics

```
GET /api/support-needs/statistics
```

#### Parameters

- `type` (query, optional): Support type (SCHOLARSHIP, ACADEMIC_HELP, ADVISING)
- `status` (query, optional): Support need status (PENDING, FULFILLED, CANCELLED)

#### Response

- `200 OK`: Returns a count of support needs matching the criteria
- `400 Bad Request`: Invalid type or status

#### Example Request

```
GET /api/support-needs/statistics?type=ACADEMIC_HELP&status=PENDING
```

---

### Delete Support Need

```
DELETE /api/support-needs/{supportNeedId}
```

#### Parameters

- `supportNeedId` (path, required): UUID of the support need

#### Response

- `204 No Content`: Successfully deleted
- `404 Not Found`: Support need not found

#### Example Request

```
DELETE /api/support-needs/70707070-7070-7070-7070-707070707070
```

---

## Staff Management

### Get All Staff

```
GET /api/staff
```

#### Parameters

- `role` (query, optional): Staff role (ADS, OAA, OSA)
- `department` (query, optional): Department name (e.g., "Academic Development Services")

#### Response

- `200 OK`: Returns an array of staff objects
- `400 Bad Request`: Invalid role

#### Example Request

```
GET /api/staff?role=ADS&department=Academic%20Development%20Services
```

---

### Get Staff by UUID

```
GET /api/staff/{uuid}
```

#### Parameters

- `uuid` (path, required): UUID of the staff member

#### Response

- `200 OK`: Returns staff object
- `404 Not Found`: Staff not found

#### Example Request

```
GET /api/staff/20202020-2020-2020-2020-202020202020
```

---

### Get Staff by Staff ID

```
GET /api/staff/staff-id/{staffId}
```

#### Parameters

- `staffId` (path, required): Staff ID (e.g., staff-001)

#### Response

- `200 OK`: Returns staff object
- `404 Not Found`: Staff not found

#### Example Request

```
GET /api/staff/staff-id/staff-001
```

---

### Create New Staff

```
POST /api/staff
```

#### Request Body (JSON)

```json
{
  "staffId": "staff-004",
  "firstName": "James",
  "lastName": "Wilson",
  "email": "james.wilson@hcmut.edu.vn",
  "phoneNumber": "0912345678",
  "staffRole": "ADS",
  "department": "Academic Development Services",
  "campus": "Campus 02"
}
```

#### Response

- `201 Created`: Returns newly created staff object
- `400 Bad Request`: Invalid request format

#### Example Request

```
POST /api/staff
Content-Type: application/json
{
  "staffId": "staff-004",
  "firstName": "James",
  "lastName": "Wilson",
  "email": "james.wilson@hcmut.edu.vn",
  "phoneNumber": "0912345678",
  "staffRole": "ADS",
  "department": "Academic Development Services",
  "campus": "Campus 02"
}
```

---

### Update Staff

```
PUT /api/staff/{uuid}
```

#### Parameters

- `uuid` (path, required): UUID of the staff member

#### Request Body (JSON)

```json
{
  "staffId": "staff-001",
  "firstName": "Sarah",
  "lastName": "Martinez-Chen",
  "email": "sarah.martinez@hcmut.edu.vn",
  "phoneNumber": "0977777777",
  "staffRole": "ADS",
  "department": "Academic Development Services",
  "campus": "Campus 01"
}
```

#### Response

- `200 OK`: Returns updated staff object
- `400 Bad Request`: Invalid request format
- `404 Not Found`: Staff not found

#### Example Request

```
PUT /api/staff/20202020-2020-2020-2020-202020202020
Content-Type: application/json
{
  "staffId": "staff-001",
  "firstName": "Sarah",
  "lastName": "Martinez-Chen",
  "email": "sarah.martinez@hcmut.edu.vn",
  "phoneNumber": "0977777777",
  "staffRole": "ADS",
  "department": "Academic Development Services",
  "campus": "Campus 01"
}
```

---

### Delete Staff

```
DELETE /api/staff/{uuid}
```

#### Parameters

- `uuid` (path, required): UUID of the staff member

#### Response

- `204 No Content`: Successfully deleted
- `404 Not Found`: Staff not found

#### Example Request

```
DELETE /api/staff/20202020-2020-2020-2020-202020202020
```

---

### Get Staff Count by Role

```
GET /api/staff/statistics/count-by-role
```

#### Parameters

- `role` (query, required): Staff role (ADS, OAA, OSA)

#### Response

- `200 OK`: Returns count of staff members with the specified role
- `400 Bad Request`: Invalid role

#### Example Request

```
GET /api/staff/statistics/count-by-role?role=ADS
```

---

## Validation Rules

### Student ID Format

- Must be 7 digits (e.g., 2110001)

### Subject Code Format

- Must be 2 letters followed by 4 digits (e.g., CO3001)

### Semester Format

- Must be YYS format where YY represents the last two digits of the academic year and S is the semester number (e.g., 241 for semester 1 of 2024-2025)

### Support Type Values

- SCHOLARSHIP
- ACADEMIC_HELP
- ADVISING
- EMPTY

### Staff Role Values

- ADS (Academic Development Services)
- OAA (Office of Academic Affairs)
- OSA (Office of Student Affairs)
- EMPTY

---

## Error Handling

The system returns different HTTP status codes to indicate the result of API calls:

### 400 Bad Request

Returned when:

- Student ID format is invalid (must be 7 digits)
- Course code format is invalid (must be 2 letters + 4 digits)
- Semester format is invalid (must be YYS format)
- Support type is invalid
- Staff role is invalid

### 404 Not Found

Returned when:

- Student with the specified ID does not exist
- Enrollment with the specified ID does not exist
- Support need with the specified ID does not exist
- Staff member with the specified ID does not exist

### 409 Conflict

Returned when:

- Attempting to create a duplicate enrollment (same student, subject, and semester)
