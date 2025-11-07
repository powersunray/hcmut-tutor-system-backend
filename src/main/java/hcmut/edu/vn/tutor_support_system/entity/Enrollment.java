package hcmut.edu.vn.tutor_support_system.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    private String id;
    private Student student;

    private String programName;       // e.g., "Computer Science"
    private String major;
    private String minor;

    private List<String> courseCodes = new ArrayList<>(); // "CO2013", "CO3001", ...

    private LocalDate declaredAt;
}
