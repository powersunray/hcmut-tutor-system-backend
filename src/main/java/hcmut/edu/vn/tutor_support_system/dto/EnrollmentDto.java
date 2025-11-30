package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;

import java.time.LocalDate;

public record EnrollmentDto(
        Long id,
        Long studentId,
        String programName,
        String courseCode,
        String semester,
        EnrollmentStatus status,
        LocalDate enrollmentDate
) {
}
