package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SessionBookingRequest;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.*;
import hcmut.edu.vn.tutor_support_system.exception.InvalidSessionDetailsException;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.exception.SlotUnavailableException;
import hcmut.edu.vn.tutor_support_system.repository.AvailabilityRepository;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SessionBookingService {

  private final TutorRepository tutorRepository;
  private final StudentRepository studentRepository;
  private final SessionRepository sessionRepository;
  private final AvailabilityRepository availabilityRepository;

  /** UC-5 step 2 (+ alt 2a): show available slots for a tutor. */
  @Transactional(readOnly = true)
  public List<Availability> getAvailableSlots(String tutorId) {
    // Preconditions PRE-3 & PRE-4 are checked by ensuring tutor exists and has
    // slots
    log.info("Fetching available slots for tutor: {}", tutorId);
    Tutor tutor =
        tutorRepository
            .findByTutorId(tutorId)
            .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

    List<Availability> slots = availabilityRepository.findByTutorAndPublishedTrue(tutor);
    log.info("Found {} available slots for tutor: {}", slots.size(), tutorId);
    return slots;
  }

  /** UC-5 steps 3–7: booking a session after the student picks a slot. */
  public SessionResponseDto bookSession(String tutorId, SessionBookingRequest request) {
    log.info("Booking session for tutor: {}, student: {}", tutorId, request.getStudentId());

    // PRE-2/3: student profile authenticated + selected tutor (authentication via
    // SSO is outside this service)
    Student student =
        studentRepository
            .findByStudentId(request.getStudentId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Student not found: " + request.getStudentId()));

    Tutor tutor =
        tutorRepository
            .findByTutorId(tutorId)
            .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

    // Step 3: user selects a preferred slot
    Availability availability =
        availabilityRepository
            .findByAvailabilityId(request.getAvailabilityId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Availability slot not found: " + request.getAvailabilityId()));

    // Exception 3a: missing or invalid session details (time, location, mode)
    if (availability.getStartTime() == null
        || availability.getEndTime() == null
        || availability.getMode() == null
        || availability.getLocationOrLink() == null) {
      throw new InvalidSessionDetailsException("Slot has invalid or incomplete details.");
    }

    // Handle alt 2a: when availability supports both modes (HYBRID), we use
    // student's preferred mode
    SessionMode finalMode = availability.getMode();
    if (availability.getMode() == SessionMode.HYBRID && request.getPreferredMode() != null) {
      finalMode = request.getPreferredMode();
    }

    // Exception 4b: multiple booking / overlapping
    // For MVP we simply check whether another session with the same tutor overlaps
    // exactly in time.
    for (Session existing : sessionRepository.findByTutor(tutor)) {
      boolean sameSlot =
          existing
                  .getStartTime()
                  .equals(toDateTime(availability.getDayOfWeek(), availability.getStartTime()))
              && existing
                  .getEndTime()
                  .equals(toDateTime(availability.getDayOfWeek(), availability.getEndTime()));
      if (sameSlot) {
        throw new SlotUnavailableException("Slot unavailable: already booked.");
      }
    }

    // Decide status:
    // Alt 4a: if tutor requires confirmation, mark as PENDING_TUTOR_APPROVAL
    boolean tutorRequiresConfirmation = true; // for demo; later can be field in Tutor
    SessionStatus status =
        tutorRequiresConfirmation ? SessionStatus.PENDING : SessionStatus.CONFIRMED;

    // Step 5: create session record
    Session session = new Session();
//    session.setId(UUID.randomUUID());
    session.setSessionId(UUID.randomUUID().toString());
    session.setTutor(tutor);
    session.setStudent(student);
    session.setTitle("Tutoring session with " + tutor.getFirstName() + " " + tutor.getLastName());

    LocalDateTime start = toDateTime(availability.getDayOfWeek(), availability.getStartTime());
    LocalDateTime end = toDateTime(availability.getDayOfWeek(), availability.getEndTime());

    session.setStartTime(start);
    session.setEndTime(end);
    session.setLocationOrLink(availability.getLocationOrLink());
    session.setMode(finalMode);
    session.setStatus(status);

    Session savedSession = sessionRepository.save(session);
    log.info("Session created with ID: {}", savedSession.getSessionId());

    // Steps 6 & 7: update calendars + send notifications (MVP: stub methods)
    updateCalendars(session);
    sendBookingNotifications(session);

    return toSessionResponseDto(session);
  }

  private LocalDateTime toDateTime(java.time.DayOfWeek dayOfWeek, java.time.LocalTime time) {
    // For MVP we just map to "next" occurrence in the current week.
    LocalDateTime now = LocalDateTime.now();
    java.time.LocalDate date = now.toLocalDate();
    while (date.getDayOfWeek() != dayOfWeek) {
      date = date.plusDays(1);
    }
    return LocalDateTime.of(date, time);
  }

  private void updateCalendars(Session session) {
    log.info("Updating calendars for session: {}", session.getId());
  }

  private void sendBookingNotifications(Session session) {
    log.info("Sending booking notifications for session: {}", session.getId());
  }

  private SessionResponseDto toSessionResponseDto(Session session) {
    return SessionResponseDto.builder()
        .sessionId(session.getId())
        .tutorId(session.getTutor().getTutorId())
        .tutorName(session.getTutor().getFirstName())
        .tutorName(session.getTutor().getLastName())
        .studentId(session.getStudent().getStudentId())
        .studentName(session.getStudent().getFirstName())
        .studentName(session.getStudent().getLastName())
        .startTime(session.getStartTime())
        .endTime(session.getEndTime())
        .mode(session.getMode())
        .locationOrLink(session.getLocationOrLink())
        .status(session.getStatus())
        .build();
  }
}
