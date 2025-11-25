package hcmut.edu.vn.tutor_support_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("STAFF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Staff extends User {

    @Column(name = "staff_id", unique = true, length = 50)
    private String staffId;

    @Enumerated(EnumType.STRING)
    @Column(name = "staff_role", length = 50)
    private StaffRole staffRole;

    @Column(name = "department")
    private String department;
}

