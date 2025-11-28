package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportNeedDto {

  private String supportNeedId;
  private String studentId;
  private String studentName;
  private SupportType supportType;
  private String description;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
