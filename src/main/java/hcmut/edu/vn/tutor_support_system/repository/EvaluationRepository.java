package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Evaluation;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {

  Optional<Evaluation> findByEvaluationId(String evaluationId);

  Optional<Evaluation> findBySession(Session session);

  List<Evaluation> findByEvaluator(User evaluator);

  @Query(
      "SELECT e FROM Evaluation e JOIN e.session s JOIN s.tutor t WHERE t.tutorId = :tutorId ORDER"
          + " BY e.createdAt DESC")
  List<Evaluation> findByTutorId(@Param("tutorId") String tutorId);

  @Query("SELECT COUNT(e) FROM Evaluation e WHERE e.evaluator.id = :evaluatorId")
  Long countByEvaluatorId(@Param("evaluatorId") UUID evaluatorId);
}
