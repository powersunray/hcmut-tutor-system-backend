package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.NotificationDto;
import hcmut.edu.vn.tutor_support_system.service.NotificationService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping("/send-email")
  public ResponseEntity<NotificationDto> sendEmailNotification(
      @RequestParam String userId,
      @RequestParam String recipientEmail,
      @RequestParam String subject,
      @RequestParam String message) {

    notificationService.sendEmailNotification(recipientEmail, subject, message);

    NotificationDto notification =
        notificationService.createNotificationDto(
            userId, recipientEmail, subject, message, "EMAIL");

    return ResponseEntity.ok(notification);
  }

  @PostMapping("/session-booking")
  public ResponseEntity<String> sendSessionBookingNotification(
      @RequestParam String studentEmail,
      @RequestParam String tutorEmail,
      @RequestParam String sessionDetails) {

    notificationService.sendSessionBookingNotification(studentEmail, tutorEmail, sessionDetails);
    return ResponseEntity.ok("Session booking notifications sent successfully");
  }

  @PostMapping("/feedback-submitted")
  public ResponseEntity<String> sendFeedbackNotification(
      @RequestParam String tutorEmail, @RequestParam String feedbackDetails) {

    notificationService.sendFeedbackSubmittedNotification(tutorEmail, feedbackDetails);
    return ResponseEntity.ok("Feedback notification sent successfully");
  }

  @PostMapping("/material-uploaded")
  public ResponseEntity<String> sendMaterialUploadNotification(
      @RequestParam String studentEmail, @RequestParam String materialDetails) {

    notificationService.sendMaterialUploadNotification(studentEmail, materialDetails);
    return ResponseEntity.ok("Material upload notification sent successfully");
  }

  @PostMapping("/evaluation-received")
  public ResponseEntity<String> sendEvaluationNotification(
      @RequestParam String tutorEmail, @RequestParam String evaluationDetails) {

    notificationService.sendEvaluationNotification(tutorEmail, evaluationDetails);
    return ResponseEntity.ok("Evaluation notification sent successfully");
  }

  @GetMapping("/history")
  public ResponseEntity<List<NotificationDto>> getNotificationHistory() {
    List<NotificationDto> history = notificationService.getNotificationHistory();
    return ResponseEntity.ok(history);
  }

  @PutMapping("/preferences/{userId}")
  public ResponseEntity<String> updateNotificationPreferences(
      @PathVariable String userId, @RequestBody Map<String, Boolean> preferences) {

    notificationService.updateNotificationPreferences(userId, preferences);
    return ResponseEntity.ok("Notification preferences updated successfully");
  }
}
