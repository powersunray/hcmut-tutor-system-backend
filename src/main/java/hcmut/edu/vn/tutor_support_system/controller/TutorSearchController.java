package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.TutorProfileDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorSearchResultDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
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
        return ResponseEntity.ok(results);
    }

    // UC-4 step 6: return a DTO profile (no cycles)
    @GetMapping("/{tutorId}")
    public ResponseEntity<TutorProfileDto> getTutorProfile(@PathVariable String tutorId) {
        Tutor t = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

        // Use the SAME ID getter your repo expects (tutorId vs id).
        // If your repo uses the string passed in the path, just reuse `tutorId`.
        List<Availability> slots = tutorRepository.findAvailabilitiesByTutorId(tutorId);

        String fullName = (t.getFirstName() != null ? t.getFirstName() : "")
                + (t.getLastName() != null ? " " + t.getLastName() : "");

        TutorProfileDto dto = TutorProfileDto.builder()
                .tutorId(tutorId)
                .tutorName(fullName.trim())
                .email(t.getEmail())
                .campus(t.getProfile() != null ? t.getProfile().getCampus() : null)
                .rating(t.getAverageRating())
                .ratingCount(t.getRatingCount())
                .expertiseAreas(t.getExpertiseAreas())
                .availableSlots(
                        slots.stream()
                                .map(DtoMapper::toAvailabilityDto)
                                .toList()
                )
                .build();

        return ResponseEntity.ok(dto);
    }
}