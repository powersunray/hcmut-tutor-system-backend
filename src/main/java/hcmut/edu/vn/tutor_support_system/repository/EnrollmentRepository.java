package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepository {

    private final StudentRepository studentRepository;
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        studentRepository.findByStudentId(1001L).ifPresent(student -> {
            Enrollment sample = Enrollment.builder()
                    .id(idSequence.getAndIncrement())
                    .student(student)
                    .programName("Software Engineering")
                    .courseCode("CO3001")
                    .semester("2024A")
                    .status(EnrollmentStatus.ACTIVE)
                    .enrollmentDate(LocalDate.now().minusDays(7))
                    .build();
            enrollments.add(sample);
        });
    }

    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            enrollment.setId(idSequence.getAndIncrement());
        }
        enrollments.removeIf(existing -> existing.getId().equals(enrollment.getId()));
        enrollments.add(enrollment);
        return enrollment;
    }

    public List<Enrollment> saveAll(List<Enrollment> items) {
        items.forEach(this::save);
        return items;
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public List<Enrollment> findByStudentIdAndSemester(Long studentId, String semester) {
        return enrollments.stream()
                .filter(e -> e.getStudent() != null
                        && e.getStudent().getStudentId().equals(studentId)
                        && e.getSemester().equalsIgnoreCase(semester))
                .toList();
    }

    public List<Enrollment> findActiveEnrollmentsByStudent(Long studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudent() != null
                        && e.getStudent().getStudentId().equals(studentId)
                        && e.getStatus() == EnrollmentStatus.ACTIVE)
                .toList();
    }

    public List<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable) {
        List<Enrollment> filtered = enrollments.stream()
                .filter(e -> e.getStatus() == status)
                .toList();
        if (pageable == null) {
            return filtered;
        }
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        if (start >= filtered.size()) {
            return List.of();
        }
        return filtered.subList(start, end);
    }

    public List<Enrollment> findByStudentStudentId(Long studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudent() != null && e.getStudent().getStudentId().equals(studentId))
                .toList();
    }

    public List<Enrollment> findAll() {
        return enrollments;
    }

    public void deleteById(Long id) {
        enrollments.removeIf(e -> e.getId().equals(id));
    }

    public void deleteByCourseCode(String courseCode) {
        enrollments.removeIf(e -> e.getCourseCode() != null && e.getCourseCode().equalsIgnoreCase(courseCode));
    }
}
