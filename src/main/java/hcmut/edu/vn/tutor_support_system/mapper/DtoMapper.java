package hcmut.edu.vn.tutor_support_system.mapper;

import hcmut.edu.vn.tutor_support_system.dto.*;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.Staff;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.Session;
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

  public static EnrollmentDto toEnrollmentDto(Enrollment enrollment) {
    return EnrollmentDto.builder()
        .enrollmentId(enrollment.getId() != null ? enrollment.getId().toString() : null)
        .studentId(enrollment.getStudent() != null ? enrollment.getStudent().getStudentId() : null)
        .studentName(
            enrollment.getStudent() != null
                ? enrollment.getStudent().getFirstName()
                    + " "
                    + enrollment.getStudent().getLastName()
                : null)
        .subjectId(
            enrollment.getSubject() != null ? enrollment.getSubject().getId().toString() : null)
        .subjectCode(enrollment.getSubject() != null ? enrollment.getSubject().getCode() : null)
        .subjectName(enrollment.getSubject() != null ? enrollment.getSubject().getName() : null)
        .courseCode(enrollment.getCourseCode())
        .semester(enrollment.getSemester())
        .grade(enrollment.getGrade())
        .enrollmentStatus(enrollment.getEnrollmentStatus())
        .createdAt(enrollment.getCreatedAt())
        .updatedAt(enrollment.getUpdatedAt())
        .build();
  }

  public static SupportNeedDto toSupportNeedDto(SupportNeed supportNeed) {
    return SupportNeedDto.builder()
        .supportNeedId(supportNeed.getId() != null ? supportNeed.getId().toString() : null)
        .studentId(
            supportNeed.getStudent() != null ? supportNeed.getStudent().getStudentId() : null)
        .studentName(
            supportNeed.getStudent() != null
                ? supportNeed.getStudent().getFirstName()
                    + " "
                    + supportNeed.getStudent().getLastName()
                : null)
        .supportType(supportNeed.getSupportType())
        .description(supportNeed.getDescription())
        .status(supportNeed.getStatus())
        .createdAt(supportNeed.getCreatedAt())
        .updatedAt(supportNeed.getUpdatedAt())
        .build();
  }

  public static StaffDto toStaffDto(Staff staff) {
    return StaffDto.builder()
        .staffId(staff.getStaffId())
        .firstName(staff.getFirstName())
        .lastName(staff.getLastName())
        .email(staff.getEmail())
        .phoneNumber(staff.getProfile() != null ? staff.getProfile().getPhoneNumber() : null)
        .staffRole(staff.getStaffRole())
        .department(staff.getDepartment())
        .campus(staff.getProfile() != null ? staff.getProfile().getCampus() : null)
        .build();
  }

  public static SessionResponseDto toSessionResponseDto(Session session) {
    if (session == null) {
      return null;
    }

    String tutorName = (session.getTutor() != null)
        ? session.getTutor().getFirstName() + " " + session.getTutor().getLastName()
        : null;

    String studentName = (session.getStudent() != null)
        ? session.getStudent().getFirstName() + " " + session.getStudent().getLastName()
        : null;

    return SessionResponseDto.builder()
        .sessionId(session.getSessionId())
        .tutorId(session.getTutor() != null ? session.getTutor().getTutorId() : null)
        .tutorName(tutorName)
        .studentId(session.getStudent() != null ? session.getStudent().getStudentId() : null)
        .studentName(studentName)
        .startTime(session.getStartTime())
        .endTime(session.getEndTime())
        .mode(session.getMode())
        .locationOrLink(session.getLocationOrLink())
        .status(session.getStatus())
        .build();
  }
}
