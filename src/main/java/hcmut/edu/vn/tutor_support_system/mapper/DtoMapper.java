package hcmut.edu.vn.tutor_support_system.mapper;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;

import java.time.format.DateTimeFormatter;

public final class DtoMapper {
    private DtoMapper() {}

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    public static AvailabilityDto toAvailabilityDto(Availability a) {
        return AvailabilityDto.builder()
                .availabilityId(a.getId())
                .dayOfWeek(a.getDayOfWeek().name())
                .startTime(a.getStartTime().format(TIME))
                .endTime(a.getEndTime().format(TIME))
                .mode(a.getMode())
                .locationOrLink(a.getLocationOrLink())
                .build();
    }
}