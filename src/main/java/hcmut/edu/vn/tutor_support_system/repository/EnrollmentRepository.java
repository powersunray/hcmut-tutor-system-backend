package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.id = :id")
  Optional<Enrollment> findByIdWithRelations(@Param("id") UUID id);

  List<Enrollment> findByStudent(Student student);

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.student.studentId = :studentId")
  List<Enrollment> findByStudentId(@Param("studentId") String studentId);

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.student.studentId = :studentId AND e.semester = :semester")
  List<Enrollment> findByStudentIdAndSemester(
      @Param("studentId") String studentId, @Param("semester") String semester);

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.student.studentId = :studentId AND e.courseCode = :courseCode AND e.semester = :semester")
  Optional<Enrollment> findByStudentIdAndCourseCodeAndSemester(
      @Param("studentId") String studentId,
      @Param("courseCode") String courseCode,
      @Param("semester") String semester);

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.enrollmentStatus = :status")
  List<Enrollment> findByEnrollmentStatus(@Param("status") EnrollmentStatus status);

  @Query(
      "SELECT e FROM Enrollment e "
          + "LEFT JOIN FETCH e.student "
          + "LEFT JOIN FETCH e.subject "
          + "WHERE e.semester = :semester")
  List<Enrollment> findBySemester(@Param("semester") String semester);

  @Query(
      "SELECT COUNT(e) FROM Enrollment e WHERE e.student.studentId = :studentId AND e.semester = :semester")
  Long countByStudentIdAndSemester(
      @Param("studentId") String studentId, @Param("semester") String semester);
}
