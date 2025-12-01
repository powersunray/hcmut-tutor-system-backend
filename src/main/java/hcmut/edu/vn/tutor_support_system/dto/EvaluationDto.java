package hcmut.edu.vn.tutor_support_system.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDto {
  private String evaluationId;
  private String sessionId;
  private String evaluatorName;
  private String evaluatorEmail;
  private String tutorName;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
