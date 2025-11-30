#!/bin/bash


BASE_URL="http://192.168.208.1:8080/api/users"
echo "Testing User API routes..."

# 1. List users (GET /api/users)
echo "\n[1] GET /api/users"
resp=$(curl -s -w "\n%{http_code}" "$BASE_URL")
body=$(echo "$resp" | head -n -1)
status=$(echo "$resp" | tail -n1)
if [ "$status" -eq 200 ]; then
  echo "List users: OK"
else
  echo "List users: FAIL ($status)"; exit 1
fi

# 2. Create user (POST /api/users)
echo "\n[2] POST /api/users"
# The API expects CreateUserRequest: firstName, lastName, email, role (UserRole enum)
create_payload='{ "firstName": "Api", "lastName": "Test", "email": "apitestuser@example.com", "role": "STUDENT" }'
resp=$(curl -s -w "\n%{http_code}" -H "Content-Type: application/json" -d "$create_payload" "$BASE_URL")
body=$(echo "$resp" | head -n -1)
status=$(echo "$resp" | tail -n1)
if [ "$status" -eq 201 ]; then
  echo "Create user: OK"
  # Extract user id (naive, no jq)
  user_id=$(echo "$body" | grep -o '"id":"[^"]*"' | head -n1 | cut -d '"' -f4)
  echo "Created user id: $user_id"
else
  echo "Create user: FAIL ($status)"
  echo "Response body: $body"
  exit 1
fi

# 3. Get user (GET /api/users/{id})
echo "\n[3] GET /api/users/{id}"
resp=$(curl -s -w "\n%{http_code}" "$BASE_URL/$user_id")
body=$(echo "$resp" | head -n -1)
status=$(echo "$resp" | tail -n1)
if [ "$status" -eq 200 ]; then
  echo "Get user: OK"
else
  echo "Get user: FAIL ($status)"; exit 1
fi

# 4. Update user (PUT /api/users/{id})
echo "\n[4] PUT /api/users/{id}"
update_payload='{ "email": "apitestuser2@example.com" }'
resp=$(curl -s -w "\n%{http_code}" -X PUT -H "Content-Type: application/json" -d "$update_payload" "$BASE_URL/$user_id")
body=$(echo "$resp" | head -n -1)
status=$(echo "$resp" | tail -n1)
if [ "$status" -eq 200 ]; then
  echo "Update user: OK"
else
  echo "Update user: FAIL ($status)"; exit 1
fi

# 5. Delete user (DELETE /api/users/{id})
echo "\n[5] DELETE /api/users/{id}"
resp=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/$user_id")
status=$(echo "$resp" | tail -n1)
if [ "$status" -eq 204 ]; then
  echo "Delete user: OK"
else
  echo "Delete user: FAIL ($status)"; exit 1
fi

echo "\nAll User API route tests passed."