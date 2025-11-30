package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private UserRole role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}