package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.*;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Repository
@Getter
public class TutorRepository {

    private final List<Tutor> tutors = new ArrayList<>();
    private final List<Availability> availabilities = new ArrayList<>();

    @PostConstruct
    public void init() {
        // ==== Tutor 1 ====
        Tutor t1 = new Tutor();
        t1.setTutorId("tutor-1");
//        t1.setSsoId("21520001");
        t1.setFirstName("Hieu");
        t1.setLastName("Tran");
        t1.setEmail("hieu.tran@hcmut.edu.vn");
        t1.setRole(UserRole.TUTOR);
        t1.setAverageRating(4.8);
        t1.setRatingCount(12);
        t1.setExpertiseAreas(Arrays.asList("CO3001", "Software Engineering"));

        Profile p1 = new Profile();
        p1.setProfileId("profile-t1");
        p1.setCampus("Campus 01");
        p1.setPhoneNumber("0909999999");
        t1.setProfile(p1);

        // some availability slots (online/offline) for UC-5 step 2 & alt 2a
        Availability a1 = new Availability(
                "slot-1", t1,
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                SessionMode.OFFLINE,
                "Room C6-403",
                3,
                true
        );

        Availability a2 = new Availability(
                "slot-2", t1,
                DayOfWeek.WEDNESDAY,
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                SessionMode.ONLINE,
                "Zoom link: https://zoom.us/tutor1",
                5,
                true
        );

        // Example slot supporting both ONLINE/OFFLINE choices (UC-5 alt 2a)
        Availability a3 = new Availability(
                "slot-3", t1,
                DayOfWeek.FRIDAY,
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                SessionMode.HYBRID,
                "Room B1-209 & Zoom link",
                4,
                true
        );

        t1.getAvailabilitySlots().addAll(Arrays.asList(a1, a2, a3));

        tutors.add(t1);
        availabilities.addAll(Arrays.asList(a1, a2, a3));

        // ==== Tutor 2 ====
        Tutor t2 = new Tutor();
        t2.setTutorId("tutor-2");
//        t2.setSsoId("21520002");
        t2.setFirstName("Tam");
        t2.setLastName("Nguyen");
        t2.setEmail("tam.nguyen@hcmut.edu.vn");
        t2.setRole(UserRole.TUTOR);
        t2.setAverageRating(4.2);
        t2.setRatingCount(7);
        t2.setExpertiseAreas(Arrays.asList("CO2013", "Discrete Math"));

        Profile p2 = new Profile();
        p2.setProfileId("profile-t2");
        p2.setCampus("Campus 02");
        p2.setPhoneNumber("0911111111");
        t2.setProfile(p2);

        Availability b1 = new Availability(
                "slot-4", t2,
                DayOfWeek.TUESDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                SessionMode.OFFLINE,
                "Room H1-302",
                2,
                true
        );

        t2.getAvailabilitySlots().add(b1);

        tutors.add(t2);
        availabilities.add(b1);
    }

    public List<Tutor> findAll() {
        return tutors;
    }

    public Optional<Tutor> findById(String id) {
        return tutors.stream()
                .filter(t -> Objects.equals(t.getTutorId(), id))
                .findFirst();
    }

    public List<Availability> findAvailabilitiesByTutorId(String tutorId) {
        List<Availability> result = new ArrayList<>();
        for (Availability a : availabilities) {
            if (a.getTutor() != null
                    && Objects.equals(a.getTutor().getTutorId(), tutorId)
                    && a.isPublished()) {
                result.add(a);
            }
        }
        return result;
    }

    public Optional<Availability> findAvailabilityById(String availabilityId) {
        return availabilities.stream()
                .filter(a -> Objects.equals(a.getId(), availabilityId))
                .findFirst();
    }
}
