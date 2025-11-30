package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Subject;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SubjectRepository {

    private final List<Subject> subjects = java.util.Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        save(Subject.builder()
                .code("CO3001")
                .name("Software Engineering")
                .description("Requirements, design, testing practices")
                .credits(3)
                .prerequisites(List.of("CO2011"))
                .build());
        save(Subject.builder()
                .code("CO2013")
                .name("Discrete Mathematics")
                .description("Logic, combinatorics, graph theory")
                .credits(3)
                .prerequisites(List.of())
                .build());
    }

    public Optional<Subject> findById(Long id) {
        return subjects.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public Optional<Subject> findByCode(String code) {
        if (code == null) return Optional.empty();
        String normalized = code.trim().toUpperCase();
        return subjects.stream()
            .filter(s -> s.getCode() != null && s.getCode().toUpperCase().equals(normalized))
            .findFirst();
    }

    public List<Subject> findByPrerequisitesContaining(String prerequisiteCode) {
        return subjects.stream()
                .filter(s -> s.getPrerequisites() != null && s.getPrerequisites().contains(prerequisiteCode))
                .toList();
    }

    public List<Subject> findByNameContainingIgnoreCaseAndCreditsGreaterThanEqual(String name, Integer credits) {
        String lowered = name == null ? "" : name.toLowerCase();
        int minCredits = credits == null ? 0 : credits;
        return subjects.stream()
                .filter(s -> s.getName() != null && s.getName().toLowerCase().contains(lowered))
                .filter(s -> s.getCredits() != null && s.getCredits() >= minCredits)
                .toList();
    }

    public List<Subject> findAll() {
        return subjects;
    }

    public Subject save(Subject subject) {
        // normalize code for deterministic comparisons
        if (subject.getCode() != null) {
            subject.setCode(subject.getCode().trim().toUpperCase());
        }
        if (subject.getId() == null) {
            subject.setId(idSequence.getAndIncrement());
        }
        subjects.removeIf(existing -> existing.getId().equals(subject.getId()));
        subjects.add(subject);
        return subject;
    }

    public void deleteById(Long id) {
        subjects.removeIf(s -> s.getId().equals(id));
    }
}
