package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import hcmut.edu.vn.tutor_support_system.service.SupportNeedService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SupportNeedController {

  private final SupportNeedService supportNeedService;

  @GetMapping("/students/{studentId}/support-needs")
  public ResponseEntity<List<SupportNeedDto>> getStudentSupportNeeds(
      @PathVariable String studentId, @RequestParam(required = false) SupportType type) {
    List<SupportNeedDto> supportNeeds;

    if (type != null) {
      supportNeeds = supportNeedService.getSupportNeedsByStudentIdAndType(studentId, type);
    } else {
      supportNeeds = supportNeedService.getSupportNeedsByStudentId(studentId);
    }

    return ResponseEntity.ok(supportNeeds);
  }

  @GetMapping("/support-needs/{supportNeedId}")
  public ResponseEntity<SupportNeedDto> getSupportNeedById(@PathVariable UUID supportNeedId) {
    SupportNeedDto supportNeed = supportNeedService.getSupportNeedById(supportNeedId);
    return ResponseEntity.ok(supportNeed);
  }

  @PostMapping("/students/{studentId}/support-needs")
  public ResponseEntity<SupportNeedDto> createSupportNeed(
      @PathVariable String studentId,
      @RequestParam SupportType supportType,
      @RequestParam(required = false) String description) {
    SupportNeedDto supportNeed =
        supportNeedService.createSupportNeed(studentId, supportType, description);
    return ResponseEntity.status(HttpStatus.CREATED).body(supportNeed);
  }

  @PutMapping("/support-needs/{supportNeedId}/status")
  public ResponseEntity<SupportNeedDto> updateSupportNeedStatus(
      @PathVariable UUID supportNeedId, @RequestParam String status) {
    SupportNeedDto supportNeed = supportNeedService.updateSupportNeedStatus(supportNeedId, status);
    return ResponseEntity.ok(supportNeed);
  }

  @PutMapping("/support-needs/{supportNeedId}/description")
  public ResponseEntity<SupportNeedDto> updateSupportNeedDescription(
      @PathVariable UUID supportNeedId, @RequestParam String description) {
    SupportNeedDto supportNeed =
        supportNeedService.updateSupportNeedDescription(supportNeedId, description);
    return ResponseEntity.ok(supportNeed);
  }

  @DeleteMapping("/support-needs/{supportNeedId}")
  public ResponseEntity<Void> deleteSupportNeed(@PathVariable UUID supportNeedId) {
    supportNeedService.deleteSupportNeed(supportNeedId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/support-needs")
  public ResponseEntity<List<SupportNeedDto>> getSupportNeeds(
      @RequestParam(required = false) SupportType type,
      @RequestParam(required = false) String status) {
    List<SupportNeedDto> supportNeeds;

    if (type != null) {
      supportNeeds = supportNeedService.getSupportNeedsByType(type);
    } else if (status != null && !status.isEmpty()) {
      supportNeeds = supportNeedService.getSupportNeedsByStatus(status);
    } else {
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(supportNeeds);
  }

  @GetMapping("/support-needs/statistics")
  public ResponseEntity<Long> getSupportNeedsCount(
      @RequestParam SupportType type, @RequestParam String status) {
    Long count = supportNeedService.countSupportNeedsByTypeAndStatus(type, status);
    return ResponseEntity.ok(count);
  }
}
