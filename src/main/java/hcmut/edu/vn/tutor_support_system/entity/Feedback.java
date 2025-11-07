package hcmut.edu.vn.tutor_support_system.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    private String id;
    private Session session;

    private Student student;        // feedback giver
    private int rating;             // 1–5
    private String comment;

    private List<String> tags = new ArrayList<>(); // "helpful", "clear", etc.

    private LocalDateTime createdAt;
}
