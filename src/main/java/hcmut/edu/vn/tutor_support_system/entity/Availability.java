package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "availabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "availability_id", unique = true, nullable = false, length = 100)
  private String availabilityId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tutor_id")
  private Tutor tutor;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false, length = 20)
  private DayOfWeek dayOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "mode", nullable = false, length = 20)
  private SessionMode mode;

  @Column(name = "location_or_link", length = 500)
  private String locationOrLink;

  @Column(name = "capacity")
  private Integer capacity;

  @Column(name = "published")
  private Boolean published;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Backward compatibility methods
  @Transient
  public String getId() {
    return availabilityId;
  }

  public void setId(String id) {
    this.availabilityId = id;
  }

  public int getCapacity() {
    return capacity != null ? capacity : 1;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public boolean isPublished() {
    return published != null && published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }
}
