package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@Getter
public class SessionRepository {

    private final List<Session> sessions = new ArrayList<>();

    public Session save(Session session) {
        sessions.add(session);
        return session;
    }

    public List<Session> findAll() {
        return sessions;
    }

    public List<Session> findByTutor(Tutor tutor) {
        return sessions.stream()
                .filter(s -> s.getTutor() != null &&
                        Objects.equals(s.getTutor().getTutorId(), tutor.getTutorId()))
                .collect(Collectors.toList());
    }
}

