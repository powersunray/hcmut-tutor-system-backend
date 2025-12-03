# **Profile API Documentation**

**Base URL**: /api/profiles

This API manages user profiles for Students and Tutors. It handles fetching profile details, updating personal information, and uploading specific credentials for tutors.

## **1\. Get User Profile**

Fetches the profile details for a specific user by their User ID (UUID).

* **Method**: GET  
* **Endpoint**: /{id}  
* **Full Path**: /api/profiles/{id}

### **Path Parameters**

| Parameter | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| id | UUID | Yes | The unique UUID of the user (Student or Tutor). |

### **Response**

**Status**: 200 OK **Content-Type**: application/json

{  
  "id": "550e8400-e29b-41d4-a716-446655440000",  
  "firstName": "Nguyen",  
  "lastName": "Van A",  
  "email": "nguyen.vana@hcmut.edu.vn",  
  "phoneNumber": "0901234567",  
  "campus": "Ly Thuong Kiet",  
  "address": "123 Street, District 10",  
  "gender": "MALE",  
  "role": "STUDENT",  
    
  // Student specific fields (nullable if user is Tutor)  
  "faculty": "Computer Science",  
  "major": "Software Engineering",

  // Tutor specific fields (nullable if user is Student)  
  "bio": null,  
  "expertiseAreas": null  
}

**Status**: 404 Not Found

* Occurs if the User ID does not exist in the database.

## **2\. Update User Profile**

Updates the profile information for a user. The backend logic automatically detects if the user is a **Student** or a **Tutor** and updates the relevant specific fields accordingly.

* **Method**: PUT  
* **Endpoint**: /{id}  
* **Full Path**: /api/profiles/{id}

### **Path Parameters**

| Parameter | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| id | UUID | Yes | The unique UUID of the user to update. |

### **Request Body**

**Content-Type**: application/json

The request body should contain the UserProfileDto.

| Field | Type | Description |
| :---- | :---- | :---- |
| firstName | String | User's first name. |
| lastName | String | User's last name. |
| phoneNumber | String | Contact phone number. |
| campus | String | Campus location (e.g., "Di An", "Ly Thuong Kiet"). |
| address | String | Physical address. |
| gender | String | Gender (e.g., "MALE", "FEMALE"). |
| faculty | String | **(Student Only)** The faculty name. |
| major | String | **(Student Only)** The student's major. |
| bio | String | **(Tutor Only)** A short biography. |
| expertiseAreas | String | **(Tutor Only)** Areas of expertise. |

### **Example Request**

{  
  "firstName": "Nguyen",  
  "lastName": "Van A",  
  "phoneNumber": "0987654321",  
  "campus": "Di An",  
  "address": "456 Dormitory B",  
  "gender": "MALE",  
  "faculty": "Computer Science",   
  "major": "Information Systems"  
}

### **Response**

**Status**: 200 OK **Content-Type**: application/json

Returns the updated UserProfileDto (same structure as the GET response).

## **3\. Upload Tutor Credentials**

Allows uploading a file (e.g., certificate, CV) for a specific tutor.

* **Method**: POST  
* **Endpoint**: /tutor/{tutorId}/credentials  
* **Full Path**: /api/profiles/tutor/{tutorId}/credentials

### **Path Parameters**

| Parameter | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| tutorId | String | Yes | The specific ID of the Tutor entity (not UUID). |

### **Request Body (Form Data)**

**Content-Type**: multipart/form-data

| Key | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| file | File | Yes | The binary file to upload (PDF, IMG, etc.). |

### **Example Request (JavaScript/FormData)**

const formData \= new FormData();  
formData.append("file", fileInput.files\[0\]);

fetch('/api/profiles/tutor/TUTOR-123/credentials', {  
  method: 'POST',  
  body: formData  
});

### **Response**

Status: 200 OK  
Content-Type: text/plain  
Credentials uploaded: certificate.pdf

**Status**: 404 Not Found

* Occurs if the tutorId is invalid.

# **Tutor Search & Recommendation API Documentation**

**Base URL**: /api/tutors

This API handles searching for tutors based on filters, generating AI-based recommendations, and retrieving public profile details for specific tutors.

## **1\. Search Tutors**

Allows students to search for tutors using various filters like course name, tutor name, campus, and session mode. It also supports an optional AI-enhanced search.

