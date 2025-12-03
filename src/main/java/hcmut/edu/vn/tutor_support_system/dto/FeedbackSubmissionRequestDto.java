package hcmut.edu.vn.tutor_support_system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSubmissionRequestDto {

  @NotBlank(message = "Session ID is required") private String sessionId;

  @Min(value = 1, message = "Rating must be at least 1") @Max(value = 5, message = "Rating must be at most 5") private int rating;

  private String comment;
}
