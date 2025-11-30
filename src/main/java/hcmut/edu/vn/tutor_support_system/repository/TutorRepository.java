package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, UUID> {

  Optional<Tutor> findByTutorId(String tutorId);

  @Query(
      "SELECT DISTINCT t FROM Tutor t "
          + "LEFT JOIN t.availabilitySlots a "
          + "WHERE (:name IS NULL OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :name, '%'))) "
          + "AND (:expertise IS NULL OR t.expertiseAreasString LIKE CONCAT('%', :expertise, '%')) "
          + "AND (:campus IS NULL OR t.profile.campus = :campus) "
          + "AND (:minRating IS NULL OR t.averageRating >= :minRating) "
          + "AND (:mode IS NULL OR a.mode = :mode OR a.mode = 'HYBRID')")
  List<Tutor> searchTutors(
      @Param("name") String name,
      @Param("expertise") String expertise,
      @Param("campus") String campus,
      @Param("minRating") Double minRating,
      @Param("mode") SessionMode mode);
}
