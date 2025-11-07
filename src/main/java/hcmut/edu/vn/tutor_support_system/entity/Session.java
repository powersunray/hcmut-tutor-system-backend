package hcmut.edu.vn.tutor_support_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private String id;

    private Tutor tutor;
    private Student student;

    private String title;                // e.g., "CO2013 Midterm Review"
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private SessionMode mode;
    private String locationOrLink;

    private SessionStatus status;

    private List<MeetingNote> meetingNotes = new ArrayList<>();

    private List<Material> materials = new ArrayList<>();

    private Feedback studentFeedback;    // feedback from student
    private Evaluation evaluation;       // evaluation written by tutor/staff
}
