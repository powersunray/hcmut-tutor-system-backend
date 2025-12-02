package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

  List<Session> findByTutor(Tutor tutor);

  Optional<Session> findBySessionId(String sessionId);

  List<Session> findByStudent(Student student);

  List<Session> findByStatus(SessionStatus status);

  List<Session> findByTutorAndStatus(Tutor tutor, SessionStatus status);

  List<Session> findByStudentAndStatus(Student student, SessionStatus status);

  @Query(
          "SELECT s FROM Session s WHERE s.tutor = :tutor "
                  + "AND ((s.startTime < :endTime AND s.endTime > :startTime) "
                  + "OR s.startTime = :startTime) AND s.status != 'CANCELLED'")
  List<Session> findConflictingSessions(
          @Param("tutor") Tutor tutor,
          @Param("startTime") LocalDateTime startTime,
          @Param("endTime") LocalDateTime endTime);
}
