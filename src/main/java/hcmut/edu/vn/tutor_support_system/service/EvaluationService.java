package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.EvaluationDto;
import hcmut.edu.vn.tutor_support_system.entity.Evaluation;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.EvaluationRepository;
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
public class EvaluationService {

  private final EvaluationRepository evaluationRepository;
  private final SessionRepository sessionRepository;

  @Transactional(readOnly = true)
  public EvaluationDto getEvaluationById(String evaluationId) {
    ValidationUtil.validateNotEmpty(evaluationId, "Evaluation ID");

    Evaluation evaluation =
        evaluationRepository
            .findByEvaluationId(evaluationId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Evaluation not found with id: " + evaluationId));
    return DtoMapper.toEvaluationDto(evaluation);
  }

  @Transactional(readOnly = true)
  public List<EvaluationDto> getEvaluationsByTutorId(String tutorId) {
    ValidationUtil.validateNotEmpty(tutorId, "Tutor ID");

    List<Evaluation> evaluations = evaluationRepository.findByTutorId(tutorId);
    return evaluations.stream().map(DtoMapper::toEvaluationDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public EvaluationDto getEvaluationBySessionId(String sessionId) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    Evaluation evaluation =
        evaluationRepository
            .findBySession(session)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Evaluation not found for session: " + sessionId));
    return DtoMapper.toEvaluationDto(evaluation);
  }

  @Transactional
  public EvaluationDto createEvaluation(String sessionId, UUID evaluatorId, String content) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");
    ValidationUtil.validateNotNull(evaluatorId, "Evaluator ID");
    ValidationUtil.validateNotEmpty(content, "Evaluation content");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    Evaluation evaluation = new Evaluation();
    evaluation.setEvaluationId("EV-" + UUID.randomUUID().toString());
    evaluation.setSession(session);
    User evaluator = new User();
    evaluator.setId(evaluatorId);
    evaluation.setEvaluator(evaluator);
    evaluation.setContent(content);

    Evaluation savedEvaluation = evaluationRepository.save(evaluation);
    return DtoMapper.toEvaluationDto(savedEvaluation);
  }

  @Transactional
  public EvaluationDto updateEvaluation(String evaluationId, String content) {
    ValidationUtil.validateNotEmpty(evaluationId, "Evaluation ID");
    ValidationUtil.validateNotEmpty(content, "Evaluation content");

    Evaluation evaluation =
        evaluationRepository
            .findByEvaluationId(evaluationId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Evaluation not found with id: " + evaluationId));

    evaluation.setContent(content);

    Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
    return DtoMapper.toEvaluationDto(updatedEvaluation);
  }

  @Transactional
  public void deleteEvaluation(String evaluationId) {
    ValidationUtil.validateNotEmpty(evaluationId, "Evaluation ID");

    Evaluation evaluation =
        evaluationRepository
            .findByEvaluationId(evaluationId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Evaluation not found with id: " + evaluationId));

    evaluationRepository.delete(evaluation);
  }

  @Transactional(readOnly = true)
  public List<EvaluationDto> getAllEvaluations() {
    List<Evaluation> evaluations = evaluationRepository.findAll();
    return evaluations.stream().map(DtoMapper::toEvaluationDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Long getEvaluationCountByEvaluatorId(UUID evaluatorId) {
    ValidationUtil.validateNotNull(evaluatorId, "Evaluator ID");

    return evaluationRepository.countByEvaluatorId(evaluatorId);
  }
}
