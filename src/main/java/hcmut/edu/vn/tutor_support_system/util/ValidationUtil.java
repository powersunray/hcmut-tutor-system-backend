package hcmut.edu.vn.tutor_support_system.util;

import hcmut.edu.vn.tutor_support_system.exception.ValidationException;
import java.util.regex.Pattern;

public final class ValidationUtil {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
  private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^\\d{7}$");
  private static final Pattern COURSE_CODE_PATTERN = Pattern.compile("^[A-Z]{2}\\d{4}$");
  private static final Pattern SEMESTER_PATTERN = Pattern.compile("^\\d{2}[1-3]$");

  private ValidationUtil() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static void validateNotNull(Object value, String fieldName) {
    if (value == null) {
      throw new ValidationException(fieldName + " cannot be null");
    }
  }

  public static void validateNotEmpty(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new ValidationException(fieldName + " cannot be empty");
    }
  }

  public static void validateEmail(String email) {
    validateNotEmpty(email, "Email");
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new ValidationException("Invalid email format: " + email);
    }
  }

  public static void validateStudentId(String studentId) {
    validateNotEmpty(studentId, "Student ID");
    if (!STUDENT_ID_PATTERN.matcher(studentId).matches()) {
      throw new ValidationException("Invalid student ID format. Must be 7 digits: " + studentId);
    }
  }

  public static void validateCourseCode(String courseCode) {
    validateNotEmpty(courseCode, "Course code");
    if (!COURSE_CODE_PATTERN.matcher(courseCode).matches()) {
      throw new ValidationException(
          "Invalid course code format. Must be 2 letters followed by 4 digits (e.g., CO3001): "
              + courseCode);
    }
  }

  public static void validateSemester(String semester) {
    validateNotEmpty(semester, "Semester");
    if (!SEMESTER_PATTERN.matcher(semester).matches()) {
      throw new ValidationException(
          "Invalid semester format. Must be YYS (e.g., 241): " + semester);
    }
  }

  public static void validatePositive(Integer value, String fieldName) {
    validateNotNull(value, fieldName);
    if (value <= 0) {
      throw new ValidationException(fieldName + " must be positive");
    }
  }

  public static void validateRange(Integer value, int min, int max, String fieldName) {
    validateNotNull(value, fieldName);
    if (value < min || value > max) {
      throw new ValidationException(fieldName + " must be between " + min + " and " + max);
    }
  }

  public static void validateMaxLength(String value, int maxLength, String fieldName) {
    if (value != null && value.length() > maxLength) {
      throw new ValidationException(
          fieldName + " exceeds maximum length of " + maxLength + " characters");
    }
  }

  public static boolean isValidEmail(String email) {
    return email != null && EMAIL_PATTERN.matcher(email).matches();
  }

  public static boolean isValidStudentId(String studentId) {
    return studentId != null && STUDENT_ID_PATTERN.matcher(studentId).matches();
  }

  public static boolean isValidCourseCode(String courseCode) {
    return courseCode != null && COURSE_CODE_PATTERN.matcher(courseCode).matches();
  }

  public static boolean isValidSemester(String semester) {
    return semester != null && SEMESTER_PATTERN.matcher(semester).matches();
  }
}
