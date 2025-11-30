package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.SupportPriority;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class SupportNeedRepository {

    private final StudentRepository studentRepository;
    private final SupportTypeRepository supportTypeRepository;
    private final List<SupportNeed> supportNeeds = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        studentRepository.findByStudentId(1001L).ifPresent(student ->
                supportTypeRepository.findById(1L).ifPresent(type -> {
                    SupportNeed initial = SupportNeed.builder()
                            .id(idSequence.getAndIncrement())
                            .student(student)
                            .supportType(type)
                            .description("Needs study plan for CO3001")
                            .priority(SupportPriority.MEDIUM)
                            .declaredDate(LocalDate.now().minusDays(3))
                            .updatedDate(LocalDate.now().minusDays(1))
                            .build();
                    supportNeeds.add(initial);
                }));
    }

    public SupportNeed save(SupportNeed supportNeed) {
        if (supportNeed.getId() == null) {
            supportNeed.setId(idSequence.getAndIncrement());
        }
        supportNeeds.removeIf(existing -> existing.getId().equals(supportNeed.getId()));
        supportNeeds.add(supportNeed);
        return supportNeed;
    }

    public List<SupportNeed> saveAll(List<SupportNeed> needs) {
        needs.forEach(this::save);
        return needs;
    }

    public Optional<SupportNeed> findById(Long id) {
        return supportNeeds.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public List<SupportNeed> findByStudentIdAndPriority(Long studentId, SupportPriority priority) {
        return supportNeeds.stream()
                .filter(n -> n.getStudent() != null && n.getStudent().getStudentId().equals(studentId))
                .filter(n -> priority == null || n.getPriority() == priority)
                .toList();
    }

    public List<SupportNeed> findRecentSupportNeeds(LocalDate fromDate) {
        return supportNeeds.stream()
                .filter(n -> n.getDeclaredDate() != null && !n.getDeclaredDate().isBefore(fromDate))
                .toList();
    }

    public void deleteById(Long id) {
        supportNeeds.removeIf(n -> n.getId().equals(id));
    }
}
