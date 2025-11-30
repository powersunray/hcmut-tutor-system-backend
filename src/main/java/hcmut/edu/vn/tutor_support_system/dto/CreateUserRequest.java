package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
  @NotBlank private String firstName;

  @NotBlank private String lastName;

  @Email
  @NotBlank
  private String email;

  @NotNull private UserRole role;

  // Add password here if you plan authentication; handle hashing in service layer.
}
