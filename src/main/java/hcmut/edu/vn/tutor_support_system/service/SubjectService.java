package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.SubjectDto;
import hcmut.edu.vn.tutor_support_system.entity.Subject;
import hcmut.edu.vn.tutor_support_system.exception.InvalidEnrollmentException;
import hcmut.edu.vn.tutor_support_system.repository.EnrollmentRepository;
import hcmut.edu.vn.tutor_support_system.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SubjectDto createSubject(SubjectDto dto) {
        subjectRepository.findByCode(dto.code()).ifPresent(s -> {
            throw new InvalidEnrollmentException("Subject code already exists");
        });
        Subject subject = toEntity(dto);
        return toDto(subjectRepository.save(subject));
    }

    public SubjectDto updateSubject(Long id, SubjectDto dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new InvalidEnrollmentException("Subject not found: " + id));
        subject.setName(dto.name());
        subject.setDescription(dto.description());
        subject.setCredits(dto.credits());
        subject.setPrerequisites(dto.prerequisites());
        return toDto(subjectRepository.save(subject));
    }

    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new InvalidEnrollmentException("Subject not found: " + id));

        // remove enrollments that still point at this subject so deletion can proceed cleanly
        enrollmentRepository.deleteByCourseCode(subject.getCode());
        subjectRepository.deleteById(id);
    }

    public SubjectDto getSubject(Long id) {
        return subjectRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new InvalidEnrollmentException("Subject not found: " + id));
    }

    public List<SubjectDto> getSubjectsByFilter(String nameFilter, Integer minCredits) {
        if (nameFilter == null) {
            nameFilter = "";
        }
        if (minCredits == null) {
            minCredits = 0;
        }
        return subjectRepository.findByNameContainingIgnoreCaseAndCreditsGreaterThanEqual(nameFilter, minCredits)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private Subject toEntity(SubjectDto dto) {
        return Subject.builder()
                .id(dto.id())
                .code(dto.code())
                .name(dto.name())
                .description(dto.description())
                .credits(dto.credits())
                .prerequisites(dto.prerequisites())
                .build();
    }

    private SubjectDto toDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getDescription(),
                subject.getCredits(),
                subject.getPrerequisites()
        );
    }
}
