package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Profile {

    private String profileId;

    private String phoneNumber;
    private String campus;          // "Cơ sở 1", "Cơ sở 2"

    private String address;
    private String gender;          // keep simple for MVP
}
