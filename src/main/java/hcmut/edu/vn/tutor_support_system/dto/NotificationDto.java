package hcmut.edu.vn.tutor_support_system.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
  private String notificationId;
  private String userId;
  private String recipientEmail;
  private String subject;
  private String message;
  private String notificationType;
  private boolean sent;
  private LocalDateTime sentAt;
  private LocalDateTime createdAt;
}
