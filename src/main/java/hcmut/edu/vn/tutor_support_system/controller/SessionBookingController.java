package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionBookingRequestDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.service.SessionBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SessionBookingController {

  private final SessionBookingService sessionBookingService;

  @GetMapping("/tutors/{tutorId}/slots")
  public ResponseEntity<List<AvailabilityDto>> getAvailableSlots(
      @PathVariable String tutorId) {
    log.info("GET /tutors/{}/slots", tutorId);
    List<Availability> slots = sessionBookingService.getAvailableSlots(tutorId);
    List<AvailabilityDto> dtos =
        slots.stream().map(DtoMapper::toAvailabilityDto).toList();
    log.info("Returning {} slots", dtos.size());
    return ResponseEntity.ok(dtos);
  }

  @PostMapping("/tutors/{tutorId}/sessions")
  public ResponseEntity<SessionResponseDto> bookSession(
      @PathVariable String tutorId,
      @RequestBody SessionBookingRequestDto request) {
    log.info("POST /tutors/{}/sessions - student: {}", tutorId, request.getStudentId());
    SessionResponseDto response = sessionBookingService.bookSession(tutorId, request);
    log.info("Session booked: {}", response.getSessionId());
    return ResponseEntity.ok(response);
  }
}