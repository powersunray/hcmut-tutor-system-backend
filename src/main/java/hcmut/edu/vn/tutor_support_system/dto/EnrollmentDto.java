package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {

  private String enrollmentId;
  private String studentId;
  private String studentName;
  private String subjectId;
  private String subjectCode;
  private String subjectName;
  private String courseCode;
  private String semester;
  private BigDecimal grade;
  private EnrollmentStatus enrollmentStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer credits;
}
