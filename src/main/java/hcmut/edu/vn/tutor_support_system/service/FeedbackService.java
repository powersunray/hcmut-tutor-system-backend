package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.FeedbackDto;
import hcmut.edu.vn.tutor_support_system.dto.FeedbackSubmissionRequestDto;
import hcmut.edu.vn.tutor_support_system.entity.Feedback;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.exception.FeedbackException;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.FeedbackRepository;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final FeedbackRepository feedbackRepository;
  private final SessionRepository sessionRepository;

  @Transactional(readOnly = true)
  public FeedbackDto getFeedbackById(String feedbackId) {
    ValidationUtil.validateNotEmpty(feedbackId, "Feedback ID");

    Feedback feedback =
        feedbackRepository
            .findByFeedbackId(feedbackId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));
    return DtoMapper.toFeedbackDto(feedback);
  }

  @Transactional(readOnly = true)
  public List<FeedbackDto> getFeedbackByTutorId(String tutorId) {
    ValidationUtil.validateNotEmpty(tutorId, "Tutor ID");

    List<Feedback> feedbacks = feedbackRepository.findByTutorId(tutorId);
    return feedbacks.stream().map(DtoMapper::toFeedbackDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public FeedbackDto getFeedbackBySessionId(String sessionId) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    Feedback feedback =
        feedbackRepository
            .findBySession(session)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Feedback not found for session: " + sessionId));
    return DtoMapper.toFeedbackDto(feedback);
  }

  @Transactional
  public FeedbackDto submitFeedback(FeedbackSubmissionRequestDto request) {
    ValidationUtil.validateNotNull(request, "Feedback submission request");
    ValidationUtil.validateNotEmpty(request.getSessionId(), "Session ID");

    Session session =
        sessionRepository
            .findBySessionId(request.getSessionId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Session not found with id: " + request.getSessionId()));

    // Check if feedback already exists for this session
    feedbackRepository
        .findBySession(session)
        .ifPresent(
            f -> {
              throw new FeedbackException(
                  "Feedback already exists for session: " + request.getSessionId());
            });

    Feedback feedback = new Feedback();
    feedback.setFeedbackId("FB-" + UUID.randomUUID().toString());
    feedback.setSession(session);
    feedback.setRating(request.getRating());
    feedback.setComment(request.getComment());

    Feedback savedFeedback = feedbackRepository.save(feedback);
    return DtoMapper.toFeedbackDto(savedFeedback);
  }

  @Transactional
  public FeedbackDto updateFeedback(String feedbackId, FeedbackSubmissionRequestDto request) {
    ValidationUtil.validateNotEmpty(feedbackId, "Feedback ID");
    ValidationUtil.validateNotNull(request, "Feedback update request");

    Feedback feedback =
        feedbackRepository
            .findByFeedbackId(feedbackId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));

    feedback.setRating(request.getRating());
    feedback.setComment(request.getComment());

    Feedback updatedFeedback = feedbackRepository.save(feedback);
    return DtoMapper.toFeedbackDto(updatedFeedback);
  }

  @Transactional
  public void deleteFeedback(String feedbackId) {
    ValidationUtil.validateNotEmpty(feedbackId, "Feedback ID");

    Feedback feedback =
        feedbackRepository
            .findByFeedbackId(feedbackId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Feedback not found with id: " + feedbackId));

    feedbackRepository.delete(feedback);
  }

  @Transactional(readOnly = true)
  public Double getAverageRatingByTutorId(String tutorId) {
    ValidationUtil.validateNotEmpty(tutorId, "Tutor ID");

    Double avgRating = feedbackRepository.calculateAverageRatingByTutorId(tutorId);
    return avgRating != null ? avgRating : 0.0;
  }

  @Transactional(readOnly = true)
  public Long getFeedbackCountByTutorId(String tutorId) {
    ValidationUtil.validateNotEmpty(tutorId, "Tutor ID");

    return feedbackRepository.countByTutorId(tutorId);
  }

  @Transactional(readOnly = true)
  public List<FeedbackDto> getAllFeedback() {
    List<Feedback> feedbacks = feedbackRepository.findAll();
    return feedbacks.stream().map(DtoMapper::toFeedbackDto).collect(Collectors.toList());
  }
}
