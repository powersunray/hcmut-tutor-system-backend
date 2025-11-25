package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

  @Column(name = "student_id", unique = true, length = 50)
  private String studentId;

  @Column(name = "faculty")
  private String faculty;

  @Column(name = "major")
  private String major;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Enrollment> enrollments = new ArrayList<>();

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Session> bookedSessions = new ArrayList<>();

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SupportNeed> supportNeeds = new ArrayList<>();
}
