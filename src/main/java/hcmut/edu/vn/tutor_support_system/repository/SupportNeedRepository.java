package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.SupportPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SupportNeedRepository extends JpaRepository<SupportNeed, Long> {
    @Query("SELECT s FROM SupportNeed s WHERE s.student.studentId = :studentId AND (:priority IS NULL OR s.priority = :priority)")
    List<SupportNeed> findByStudentIdAndPriority(Long studentId, SupportPriority priority);

    @Query("SELECT s FROM SupportNeed s WHERE s.declaredDate >= :fromDate")
    List<SupportNeed> findRecentSupportNeeds(LocalDate fromDate);
}
