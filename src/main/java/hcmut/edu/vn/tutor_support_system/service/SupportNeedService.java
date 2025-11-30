package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.SupportPriority;
import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import hcmut.edu.vn.tutor_support_system.exception.InvalidEnrollmentException;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.SupportNeedRepository;
import hcmut.edu.vn.tutor_support_system.repository.SupportTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportNeedService {
    private final SupportNeedRepository supportNeedRepository;
    private final StudentRepository studentRepository;
    private final SupportTypeRepository supportTypeRepository;

    public SupportNeedDto declareSupportNeed(SupportNeedDto dto) {
        Student student = studentRepository.findByStudentId(dto.studentId())
                .orElseThrow(() -> new InvalidEnrollmentException("Student not found: " + dto.studentId()));
        SupportType supportType = supportTypeRepository.findById(dto.supportTypeId())
                .orElseThrow(() -> new InvalidEnrollmentException("Support type not found: " + dto.supportTypeId()));

        SupportNeed supportNeed = SupportNeed.builder()
                .student(student)
                .supportType(supportType)
                .description(dto.description())
                .priority(dto.priority())
                .declaredDate(dto.declaredDate() != null ? dto.declaredDate() : LocalDate.now())
                .updatedDate(dto.updatedDate())
                .build();
        return toDto(supportNeedRepository.save(supportNeed));
    }

    public SupportNeedDto updateSupportNeed(Long id, SupportNeedDto dto) {
        SupportNeed supportNeed = supportNeedRepository.findById(id)
                .orElseThrow(() -> new InvalidEnrollmentException("Support need not found: " + id));
        supportNeed.setDescription(dto.description());
        supportNeed.setPriority(dto.priority());
        supportNeed.setUpdatedDate(LocalDate.now());
        return toDto(supportNeedRepository.save(supportNeed));
    }

    public List<SupportNeedDto> getSupportNeedsByStudent(Long studentId, SupportPriority filterPriority) {
        return supportNeedRepository.findByStudentIdAndPriority(studentId, filterPriority)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public void archiveOldNeeds(LocalDate thresholdDate) {
        List<SupportNeed> needs = supportNeedRepository.findRecentSupportNeeds(thresholdDate);
        needs.forEach(need -> need.setPriority(SupportPriority.LOW));
        supportNeedRepository.saveAll(needs);
    }

    public void deleteSupportNeed(Long id) {
        supportNeedRepository.deleteById(id);
    }

    private SupportNeedDto toDto(SupportNeed supportNeed) {
        return new SupportNeedDto(
                supportNeed.getId(),
                supportNeed.getStudent().getStudentId(),
                supportNeed.getSupportType().getId(),
                supportNeed.getDescription(),
                supportNeed.getPriority(),
                supportNeed.getDeclaredDate(),
                supportNeed.getUpdatedDate()
        );
    }
}
