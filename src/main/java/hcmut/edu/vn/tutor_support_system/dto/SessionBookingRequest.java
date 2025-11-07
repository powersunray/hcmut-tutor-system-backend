package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionBookingRequest {

    private String studentId;
    private String availabilityId;

    // Only needed if a slot supports both modes (UC-5 alt 2a)
    private SessionMode preferredMode;
}
