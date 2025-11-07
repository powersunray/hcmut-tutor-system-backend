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
public class MeetingNote {

    private String id;
    private Session session;
    private Tutor author;

    private String content;                 // free text
    private String topicsSummary;           // short topics list

    private LocalDateTime createdAt;
    private LocalDateTime followUpScheduledAt;
}

