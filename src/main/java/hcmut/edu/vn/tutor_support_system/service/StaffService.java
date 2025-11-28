package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.StaffDto;
import hcmut.edu.vn.tutor_support_system.entity.Staff;
import hcmut.edu.vn.tutor_support_system.entity.StaffRole;
import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.StaffRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService {

  private final StaffRepository staffRepository;

  @Transactional(readOnly = true)
  public List<StaffDto> getAllStaff() {
    List<Staff> staffList = staffRepository.findAllWithProfile();
    return staffList.stream().map(DtoMapper::toStaffDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public StaffDto getStaffById(UUID id) {
    ValidationUtil.validateNotNull(id, "Staff ID");

    Staff staff =
        staffRepository
            .findByIdWithProfile(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
    return DtoMapper.toStaffDto(staff);
  }

  @Transactional(readOnly = true)
  public StaffDto getStaffByStaffId(String staffId) {
    ValidationUtil.validateNotEmpty(staffId, "Staff ID");

    Staff staff =
        staffRepository
            .findByStaffId(staffId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Staff not found with staff ID: " + staffId));
    return DtoMapper.toStaffDto(staff);
  }

  @Transactional(readOnly = true)
  public List<StaffDto> getStaffByRole(StaffRole staffRole) {
    ValidationUtil.validateNotNull(staffRole, "Staff role");

    List<Staff> staffList = staffRepository.findByStaffRole(staffRole);
    return staffList.stream().map(DtoMapper::toStaffDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<StaffDto> getStaffByDepartment(String department) {
    ValidationUtil.validateNotEmpty(department, "Department");

    List<Staff> staffList = staffRepository.findByDepartment(department);
    return staffList.stream().map(DtoMapper::toStaffDto).collect(Collectors.toList());
  }

  @Transactional
  public StaffDto createStaff(StaffDto staffDto) {
    ValidationUtil.validateNotNull(staffDto, "Staff DTO");
    ValidationUtil.validateNotEmpty(staffDto.getStaffId(), "Staff ID");
    ValidationUtil.validateEmail(staffDto.getEmail());
    ValidationUtil.validateNotEmpty(staffDto.getFirstName(), "First name");
    ValidationUtil.validateNotEmpty(staffDto.getLastName(), "Last name");

    staffRepository
        .findByStaffId(staffDto.getStaffId())
        .ifPresent(
            s -> {
              throw new IllegalArgumentException(
                  "Staff with ID " + staffDto.getStaffId() + " already exists");
            });

    staffRepository
        .findByEmail(staffDto.getEmail())
        .ifPresent(
            s -> {
              throw new IllegalArgumentException(
                  "Staff with email " + staffDto.getEmail() + " already exists");
            });

    Staff staff = new Staff();
    staff.setStaffId(staffDto.getStaffId());
    staff.setFirstName(staffDto.getFirstName());
    staff.setLastName(staffDto.getLastName());
    staff.setEmail(staffDto.getEmail());
    staff.setRole(UserRole.STAFF);
    staff.setStaffRole(staffDto.getStaffRole());
    staff.setDepartment(staffDto.getDepartment());

    Staff savedStaff = staffRepository.save(staff);
    return DtoMapper.toStaffDto(savedStaff);
  }

  @Transactional
  public StaffDto updateStaff(UUID id, StaffDto staffDto) {
    ValidationUtil.validateNotNull(id, "Staff ID");
    ValidationUtil.validateNotNull(staffDto, "Staff DTO");

    Staff staff =
        staffRepository
            .findByIdWithProfile(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

    if (staffDto.getFirstName() != null && !staffDto.getFirstName().isEmpty()) {
      staff.setFirstName(staffDto.getFirstName());
    }

    if (staffDto.getLastName() != null && !staffDto.getLastName().isEmpty()) {
      staff.setLastName(staffDto.getLastName());
    }

    if (staffDto.getEmail() != null && !staffDto.getEmail().isEmpty()) {
      ValidationUtil.validateEmail(staffDto.getEmail());
      staff.setEmail(staffDto.getEmail());
    }

    if (staffDto.getStaffRole() != null) {
      staff.setStaffRole(staffDto.getStaffRole());
    }

    if (staffDto.getDepartment() != null && !staffDto.getDepartment().isEmpty()) {
      staff.setDepartment(staffDto.getDepartment());
    }

    Staff updatedStaff = staffRepository.save(staff);
    return DtoMapper.toStaffDto(updatedStaff);
  }

  @Transactional
  public void deleteStaff(UUID id) {
    ValidationUtil.validateNotNull(id, "Staff ID");

    Staff staff =
        staffRepository
            .findByIdWithProfile(id)
            .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

    staffRepository.delete(staff);
  }

  @Transactional(readOnly = true)
  public Long countStaffByRole(StaffRole staffRole) {
    ValidationUtil.validateNotNull(staffRole, "Staff role");

    return staffRepository.countByStaffRole(staffRole);
  }

  @Transactional(readOnly = true)
  public List<StaffDto> getStaffByRoleAndDepartment(StaffRole staffRole, String department) {
    ValidationUtil.validateNotNull(staffRole, "Staff role");
    ValidationUtil.validateNotEmpty(department, "Department");

    List<Staff> staffList = staffRepository.findByStaffRoleAndDepartment(staffRole, department);
    return staffList.stream().map(DtoMapper::toStaffDto).collect(Collectors.toList());
  }
}
