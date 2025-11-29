package hcmut.edu.vn.tutor_support_system.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String message) {
        super(message);
    }
    public SessionNotFoundException(String message, Throwable cause) {
      super(message, cause);
    }
}
