package hcmut.edu.vn.tutor_support_system.exception;

public class DuplicateEnrollmentException extends EnrollmentException {

  public DuplicateEnrollmentException(String message) {
    super(message);
  }

  public DuplicateEnrollmentException(String message, Throwable cause) {
    super(message, cause);
  }
}
