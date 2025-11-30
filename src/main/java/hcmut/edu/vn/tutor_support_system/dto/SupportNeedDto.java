package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.SupportPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record SupportNeedDto(
        Long id,
        @NotNull @Positive Long studentId,
        @NotNull @Positive Long supportTypeId,
        @NotBlank String description,
        @NotNull SupportPriority priority,
        LocalDate declaredDate,
        LocalDate updatedDate
) {
}
