package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("SELECT e FROM Enrollment e WHERE e.student.studentId = :studentId AND e.semester = :semester")
    List<Enrollment> findByStudentIdAndSemester(Long studentId, String semester);

    @Query("SELECT e FROM Enrollment e WHERE e.student.studentId = :studentId AND e.status = 'ACTIVE'")
    List<Enrollment> findActiveEnrollmentsByStudent(Long studentId);

    List<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    List<Enrollment> findByStudentStudentId(Long studentId);
}
