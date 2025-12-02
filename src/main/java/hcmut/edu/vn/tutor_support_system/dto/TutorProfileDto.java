package hcmut.edu.vn.tutor_support_system.dto;

import java.util.List;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorProfileDto {
  private String id; // The UUID, used internally

  @NotBlank(message = "Tutor ID is required")
  private String tutorId; // The business ID, shown to users

  @NotBlank(message = "Tutor name is required")
  private String tutorName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  private String email;

  private String campus;
  private String bio; // From Tutor entity
  private String phoneNumber; // From Profile entity

  @Min(value = 0, message = "Rating must be greater than or equal to 0")
  private double rating;

  @Min(value = 0, message = "Rating count must be greater than or equal to 0")
  private int ratingCount;

  @NotEmpty(message = "Expertise areas cannot be empty")
  private List<String> expertiseAreas;
  
  private List<AvailabilityDto> availableSlots; // reuse your existing AvailabilityDto (no tutor inside)
}
