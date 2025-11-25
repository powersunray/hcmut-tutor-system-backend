package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, UUID> {

    Optional<Tutor> findByTutorId(String tutorId);

    @Query("SELECT t FROM Tutor t WHERE t.tutorId = :tutorId")
    Optional<Tutor> findById(@Param("tutorId") String tutorId);
}
