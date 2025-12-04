# User CRUD API Documentation

This document describes the RESTful API endpoints for managing users in the HCMUT Tutor Support System.

## Base URL

```
/api/users
```

## Endpoints

### 1. Create User

Creates a new user in the system.

**Endpoint:** `POST /api/users`

**Request Headers:**

- `Content-Type: application/json`

**Request Body:**

```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole"
}
```

**Field Validations:**

- `firstName`: Required, cannot be blank
- `lastName`: Required, cannot be blank
- `email`: Required, cannot be blank, must be valid email format
- `role`: Required, must be one of: `STUDENT`, `TUTOR`, `STAFF`, `ADMIN`, `EMPTY`

**Success Response:**

- **Status Code:** `201 Created`
- **Headers:** `Location: /api/users/{id}`
- **Body:**

```json
{
  "id": "uuid",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Error Responses:**

- `400 Bad Request`: Validation failed or duplicate email
- `409 Conflict`: User with email already exists

**Example:**

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@hcmut.edu.vn",
    "role": "STUDENT"
  }'
```

---

### 2. Get User by ID

Retrieves a specific user by their unique identifier.

**Endpoint:** `GET /api/users/{id}`

**Path Parameters:**

- `id` (UUID): The unique identifier of the user

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

```json
{
  "id": "uuid",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Error Responses:**

- `404 Not Found`: User with the specified ID does not exist

**Example:**

```bash
curl -X GET http://localhost:8080/api/users/123e4567-e89b-12d3-a456-426614174000
```

---

### 3. List Users (Paginated)

Retrieves a paginated list of all users.

**Endpoint:** `GET /api/users`

**Query Parameters:**

- `page` (optional, default: 0): Page number (0-indexed)
- `size` (optional, default: 20): Number of items per page
- `sort` (optional): Sort criteria in the format `property,direction` (e.g., `firstName,asc`)

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

```json
{
  "content": [
    {
      "id": "uuid",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "role": "UserRole",
      "createdAt": "timestamp",
      "updatedAt": "timestamp"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": { ... },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "size": 20,
  "number": 0,
  "sort": { ... },
  "first": true,
  "numberOfElements": 20,
  "empty": false
}
```

**Example:**

```bash
# Get first page (20 users)
curl -X GET http://localhost:8080/api/users

# Get second page with 10 users, sorted by email
curl -X GET "http://localhost:8080/api/users?page=1&size=10&sort=email,asc"
```

---

### 4. Update User

Updates an existing user's information.

**Endpoint:** `PUT /api/users/{id}`

**Path Parameters:**

- `id` (UUID): The unique identifier of the user to update

**Request Headers:**

- `Content-Type: application/json`

**Request Body:**

```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole"
}
```

**Field Validations:**

- All fields are optional
- `email`: If provided, must be valid email format
- `role`: If provided, must be one of: `STUDENT`, `TUTOR`, `STAFF`, `ADMIN`, `EMPTY`

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

```json
{
  "id": "uuid",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

**Error Responses:**

- `400 Bad Request`: Validation failed
- `404 Not Found`: User with the specified ID does not exist
- `409 Conflict`: Email already in use by another user

**Example:**

```bash
curl -X PUT http://localhost:8080/api/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe.updated@hcmut.edu.vn",
    "role": "TUTOR"
  }'
```

---

### 5. Delete User

Deletes a user from the system.

**Endpoint:** `DELETE /api/users/{id}`

**Path Parameters:**

- `id` (UUID): The unique identifier of the user to delete

**Success Response:**

- **Status Code:** `204 No Content`
- **Body:** Empty

**Error Responses:**

- `404 Not Found`: User with the specified ID does not exist

**Example:**

```bash
curl -X DELETE http://localhost:8080/api/users/123e4567-e89b-12d3-a456-426614174000
```

---

## Data Models

### UserDto (Response)

```json
{
  "id": "UUID",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "role": "UserRole",
  "createdAt": "ISO 8601 timestamp",
  "updatedAt": "ISO 8601 timestamp"
}
```

### CreateUserRequest

```json
{
  "firstName": "string (required, not blank)",
  "lastName": "string (required, not blank)",
  "email": "string (required, valid email)",
  "role": "UserRole (required)"
}
```

### UpdateUserRequest

```json
{
  "firstName": "string (optional)",
  "lastName": "string (optional)",
  "email": "string (optional, valid email if provided)",
  "role": "UserRole (optional)"
}
```

### UserRole Enum

Valid values:

- `STUDENT`
- `TUTOR`
- `STAFF`
- `ADMIN`
- `EMPTY`

---

## Business Logic

### UserService Implementation

The `UserService` provides the following business logic:

1. **createUser(CreateUserRequest)**

   - Validates that email is unique
   - Throws `DuplicateResourceException` if email already exists
   - Maps request DTO to entity
   - Saves user to database
   - Returns mapped UserDto

2. **getUser(UUID)**

   - Retrieves user by ID
   - Throws `ResourceNotFoundException` if user not found
   - Returns mapped UserDto

3. **listUsers(Pageable)**

   - Retrieves paginated list of users
   - Returns Page of UserDto with pagination metadata

4. **updateUser(UUID, UpdateUserRequest)**

   - Retrieves existing user by ID
   - Throws `ResourceNotFoundException` if user not found
   - Validates email uniqueness if email is being changed
   - Throws `DuplicateResourceException` if new email already in use
   - Updates only non-null fields
   - Saves updated user
   - Returns mapped UserDto

5. **deleteUser(UUID)**
   - Retrieves user by ID
   - Throws `ResourceNotFoundException` if user not found
   - Deletes user from database

---

## Error Handling

### Common Error Response Format

```json
{
  "timestamp": "ISO 8601 timestamp",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users"
}
```

### Exception Types

- `ResourceNotFoundException` (404): Entity not found
- `DuplicateResourceException` (409): Unique constraint violation
- `MethodArgumentNotValidException` (400): Bean validation failure

---

## Testing

### Running the Test Script

A bash script is provided for testing all endpoints:

```bash
chmod +x tests/user_api_test.sh
./tests/user_api_test.sh
```

The script tests:

1. List users
2. Create user
3. Get user by ID
4. Update user
5. Delete user

### Manual Testing with curl

See the examples in each endpoint section above.

---

## Notes

- All endpoints use JSON for request and response bodies
- All endpoints are transactional (except list operations which are read-only)
- Email addresses must be unique across all users
- UUIDs are automatically generated for new users
- Timestamps are automatically managed by the system
- Pagination uses Spring Data's default settings (page=0, size=20) if not specified
