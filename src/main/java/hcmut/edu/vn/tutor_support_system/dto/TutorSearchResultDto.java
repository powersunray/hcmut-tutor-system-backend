package hcmut.edu.vn.tutor_support_system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorSearchResultDto {

    private String tutorId;
    private String tutorName;
    private String campus;

    private List<String> courses;      // expertise areas
    private double rating;

    private List<AvailabilityDto> availableSlots;

    // If AI is used, this contains a short “Why recommended” note.
    private String whyRecommended;
}
