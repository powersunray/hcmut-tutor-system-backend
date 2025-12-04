package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponseDto {

  private String sessionId;

  private String tutorId;
  private String tutorName;

  private String studentId; // Student number (e.g., "2110001")
  private String studentUuid; // Student UUID for fetching profile
  private String studentName;
  private String studentEmail;

  private LocalDateTime startTime;
  private LocalDateTime endTime;

  private SessionMode mode;
  private String locationOrLink;

  private SessionStatus status;
}
