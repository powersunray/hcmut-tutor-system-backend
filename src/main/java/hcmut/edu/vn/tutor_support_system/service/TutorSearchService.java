package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorSearchResultDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.repository.AvailabilityRepository;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TutorSearchService {

  private final TutorRepository tutorRepository;
  private final AvailabilityRepository availabilityRepository;
  private final StudentRepository studentRepository;
  private final AIMatchingService aiMatchingService;

  /**
   * UC-4: Tutor Search & Intelligent Matching (Browse + AI Recommendation)
   *
   * <p>This method corresponds to: - Step 3: student sets filters + optional "Use AI" checkbox -
   * Step 4: system runs search; AI part will be added later - Step 5/5a: ranked list or "no
   * matches"
   */
  public List<TutorSearchResultDto> searchTutors(
      String courseCode,
      String tutorName,
      String campus,
      SessionMode mode,
      Double minRating,
      boolean useAiRecommendations) {
    String modeStr = (mode != null) ? mode.name() : null; 
    List<Tutor> filtered =
        tutorRepository.searchTutors(tutorName, courseCode, campus, minRating, modeStr);

    if (filtered.isEmpty()) {
      return Collections.emptyList();
    }

    // Default sorting by rating
    if (!useAiRecommendations) {
      filtered.sort(Comparator.comparingDouble(Tutor::getAverageRating).reversed());
    }

    List<TutorSearchResultDto> result = new ArrayList<>();
    for (Tutor tutor : filtered) {
      List<Availability> slots = availabilityRepository.findByTutorAndPublishedTrue(tutor);

      List<AvailabilityDto> slotDtos =
          slots.stream().map(this::toAvailabilityDto).collect(Collectors.toList());

      String campusValue = tutor.getProfile() != null ? tutor.getProfile().getCampus() : null;

      String whyRecommended = null;
      // Note: Full AI recommendation requires context of "current user" which we don't have passed in here
      // except implicitly if we had authentication context.
      // For now, recommendation sorting logic is applied in separate method or if user info was available.

      TutorSearchResultDto dto =
          TutorSearchResultDto.builder()
              .tutorId(tutor.getTutorId())
              .tutorName(tutor.getFirstName() + " " + tutor.getLastName())
              .campus(campusValue)
              .courses(tutor.getExpertiseAreas())
              .rating(tutor.getAverageRating())
              .availableSlots(slotDtos)
              .whyRecommended(whyRecommended)
              .build();

      result.add(dto);
    }

    return result;
  }
  
  public List<TutorSearchResultDto> recommendTutors(String studentId, String subjectId) {
      Student student = studentRepository.findByStudentId(studentId)
          .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
      
      // Get all tutors or filter by subject initially
      List<Tutor> candidates = tutorRepository.searchTutors(null, subjectId, null, null, null);
      
      List<Tutor> recommended = aiMatchingService.recommendTutors(student, candidates, subjectId);
      
      return recommended.stream().map(tutor -> {
          List<Availability> slots = availabilityRepository.findByTutorAndPublishedTrue(tutor);
          List<AvailabilityDto> slotDtos = slots.stream().map(this::toAvailabilityDto).collect(Collectors.toList());
          String campusValue = tutor.getProfile() != null ? tutor.getProfile().getCampus() : null;

          return TutorSearchResultDto.builder()
              .tutorId(tutor.getTutorId())
              .tutorName(tutor.getFirstName() + " " + tutor.getLastName())
              .campus(campusValue)
              .courses(tutor.getExpertiseAreas())
              .rating(tutor.getAverageRating())
              .availableSlots(slotDtos)
              .whyRecommended("Recommended based on your profile and search.")
              .build();
      }).collect(Collectors.toList());
  }

  private AvailabilityDto toAvailabilityDto(Availability a) {
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    return AvailabilityDto.builder()
        .availabilityId(a.getId())
        .dayOfWeek(a.getDayOfWeek().name())
        .startTime(a.getStartTime().format(timeFormatter))
        .endTime(a.getEndTime().format(timeFormatter))
        .mode(a.getMode())
        .locationOrLink(a.getLocationOrLink())
        .build();
  }
}
