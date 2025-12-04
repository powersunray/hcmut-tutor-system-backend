#!/bin/bash

# API Test Script for Student Tutoring System

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BASE_URL="http://localhost:8080"
TEST_RESULTS=()
PASS_COUNT=0
FAIL_COUNT=0

# Helper Functions
print_section() {
    echo -e "\n${BLUE}=================================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}=================================================${NC}\n"
}

print_test() {
    echo -e "${YELLOW}TEST:${NC} $1"
}

print_pass() {
    echo -e "${GREEN}✓ PASS${NC}: $1\n"
    ((PASS_COUNT++)) || true
    TEST_RESULTS+=("PASS: $1")
}

print_fail() {
    echo -e "${RED}✗ FAIL${NC}: $1"
    echo -e "${RED}Expected:${NC} $2"
    echo -e "${RED}Got:${NC} $3\n"
    ((FAIL_COUNT++)) || true
    TEST_RESULTS+=("FAIL: $1")
}

check_http_status() {
    local url="$1"
    local expected_status="$2"
    local method="${3:-GET}"
    local data="$4"
    local content_type="$5"

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$url")
    elif [ "$method" = "POST" ] && [ -n "$content_type" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST "$url" -H "Content-Type: $content_type" -d "$data")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST "$url")
    elif [ "$method" = "PUT" ] && [ -n "$content_type" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT "$url" -H "Content-Type: $content_type" -d "$data")
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT "$url")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -X DELETE "$url")
    fi

    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    if [ "$http_code" = "$expected_status" ]; then
        return 0
    else
        echo "$body"
        return 1
    fi
}

check_json_field() {
    local json="$1"
    local field="$2"
    local expected="$3"

    actual=$(echo "$json" | grep -o "\"$field\":\"[^\"]*\"" | cut -d'"' -f4)

    if [ "$actual" = "$expected" ]; then
        return 0
    else
        return 1
    fi
}

# Prerequisites Check
print_section "Prerequisites Check"

echo "Checking if database is running..."
if docker ps | grep -q postgres; then
    print_pass "PostgreSQL container is running"
else
    echo -e "${RED}Error: PostgreSQL container is not running${NC}"
    echo "Please run: ./setup_postgres_db.sh"
    exit 1
fi

echo "Checking if backend server is accessible..."
if curl -s -f "$BASE_URL/actuator/health" > /dev/null 2>&1 || curl -s -f "$BASE_URL/" > /dev/null 2>&1; then
    print_pass "Backend server is running at $BASE_URL"
else
    echo -e "${RED}Error: Backend server is not accessible at $BASE_URL${NC}"
    echo "Please run: mvn clean install && java -jar target/tutor-support-system-0.0.1-SNAPSHOT.jar"
    exit 1
fi

# ========================================
# 1. ENROLLMENT API TESTS
# ========================================

print_section "1. ENROLLMENT API TESTS"

# 1.1 Get All Enrollments for a Student
print_test "1.1 Get all enrollments for student 2110001"
response=$(curl -s "$BASE_URL/api/students/2110001/enrollments")
if echo "$response" | grep -q "2110001" && [ "$(echo "$response" | grep -o "enrollmentId" | wc -l)" -ge 1 ]; then
    print_pass "Retrieved enrollments for student 2110001"
else
    print_fail "Get enrollments for student 2110001" "Array with enrollments" "$response"
fi

# 1.2 Get Enrollments by Student and Semester
print_test "1.2 Get enrollments for student 2110001 in semester 241"
response=$(curl -s "$BASE_URL/api/students/2110001/enrollments?semester=241")
if echo "$response" | grep -q "241" && echo "$response" | grep -q "2110001"; then
    print_pass "Retrieved enrollments for student 2110001 in semester 241"
else
    print_fail "Get enrollments by semester" "Enrollments for semester 241" "$response"
fi

# 1.3 Get All Enrollments by Semester
print_test "1.3 Get all enrollments in semester 251"
response=$(curl -s "$BASE_URL/api/enrollments?semester=251")
if echo "$response" | grep -q "251" && [ "$(echo "$response" | grep -o "enrollmentId" | wc -l)" -ge 1 ]; then
    print_pass "Retrieved all enrollments for semester 251"
else
    print_fail "Get all enrollments by semester" "Array of enrollments" "$response"
fi

# 1.4 Get Enrollments by Status
print_test "1.4 Get all ACTIVE enrollments"
response=$(curl -s "$BASE_URL/api/enrollments?status=ACTIVE")
if echo "$response" | grep -q "ACTIVE"; then
    print_pass "Retrieved ACTIVE enrollments"
