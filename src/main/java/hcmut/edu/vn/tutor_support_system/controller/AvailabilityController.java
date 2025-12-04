package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.AvailabilityNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.AvailabilityRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import jakarta.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class AvailabilityController {

  private final AvailabilityRepository availabilityRepository;
  private final TutorRepository tutorRepository;

  // GET /api/availabilities/tutor/{tutorId} - Get all availabilities for a tutor
  @GetMapping("/tutor/{tutorId}")
  public ResponseEntity<List<AvailabilityDto>> getTutorAvailabilities(
      @PathVariable String tutorId) {
    Tutor tutor =
        tutorRepository
            .findByTutorId(tutorId)
            .orElseThrow(() -> new RuntimeException("Tutor not found: " + tutorId));

    List<Availability> availabilities = availabilityRepository.findByTutor(tutor);
    List<AvailabilityDto> dtos =
        availabilities.stream().map(DtoMapper::toAvailabilityDto).toList();

    return ResponseEntity.ok(dtos);
  }

  // POST /api/availabilities - Create new availability
  @PostMapping
  public ResponseEntity<AvailabilityDto> createAvailability(
      @Valid @RequestBody CreateAvailabilityRequest request) {
    Tutor tutor =
        tutorRepository
            .findByTutorId(request.getTutorId())
            .orElseThrow(
                () -> new RuntimeException("Tutor not found: " + request.getTutorId()));

    // Validation
    LocalTime startTime = LocalTime.parse(request.getStartTime());
    LocalTime endTime = LocalTime.parse(request.getEndTime());
    if (!startTime.isBefore(endTime)) {
      throw new IllegalArgumentException("Start time must be before end time");
    }
    if (request.getCapacity() <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0");
    }

    Availability availability = new Availability();
    availability.setAvailabilityId("slot-" + UUID.randomUUID().toString().substring(0, 8));
    availability.setTutor(tutor);
    availability.setDayOfWeek(DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase()));
    availability.setStartTime(startTime);
    availability.setEndTime(endTime);
    availability.setMode(SessionMode.valueOf(request.getMode().toUpperCase()));
    availability.setLocationOrLink(request.getLocationOrLink());
    availability.setCapacity(request.getCapacity());
    availability.setPublished(false); // Default to unpublished

    Availability saved = availabilityRepository.save(availability);
    return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toAvailabilityDto(saved));
  }

  // PUT /api/availabilities/{id} - Update availability
  @PutMapping("/{id}")
  public ResponseEntity<AvailabilityDto> updateAvailability(
      @PathVariable UUID id, @Valid @RequestBody UpdateAvailabilityRequest request) {
    Availability availability =
        availabilityRepository
            .findById(id)
            .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found: " + id));

    // Partial update - only update provided fields
    if (request.getDayOfWeek() != null) {
      availability.setDayOfWeek(DayOfWeek.valueOf(request.getDayOfWeek().toUpperCase()));
    }
    if (request.getStartTime() != null) {
      availability.setStartTime(LocalTime.parse(request.getStartTime()));
    }
    if (request.getEndTime() != null) {
      availability.setEndTime(LocalTime.parse(request.getEndTime()));
    }
    if (request.getMode() != null) {
      availability.setMode(SessionMode.valueOf(request.getMode().toUpperCase()));
    }
    if (request.getLocationOrLink() != null) {
      availability.setLocationOrLink(request.getLocationOrLink());
    }
    if (request.getCapacity() != null) {
      if (request.getCapacity() <= 0) {
        throw new IllegalArgumentException("Capacity must be greater than 0");
      }
      availability.setCapacity(request.getCapacity());
    }

    // Validate times if both are present
    if (!availability.getStartTime().isBefore(availability.getEndTime())) {
      throw new IllegalArgumentException("Start time must be before end time");
    }

    Availability updated = availabilityRepository.save(availability);
    return ResponseEntity.ok(DtoMapper.toAvailabilityDto(updated));
  }

  // DELETE /api/availabilities/{id} - Delete availability
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAvailability(@PathVariable UUID id) {
    if (!availabilityRepository.existsById(id)) {
      throw new AvailabilityNotFoundException("Availability not found: " + id);
    }
    availabilityRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  // PUT /api/availabilities/{id}/publish - Publish availability
  @PutMapping("/{id}/publish")
  public ResponseEntity<AvailabilityDto> publishAvailability(@PathVariable UUID id) {
    Availability availability =
        availabilityRepository
            .findById(id)
            .orElseThrow(() -> new AvailabilityNotFoundException("Availability not found: " + id));

    availability.setPublished(true);
    Availability updated = availabilityRepository.save(availability);
    return ResponseEntity.ok(DtoMapper.toAvailabilityDto(updated));
  }

  // DTOs for request bodies
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CreateAvailabilityRequest {
    private String tutorId;
    private String dayOfWeek; // MONDAY, TUESDAY, etc.
    private String startTime; // HH:mm format
    private String endTime; // HH:mm format
    private String mode; // ONLINE, OFFLINE, HYBRID
    private String locationOrLink;
    private Integer capacity;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateAvailabilityRequest {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String mode;
    private String locationOrLink;
    private Integer capacity;
  }
}
