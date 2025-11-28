package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeedStatus;
import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.SupportNeedRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupportNeedService {

  private final SupportNeedRepository supportNeedRepository;
  private final StudentRepository studentRepository;

  @Transactional(readOnly = true)
  public List<SupportNeedDto> getSupportNeedsByStudentId(String studentId) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");

    List<SupportNeed> supportNeeds = supportNeedRepository.findByStudentId(studentId);
    return supportNeeds.stream().map(DtoMapper::toSupportNeedDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SupportNeedDto> getSupportNeedsByType(SupportType supportType) {
    ValidationUtil.validateNotNull(supportType, "Support type");

    List<SupportNeed> supportNeeds = supportNeedRepository.findBySupportType(supportType);
    return supportNeeds.stream().map(DtoMapper::toSupportNeedDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<SupportNeedDto> getSupportNeedsByStatus(SupportNeedStatus status) {
    ValidationUtil.validateNotNull(status, "Status");

    List<SupportNeed> supportNeeds = supportNeedRepository.findByStatus(status);
    return supportNeeds.stream().map(DtoMapper::toSupportNeedDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public SupportNeedDto getSupportNeedById(UUID supportNeedId) {
    ValidationUtil.validateNotNull(supportNeedId, "Support need ID");

    SupportNeed supportNeed =
        supportNeedRepository
            .findByIdWithStudent(supportNeedId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Support need not found with id: " + supportNeedId));
    return DtoMapper.toSupportNeedDto(supportNeed);
  }

  @Transactional
  public SupportNeedDto createSupportNeed(
      String studentId, SupportType supportType, String description) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");
    ValidationUtil.validateNotNull(supportType, "Support type");

    Student student =
        studentRepository
            .findByStudentId(studentId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + studentId));

    SupportNeed supportNeed = new SupportNeed();
    supportNeed.setStudent(student);
    supportNeed.setSupportType(supportType);
    supportNeed.setDescription(description);
    supportNeed.setStatus(SupportNeedStatus.PENDING);

    SupportNeed savedSupportNeed = supportNeedRepository.save(supportNeed);
    return DtoMapper.toSupportNeedDto(savedSupportNeed);
  }

  @Transactional
  public SupportNeedDto updateSupportNeedStatus(UUID supportNeedId, SupportNeedStatus status) {
    ValidationUtil.validateNotNull(supportNeedId, "Support need ID");
    ValidationUtil.validateNotNull(status, "Status");

    SupportNeed supportNeed =
        supportNeedRepository
            .findByIdWithStudent(supportNeedId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Support need not found with id: " + supportNeedId));

    supportNeed.setStatus(status);
    SupportNeed updatedSupportNeed = supportNeedRepository.save(supportNeed);
    return DtoMapper.toSupportNeedDto(updatedSupportNeed);
  }

  @Transactional
  public SupportNeedDto updateSupportNeedDescription(UUID supportNeedId, String description) {
    ValidationUtil.validateNotNull(supportNeedId, "Support need ID");
    ValidationUtil.validateNotEmpty(description, "Description");

    SupportNeed supportNeed =
        supportNeedRepository
            .findByIdWithStudent(supportNeedId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Support need not found with id: " + supportNeedId));

    supportNeed.setDescription(description);
    SupportNeed updatedSupportNeed = supportNeedRepository.save(supportNeed);
    return DtoMapper.toSupportNeedDto(updatedSupportNeed);
  }

  @Transactional
  public void deleteSupportNeed(UUID supportNeedId) {
    ValidationUtil.validateNotNull(supportNeedId, "Support need ID");

    SupportNeed supportNeed =
        supportNeedRepository
            .findByIdWithStudent(supportNeedId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Support need not found with id: " + supportNeedId));

    supportNeedRepository.delete(supportNeed);
  }

  @Transactional(readOnly = true)
  public List<SupportNeedDto> getSupportNeedsByStudentIdAndType(
      String studentId, SupportType supportType) {
    ValidationUtil.validateNotEmpty(studentId, "Student ID");
    ValidationUtil.validateNotNull(supportType, "Support type");

    List<SupportNeed> supportNeeds =
        supportNeedRepository.findByStudentIdAndSupportType(studentId, supportType);
    return supportNeeds.stream().map(DtoMapper::toSupportNeedDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Long countSupportNeedsByTypeAndStatus(SupportType supportType, SupportNeedStatus status) {
    ValidationUtil.validateNotNull(supportType, "Support type");
    ValidationUtil.validateNotNull(status, "Status");

    return supportNeedRepository.countBySupportTypeAndStatus(supportType, status);
  }
}