else
    print_fail "Get enrollments by status" "ACTIVE enrollments" "$response"
fi

# 1.5 Create New Enrollment
print_test "1.5 Create new enrollment for student 2110005"
if check_http_status "$BASE_URL/api/students/2110005/enrollments?subjectCode=CO3015&semester=251" "201" "POST"; then
    print_pass "Created new enrollment successfully (201)"
else
    print_fail "Create enrollment" "201 Created" "Different status code"
fi

# 1.6 Duplicate Enrollment (Should Fail)
print_test "1.6 Try duplicate enrollment (should fail with 409)"
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/students/2110001/enrollments?subjectCode=CO3015&semester=251")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "409" ]; then
    print_pass "Duplicate enrollment correctly rejected with 409"
else
    print_fail "Duplicate enrollment validation" "409 Conflict" "HTTP $http_code"
fi

# ========================================
# 2. SUPPORT NEEDS API TESTS
# ========================================

print_section "2. SUPPORT NEEDS API TESTS"

# 2.1 Get All Support Needs for a Student
print_test "2.1 Get all support needs for student 2110001"
response=$(curl -s "$BASE_URL/api/students/2110001/support-needs")
if echo "$response" | grep -q "2110001" || echo "$response" | grep -q "supportNeedId"; then
    print_pass "Retrieved support needs for student 2110001"
else
    print_fail "Get support needs for student" "Array with support needs" "$response"
fi

# 2.2 Get Support Needs by Type
print_test "2.2 Get ACADEMIC_HELP support needs for student 2110001"
response=$(curl -s "$BASE_URL/api/students/2110001/support-needs?type=ACADEMIC_HELP")
if echo "$response" | grep -q "ACADEMIC_HELP"; then
    print_pass "Retrieved ACADEMIC_HELP support needs"
else
    print_fail "Get support needs by type" "ACADEMIC_HELP needs" "$response"
fi

# 2.3 Get Support Needs by Type (All Students)
print_test "2.3 Get all SCHOLARSHIP support needs"
response=$(curl -s "$BASE_URL/api/support-needs?type=SCHOLARSHIP")
if echo "$response" | grep -q "SCHOLARSHIP" || [ "$response" = "[]" ]; then
    print_pass "Retrieved all SCHOLARSHIP support needs"
else
    print_fail "Get all support needs by type" "SCHOLARSHIP needs" "$response"
fi

# 2.4 Get Support Needs by Status
print_test "2.4 Get all PENDING support needs"
response=$(curl -s "$BASE_URL/api/support-needs?status=PENDING")
if echo "$response" | grep -q "PENDING" || [ "$response" = "[]" ]; then
    print_pass "Retrieved PENDING support needs"
else
    print_fail "Get support needs by status" "PENDING needs" "$response"
fi

# 2.5 Create New Support Need
print_test "2.5 Create ACADEMIC_HELP support need for student 2110005"
if check_http_status "$BASE_URL/api/students/2110005/support-needs?supportType=ACADEMIC_HELP&description=Need%20help%20with%20final%20project" "201" "POST"; then
    print_pass "Created new support need successfully (201)"
else
    print_fail "Create support need" "201 Created" "Different status code"
fi

# 2.6 Get Support Needs Statistics
print_test "2.6 Get statistics for PENDING ACADEMIC_HELP requests"
response=$(curl -s "$BASE_URL/api/support-needs/statistics?type=ACADEMIC_HELP&status=PENDING")
if [[ "$response" =~ ^[0-9]+$ ]]; then
    print_pass "Retrieved support needs statistics (count: $response)"
else
    print_fail "Get support needs statistics" "Numeric count" "$response"
fi

# ========================================
# 3. STAFF API TESTS
# ========================================

print_section "3. STAFF API TESTS"

# 3.1 Get All Staff
print_test "3.1 Get all staff members"
response=$(curl -s "$BASE_URL/api/staff")
if echo "$response" | grep -q "staffId" || [ "$response" = "[]" ]; then
    print_pass "Retrieved all staff members"
else
    print_fail "Get all staff" "Array of staff members" "$response"
fi

# 3.2 Get Staff by Role
print_test "3.2 Get all ADS staff"
response=$(curl -s "$BASE_URL/api/staff?role=ADS")
if echo "$response" | grep -q "ADS" || [ "$response" = "[]" ]; then
    print_pass "Retrieved ADS staff members"
else
    print_fail "Get staff by role" "ADS staff" "$response"
fi

