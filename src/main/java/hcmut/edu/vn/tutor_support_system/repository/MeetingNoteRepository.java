package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.MeetingNote;
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
public interface MeetingNoteRepository extends JpaRepository<MeetingNote, UUID> {

  Optional<MeetingNote> findByNoteId(String noteId);

  List<MeetingNote> findBySession(Session session);

  List<MeetingNote> findByCreatedBy(User createdBy);

  @Query(
      "SELECT mn FROM MeetingNote mn WHERE mn.session.id = :sessionId ORDER BY mn.createdAt DESC")
  List<MeetingNote> findBySessionIdOrderByCreatedAtDesc(@Param("sessionId") UUID sessionId);

  @Query(
      "SELECT mn FROM MeetingNote mn JOIN mn.session s JOIN s.tutor t WHERE t.tutorId = :tutorId"
          + " ORDER BY mn.createdAt DESC")
  List<MeetingNote> findByTutorId(@Param("tutorId") String tutorId);

  @Query("SELECT COUNT(mn) FROM MeetingNote mn WHERE mn.createdBy.id = :userId")
  Long countByCreatedById(@Param("userId") UUID userId);
}
