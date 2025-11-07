package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorSearchResultDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TutorSearchService {

    private final TutorRepository tutorRepository;

    /**
     * UC-4: Tutor Search & Intelligent Matching (Browse + AI Recommendation)
     *
     * This method corresponds to:
     * - Step 3: student sets filters + optional "Use AI" checkbox
     * - Step 4: system runs search; AI part will be added later
     * - Step 5/5a: ranked list or "no matches"
     */
    public List<TutorSearchResultDto> searchTutors(
            String courseCode,
            String tutorName,
            String campus,
            SessionMode mode,
            Double minRating,
            boolean useAiRecommendations
    ) {
        List<Tutor> tutors = tutorRepository.findAll();

        // Basic filter by text & rating (UC-4 step 4, non-AI search)
        List<Tutor> filtered = tutors.stream()
                .filter(t -> courseCode == null
                        || t.getExpertiseAreas() != null
                        && t.getExpertiseAreas().stream()
                        .anyMatch(c -> c.toLowerCase().contains(courseCode.toLowerCase())))
                .filter(t -> tutorName == null
                        || t.getFirstName().toLowerCase().contains(tutorName.toLowerCase()))
                .filter(t -> campus == null
                        || (t.getProfile() != null
                        && campus.equalsIgnoreCase(t.getProfile().getCampus())))
                .filter(t -> minRating == null
                        || t.getAverageRating() >= minRating)
                .collect(Collectors.toList());

        // Filter by mode using availabilities
        if (mode != null) {
            filtered = filtered.stream()
                    .filter(t -> tutorRepository.findAvailabilitiesByTutorId(t.getTutorId())
                            .stream()
                            .anyMatch(a -> a.getMode() == mode
                                    || a.getMode() == SessionMode.HYBRID))
                    .collect(Collectors.toList());
        }

        if (filtered.isEmpty()) {
            // UC-4 alt flow 5a: "No matches found" (controller will handle message/UI)
            return Collections.emptyList();
        }

        // Simple ranking: sort by rating descending as a placeholder for "intelligent matching"
//        filtered.sort(Comparator.comparingDouble(Tutor::getAverageRating).reversed());

        // If AI is requested but not implemented, we gracefully fall back (UC-4 exception 4a)
        boolean aiUsedSuccessfully = false; // currently hard-coded to false

        List<TutorSearchResultDto> result = new ArrayList<>();
        for (Tutor tutor : filtered) {
            List<Availability> slots = tutorRepository.findAvailabilitiesByTutorId(tutor.getTutorId());

            List<AvailabilityDto> slotDtos = slots.stream()
                    .map(this::toAvailabilityDto)
                    .collect(Collectors.toList());

            String campusValue = tutor.getProfile() != null ? tutor.getProfile().getCampus() : null;

            String whyRecommended = null;
            if (useAiRecommendations && aiUsedSuccessfully) {
                // Placeholder; real AI logic later
                whyRecommended = "Matched by AI based on your course history and availability.";
            }

            TutorSearchResultDto dto = TutorSearchResultDto.builder()
                    .tutorId(tutor.getTutorId())
                    .tutorName(tutor.getFirstName())
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
