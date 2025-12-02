package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SchedulingService {

  private final AvailabilityRepository availabilityRepository;

  public Optional<Availability> getAvailabilityById(String availabilityId) {
    log.info("Fetching availability for availabilityId: {}", availabilityId);
    return availabilityRepository.findByAvailabilityId(availabilityId);
  }

  public List<Availability> getAvailableSlotsByDateRange(
      LocalDateTime startDateTime, LocalDateTime endDateTime) {
    log.info(
        "Fetching available slots for date range: {} to {}",
        startDateTime,
        endDateTime);
    return availabilityRepository.findAll().stream()
        .filter(a -> {
          // Convert recurring weekly slot to actual date range
          LocalDate startDate = startDateTime.toLocalDate();
          LocalDate endDate = endDateTime.toLocalDate();

          // Check if the slot's day of week falls within the date range
          LocalDate current = startDate;
          while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek() == a.getDayOfWeek()) {
              LocalDateTime slotDateTime = LocalDateTime.of(current, a.getStartTime());
              // Check if slot is within the requested range
              if (!slotDateTime.isBefore(startDateTime) && !slotDateTime.isAfter(endDateTime)) {
                return true;
              }
            }
            current = current.plusDays(1);
          }
          return false;
        })
        .toList();
  }

  public List<Availability> getAllAvailableSlots() {
    log.info("Fetching all available slots");
    List<Availability> slots = availabilityRepository.findAll().stream()
        .filter(a -> a.getPublished() != null && a.getPublished())
        .toList();
    log.info("Found {} published slots", slots.size());
    return slots;
  }

  public boolean isSlotAvailable(Availability availability) {
    boolean isAvailable = availability.getPublished() != null && availability.getPublished();
    log.info("Slot {} availability status: {}", availability.getAvailabilityId(), isAvailable);
    return isAvailable;
  }
}