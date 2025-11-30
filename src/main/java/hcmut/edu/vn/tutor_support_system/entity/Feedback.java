package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "feedback_id", unique = true, nullable = false, length = 100)
  private String feedbackId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id")
  private Session session;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Backward compatibility
  @Transient
  public String getId() {
    return feedbackId;
  }

  public void setId(String id) {
    this.feedbackId = id;
  }

  public int getRating() {
    return rating != null ? rating : 0;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }
}
