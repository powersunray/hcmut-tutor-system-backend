package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentRequest;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.exception.InvalidEnrollmentException;
import hcmut.edu.vn.tutor_support_system.repository.EnrollmentRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public EnrollmentDto enrollStudent(EnrollmentRequest request) {
        Student student = studentRepository.findByStudentId(request.studentId())
                .orElseThrow(() -> new InvalidEnrollmentException("Student not found: " + request.studentId()));

        // Use the external studentId for repository lookups (repositories compare against Student.studentId)
        boolean duplicate = enrollmentRepository.findByStudentIdAndSemester(student.getStudentId(), request.semester())
                .stream()
                .anyMatch(e -> e.getCourseCode() != null && e.getCourseCode().equalsIgnoreCase(request.courseCode()));
        if (duplicate) {
            throw new InvalidEnrollmentException("Enrollment already exists for course and semester");
        }

        if (request.courseCode() != null) {
            subjectRepository.findByCode(request.courseCode())
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Subject not found: " + request.courseCode()));
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .programName(request.programName())
                .courseCode(request.courseCode())
                .semester(request.semester())
                .status(EnrollmentStatus.ACTIVE)
                .enrollmentDate(LocalDate.now())
                .build();
        Enrollment saved = enrollmentRepository.save(enrollment);
        return toDto(saved);
    }

    public List<EnrollmentDto> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public EnrollmentDto updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus newStatus) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new InvalidEnrollmentException("Enrollment not found: " + enrollmentId));
        enrollment.setStatus(newStatus);
        return toDto(enrollmentRepository.save(enrollment));
    }

    public void cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new InvalidEnrollmentException("Enrollment not found: " + enrollmentId));
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDto toDto(Enrollment enrollment) {
        Long studentId = Optional.ofNullable(enrollment.getStudent()).map(Student::getStudentId).orElse(null);
        return new EnrollmentDto(
                enrollment.getId(),
                studentId,
                enrollment.getProgramName(),
                enrollment.getCourseCode(),
                enrollment.getSemester(),
                enrollment.getStatus(),
                enrollment.getEnrollmentDate()
        );
    }
}
