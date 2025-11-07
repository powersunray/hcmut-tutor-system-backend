package hcmut.edu.vn.tutor_support_system.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tutor extends User {

    private String tutorCode;          // can be staff ID or student ID for peer tutor
    private String bio;

    private List<String> expertiseAreas = new ArrayList<>(); // "CO2013", "AI", etc.

    private double averageRating;
    private int ratingCount;

    private List<Availability> availabilitySlots = new ArrayList<>();
}
