package hcmut.edu.vn.tutor_support_system.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsReportDto {
  private String reportType;
  private LocalDateTime generatedAt;
  private String generatedBy;
  private Map<String, Object> data;
  private String summary;
}
