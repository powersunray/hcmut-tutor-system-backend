package hcmut.edu.vn.tutor_support_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EnrollmentRequest(
        @NotNull @Positive Long studentId,
        @NotBlank String programName,
        String courseCode,
        @NotBlank String semester
) {
}
