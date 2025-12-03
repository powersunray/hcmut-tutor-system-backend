package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.NotificationDto;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  @Async
  public void sendEmailNotification(String recipientEmail, String subject, String message) {
    ValidationUtil.validateNotEmpty(recipientEmail, "Recipient email");
    ValidationUtil.validateNotEmpty(subject, "Subject");
    ValidationUtil.validateNotEmpty(message, "Message");

    log.info("Sending email to: {} with subject: {}", recipientEmail, subject);

    // TODO: Implement actual email sending logic with JavaMailSender
    // For now, just log the notification
    try {
      Thread.sleep(1000); // Simulate email sending delay
      log.info("Email sent successfully to: {}", recipientEmail);
    } catch (InterruptedException e) {
      log.error("Error sending email to: {}", recipientEmail, e);
      Thread.currentThread().interrupt();
    }
  }

  @Async
  public void sendSessionBookingNotification(
      String studentEmail, String tutorEmail, String sessionDetails) {
    ValidationUtil.validateNotEmpty(studentEmail, "Student email");
    ValidationUtil.validateNotEmpty(tutorEmail, "Tutor email");

    String studentSubject = "Session Booking Confirmation";
    String studentMessage = "Your tutoring session has been booked. Details: " + sessionDetails;
    sendEmailNotification(studentEmail, studentSubject, studentMessage);

    String tutorSubject = "New Session Booking";
    String tutorMessage =
        "A new tutoring session has been booked with you. Details: " + sessionDetails;
    sendEmailNotification(tutorEmail, tutorSubject, tutorMessage);
  }

  @Async
  public void sendFeedbackSubmittedNotification(String tutorEmail, String feedbackDetails) {
    ValidationUtil.validateNotEmpty(tutorEmail, "Tutor email");

    String subject = "New Feedback Received";
    String message =
        "You have received new feedback for a tutoring session. Details: " + feedbackDetails;
    sendEmailNotification(tutorEmail, subject, message);
  }

  @Async
  public void sendMaterialUploadNotification(String studentEmail, String materialDetails) {
    ValidationUtil.validateNotEmpty(studentEmail, "Student email");

    String subject = "New Material Uploaded";
    String message = "New material has been uploaded for your session. Details: " + materialDetails;
    sendEmailNotification(studentEmail, subject, message);
  }

  @Async
  public void sendEvaluationNotification(String tutorEmail, String evaluationDetails) {
    ValidationUtil.validateNotEmpty(tutorEmail, "Tutor email");

    String subject = "New Evaluation Received";
    String message = "You have received a new evaluation. Details: " + evaluationDetails;
    sendEmailNotification(tutorEmail, subject, message);
  }

  public NotificationDto createNotificationDto(
      String userId,
      String recipientEmail,
      String subject,
      String message,
      String notificationType) {

    return NotificationDto.builder()
        .notificationId("NOTIF-" + UUID.randomUUID().toString())
        .userId(userId)
        .recipientEmail(recipientEmail)
        .subject(subject)
        .message(message)
        .notificationType(notificationType)
        .sent(true)
        .sentAt(LocalDateTime.now())
        .createdAt(LocalDateTime.now())
        .build();
  }

  public List<NotificationDto> getNotificationHistory() {
    // TODO: Implement notification history retrieval from database
    // For now, return empty list
    log.info("Retrieving notification history");
    return new ArrayList<>();
  }

  public void updateNotificationPreferences(String userId, Map<String, Boolean> preferences) {
    ValidationUtil.validateNotEmpty(userId, "User ID");
    ValidationUtil.validateNotNull(preferences, "Preferences");

    // TODO: Implement notification preference storage
    log.info("Updating notification preferences for user: {}", userId);
  }
}
