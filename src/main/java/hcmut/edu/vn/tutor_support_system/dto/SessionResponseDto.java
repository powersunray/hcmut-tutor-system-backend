package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponseDto {

    private String sessionId;

    private String tutorId;
    private String tutorName;

    private Long studentId;
    private String studentName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private SessionMode mode;
    private String locationOrLink;

    private SessionStatus status;
}