* **Method**: GET  
* **Endpoint**: /search  
* **Full Path**: /api/tutors/search

### **Query Parameters**

| Parameter | Type | Required | Default | Description |
| :---- | :---- | :---- | :---- | :---- |
| course | String | No | \- | Filter by course name or code (e.g., "Calculus", "CO101"). |
| name | String | No | \- | Filter by tutor's name. |
| campus | String | No | \- | Filter by campus location. |
| mode | String | No | \- | Filter by session mode (e.g., ONLINE, OFFLINE). |
| minRating | Double | No | \- | Minimum average rating (e.g., 4.0). |
| useAi | Boolean | No | false | If true, uses advanced matching logic (e.g., NLP or smart ranking). |

### **Response**

**Status**: 200 OK **Content-Type**: application/json

Returns a list of TutorSearchResultDto.

\[  
  {  
    "tutorId": "TUTOR-001",  
    "fullName": "Nguyen Van B",  
    "rating": 4.8,  
    "expertiseAreas": "Math, Physics",  
    "campus": "Ly Thuong Kiet",  
    "hourlyRate": 150000  
  },  
  {  
    "tutorId": "TUTOR-002",  
    "fullName": "Le Thi C",  
    "rating": 4.5,  
    "expertiseAreas": "Chemistry",  
    "campus": "Di An",  
    "hourlyRate": 120000  
  }  
\]

### **Example Request**

GET /api/tutors/search?course=Calculus\&minRating=4.0\&mode=ONLINE HTTP/1.1

## **2\. Recommend Tutors**

Generates a list of recommended tutors for a specific student, optionally focused on a specific subject.

* **Method**: GET  
* **Endpoint**: /recommend  
* **Full Path**: /api/tutors/recommend

### **Query Parameters**

| Parameter | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| studentId | String | Yes | The ID of the student requesting recommendations. |
| subjectId | String | No | Specific subject ID to tailor recommendations for. |

### **Response**

**Status**: 200 OK **Content-Type**: application/json

Returns a list of TutorSearchResultDto (same structure as Search).

\[  
  {  
    "tutorId": "TUTOR-005",  
    "fullName": "Tran Van D",  
    "rating": 4.9,  
    "expertiseAreas": "Algorithms",  
    "matchScore": 0.95   
  }  
\]

### **Example Request**

// Fetch recommendations for student "STU-123" for subject "SUB-456"  
fetch('/api/tutors/recommend?studentId=STU-123\&subjectId=SUB-456');

## **3\. Get Public Tutor Profile**

Retrieves detailed public information about a specific tutor, including their bio, rating statistics, and available time slots. This endpoint is safe to use for public viewing (excludes sensitive user data).

* **Method**: GET  
* **Endpoint**: /{tutorId}  
* **Full Path**: /api/tutors/{tutorId}

### **Path Parameters**

| Parameter | Type | Required | Description |
| :---- | :---- | :---- | :---- |
| tutorId | String | Yes | The unique Tutor ID (e.g., "TUTOR-123"). |

### **Response**

Status: 200 OK  
Content-Type: application/json  
Returns a TutorProfileDto.

{  
  "tutorId": "TUTOR-123",  
  "tutorName": "Nguyen Van A",  
  "email": "tutor.a@hcmut.edu.vn",  
  "campus": "Ly Thuong Kiet",  
  "rating": 4.7,  
  "ratingCount": 25,  
  "expertiseAreas": "Data Structures, Java Programming",  
  "availableSlots": \[  
    {  
      "id": "SLOT-1",  
      "dayOfWeek": "MONDAY",  
      "startTime": "09:00:00",  
      "endTime": "11:00:00",  
      "mode": "ONLINE"  
    },  
    {  
      "id": "SLOT-2",  
      "dayOfWeek": "WEDNESDAY",  
      "startTime": "14:00:00",  
      "endTime": "16:00:00",  
      "mode": "OFFLINE"  
    }  
  \]  
}

**Status**: 404 Not Found

* Occurs if the tutorId does not exist.

### **Example Request**

const tutorId \= "TUTOR-123";  
fetch(\`/api/tutors/${tutorId}\`)  
  .then(response \=\> response.json())  
  .then(data \=\> console.log(data));