# 3.3 Get Staff by Department
print_test "3.3 Get staff from Academic Development Services"
response=$(curl -s "$BASE_URL/api/staff?department=Academic%20Development%20Services")
if echo "$response" | grep -q "Academic Development Services" || [ "$response" = "[]" ]; then
    print_pass "Retrieved staff by department"
else
    print_fail "Get staff by department" "Staff from specific department" "$response"
fi

# 3.4 Get Staff by Staff ID
print_test "3.4 Get staff by staff-001"
response=$(curl -s "$BASE_URL/api/staff/staff-id/staff-001")
if echo "$response" | grep -q "staff-001"; then
    print_pass "Retrieved staff by staff ID"
else
    print_fail "Get staff by staff ID" "Staff with staff-001" "$response"
fi

# 3.5 Create New Staff
print_test "3.5 Create new staff member"
staff_json='{
  "staffId": "staff-999",
  "firstName": "Test",
  "lastName": "User",
  "email": "test.user@hcmut.edu.vn",
  "phoneNumber": "0912345678",
  "staffRole": "ADS",
  "department": "Academic Development Services",
  "campus": "Campus 02"
}'
if check_http_status "$BASE_URL/api/staff" "201" "POST" "$staff_json" "application/json"; then
    print_pass "Created new staff member successfully (201)"
else
    print_fail "Create staff member" "201 Created" "Different status code"
fi

# 3.6 Get Staff Count by Role
print_test "3.6 Count ADS staff"
response=$(curl -s "$BASE_URL/api/staff/statistics/count-by-role?role=ADS")
if [[ "$response" =~ ^[0-9]+$ ]]; then
    print_pass "Retrieved staff count by role (count: $response)"
else
    print_fail "Get staff count by role" "Numeric count" "$response"
fi

# ========================================
# 4. VALIDATION TESTING
# ========================================

print_section "4. VALIDATION TESTS"

# 4.1 Invalid Student ID Format
print_test "4.1 Try invalid student ID format (should fail with 400)"
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/students/123/enrollments?subjectCode=CO3001&semester=251")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "400" ] || [ "$http_code" = "404" ]; then
    print_pass "Invalid student ID correctly rejected"
else
    print_fail "Invalid student ID validation" "400/404" "HTTP $http_code"
fi

# 4.2 Invalid Course Code Format
print_test "4.2 Try invalid course code format (should fail with 400)"
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/students/2110001/enrollments?subjectCode=INVALID&semester=251")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "400" ] || [ "$http_code" = "404" ]; then
    print_pass "Invalid course code correctly rejected"
else
    print_fail "Invalid course code validation" "400/404" "HTTP $http_code"
fi

# 4.3 Invalid Support Type
print_test "4.3 Try invalid support type (should fail with 400)"
response=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL/api/students/2110001/support-needs?supportType=INVALID_TYPE")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "400" ]; then
    print_pass "Invalid support type correctly rejected with 400"
else
    print_fail "Invalid support type validation" "400 Bad Request" "HTTP $http_code"
fi

# ========================================
# 5. ERROR HANDLING TESTS
# ========================================

print_section "5. ERROR HANDLING TESTS"

# 5.1 Non-existent Student
print_test "5.1 Try to get enrollments for non-existent student (should fail with 404)"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/students/9999999/enrollments")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "404" ]; then
    print_pass "Non-existent student correctly returns 404"
else
    print_fail "Non-existent student handling" "404 Not Found" "HTTP $http_code"
fi

# 5.2 Non-existent Staff ID
print_test "5.2 Try to get non-existent staff (should fail with 404)"
response=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/staff/staff-id/nonexistent")
http_code=$(echo "$response" | tail -n1)
if [ "$http_code" = "404" ]; then
    print_pass "Non-existent staff correctly returns 404"
else
    print_fail "Non-existent staff handling" "404 Not Found" "HTTP $http_code"
fi

# ========================================
# TEST SUMMARY
# ========================================

print_section "TEST SUMMARY"

total_tests=$((PASS_COUNT + FAIL_COUNT))
pass_percentage=$((PASS_COUNT * 100 / total_tests))

echo -e "${GREEN}Passed:${NC} $PASS_COUNT / $total_tests ($pass_percentage%)"
echo -e "${RED}Failed:${NC} $FAIL_COUNT / $total_tests"

if [ $FAIL_COUNT -eq 0 ]; then
    echo -e "\n${GREEN}🎉 All tests passed!${NC}\n"
    exit 0
else
    echo -e "\n${RED}❌ Some tests failed. Please review the output above.${NC}\n"
    exit 1
fi
