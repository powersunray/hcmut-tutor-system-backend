package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "profile_id", unique = true, nullable = false, length = 100)
  private String profileId;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "campus", length = 100)
  private String campus;

  @Column(name = "address", columnDefinition = "TEXT")
  private String address;

  @Column(name = "gender", length = 20)
  private String gender;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
