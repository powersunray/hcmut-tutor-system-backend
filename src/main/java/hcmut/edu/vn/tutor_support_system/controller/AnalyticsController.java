package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.AnalyticsReportDto;
import hcmut.edu.vn.tutor_support_system.entity.StaffRole;
import hcmut.edu.vn.tutor_support_system.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @GetMapping("/reports/tutor-performance")
  public ResponseEntity<AnalyticsReportDto> getTutorPerformanceReport(
      @RequestParam(defaultValue = "system") String generatedBy) {
    AnalyticsReportDto report = analyticsService.generateTutorPerformanceReport(generatedBy);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/reports/student-engagement")
  public ResponseEntity<AnalyticsReportDto> getStudentEngagementReport(
      @RequestParam(defaultValue = "system") String generatedBy) {
    AnalyticsReportDto report = analyticsService.generateStudentEngagementReport(generatedBy);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/reports/material-usage")
  public ResponseEntity<AnalyticsReportDto> getMaterialUsageReport(
      @RequestParam(defaultValue = "system") String generatedBy) {
    AnalyticsReportDto report = analyticsService.generateMaterialUsageReport(generatedBy);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/reports/by-role/{staffRole}")
  public ResponseEntity<AnalyticsReportDto> getReportByStaffRole(
      @PathVariable StaffRole staffRole,
      @RequestParam(defaultValue = "system") String generatedBy) {
    AnalyticsReportDto report = analyticsService.generateReportByStaffRole(staffRole, generatedBy);
    return ResponseEntity.ok(report);
  }

  @GetMapping("/reports/comprehensive")
  public ResponseEntity<AnalyticsReportDto> getComprehensiveReport(
      @RequestParam(defaultValue = "system") String generatedBy) {
    AnalyticsReportDto report = analyticsService.generateComprehensiveReport(generatedBy);
    return ResponseEntity.ok(report);
  }
}
