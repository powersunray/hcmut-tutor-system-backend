package hcmut.edu.vn.tutor_support_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEnrollmentException extends EnrollmentException {
    public InvalidEnrollmentException(String message) {
        super(message);
    }
}
