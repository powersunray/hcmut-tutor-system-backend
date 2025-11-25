package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("TUTOR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tutor extends User {

    @Column(name = "tutor_id", unique = true, length = 50)
    private String tutorId;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "expertise_areas", columnDefinition = "TEXT")
    private String expertiseAreasString; // Stored as comma-separated string

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Availability> availabilitySlots = new ArrayList<>();

    // Convenience methods for expertise areas
    @Transient
    public List<String> getExpertiseAreas() {
        if (expertiseAreasString == null || expertiseAreasString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(expertiseAreasString.split(","));
    }

    public void setExpertiseAreas(List<String> expertiseAreas) {
        if (expertiseAreas == null || expertiseAreas.isEmpty()) {
            this.expertiseAreasString = "";
        } else {
            this.expertiseAreasString = String.join(",", expertiseAreas);
        }
    }

    // Override getters/setters for backward compatibility
    public double getAverageRating() {
        return averageRating != null ? averageRating.doubleValue() : 0.0;
    }

    public void setAverageRating(double rating) {
        this.averageRating = BigDecimal.valueOf(rating);
    }

    public int getRatingCount() {
        return ratingCount != null ? ratingCount : 0;
    }

    public void setRatingCount(int count) {
        this.ratingCount = count;
    }
}
