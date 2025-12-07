package hcmut.edu.vn.tutor_support_system.dto;

public record AvailabilityDTO1(
    String id,
    String day,
    String startTime,
    String endTime,
    String mode,
    String location_or_link,
    int capacity,
    boolean requiresApproval
) {}
