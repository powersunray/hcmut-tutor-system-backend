package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SessionMode;
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
public class SessionUpdateRequestDto {

  private SessionMode mode;

  private String locationOrLink;
}