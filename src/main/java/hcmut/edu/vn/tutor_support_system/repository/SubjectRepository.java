package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCode(String code);

    List<Subject> findByPrerequisitesContaining(String prerequisiteCode);

    List<Subject> findByNameContainingIgnoreCaseAndCreditsGreaterThanEqual(String name, Integer credits);
}
