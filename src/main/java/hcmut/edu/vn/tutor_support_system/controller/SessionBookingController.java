package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.SessionBookingRequest;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.service.SessionBookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SessionBookingController {

  private final SessionBookingService sessionBookingService;

  // UC-5 step 2 (+ alt 2a): return DTOs to avoid cycles
  @GetMapping("/tutors/{tutorId}/slots")
  public ResponseEntity<List<AvailabilityDto>> getAvailableSlots(@PathVariable String tutorId) {
    List<Availability> slots = sessionBookingService.getAvailableSlots(tutorId);
    List<AvailabilityDto> dtos = slots.stream().map(DtoMapper::toAvailabilityDto).toList();
    return ResponseEntity.ok(dtos);
  }

  // UC-5 steps 3–7
  @PostMapping("/tutors/{tutorId}/sessions")
  public ResponseEntity<SessionResponseDto> bookSession(
      @PathVariable String tutorId, @RequestBody SessionBookingRequest request) {
    SessionResponseDto response = sessionBookingService.bookSession(tutorId, request);
    return ResponseEntity.ok(response);
  }
}
