package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SessionRescheduleRequestDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionUpdateRequestDto;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.SessionNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SessionManagementService {

  private final SessionRepository sessionRepository;
  private final TutorRepository tutorRepository;
  private final StudentRepository studentRepository;

  public SessionManagementService(
      SessionRepository sessionRepository,
      TutorRepository tutorRepository,
      StudentRepository studentRepository) {
    this.sessionRepository = sessionRepository;
    this.tutorRepository = tutorRepository;
    this.studentRepository = studentRepository;
  }

  @Transactional(readOnly = true)
  public SessionResponseDto getSessionById(UUID sessionId) {
    log.info("Fetching session with ID: {}", sessionId);
    Session session =
        sessionRepository
            .findById(sessionId)
            .orElseThrow(
                () ->
                    new SessionNotFoundException(
                        "Session not found with ID: " + sessionId));
    return DtoMapper.toSessionResponseDto(session);
  }

  @Transactional(readOnly = true)
  public SessionResponseDto getSessionBySessionId(String sessionId) {
    Session session = sessionRepository.findBySessionId(sessionId)
        .orElseThrow(() -> new SessionNotFoundException("Session not found: " + sessionId));
    return DtoMapper.toSessionResponseDto(session);
  }

  @Transactional(readOnly = true)
  public List<SessionResponseDto> getSessionsByTutorId(String tutorId) {
    log.info("Fetching sessions for tutor: {}", tutorId);
    Tutor tutor =
        tutorRepository
            .findByTutorId(tutorId)
            .orElseThrow(
                () -> new SessionNotFoundException("Tutor not found with ID: " + tutorId));
    List<Session> sessions = sessionRepository.findByTutor(tutor);
    log.info("Found {} sessions for tutor: {}", sessions.size(), tutorId);
    return sessions.stream().map(DtoMapper::toSessionResponseDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SessionResponseDto> getSessionsByStudentId(String studentId) {
    log.info("Fetching sessions for student: {}", studentId);
    Student student =
        studentRepository
            .findByStudentId(studentId)
            .orElseThrow(
                () ->
                    new SessionNotFoundException(
                        "Student not found with ID: " + studentId));
    List<Session> sessions = sessionRepository.findByStudent(student);
    log.info("Found {} sessions for student: {}", sessions.size(), studentId);
    return sessions.stream().map(DtoMapper::toSessionResponseDto).collect(Collectors.toList());
  }

  public SessionResponseDto updateSessionStatus(UUID sessionId, SessionStatus newStatus) {
    log.info("Updating session {} status to {}", sessionId, newStatus);
    Session session =
        sessionRepository
            .findById(sessionId)
            .orElseThrow(
                () ->
                    new SessionNotFoundException(
                        "Session not found with ID: " + sessionId));

    session.setStatus(newStatus);
    Session updatedSession = sessionRepository.save(session);
    log.info("Session {} status updated to {}", sessionId, newStatus);
    return DtoMapper.toSessionResponseDto(updatedSession);
  }

  public SessionResponseDto updateSession(UUID sessionId, SessionUpdateRequestDto request) {
    log.info("Updating session: {}", sessionId);
    Session session =
        sessionRepository
            .findById(sessionId)
            .orElseThrow(
                () ->
                    new SessionNotFoundException(
                        "Session not found with ID: " + sessionId));

    if (request.getMode() != null) {
      session.setMode(request.getMode());
      log.info("Session {} mode updated to {}", sessionId, request.getMode());
    }
    if (request.getLocationOrLink() != null) {
      session.setLocationOrLink(request.getLocationOrLink());
      log.info("Session {} location/link updated", sessionId);
    }

    Session updatedSession = sessionRepository.save(session);
    return DtoMapper.toSessionResponseDto(updatedSession);
  }

  public SessionResponseDto rescheduleSession(UUID sessionId, SessionRescheduleRequestDto request) {
    log.info("Rescheduling session: {} to availability: {}", sessionId, request.getAvailabilityId());
    Session session = sessionRepository.findById(sessionId)
        .orElseThrow(() -> new SessionNotFoundException("Session not found: " + sessionId));

    session.setStatus(SessionStatus.PENDING);
    Session updated = sessionRepository.save(session);

    log.info("Session rescheduled: {}", sessionId);
    return DtoMapper.toSessionResponseDto(updated);
  }
}