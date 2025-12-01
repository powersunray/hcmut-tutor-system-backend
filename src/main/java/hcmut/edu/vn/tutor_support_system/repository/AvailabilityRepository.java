package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {

  @Query("SELECT a FROM Availability a WHERE a.tutor = :tutor AND a.published = true")
  List<Availability> findByTutorAndPublishedTrue(Tutor tutor);

  Optional<Availability> findByAvailabilityId(String availabilityId);

  List<Availability> findByTutor(Tutor tutor);
}
