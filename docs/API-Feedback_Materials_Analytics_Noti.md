# API Documentation - Feedback, Materials & Analytics Module

**Version**: 1.0.0
**Base URL**: `http://localhost:8080`
**Last Updated**: December 4, 2025

## Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Common Response Format](#common-response-format)
4. [Error Handling](#error-handling)
5. [Feedback API](#feedback-api)
6. [Material Management API](#material-management-api)
7. [Analytics & Reporting API](#analytics--reporting-api)
8. [Notification API](#notification-api)
9. [Data Models](#data-models)
10. [HTTP Status Codes](#http-status-codes)

---

## Overview

This API provides comprehensive functionality for managing feedback, materials, analytics, and notifications in the HCMUT Tutor Support System. The module supports:

- **Feedback Management**: Students submit session feedback with ratings and comments
- **Material Management**: Upload, manage, and control visibility of session materials
- **Analytics & Reporting**: Generate role-based performance and engagement reports
- **Notification System**: Async email notifications for system events

### Key Features

- RESTful API design
- JSON request/response format
- Validation on all input data
- Centralized exception handling
- Asynchronous notification processing

---

## Authentication

**Current Status**: Authentication is **disabled** in the MVP version.

When authentication is enabled, all requests will require:
- JWT token in `Authorization` header
- Format: `Authorization: Bearer <token>`

---

## Common Response Format

### Success Response

All successful responses return the requested data with appropriate HTTP status codes (200, 201, 204).

```json
{
  "feedbackId": "FB-12345",
  "sessionId": "SESSION-123",
  "rating": 5,
  "comment": "Excellent session!",
  "createdAt": "2025-12-04T10:30:45.123"
}
```

### Pagination

Currently not implemented. All list endpoints return complete result sets.

---

## Error Handling

### Error Response Format

All errors follow a standardized format:

```json
{
  "status": 404,
  "message": "Feedback not found with ID: FB-99999",
  "timestamp": "2025-12-04T10:30:45.123",
  "path": "uri=/api/feedback/FB-99999"
}
```

### Common Error Codes

| Status Code | Description | Example |
|-------------|-------------|---------|
| 400 | Bad Request | Invalid input, validation error |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Duplicate resource, constraint violation |
| 500 | Internal Server Error | Unexpected server error |

---

## Feedback API

### Submit Feedback

Submit new feedback for a completed tutoring session.

**Endpoint**: `POST /api/feedback`

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "sessionId": "SESSION-123",
  "rating": 5,
  "comment": "Excellent tutoring session! The tutor explained concepts clearly."
}
```

**Validation Rules**:
- `sessionId`: Required, cannot be blank
- `rating`: Required, must be between 1 and 5 (inclusive)
- `comment`: Optional, can be blank

**Response**: `201 Created`

```json
{
  "feedbackId": "FB-12345",
  "sessionId": "SESSION-123",
  "studentName": "John Doe",
  "tutorName": "Jane Smith",
  "rating": 5,
  "comment": "Excellent tutoring session! The tutor explained concepts clearly.",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "SESSION-123",
    "rating": 5,
    "comment": "Excellent tutoring session!"
  }'
```

**Error Responses**:
- `400 Bad Request`: Invalid rating (not 1-5), missing sessionId
- `400 Bad Request`: Duplicate feedback for session (only one feedback per session allowed)
- `404 Not Found`: Session does not exist

---

### Get Feedback by ID

Retrieve a specific feedback entry by its ID.

**Endpoint**: `GET /api/feedback/{feedbackId}`

**Path Parameters**:
- `feedbackId` (string, required): The unique identifier of the feedback

**Response**: `200 OK`

```json
{
  "feedbackId": "FB-12345",
  "sessionId": "SESSION-123",
  "studentName": "John Doe",
  "tutorName": "Jane Smith",
  "rating": 5,
  "comment": "Excellent tutoring session!",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/feedback/FB-12345
```

**Error Responses**:
- `404 Not Found`: Feedback with specified ID does not exist

---

### Get Feedback by Tutor

Retrieve all feedback for a specific tutor.

**Endpoint**: `GET /api/feedback/tutor/{tutorId}`

**Path Parameters**:
- `tutorId` (string, required): The unique identifier of the tutor

**Response**: `200 OK`

```json
[
  {
    "feedbackId": "FB-12345",
    "sessionId": "SESSION-123",
    "studentName": "John Doe",
    "tutorName": "Jane Smith",
    "rating": 5,
    "comment": "Excellent session!",
    "createdAt": "2025-12-04T10:30:45.123",
    "updatedAt": "2025-12-04T10:30:45.123"
  },
  {
    "feedbackId": "FB-12346",
    "sessionId": "SESSION-124",
    "studentName": "Alice Johnson",
    "tutorName": "Jane Smith",
    "rating": 4,
    "comment": "Very helpful!",
    "createdAt": "2025-12-03T14:20:30.456",
    "updatedAt": "2025-12-03T14:20:30.456"
  }
]
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/feedback/tutor/tutor-1
```

**Error Responses**:
- `404 Not Found`: Tutor does not exist
- Returns empty array `[]` if tutor has no feedback

---

### Get Feedback by Session

Retrieve feedback for a specific session.

**Endpoint**: `GET /api/feedback/session/{sessionId}`

**Path Parameters**:
- `sessionId` (string, required): The unique identifier of the session

**Response**: `200 OK`

```json
{
  "feedbackId": "FB-12345",
  "sessionId": "SESSION-123",
  "studentName": "John Doe",
  "tutorName": "Jane Smith",
  "rating": 5,
  "comment": "Excellent session!",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/feedback/session/SESSION-123
```

**Error Responses**:
- `404 Not Found`: Session does not exist or has no feedback

---

### Get Tutor Feedback Statistics

Retrieve aggregated feedback statistics for a tutor.

**Endpoint**: `GET /api/feedback/tutor/{tutorId}/stats`

**Path Parameters**:
- `tutorId` (string, required): The unique identifier of the tutor

**Response**: `200 OK`

```json
{
  "tutorId": "tutor-1",
  "averageRating": 4.67,
  "totalFeedback": 15
}
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/feedback/tutor/tutor-1/stats
```

**Error Responses**:
- `404 Not Found`: Tutor does not exist
- Returns `averageRating: 0.0` and `totalFeedback: 0` if no feedback exists

---

### Update Feedback

Update an existing feedback entry.

**Endpoint**: `PUT /api/feedback/{feedbackId}`

**Path Parameters**:
- `feedbackId` (string, required): The unique identifier of the feedback

**Request Body**:
```json
{
  "sessionId": "SESSION-123",
  "rating": 4,
  "comment": "Good session, updated my review."
}
```

**Response**: `200 OK`

```json
{
  "feedbackId": "FB-12345",
  "sessionId": "SESSION-123",
  "studentName": "John Doe",
  "tutorName": "Jane Smith",
  "rating": 4,
  "comment": "Good session, updated my review.",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T15:45:20.789"
}
```

**cURL Example**:
```bash
curl -X PUT http://localhost:8080/api/feedback/FB-12345 \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "SESSION-123",
    "rating": 4,
    "comment": "Good session, updated my review."
  }'
```

**Error Responses**:
- `404 Not Found`: Feedback does not exist
- `400 Bad Request`: Invalid rating or validation error

---

### Delete Feedback

Delete a feedback entry.

**Endpoint**: `DELETE /api/feedback/{feedbackId}`

**Path Parameters**:
- `feedbackId` (string, required): The unique identifier of the feedback

**Response**: `204 No Content`

**cURL Example**:
```bash
curl -X DELETE http://localhost:8080/api/feedback/FB-12345
```

**Error Responses**:
- `404 Not Found`: Feedback does not exist

---

### Get All Feedback

Retrieve all feedback entries in the system.

**Endpoint**: `GET /api/feedback`

**Response**: `200 OK`

```json
[
  {
    "feedbackId": "FB-12345",
    "sessionId": "SESSION-123",
    "studentName": "John Doe",
    "tutorName": "Jane Smith",
    "rating": 5,
    "comment": "Excellent!",
    "createdAt": "2025-12-04T10:30:45.123",
    "updatedAt": "2025-12-04T10:30:45.123"
  },
  {
    "feedbackId": "FB-12346",
    "sessionId": "SESSION-124",
    "studentName": "Alice Johnson",
    "tutorName": "Bob Wilson",
    "rating": 4,
    "comment": "Very helpful!",
    "createdAt": "2025-12-03T14:20:30.456",
    "updatedAt": "2025-12-03T14:20:30.456"
  }
]
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/feedback
```

---

## Material Management API

### Upload Material

Upload a new material for a tutoring session.

**Endpoint**: `POST /api/materials`

**Request Body**:
```json
{
  "sessionId": "SESSION-123",
  "name": "Calculus Chapter 5 Notes",
  "sourceType": "LOCAL_UPLOAD",
  "contentUrl": "https://storage.example.com/materials/calc-ch5.pdf",
  "visibility": "SHARED_WITH_STUDENT",
  "uploadedByUserId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Field Descriptions**:
- `sessionId`: Required, the session this material belongs to
- `name`: Required, descriptive name of the material
- `sourceType`: Required, one of: `LOCAL_UPLOAD`, `LIBRARY_RESOURCE`, `EMPTY`
- `contentUrl`: Required, URL or file path to the material
- `visibility`: Required, one of: `PRIVATE`, `SHARED_WITH_STUDENT`, `EMPTY`
- `uploadedByUserId`: Required, UUID of the user uploading the material

**Response**: `201 Created`

```json
{
  "materialId": "MAT-12345",
  "sessionId": "SESSION-123",
  "name": "Calculus Chapter 5 Notes",
  "sourceType": "LOCAL_UPLOAD",
  "contentUrl": "https://storage.example.com/materials/calc-ch5.pdf",
  "visibility": "SHARED_WITH_STUDENT",
  "uploadedByName": "Jane Smith",
  "uploadedByEmail": "jane.smith@example.com",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/materials \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "SESSION-123",
    "name": "Calculus Chapter 5 Notes",
    "sourceType": "LOCAL_UPLOAD",
    "contentUrl": "https://storage.example.com/materials/calc-ch5.pdf",
    "visibility": "SHARED_WITH_STUDENT",
    "uploadedByUserId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

**Error Responses**:
- `400 Bad Request`: Missing required fields, empty contentUrl
- `404 Not Found`: Session or user does not exist

---

### Get Material by ID

Retrieve a specific material by its ID.

**Endpoint**: `GET /api/materials/{materialId}`

**Path Parameters**:
- `materialId` (string, required): The unique identifier of the material

**Response**: `200 OK`

```json
{
  "materialId": "MAT-12345",
  "sessionId": "SESSION-123",
  "name": "Calculus Chapter 5 Notes",
  "sourceType": "LOCAL_UPLOAD",
  "contentUrl": "https://storage.example.com/materials/calc-ch5.pdf",
  "visibility": "SHARED_WITH_STUDENT",
  "uploadedByName": "Jane Smith",
  "uploadedByEmail": "jane.smith@example.com",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/materials/MAT-12345
```

**Error Responses**:
- `404 Not Found`: Material does not exist

---

### Get Materials by Session

Retrieve all materials for a specific session, ordered by creation date (newest first).

**Endpoint**: `GET /api/materials/session/{sessionId}`

**Path Parameters**:
- `sessionId` (string, required): The unique identifier of the session

**Response**: `200 OK`

```json
[
  {
    "materialId": "MAT-12346",
    "sessionId": "SESSION-123",
    "name": "Practice Problems",
    "sourceType": "LOCAL_UPLOAD",
    "contentUrl": "https://storage.example.com/materials/practice.pdf",
    "visibility": "SHARED_WITH_STUDENT",
    "uploadedByName": "Jane Smith",
    "uploadedByEmail": "jane.smith@example.com",
    "createdAt": "2025-12-04T14:20:30.456",
    "updatedAt": "2025-12-04T14:20:30.456"
  },
  {
    "materialId": "MAT-12345",
    "sessionId": "SESSION-123",
    "name": "Calculus Chapter 5 Notes",
    "sourceType": "LOCAL_UPLOAD",
    "contentUrl": "https://storage.example.com/materials/calc-ch5.pdf",
    "visibility": "SHARED_WITH_STUDENT",
    "uploadedByName": "Jane Smith",
    "uploadedByEmail": "jane.smith@example.com",
    "createdAt": "2025-12-04T10:30:45.123",
    "updatedAt": "2025-12-04T10:30:45.123"
  }
]
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/materials/session/SESSION-123
```

**Error Responses**:
- `404 Not Found`: Session does not exist
- Returns empty array `[]` if session has no materials

---

### Get Materials (All or by Visibility)

Retrieve all materials or filter by visibility level.

**Endpoint**: `GET /api/materials`

**Query Parameters**:
- `visibility` (string, optional): Filter by visibility level (`PRIVATE`, `SHARED_WITH_STUDENT`, `EMPTY`)

**Response**: `200 OK`

**Example 1: Get all materials**
```bash
curl -X GET http://localhost:8080/api/materials
```

**Example 2: Get only shared materials**
```bash
curl -X GET "http://localhost:8080/api/materials?visibility=SHARED_WITH_STUDENT"
```

**Response**:
```json
[
  {
    "materialId": "MAT-12345",
    "sessionId": "SESSION-123",
    "name": "Calculus Notes",
    "sourceType": "LOCAL_UPLOAD",
    "contentUrl": "https://storage.example.com/materials/calc.pdf",
    "visibility": "SHARED_WITH_STUDENT",
    "uploadedByName": "Jane Smith",
    "uploadedByEmail": "jane.smith@example.com",
    "createdAt": "2025-12-04T10:30:45.123",
    "updatedAt": "2025-12-04T10:30:45.123"
  }
]
```

---

### Update Material

Update an existing material's metadata.

**Endpoint**: `PUT /api/materials/{materialId}`

**Path Parameters**:
- `materialId` (string, required): The unique identifier of the material

**Request Body**:
```json
{
  "sessionId": "SESSION-123",
  "name": "Calculus Chapter 5 Notes - Updated",
  "sourceType": "LOCAL_UPLOAD",
  "contentUrl": "https://storage.example.com/materials/calc-ch5-v2.pdf",
  "visibility": "PRIVATE",
  "uploadedByUserId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Response**: `200 OK`

```json
{
  "materialId": "MAT-12345",
  "sessionId": "SESSION-123",
  "name": "Calculus Chapter 5 Notes - Updated",
  "sourceType": "LOCAL_UPLOAD",
  "contentUrl": "https://storage.example.com/materials/calc-ch5-v2.pdf",
  "visibility": "PRIVATE",
  "uploadedByName": "Jane Smith",
  "uploadedByEmail": "jane.smith@example.com",
  "createdAt": "2025-12-04T10:30:45.123",
  "updatedAt": "2025-12-04T16:15:20.789"
}
```

**cURL Example**:
```bash
curl -X PUT http://localhost:8080/api/materials/MAT-12345 \
  -H "Content-Type: application/json" \
  -d '{
    "sessionId": "SESSION-123",
    "name": "Calculus Chapter 5 Notes - Updated",
    "sourceType": "LOCAL_UPLOAD",
    "contentUrl": "https://storage.example.com/materials/calc-ch5-v2.pdf",
    "visibility": "PRIVATE",
    "uploadedByUserId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

**Error Responses**:
- `404 Not Found`: Material does not exist
- `400 Bad Request`: Validation error

---

### Delete Material

Delete a material entry.

**Endpoint**: `DELETE /api/materials/{materialId}`

**Path Parameters**:
- `materialId` (string, required): The unique identifier of the material

**Response**: `204 No Content`

**cURL Example**:
```bash
curl -X DELETE http://localhost:8080/api/materials/MAT-12345
```

**Error Responses**:
- `404 Not Found`: Material does not exist

---

## Analytics & Reporting API

### Get Tutor Performance Report

Generate a comprehensive report on tutor performance metrics. Intended for ADS (Academic Director of Studies) role.

**Endpoint**: `GET /api/analytics/reports/tutor-performance`

**Query Parameters**:
- `generatedBy` (string, optional, default: "system"): Name or ID of user generating the report

**Response**: `200 OK`

```json
{
  "reportType": "TUTOR_PERFORMANCE",
  "generatedAt": "2025-12-04T10:30:45.123",
  "generatedBy": "admin-user",
  "data": {
    "totalTutors": 50,
    "totalSessions": 247,
    "averageRating": 4.67,
    "totalFeedbackCount": 189
  },
  "summary": "Tutor Performance Report: 50 tutors, 247 sessions, 4.67 avg rating"
}
```

**Metrics Included**:
- `totalTutors`: Number of active tutors in the system
- `totalSessions`: Total tutoring sessions conducted
- `averageRating`: System-wide average feedback rating (1-5 scale)
- `totalFeedbackCount`: Total number of feedback entries

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/analytics/reports/tutor-performance?generatedBy=admin-user"
```

---

### Get Student Engagement Report

Generate a report on student engagement and feedback participation. Intended for OAA (Office of Academic Affairs) role.

**Endpoint**: `GET /api/analytics/reports/student-engagement`

**Query Parameters**:
- `generatedBy` (string, optional, default: "system"): Name or ID of user generating the report

**Response**: `200 OK`

```json
{
  "reportType": "STUDENT_ENGAGEMENT",
  "generatedAt": "2025-12-04T10:30:45.123",
  "generatedBy": "oaa-staff",
  "data": {
    "totalStudents": 320,
    "totalSessions": 247,
    "totalFeedback": 189,
    "feedbackRate": 76.52
  },
  "summary": "Student Engagement Report: 320 students, 247 sessions, 76.52% feedback rate"
}
```

**Metrics Included**:
- `totalStudents`: Number of registered students
- `totalSessions`: Total tutoring sessions
- `totalFeedback`: Number of feedback submissions
- `feedbackRate`: Percentage of sessions with feedback (feedback/sessions * 100)

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/analytics/reports/student-engagement?generatedBy=oaa-staff"
```

---

### Get Material Usage Report

Generate a report on material usage across sessions. Intended for OSA (Office of Student Affairs) role.

**Endpoint**: `GET /api/analytics/reports/material-usage`

**Query Parameters**:
- `generatedBy` (string, optional, default: "system"): Name or ID of user generating the report

**Response**: `200 OK`

```json
{
  "reportType": "MATERIAL_USAGE",
  "generatedAt": "2025-12-04T10:30:45.123",
  "generatedBy": "osa-staff",
  "data": {
    "totalMaterials": 543,
    "totalSessions": 247,
    "materialsPerSession": 2.20
  },
  "summary": "Material Usage Report: 543 materials, 247 sessions, 2.20 materials/session"
}
```

**Metrics Included**:
- `totalMaterials`: Total number of materials uploaded
- `totalSessions`: Total tutoring sessions
- `materialsPerSession`: Average materials per session (materials/sessions)

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/analytics/reports/material-usage?generatedBy=osa-staff"
```

---

### Get Report by Staff Role

Generate a role-specific report based on staff role.

**Endpoint**: `GET /api/analytics/reports/by-role/{staffRole}`

**Path Parameters**:
- `staffRole` (string, required): Staff role (`ADS`, `OAA`, or `OSA`)

**Query Parameters**:
- `generatedBy` (string, optional, default: "system"): Name or ID of user generating the report

**Response**: `200 OK`

Returns the appropriate report based on staff role:
- `ADS` → Tutor Performance Report
- `OAA` → Student Engagement Report
- `OSA` → Material Usage Report

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/analytics/reports/by-role/ADS?generatedBy=director"
```

**Error Responses**:
- `400 Bad Request`: Invalid staff role

---

### Get Comprehensive Report

Generate a comprehensive report containing all metrics across the system.

**Endpoint**: `GET /api/analytics/reports/comprehensive`

**Query Parameters**:
- `generatedBy` (string, optional, default: "system"): Name or ID of user generating the report

**Response**: `200 OK`

```json
{
  "reportType": "COMPREHENSIVE",
  "generatedAt": "2025-12-04T10:30:45.123",
  "generatedBy": "system-admin",
  "data": {
    "totalTutors": 50,
    "totalStudents": 320,
    "totalSessions": 247,
    "totalMaterials": 543,
    "totalFeedback": 189,
    "averageRating": 4.67,
    "feedbackRate": 76.52,
    "materialsPerSession": 2.20
  },
  "summary": "Comprehensive Report: 50 tutors, 320 students, 247 sessions, 4.67 avg rating"
}
```

**Metrics Included**: All metrics from individual reports combined

**cURL Example**:
```bash
curl -X GET "http://localhost:8080/api/analytics/reports/comprehensive?generatedBy=system-admin"
```

---

## Notification API

### Send Email Notification

Send a custom email notification to a user.

**Endpoint**: `POST /api/notifications/send-email`

**Request Parameters** (form-urlencoded or query parameters):
- `userId` (string, required): ID of the user sending the notification
- `recipientEmail` (string, required): Email address of the recipient
- `subject` (string, required): Email subject line
- `message` (string, required): Email body content

**Response**: `200 OK`

```json
{
  "notificationId": "NOTIF-12345",
  "userId": "user-123",
  "recipientEmail": "student@example.com",
  "subject": "Important Update",
  "message": "Your session has been confirmed.",
  "type": "EMAIL",
  "sentAt": "2025-12-04T10:30:45.123"
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/notifications/send-email \
  -d "userId=user-123" \
  -d "recipientEmail=student@example.com" \
  -d "subject=Important Update" \
  -d "message=Your session has been confirmed."
```

**Note**: Email sending is asynchronous. The response is returned immediately while the email is sent in the background.

---

### Send Session Booking Notification

Send notifications to both student and tutor about a new session booking.

**Endpoint**: `POST /api/notifications/session-booking`

**Request Parameters**:
- `studentEmail` (string, required): Email address of the student
- `tutorEmail` (string, required): Email address of the tutor
- `sessionDetails` (string, required): Details about the session

**Response**: `200 OK`

```json
"Session booking notifications sent successfully"
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/notifications/session-booking \
  -d "studentEmail=student@example.com" \
  -d "tutorEmail=tutor@example.com" \
  -d "sessionDetails=Session on Dec 5, 2025 at 2:00 PM - Calculus"
```

---

### Send Feedback Submitted Notification

Notify a tutor when they receive new feedback.

**Endpoint**: `POST /api/notifications/feedback-submitted`

**Request Parameters**:
- `tutorEmail` (string, required): Email address of the tutor
- `feedbackDetails` (string, required): Details about the feedback

**Response**: `200 OK`

```json
"Feedback notification sent successfully"
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/notifications/feedback-submitted \
  -d "tutorEmail=tutor@example.com" \
  -d "feedbackDetails=You received a 5-star rating from John Doe"
```

---

### Send Material Uploaded Notification

Notify a student when new material is uploaded for their session.

**Endpoint**: `POST /api/notifications/material-uploaded`

**Request Parameters**:
- `studentEmail` (string, required): Email address of the student
- `materialDetails` (string, required): Details about the uploaded material

**Response**: `200 OK`

```json
"Material upload notification sent successfully"
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/notifications/material-uploaded \
  -d "studentEmail=student@example.com" \
  -d "materialDetails=New material: Calculus Chapter 5 Notes"
```

---

### Send Evaluation Notification

Notify a tutor when they receive an evaluation from staff.

**Endpoint**: `POST /api/notifications/evaluation-received`

**Request Parameters**:
- `tutorEmail` (string, required): Email address of the tutor
- `evaluationDetails` (string, required): Details about the evaluation

**Response**: `200 OK`

```json
"Evaluation notification sent successfully"
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/notifications/evaluation-received \
  -d "tutorEmail=tutor@example.com" \
  -d "evaluationDetails=Staff evaluation completed for your session on Dec 1"
```

---

### Get Notification History

Retrieve the history of all sent notifications.

**Endpoint**: `GET /api/notifications/history`

**Response**: `200 OK`

```json
[
  {
    "notificationId": "NOTIF-12346",
    "userId": "user-123",
    "recipientEmail": "student@example.com",
    "subject": "Session Confirmation",
    "message": "Your session has been confirmed.",
    "type": "EMAIL",
    "sentAt": "2025-12-04T14:20:30.456"
  },
  {
    "notificationId": "NOTIF-12345",
    "userId": "user-123",
    "recipientEmail": "tutor@example.com",
    "subject": "New Feedback",
    "message": "You received new feedback.",
    "type": "EMAIL",
    "sentAt": "2025-12-04T10:30:45.123"
  }
]
```

**cURL Example**:
```bash
curl -X GET http://localhost:8080/api/notifications/history
```

---

### Update Notification Preferences

Update notification preferences for a user.

**Endpoint**: `PUT /api/notifications/preferences/{userId}`

**Path Parameters**:
- `userId` (string, required): The unique identifier of the user

**Request Body**:
```json
{
  "emailNotifications": true,
  "pushNotifications": false,
  "sessionReminders": true,
  "feedbackAlerts": true,
  "materialAlerts": false
}
```

**Response**: `200 OK`

```json
"Notification preferences updated successfully"
```

**cURL Example**:
```bash
curl -X PUT http://localhost:8080/api/notifications/preferences/user-123 \
  -H "Content-Type: application/json" \
  -d '{
    "emailNotifications": true,
    "pushNotifications": false,
    "sessionReminders": true,
    "feedbackAlerts": true,
    "materialAlerts": false
  }'
```

---

## Data Models

### FeedbackDto

Represents feedback for a tutoring session.

```json
{
  "feedbackId": "string",         // Unique identifier (e.g., "FB-12345")
  "sessionId": "string",          // Related session ID
  "studentName": "string",        // Name of student who gave feedback
  "tutorName": "string",          // Name of tutor who received feedback
  "rating": "integer",            // Rating 1-5
  "comment": "string",            // Optional feedback comment
  "createdAt": "datetime",        // ISO 8601 format
  "updatedAt": "datetime"         // ISO 8601 format
}
```

---

### FeedbackSubmissionRequestDto

Request body for submitting or updating feedback.

```json
{
  "sessionId": "string",          // Required, cannot be blank
  "rating": "integer",            // Required, must be 1-5
  "comment": "string"             // Optional
}
```

**Validation**:
- `sessionId`: Required, not blank
- `rating`: Required, minimum 1, maximum 5
- `comment`: Optional

---

### MaterialDto

Represents a session material.

```json
{
  "materialId": "string",         // Unique identifier (e.g., "MAT-12345")
  "sessionId": "string",          // Related session ID
  "name": "string",               // Material name/title
  "sourceType": "enum",           // LOCAL_UPLOAD, LIBRARY_RESOURCE, EMPTY
  "contentUrl": "string",         // URL or file path
  "visibility": "enum",           // PRIVATE, SHARED_WITH_STUDENT, EMPTY
  "uploadedByName": "string",     // Name of uploader
  "uploadedByEmail": "string",    // Email of uploader
  "createdAt": "datetime",        // ISO 8601 format
  "updatedAt": "datetime"         // ISO 8601 format
}
```

---

### MaterialUploadRequestDto

Request body for uploading or updating materials.

```json
{
  "sessionId": "string",          // Required
  "name": "string",               // Required
  "sourceType": "enum",           // Required (LOCAL_UPLOAD, LIBRARY_RESOURCE, EMPTY)
  "contentUrl": "string",         // Required
  "visibility": "enum",           // Required (PRIVATE, SHARED_WITH_STUDENT, EMPTY)
  "uploadedByUserId": "string"    // Required (UUID format)
}
```

**Validation**:
- All fields are required
- `sourceType` must be valid enum value
- `visibility` must be valid enum value
- `contentUrl` cannot be blank

---

### AnalyticsReportDto

Represents an analytics report.

```json
{
  "reportType": "string",         // Report type identifier
  "generatedAt": "datetime",      // ISO 8601 format
  "generatedBy": "string",        // User who generated report
  "data": "object",               // Key-value pairs of metrics
  "summary": "string"             // Human-readable summary
}
```

**Example Data Objects**:

**Tutor Performance**:
```json
{
  "totalTutors": 50,
  "totalSessions": 247,
  "averageRating": 4.67,
  "totalFeedbackCount": 189
}
```

**Student Engagement**:
```json
{
  "totalStudents": 320,
  "totalSessions": 247,
  "totalFeedback": 189,
  "feedbackRate": 76.52
}
```

**Material Usage**:
```json
{
  "totalMaterials": 543,
  "totalSessions": 247,
  "materialsPerSession": 2.20
}
```

---

### NotificationDto

Represents a notification entry.

```json
{
  "notificationId": "string",     // Unique identifier
  "userId": "string",             // Sender/creator user ID
  "recipientEmail": "string",     // Recipient email
  "subject": "string",            // Email subject
  "message": "string",            // Email body
  "type": "string",               // Notification type (e.g., "EMAIL")
  "sentAt": "datetime"            // ISO 8601 format
}
```

---

### TutorFeedbackStats

Aggregated statistics for a tutor's feedback.

```json
{
  "tutorId": "string",            // Tutor identifier
  "averageRating": "double",      // Average rating (0.0 if no feedback)
  "totalFeedback": "long"         // Total number of feedback entries
}
```

---

## HTTP Status Codes

### Success Codes

| Code | Description | Usage |
|------|-------------|-------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST requests creating resources |
| 204 | No Content | Successful DELETE requests |

### Client Error Codes

| Code | Description | Common Causes |
|------|-------------|---------------|
| 400 | Bad Request | Validation error, invalid input, missing required fields |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Duplicate resource, constraint violation |

### Server Error Codes

| Code | Description | Causes |
|------|-------------|--------|
| 500 | Internal Server Error | Unexpected server error, unhandled exception |

---

## Enum Values

### MaterialSourceType

| Value | Description |
|-------|-------------|
| `LOCAL_UPLOAD` | File uploaded by user |
| `LIBRARY_RESOURCE` | Resource from library |
| `EMPTY` | Default/unset value |

### MaterialVisibility

| Value | Description |
|-------|-------------|
| `PRIVATE` | Only visible to uploader |
| `SHARED_WITH_STUDENT` | Shared with session participants |
| `EMPTY` | Default/unset value |

### StaffRole

| Value | Description |
|-------|-------------|
| `ADS` | Academic Director of Studies |
| `OAA` | Office of Academic Affairs |
| `OSA` | Office of Student Affairs |

---

## Best Practices

### Request Guidelines

1. **Content-Type**: Always set `Content-Type: application/json` for POST/PUT requests with JSON bodies
2. **Validation**: Ensure all required fields are provided and valid before submission
3. **Error Handling**: Check response status codes and handle errors appropriately
4. **Async Operations**: Notification endpoints return immediately; emails are sent asynchronously

### Response Guidelines

1. **Timestamps**: All timestamps are in ISO 8601 format (`YYYY-MM-DDTHH:mm:ss.SSS`)
2. **IDs**: Use exact ID values from responses when making subsequent requests
3. **Empty Results**: List endpoints return empty arrays `[]` when no data exists (not null)

### Performance Considerations

1. **Pagination**: Not currently implemented; all list endpoints return complete results
2. **Async Notifications**: Email sending uses a thread pool (5 core, 10 max threads, 100 queue capacity)
3. **Database Queries**: Most queries are optimized with proper indexing on UUIDs and foreign keys

---

## Testing the API

### Using cURL

All examples in this documentation use cURL. Copy and paste them directly into your terminal.

### Using Postman

1. Import the endpoints into Postman
2. Set base URL: `http://localhost:8080`
3. For POST/PUT: Set body type to JSON or form-urlencoded as specified
4. Review response in Postman's response viewer

### Using Browser

Simple GET requests can be tested directly in the browser:
- `http://localhost:8080/api/feedback`
- `http://localhost:8080/api/materials?visibility=SHARED_WITH_STUDENT`
- `http://localhost:8080/api/analytics/reports/tutor-performance`

---

## Configuration

### Email Configuration (Required for Notifications)

Add to `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Async Configuration

Thread pool settings are configured in `AsyncConfig.java`:
- Core pool size: 5 threads
- Max pool size: 10 threads
- Queue capacity: 100 tasks
- Thread naming: "async-notification-{n}"

---

## Support & Contact

For API support or questions:
- Backend Team: backend@hcmut.edu.vn
- Documentation: docs@hcmut.edu.vn
- GitHub Issues: [Repository Issues Page]

---

## Changelog

### Version 1.0.0 (December 4, 2025)
- Initial API documentation
- Feedback Management endpoints
- Material Management endpoints
- Analytics & Reporting endpoints
- Notification endpoints
- Complete data models and examples

---

**End of API Documentation**
