package hcmut.edu.vn.tutor_support_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    private String id;
    private Tutor tutor;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private SessionMode mode;        // ONLINE / OFFLINE / HYBRID
    private String locationOrLink;   // room / Zoom link / Teams link
    private int capacity;            // max students per slot

    private boolean published;
}

