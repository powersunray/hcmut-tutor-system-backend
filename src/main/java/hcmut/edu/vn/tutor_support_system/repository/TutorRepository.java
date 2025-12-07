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

    @Query(value = """
            SELECT DISTINCT t.*
            FROM users t
            LEFT JOIN availabilities a ON t.id = a.tutor_id
            LEFT JOIN profiles p ON p.id = t.profile_id
            WHERE t.user_type = 'TUTOR'
            AND (:name IS NULL OR t.first_name ILIKE CONCAT('%', :name, '%')
                         OR t.last_name ILIKE CONCAT('%', :name, '%'))
            AND (:expertise IS NULL OR t.expertise_areas ILIKE CONCAT('%', :expertise, '%'))
            AND (:campus IS NULL OR p.campus = :campus)
            AND (:minRating IS NULL OR t.average_rating >= :minRating)
            AND (:mode IS NULL OR a.mode = CAST(:mode AS text) OR a.mode = 'HYBRID')
            """, nativeQuery = true)
    List<Tutor> searchTutors(
            @Param("name") String name,
            @Param("expertise") String expertise,
            @Param("campus") String campus,
            @Param("minRating") Double minRating,
            @Param("mode") String modeStr);
}
