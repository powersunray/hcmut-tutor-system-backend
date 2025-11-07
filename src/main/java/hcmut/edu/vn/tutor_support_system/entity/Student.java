package hcmut.edu.vn.tutor_support_system.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends User{
    private String studentId;
    private String faculty;
    private String major;

    private List<Enrollment> enrollments = new ArrayList<>();

    private List<Session> bookedSessions = new ArrayList<>();

    private List<SupportNeed> supportNeeds = new ArrayList<>();
}
