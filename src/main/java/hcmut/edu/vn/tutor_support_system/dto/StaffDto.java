package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.StaffRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {

  private String staffId;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private StaffRole staffRole;
  private String department;
  private String campus;
}
