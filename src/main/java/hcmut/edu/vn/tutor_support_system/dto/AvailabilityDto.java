package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityDto {

  private String availabilityId;
  private String dayOfWeek; // MONDAY, TUESDAY...
  private String startTime; // "09:00"
  private String endTime; // "10:00"

  private SessionMode mode; // ONLINE / OFFLINE / HYBRID
  private String locationOrLink;
}
