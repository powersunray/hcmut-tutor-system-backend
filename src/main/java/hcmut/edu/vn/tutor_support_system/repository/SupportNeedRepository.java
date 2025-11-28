package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportNeedRepository extends JpaRepository<SupportNeed, UUID> {

  List<SupportNeed> findByStudent(Student student);

  @Query("SELECT s FROM SupportNeed s WHERE s.student.studentId = :studentId")
  List<SupportNeed> findByStudentId(@Param("studentId") String studentId);

  @Query("SELECT s FROM SupportNeed s WHERE s.supportType = :supportType")
  List<SupportNeed> findBySupportType(@Param("supportType") SupportType supportType);

  @Query("SELECT s FROM SupportNeed s WHERE s.status = :status")
  List<SupportNeed> findByStatus(@Param("status") String status);

  @Query(
      "SELECT s FROM SupportNeed s WHERE s.student.studentId = :studentId AND s.supportType = :supportType")
  List<SupportNeed> findByStudentIdAndSupportType(
      @Param("studentId") String studentId, @Param("supportType") SupportType supportType);

  @Query(
      "SELECT s FROM SupportNeed s WHERE s.student.studentId = :studentId AND s.status = :status")
  List<SupportNeed> findByStudentIdAndStatus(
      @Param("studentId") String studentId, @Param("status") String status);

  @Query(
      "SELECT COUNT(s) FROM SupportNeed s WHERE s.supportType = :supportType AND s.status = :status")
  Long countBySupportTypeAndStatus(
      @Param("supportType") SupportType supportType, @Param("status") String status);
}
