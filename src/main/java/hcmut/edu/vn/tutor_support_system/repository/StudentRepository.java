package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Profile;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Getter
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Sample student
        Student s1 = new Student();
        s1.setStudentId("2352999");
//        s1.setSsoId("20520001");
        s1.setFirstName("Nam");
        s1.setLastName("Nguyen");
        s1.setEmail("nam.nguyen@hcmut.edu.vn");
        s1.setRole(UserRole.STUDENT);
//        s1.setCreatedAt(LocalDateTime.now());

//        Profile p1 = new Profile();
        Profile p1 = new Profile();
        p1.setProfileId("profile-s1");
        p1.setCampus("Campus 01");
        p1.setPhoneNumber("0901234567");
        s1.setProfile(p1);

        students.add(s1);
    }

    public List<Student> findAll() {
        return students;
    }

    public Optional<Student> findById(String id) {
        return students.stream()
                .filter(s -> Objects.equals(s.getStudentId(), id))
                .findFirst();
    }
}
