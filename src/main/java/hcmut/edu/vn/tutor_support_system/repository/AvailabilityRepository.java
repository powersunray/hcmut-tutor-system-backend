package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {

  @Query("SELECT a FROM Availability a WHERE a.tutor = :tutor AND a.published = true")
  List<Availability> findByTutorAndPublishedTrue(Tutor tutor);

  Optional<Availability> findByAvailabilityId(String availabilityId);

  List<Availability> findByTutor(Tutor tutor);

  List<Availability> findByTutorId(UUID tutorId);
  
  @Query("SELECT a FROM Availability a WHERE a.dayOfWeek = :dayOfWeek AND a.published = true")
  List<Availability> findByDayOfWeekAndPublishedTrue(@Param("dayOfWeek") DayOfWeek dayOfWeek);

  @Query("SELECT a FROM Availability a WHERE a.tutor.id = :tutorId AND a.published = true ORDER BY a.dayOfWeek, a.startTime")
  List<Availability> findPublishedSlotsForTutor(@Param("tutorId") UUID tutorId);
}
