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
@Table(name = "session_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "material_id", unique = true, nullable = false, length = 100)
  private String materialId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id")
  private Session session;

  @Column(name = "name", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "source_type", length = 50)
  private MaterialSourceType sourceType;

  @Column(name = "content_url", columnDefinition = "TEXT")
  private String contentUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "visibility", length = 20)
  private MaterialVisibility visibility;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "uploaded_by")
  private User uploadedBy;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Backward compatibility
  @Transient
  public String getId() {
    return materialId;
  }

  public void setId(String id) {
    this.materialId = id;
  }

  // Map old fields to new
  @Transient
  public String getTitle() {
    return name;
  }

  public void setTitle(String title) {
    this.name = title;
  }

  @Transient
  public String getUrlOrPath() {
    return contentUrl;
  }

  public void setUrlOrPath(String urlOrPath) {
    this.contentUrl = urlOrPath;
  }
}
