package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.SessionRescheduleRequestDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionUpdateRequestDto;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import hcmut.edu.vn.tutor_support_system.exception.SessionNotFoundException;
import hcmut.edu.vn.tutor_support_system.service.SessionManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Slf4j
public class SessionManagementController {

  private final SessionManagementService sessionManagementService;

  @GetMapping("/{sessionId}")
  public ResponseEntity<SessionResponseDto> getSessionById(@PathVariable String sessionId) {
    log.info("GET request for session: {}", sessionId);
    // Try UUID first, then fallback to session_id lookup
    try {
      return ResponseEntity.ok(sessionManagementService.getSessionById(UUID.fromString(sessionId)));
    } catch (IllegalArgumentException e) {
      // sessionId is not a UUID, try looking up by session_id string
      return ResponseEntity.ok(sessionManagementService.getSessionBySessionId(sessionId));
    }
  }

  @GetMapping("/tutor/{tutorId}")
  public ResponseEntity<List<SessionResponseDto>> getSessionsByTutorId(
          @PathVariable String tutorId) {
    log.info("GET /sessions/tutor/{}", tutorId);
    List<SessionResponseDto> response = sessionManagementService.getSessionsByTutorId(tutorId);
    log.info("Returning {} sessions for tutor: {}", response.size(), tutorId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/student/{studentId}")
  public ResponseEntity<List<SessionResponseDto>> getSessionsByStudentId(
          @PathVariable String studentId) {
    log.info("GET /sessions/student/{}", studentId);
    List<SessionResponseDto> response = sessionManagementService.getSessionsByStudentId(studentId);
    log.info("Returning {} sessions for student: {}", response.size(), studentId);
    return ResponseEntity.ok(response);
  }

//  @PutMapping("/{sessionId}")
//  public ResponseEntity<SessionResponseDto> updateSession(
//      @PathVariable String sessionId,
//      @RequestBody SessionUpdateRequest request) {
//    log.info("PUT request to update session: {}", sessionId);
//    SessionResponseDto response = sessionManagementService.updateSession(UUID.fromString(sessionId), request);
//    return ResponseEntity.ok(response);
//  }

  @PutMapping("/{sessionId}")
  public ResponseEntity<SessionResponseDto> updateSession(
      @PathVariable String sessionId,
      @RequestBody SessionUpdateRequestDto request) {
    log.info("PUT request to update session: {}", sessionId);
    try {
      // Try UUID first
      SessionResponseDto response = sessionManagementService.updateSession(
          UUID.fromString(sessionId), request);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      // If not UUID, it's a session_id string - but service expects UUID
      // This flow is incomplete - needs fallback handling
      throw new SessionNotFoundException("Invalid session ID format: " + sessionId);
    }
  }

  @PutMapping("/{sessionId}/status")
  public ResponseEntity<SessionResponseDto> updateSessionStatus(
      @PathVariable String sessionId,
      @RequestParam SessionStatus status) {
    log.info("PUT request to update session status: {} to {}", sessionId, status);
    SessionResponseDto response = sessionManagementService.updateSessionStatus(UUID.fromString(sessionId), status);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{sessionId}/reschedule")
  public ResponseEntity<SessionResponseDto> rescheduleSession(
      @PathVariable String sessionId,
      @RequestBody SessionRescheduleRequestDto request) {
    log.info("PUT /sessions/{}/reschedule", sessionId);
    SessionResponseDto response = sessionManagementService.rescheduleSession(
        UUID.fromString(sessionId), request);
    return ResponseEntity.ok(response);
  }
}