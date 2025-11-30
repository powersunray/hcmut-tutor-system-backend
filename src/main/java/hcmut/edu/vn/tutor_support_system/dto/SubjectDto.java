package hcmut.edu.vn.tutor_support_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubjectDto(
        Long id,
        @NotBlank @Size(max = 20) String code,
        @NotBlank @Size(max = 150) String name,
        @Size(max = 255) String description,
        @NotNull @Min(0) Integer credits,
        List<String> prerequisites
) {
}
