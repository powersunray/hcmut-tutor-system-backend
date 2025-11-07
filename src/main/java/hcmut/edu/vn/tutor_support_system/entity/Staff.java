package hcmut.edu.vn.tutor_support_system.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Staff extends User {

    private StaffRole staffRole;
    private String department;  // "Computer Science", "OAA", etc.
}

