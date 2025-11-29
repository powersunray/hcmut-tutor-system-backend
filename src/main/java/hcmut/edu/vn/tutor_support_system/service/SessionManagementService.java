package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionUpdateRequest;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.SessionNotFoundException;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionManagementService {

  private final SessionRepository sessionRepository;
  private final TutorRepository tutorRepository;
  private final StudentRepository studentRepository;

  public SessionResponseDto getSessionById(UUID sessionId) {
    log.info("Fetching session with ID: {}", sessionId);
    Session session = sessionRepository
        .findById(sessionId)
        .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));
    return convertToDto(session);
  }

  public List<SessionResponseDto> getSessionsByTutorId(String tutorId) {
    log.info("Fetching sessions for tutor: {}", tutorId);
    Tutor tutor = tutorRepository
        .findByTutorId(tutorId)
        .orElseThrow(() -> new SessionNotFoundException("Tutor not found with ID: " + tutorId));
    return sessionRepository.findByTutor(tutor).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public List<SessionResponseDto> getSessionsByStudentId(String studentId) {
    log.info("Fetching sessions for student: {}", studentId);
    Student student = studentRepository
        .findByStudentId(studentId)
        .orElseThrow(() -> new SessionNotFoundException("Student not found with ID: " + studentId));
    return sessionRepository.findByStudent(student).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public SessionResponseDto updateSessionStatus(UUID sessionId, SessionStatus newStatus) {
    log.info("Updating session {} status to {}", sessionId, newStatus);
    Session session = sessionRepository
        .findById(sessionId)
        .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));

    session.setStatus(newStatus);
    Session updatedSession = sessionRepository.save(session);
    return convertToDto(updatedSession);
  }

  @Transactional
  public SessionResponseDto updateSession(UUID sessionId, SessionUpdateRequest request) {
    log.info("Updating session: {}", sessionId);
    Session session = sessionRepository
        .findById(sessionId)
        .orElseThrow(() -> new SessionNotFoundException("Session not found with ID: " + sessionId));

    if (request.getMode() != null) {
      session.setMode(request.getMode());
    }
    if (request.getLocationOrLink() != null) {
      session.setLocationOrLink(request.getLocationOrLink());
    }

    Session updatedSession = sessionRepository.save(session);
    return convertToDto(updatedSession);
  }

  private SessionResponseDto convertToDto(Session session) {
    return SessionResponseDto.builder()
        .sessionId(session.getSessionId())
        .tutorId(session.getTutor().getTutorId())
        .tutorName(session.getTutor().getFirstName() + " " + session.getTutor().getLastName())
        .studentId(session.getStudent().getStudentId())
        .studentName(session.getStudent().getFirstName() + " " + session.getStudent().getLastName())
        .startTime(session.getStartTime())
        .endTime(session.getEndTime())
        .mode(session.getMode())
        .locationOrLink(session.getLocationOrLink())
        .status(session.getStatus())
        .build();
  }
}