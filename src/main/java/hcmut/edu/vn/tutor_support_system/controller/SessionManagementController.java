package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionUpdateRequest;
import hcmut.edu.vn.tutor_support_system.entity.SessionStatus;
import hcmut.edu.vn.tutor_support_system.service.SessionManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    SessionResponseDto response = sessionManagementService.getSessionById(UUID.fromString(sessionId));
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{sessionId}")
  public ResponseEntity<SessionResponseDto> updateSession(
      @PathVariable String sessionId,
      @RequestBody SessionUpdateRequest request) {
    log.info("PUT request to update session: {}", sessionId);
    SessionResponseDto response = sessionManagementService.updateSession(UUID.fromString(sessionId), request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{sessionId}/status")
  public ResponseEntity<SessionResponseDto> updateSessionStatus(
      @PathVariable String sessionId,
      @RequestParam SessionStatus status) {
    log.info("PUT request to update session status: {} to {}", sessionId, status);
    SessionResponseDto response = sessionManagementService.updateSessionStatus(UUID.fromString(sessionId), status);
    return ResponseEntity.ok(response);
  }
}