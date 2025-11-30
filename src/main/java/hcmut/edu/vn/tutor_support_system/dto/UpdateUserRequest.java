package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import jakarta.validation.constraints.Email;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
  private String firstName;
  private String lastName;

  @Email
  private String email;

  private UserRole role;
}
