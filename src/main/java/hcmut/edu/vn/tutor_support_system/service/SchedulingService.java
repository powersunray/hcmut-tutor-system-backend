package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SchedulingService {

  private final AvailabilityRepository availabilityRepository;

  public List<Availability> getAvailableSlotsForTutor(Availability availability) {
    log.info(
        "Fetching available slots for availability: {}",
        availability.getAvailabilityId());
    return availabilityRepository
        .findByAvailabilityId(availability.getAvailabilityId())
        .stream()
        .toList();
  }

  public List<Availability> getAvailableSlotsByDateRange(
      Availability availability, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    log.info(
        "Fetching available slots for date range: {} to {}",
        startDateTime,
        endDateTime);
    LocalTime startTime = startDateTime.toLocalTime();
    LocalTime endTime = endDateTime.toLocalTime();
    return availabilityRepository
        .findByAvailabilityId(availability.getAvailabilityId())
        .stream()
        .filter(
            a ->
                !a.getStartTime().isBefore(startTime)
                    && !a.getEndTime().isAfter(endTime))
        .toList();
  }

  public List<Availability> getAllAvailableSlots() {
    log.info("Fetching all available slots");
    List<Availability> slots =
        availabilityRepository.findAll().stream()
            .filter(a -> a.getPublished() != null && a.getPublished())
            .toList();
    log.info("Found {} published slots", slots.size());
    return slots;
  }

  public boolean isSlotAvailable(Availability availability) {
    boolean isAvailable = availability.getPublished() != null && availability.getPublished();
    log.info("Slot {} availability status: {}", availability.getId(), isAvailable);
    return isAvailable;
  }
}