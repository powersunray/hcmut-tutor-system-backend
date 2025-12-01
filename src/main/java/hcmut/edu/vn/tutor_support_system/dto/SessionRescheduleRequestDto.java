package hcmut.edu.vn.tutor_support_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionRescheduleRequestDto {

  private String availabilityId;

  private String reason;
}