package hcmut.edu.vn.tutor_support_system.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingNoteDto {
  private String noteId;
  private String sessionId;
  private String createdByName;
  private String createdByEmail;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
