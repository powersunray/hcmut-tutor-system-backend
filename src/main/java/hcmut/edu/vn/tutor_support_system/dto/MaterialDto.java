package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.MaterialSourceType;
import hcmut.edu.vn.tutor_support_system.entity.MaterialVisibility;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDto {
  private String materialId;
  private String sessionId;
  private String name;
  private MaterialSourceType sourceType;
  private String contentUrl;
  private MaterialVisibility visibility;
  private String uploadedByName;
  private String uploadedByEmail;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
