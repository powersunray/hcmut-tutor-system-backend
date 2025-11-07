package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.SessionBookingRequest;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.service.SessionBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SessionBookingController {

    private final SessionBookingService sessionBookingService;

    /**
     * UC-5 step 2 (+ alt 2a):
     * Student clicks "Book session" -> system shows available slots for the selected tutor.
     */
    @GetMapping("/tutors/{tutorId}/slots")
    public ResponseEntity<List<Availability>> getAvailableSlots(@PathVariable String tutorId) {
        List<Availability> slots = sessionBookingService.getAvailableSlots(tutorId);
        return ResponseEntity.ok(slots);
    }

    /**
     * UC-5 steps 3–7:
     * Student selects a slot and confirms booking.
     */
    @PostMapping("/tutors/{tutorId}/sessions")
    public ResponseEntity<SessionResponseDto> bookSession(
            @PathVariable String tutorId,
            @RequestBody SessionBookingRequest request
    ) {
        SessionResponseDto response = sessionBookingService.bookSession(tutorId, request);
        return ResponseEntity.ok(response);
    }
}
