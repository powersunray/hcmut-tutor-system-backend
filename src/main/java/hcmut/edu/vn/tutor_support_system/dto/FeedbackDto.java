package hcmut.edu.vn.tutor_support_system.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDto {
  private String feedbackId;
  private String sessionId;
  private String studentName;
  private String tutorName;
  private int rating;
  private String comment;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
