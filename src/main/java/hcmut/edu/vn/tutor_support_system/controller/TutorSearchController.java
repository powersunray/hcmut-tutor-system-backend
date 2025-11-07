package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorProfileDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorSearchResultDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import hcmut.edu.vn.tutor_support_system.service.TutorSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorSearchController {

    private final TutorSearchService tutorSearchService;
    private final TutorRepository tutorRepository;

    /**
     * UC-4 steps 1–5 (+ 5a):
     * Student opens "Find a tutor" page -> frontend calls this endpoint with filters.
     */
    @GetMapping("/search")
    public ResponseEntity<List<TutorSearchResultDto>> searchTutors(
            @RequestParam(required = false) String course,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String campus,
            @RequestParam(required = false) SessionMode mode,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false, defaultValue = "false") boolean useAi
    ) {
        List<TutorSearchResultDto> results = tutorSearchService.searchTutors(
                course, name, campus, mode, minRating, useAi);

        // UC-4 alt flow 5a – "No matches found" -> empty list, frontend shows message + "Reset filters".
        return ResponseEntity.ok(results);
    }

    /**
     * UC-4 step 6 (+ exception 6a):
     * Student wants to open a tutor profile.
     */
    @GetMapping("/{tutorId}")
    public ResponseEntity<TutorProfileDto> getTutorProfile(@PathVariable String tutorId) {
        Tutor t = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

        List<Availability> slots = tutorRepository.findAvailabilitiesByTutorId(t.getTutorId());

        TutorProfileDto dto = TutorProfileDto.builder()
                .tutorId(t.getTutorId())
                .tutorName(t.getFirstName())
                .tutorName(t.getLastName())
                .email(t.getEmail())
                .campus(t.getProfile() != null ? t.getProfile().getCampus() : null)
                .rating(t.getAverageRating())
                .ratingCount(t.getRatingCount())
                .expertiseAreas(t.getExpertiseAreas())
                .availableSlots(
                        slots.stream().map(a -> AvailabilityDto.builder()
                                .availabilityId(a.getId())
                                .dayOfWeek(a.getDayOfWeek().name())
                                .startTime(a.getStartTime().toString())
                                .endTime(a.getEndTime().toString())
                                .mode(a.getMode())
                                .locationOrLink(a.getLocationOrLink())
                                .build()
                        ).toList()
                )
                .build();

        return ResponseEntity.ok(dto);
    }
}
