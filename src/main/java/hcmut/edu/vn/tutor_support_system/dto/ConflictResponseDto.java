package hcmut.edu.vn.tutor_support_system.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictResponseDto {

  private String conflictingSessionId;

  private String conflictingTutorId;

  private LocalDateTime conflictStartTime;

  private LocalDateTime conflictEndTime;

  private String reason;
}