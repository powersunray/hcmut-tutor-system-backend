package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Feedback;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

  Optional<Feedback> findByFeedbackId(String feedbackId);

  Optional<Feedback> findBySession(Session session);

  @Query(
      "SELECT f FROM Feedback f JOIN f.session s JOIN s.tutor t WHERE t.tutorId = :tutorId ORDER"
          + " BY f.createdAt DESC")
  List<Feedback> findByTutorId(@Param("tutorId") String tutorId);

  @Query(
      "SELECT AVG(f.rating) FROM Feedback f JOIN f.session s JOIN s.tutor t WHERE t.tutorId ="
          + " :tutorId")
  Double calculateAverageRatingByTutorId(@Param("tutorId") String tutorId);

  @Query(
      "SELECT COUNT(f) FROM Feedback f JOIN f.session s JOIN s.tutor t WHERE t.tutorId = :tutorId")
  Long countByTutorId(@Param("tutorId") String tutorId);
}
