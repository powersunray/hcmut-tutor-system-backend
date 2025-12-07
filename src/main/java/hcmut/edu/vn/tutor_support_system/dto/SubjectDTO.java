package hcmut.edu.vn.tutor_support_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private String id;
    private String code;
    private String name;
    private Integer credits;
}
