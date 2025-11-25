package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tutoring_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "session_id", unique = true, nullable = false, length = 100)
  private String sessionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tutor_id")
  private Tutor tutor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private Student student;

  @Column(name = "title")
  private String title;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "mode", nullable = false, length = 20)
  private SessionMode mode;

  @Column(name = "location_or_link", length = 500)
  private String locationOrLink;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 50)
  private SessionStatus status;

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MeetingNote> meetingNotes = new ArrayList<>();

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Material> materials = new ArrayList<>();

  @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Feedback studentFeedback;

  @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Evaluation evaluation;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Backward compatibility
  @Transient
  public String getId() {
    return sessionId;
  }

  public void setId(String id) {
    this.sessionId = id;
  }
}
