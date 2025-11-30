package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportTypeRepository extends JpaRepository<SupportType, Long> {
    Optional<SupportType> findByNameIgnoreCase(String name);
}
