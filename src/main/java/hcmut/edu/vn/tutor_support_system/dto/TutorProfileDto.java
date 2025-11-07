package hcmut.edu.vn.tutor_support_system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorProfileDto {
    private String tutorId;
    private String tutorName;
    private String email;
    private String campus;
    private double rating;
    private int ratingCount;
    private List<String> expertiseAreas;
    private List<AvailabilityDto> availableSlots; // reuse your existing AvailabilityDto (no tutor inside)
}
