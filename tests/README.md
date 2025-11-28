# API Test Suite

Automated test suite for the HCMUT Tutor Support System backend API.

## Overview

This test suite validates all API endpoints based on the scenarios defined in [`docs/API_TEST_SCENARIOS.md`](../docs/API_TEST_SCENARIOS.md).

## Test Coverage

The test suite covers:

- **Enrollment API** (6 tests)
  - Get enrollments by student, semester, and status
  - Create new enrollments
  - Duplicate enrollment validation

- **Support Needs API** (6 tests)
  - Get support needs by student, type, and status
  - Create new support needs
  - Statistics endpoints

- **Staff API** (6 tests)
  - Get staff by role, department, and ID
  - Create new staff members
  - Staff count statistics

- **Validation Tests** (3 tests)
  - Invalid student ID format
  - Invalid course code format
  - Invalid support type

- **Error Handling Tests** (2 tests)
  - Non-existent resource handling
  - 404 error responses

**Total: 23 automated tests**

## Prerequisites

Before running the tests, ensure:

1. **Database is running** with test data:
   ```bash
   ./setup_postgres_db.sh
   ```

2. **Backend server is running**:
   ```bash
   mvn clean install && java -jar target/tutor-support-system-0.0.1-SNAPSHOT.jar
   ```

3. **Server is accessible** at `http://localhost:8080`

## Running the Tests

### Run All Tests

```bash
./tests/run_api_tests.sh
```

### Test Output

The script provides:
- Color-coded output (green for pass, red for fail, blue for sections)
- Real-time test execution status
- Prerequisites verification
- Summary with pass/fail counts and percentage

Example output:
```
=================================================
1. ENROLLMENT API TESTS
=================================================

TEST: 1.1 Get all enrollments for student 2110001
✓ PASS: Retrieved enrollments for student 2110001

TEST: 1.2 Get enrollments for student 2110001 in semester 241
✓ PASS: Retrieved enrollments for student 2110001 in semester 241

...

=================================================
TEST SUMMARY
=================================================

Passed: 23 / 23 (100%)
Failed: 0 / 23

🎉 All tests passed!
```

## Exit Codes

- `0` - All tests passed
- `1` - One or more tests failed or prerequisites not met

## CI/CD Integration

This test script can be integrated into CI/CD pipelines:

```bash
# Example GitHub Actions workflow
- name: Run API Tests
  run: ./tests/run_api_tests.sh
```

## Troubleshooting

### Database Not Running
```
Error: PostgreSQL container is not running
Please run: ./setup_postgres_db.sh
```
**Solution:** Start the database with `./setup_postgres_db.sh`

### Server Not Accessible
```
Error: Backend server is not accessible at http://localhost:8080
```
**Solution:** Start the backend server:
```bash
mvn clean install && java -jar target/tutor-support-system-0.0.1-SNAPSHOT.jar
```

### Connection Refused
If tests fail with connection errors, ensure:
- Database is running on port 5432
- Server is running on port 8080
- No firewall blocking connections

## Test Data

The tests use dummy data from `postgres_dummy_data_jpa.sql`, including:

- **Students**: 2110001-2110005 (John Doe, Jane Smith, etc.)
- **Staff**: staff-001, staff-002, staff-003
- **Subjects**: CO3001, CO2013, CO3015, MT1005
- **Semesters**: 241, 242, 251

See [`docs/API_TEST_SCENARIOS.md`](../docs/API_TEST_SCENARIOS.md) for complete test data reference.

## Extending the Tests

To add new tests:

1. Add a new test section in `run_api_tests.sh`:
   ```bash
   print_test "Your test description"
   response=$(curl -s "$BASE_URL/your/endpoint")
   if [[ condition ]]; then
       print_pass "Test passed message"
   else
       print_fail "Test name" "Expected" "Actual"
   fi
   ```

2. Update this README with the new test coverage

## Manual Testing

For manual testing with curl commands, refer to:
- [`docs/API_TEST_SCENARIOS.md`](../docs/API_TEST_SCENARIOS.md) - Complete curl command reference
- Individual test sections in `run_api_tests.sh` - See exact curl commands used

## Notes

- Tests are idempotent where possible
- Some tests create resources (enrollments, support needs, staff)
- Database should be reset between test runs for consistent results
- The script validates HTTP status codes and response content
