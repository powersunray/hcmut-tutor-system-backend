package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.service.EnrollmentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @GetMapping("/students/{studentId}/enrollments")
  public ResponseEntity<List<EnrollmentDto>> getStudentEnrollments(
      @PathVariable String studentId, @RequestParam(required = false) String semester) {
    List<EnrollmentDto> enrollments;

    if (semester != null && !semester.isEmpty()) {
      enrollments = enrollmentService.getEnrollmentsByStudentIdAndSemester(studentId, semester);
    } else {
      enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
    }

    return ResponseEntity.ok(enrollments);
  }

  @GetMapping("/enrollments/{enrollmentId}")
  public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable UUID enrollmentId) {
    EnrollmentDto enrollment = enrollmentService.getEnrollmentById(enrollmentId);
    return ResponseEntity.ok(enrollment);
  }

  @PostMapping("/students/{studentId}/enrollments")
  public ResponseEntity<EnrollmentDto> createEnrollment(
      @PathVariable String studentId,
      @RequestParam String subjectCode,
      @RequestParam String semester) {
    EnrollmentDto enrollment = enrollmentService.createEnrollment(studentId, subjectCode, semester);
    return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
  }

  @PutMapping("/enrollments/{enrollmentId}/status")
  public ResponseEntity<EnrollmentDto> updateEnrollmentStatus(
      @PathVariable UUID enrollmentId, @RequestParam String status) {
    EnrollmentDto enrollment = enrollmentService.updateEnrollmentStatus(enrollmentId, status);
    return ResponseEntity.ok(enrollment);
  }

  @PutMapping("/enrollments/{enrollmentId}/grade")
  public ResponseEntity<EnrollmentDto> updateEnrollmentGrade(
      @PathVariable UUID enrollmentId, @RequestParam String grade) {
    EnrollmentDto enrollment = enrollmentService.updateEnrollmentGrade(enrollmentId, grade);
    return ResponseEntity.ok(enrollment);
  }

  @DeleteMapping("/enrollments/{enrollmentId}")
  public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID enrollmentId) {
    enrollmentService.deleteEnrollment(enrollmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/enrollments")
  public ResponseEntity<List<EnrollmentDto>> getEnrollments(
      @RequestParam(required = false) String semester,
      @RequestParam(required = false) String status) {
    List<EnrollmentDto> enrollments;

    if (semester != null && !semester.isEmpty()) {
      enrollments = enrollmentService.getEnrollmentsBySemester(semester);
    } else if (status != null && !status.isEmpty()) {
      enrollments = enrollmentService.getEnrollmentsByStatus(status);
    } else {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(enrollments);
  }
}
