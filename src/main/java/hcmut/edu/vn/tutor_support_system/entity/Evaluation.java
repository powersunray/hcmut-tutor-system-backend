package hcmut.edu.vn.tutor_support_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {

    private String id;

    private Student student;
    private Session session;

    private User evaluator;             // tutor or staff

    private String progressSummary;
    private String achievements;
    private String concerns;

    private String overallStatus;       // "On track", "Needs support", ...

    private LocalDateTime createdAt;
}
