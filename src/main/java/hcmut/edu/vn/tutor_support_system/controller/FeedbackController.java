package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.FeedbackDto;
import hcmut.edu.vn.tutor_support_system.dto.FeedbackSubmissionRequestDto;
import hcmut.edu.vn.tutor_support_system.service.FeedbackService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

  private final FeedbackService feedbackService;

  @GetMapping("/{feedbackId}")
  public ResponseEntity<FeedbackDto> getFeedbackById(@PathVariable String feedbackId) {
    FeedbackDto feedback = feedbackService.getFeedbackById(feedbackId);
    return ResponseEntity.ok(feedback);
  }

  @GetMapping("/tutor/{tutorId}")
  public ResponseEntity<List<FeedbackDto>> getFeedbackByTutorId(@PathVariable String tutorId) {
    List<FeedbackDto> feedbacks = feedbackService.getFeedbackByTutorId(tutorId);
    return ResponseEntity.ok(feedbacks);
  }

  @GetMapping("/session/{sessionId}")
  public ResponseEntity<FeedbackDto> getFeedbackBySessionId(@PathVariable String sessionId) {
    FeedbackDto feedback = feedbackService.getFeedbackBySessionId(sessionId);
    return ResponseEntity.ok(feedback);
  }

  @GetMapping("/tutor/{tutorId}/stats")
  public ResponseEntity<TutorFeedbackStats> getTutorFeedbackStats(@PathVariable String tutorId) {
    Double avgRating = feedbackService.getAverageRatingByTutorId(tutorId);
    Long count = feedbackService.getFeedbackCountByTutorId(tutorId);

    TutorFeedbackStats stats = new TutorFeedbackStats(tutorId, avgRating, count);
    return ResponseEntity.ok(stats);
  }

  @PostMapping
  public ResponseEntity<FeedbackDto> submitFeedback(
      @Valid @RequestBody FeedbackSubmissionRequestDto request) {
    FeedbackDto feedback = feedbackService.submitFeedback(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
  }

  @PutMapping("/{feedbackId}")
  public ResponseEntity<FeedbackDto> updateFeedback(
      @PathVariable String feedbackId, @Valid @RequestBody FeedbackSubmissionRequestDto request) {
    FeedbackDto feedback = feedbackService.updateFeedback(feedbackId, request);
    return ResponseEntity.ok(feedback);
  }

  @DeleteMapping("/{feedbackId}")
  public ResponseEntity<Void> deleteFeedback(@PathVariable String feedbackId) {
    feedbackService.deleteFeedback(feedbackId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
    List<FeedbackDto> feedbacks = feedbackService.getAllFeedback();
    return ResponseEntity.ok(feedbacks);
  }

  // Inner class for stats response
  public record TutorFeedbackStats(String tutorId, Double averageRating, Long totalFeedback) {}
}
