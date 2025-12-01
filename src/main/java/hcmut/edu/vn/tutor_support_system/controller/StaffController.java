package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.StaffDto;
import hcmut.edu.vn.tutor_support_system.entity.StaffRole;
import hcmut.edu.vn.tutor_support_system.service.StaffService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

  private final StaffService staffService;

  @GetMapping
  public ResponseEntity<List<StaffDto>> getAllStaff(
      @RequestParam(required = false) StaffRole role,
      @RequestParam(required = false) String department) {
    List<StaffDto> staffList;

    if (role != null && department != null && !department.isEmpty()) {
      staffList = staffService.getStaffByRoleAndDepartment(role, department);
    } else if (role != null) {
      staffList = staffService.getStaffByRole(role);
    } else if (department != null && !department.isEmpty()) {
      staffList = staffService.getStaffByDepartment(department);
    } else {
      staffList = staffService.getAllStaff();
    }

    return ResponseEntity.ok(staffList);
  }

  @GetMapping("/{id}")
  public ResponseEntity<StaffDto> getStaffById(@PathVariable UUID id) {
    StaffDto staff = staffService.getStaffById(id);
    return ResponseEntity.ok(staff);
  }

  @GetMapping("/staff-id/{staffId}")
  public ResponseEntity<StaffDto> getStaffByStaffId(@PathVariable String staffId) {
    StaffDto staff = staffService.getStaffByStaffId(staffId);
    return ResponseEntity.ok(staff);
  }

  @PostMapping
  public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto) {
    StaffDto createdStaff = staffService.createStaff(staffDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
  }

  @PutMapping("/{id}")
  public ResponseEntity<StaffDto> updateStaff(
      @PathVariable UUID id, @RequestBody StaffDto staffDto) {
    StaffDto updatedStaff = staffService.updateStaff(id, staffDto);
    return ResponseEntity.ok(updatedStaff);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteStaff(@PathVariable UUID id) {
    staffService.deleteStaff(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/statistics/count-by-role")
  public ResponseEntity<Long> getStaffCountByRole(@RequestParam StaffRole role) {
    Long count = staffService.countStaffByRole(role);
    return ResponseEntity.ok(count);
  }
}
