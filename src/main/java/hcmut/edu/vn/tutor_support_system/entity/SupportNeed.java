package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "support_needs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportNeed {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "support_type", nullable = false, length = 50)
    private SupportType supportType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 50)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Backward compatibility
    @Transient
    public SupportType getType() {
        return supportType;
    }

    public void setType(SupportType type) {
        this.supportType = type;
    }

    @Transient
    public boolean isActive() {
        return "PENDING".equals(status) || "FULFILLED".equals(status);
    }
}
