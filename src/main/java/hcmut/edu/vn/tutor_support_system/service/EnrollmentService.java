package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.entity.Subject;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.exception.DuplicateEnrollmentException;
import hcmut.edu.vn.tutor_support_system.exception.EnrollmentException;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.EnrollmentRepository;
import hcmut.edu.vn.tutor_support_system.repository.SubjectRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final SubjectRepository subjectRepository;
  @Transactional(readOnly = true)
  public List<EnrollmentDto> getEnrollmentsByStudentId(String studentId) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");

    Student student = studentRepository
        .findByStudentId(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

    List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

    return enrollments.stream()
        .map(enrollment -> {
          Subject subject = enrollment.getSubject(); // assuming Enrollment entity has a 'subject' field
          EnrollmentDto dto = new EnrollmentDto();
          dto.setEnrollmentId(enrollment.getId().toString());
          dto.setStudentId(student.getStudentId());
          dto.setStudentName(student.getFirstName() + " " + student.getLastName());
          dto.setSubjectId(subject.getId().toString());
          dto.setSubjectCode(subject.getCode());
          dto.setSubjectName(subject.getName());
          dto.setCredits(subject.getCredits());
          dto.setSemester(enrollment.getSemester());
          dto.setGrade(enrollment.getGrade());
          dto.setEnrollmentStatus(enrollment.getEnrollmentStatus());
          dto.setCreatedAt(enrollment.getCreatedAt());
          dto.setUpdatedAt(enrollment.getUpdatedAt());
          return dto;
        })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EnrollmentDto> getEnrollmentsByStudentIdAndSemester(
      String studentId, String semester) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");
    ValidationUtil.validateSemester(semester);

    // Verify student exists
    studentRepository
        .findByStudentId(studentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Student not found with id: " + studentId));

    List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndSemester(studentId, semester);
    return enrollments.stream().map(DtoMapper::toEnrollmentDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public EnrollmentDto getEnrollmentById(UUID enrollmentId) {
    ValidationUtil.validateNotNull(enrollmentId, "Enrollment ID");

    Enrollment enrollment = enrollmentRepository
        .findByIdWithRelations(enrollmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));
    return DtoMapper.toEnrollmentDto(enrollment);
  }

  @Transactional
  public EnrollmentDto createEnrollment(String studentId, String subjectCode, String semester, String subjectId) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");
    ValidationUtil.validateCourseCode(subjectCode);
    ValidationUtil.validateSemester(semester);

    Student student = studentRepository
        .findByStudentId(studentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Student not found with id: " + studentId));

    Subject subject = subjectRepository.findById(UUID.fromString(subjectId))
        .orElseThrow(() -> new EnrollmentException("Subject not found"));

    enrollmentRepository
        .findByStudentIdAndCourseCodeAndSemester(studentId, subjectCode, semester)
        .ifPresent(
            e -> {
              throw new DuplicateEnrollmentException(
                  "Student "
                      + studentId
                      + " is already enrolled in course "
                      + subjectCode
                      + " for semester "
                      + semester);
            });

    Long enrollmentCount = enrollmentRepository.countByStudentIdAndSemester(studentId, semester);
    if (enrollmentCount >= 8) {
      throw new EnrollmentException(
          "Student has reached maximum enrollment limit for semester " + semester);
    }

    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourseCode(subjectCode);
    enrollment.setSemester(semester);
    enrollment.setSubject(subject);
    enrollment.setEnrollmentStatus(EnrollmentStatus.ACTIVE);

    Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
    return DtoMapper.toEnrollmentDto(savedEnrollment);
  }

  @Transactional
  public EnrollmentDto updateEnrollmentStatus(UUID enrollmentId, EnrollmentStatus status) {
    ValidationUtil.validateNotNull(enrollmentId, "Enrollment ID");
    ValidationUtil.validateNotNull(status, "Status");

    Enrollment enrollment = enrollmentRepository
        .findByIdWithRelations(enrollmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

    enrollment.setEnrollmentStatus(status);
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
    return DtoMapper.toEnrollmentDto(updatedEnrollment);
  }

  @Transactional
  public EnrollmentDto updateEnrollmentGrade(UUID enrollmentId, String grade) {
    ValidationUtil.validateNotNull(enrollmentId, "Enrollment ID");
    ValidationUtil.validateNotEmpty(grade, "Grade");

    Enrollment enrollment = enrollmentRepository
        .findByIdWithRelations(enrollmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

    enrollment.setGrade(grade);
    Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
    return DtoMapper.toEnrollmentDto(updatedEnrollment);
  }

  @Transactional
  public void deleteEnrollment(UUID enrollmentId) {
    ValidationUtil.validateNotNull(enrollmentId, "Enrollment ID");

    Enrollment enrollment = enrollmentRepository
        .findByIdWithRelations(enrollmentId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

    enrollmentRepository.delete(enrollment);
  }

  @Transactional(readOnly = true)
  public List<EnrollmentDto> getEnrollmentsBySemester(String semester) {
    ValidationUtil.validateSemester(semester);

    List<Enrollment> enrollments = enrollmentRepository.findBySemester(semester);
    return enrollments.stream().map(DtoMapper::toEnrollmentDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<EnrollmentDto> getEnrollmentsByStatus(EnrollmentStatus status) {
    ValidationUtil.validateNotNull(status, "Status");

    List<Enrollment> enrollments = enrollmentRepository.findByEnrollmentStatus(status);
    return enrollments.stream().map(DtoMapper::toEnrollmentDto).collect(Collectors.toList());
  }
}
