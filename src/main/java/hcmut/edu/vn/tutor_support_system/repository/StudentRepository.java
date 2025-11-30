package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import hcmut.edu.vn.tutor_support_system.entity.Profile;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Seed a couple of students so GET requests immediately return meaningful data
        Student lan = buildStudent(1L, 1001L, "Lan", "Nguyen", "lan.nguyen@hcmut.edu.vn", "0901234567", "Software Engineering", "Computer Science", 3, 3.4, "Campus 01");
        Student minh = buildStudent(2L, 1002L, "Minh", "Tran", "minh.tran@hcmut.edu.vn", "0907654321", "Information Systems", "Business IT", 2, 3.8, "Campus 02");

        students.add(lan);
        students.add(minh);
    }

    private Student buildStudent(Long id,
                                 Long studentNumber,
                                 String firstName,
                                 String lastName,
                                 String email,
                                 String phone,
                                 String major,
                                 String faculty,
                                 Integer yearOfStudy,
                                 Double gpa,
                                 String campus) {
        Student s = new Student();
        s.setId(id);
        s.setStudentId(studentNumber);
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setEmail(email);
        s.setRole(UserRole.STUDENT);
        s.setFullName(firstName + " " + lastName);
        s.setPhone(phone);
        s.setMajor(major);
        s.setFaculty(faculty);
        s.setYearOfStudy(yearOfStudy);
        s.setGpa(gpa);
        s.setProfile(new Profile("profile-" + id, phone, campus, null, null));
        s.setCreatedAt(OffsetDateTime.now());
        s.setUpdatedAt(OffsetDateTime.now());
        return s;
    }

    public Optional<Student> findByStudentId(Long studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }

    public List<Student> findAll() {
        return students;
    }
}
