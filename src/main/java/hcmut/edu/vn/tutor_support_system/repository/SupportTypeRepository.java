package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SupportTypeRepository {

    private final List<SupportType> supportTypes = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        save(new SupportType(null, "Academic", "Study skills and academic coaching"));
        save(new SupportType(null, "Mental Health", "Wellness and counseling support"));
        save(new SupportType(null, "Accessibility", "Assistive technology and accommodations"));
    }

    public Optional<SupportType> findById(Long id) {
        return supportTypes.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public List<SupportType> findAll() {
        return supportTypes;
    }

    public SupportType save(SupportType supportType) {
        if (supportType.getId() == null) {
            supportType.setId(idSequence.getAndIncrement());
        }
        supportTypes.removeIf(existing -> existing.getId().equals(supportType.getId()));
        supportTypes.add(supportType);
        return supportType;
    }
}
