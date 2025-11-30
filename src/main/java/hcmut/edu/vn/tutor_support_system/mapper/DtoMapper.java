package hcmut.edu.vn.tutor_support_system.mapper;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.dto.StaffDto;
import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.dto.TutorProfileDto;
import hcmut.edu.vn.tutor_support_system.dto.UserProfileDto;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.Staff;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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

  public static UserProfileDto toUserProfileDto(User user) {
    UserProfileDto.UserProfileDtoBuilder builder = UserProfileDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .role(user.getRole() != null ? user.getRole().name() : null)
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt());
        
    if (user.getProfile() != null) {
        builder.phoneNumber(user.getProfile().getPhoneNumber())
               .campus(user.getProfile().getCampus());
    }
    
    if (user instanceof Student) {
        Student student = (Student) user;
        builder.studentId(student.getStudentId())
               .faculty(student.getFaculty())
               .major(student.getMajor());
    } else if (user instanceof Tutor) {
        Tutor tutor = (Tutor) user;
        builder.tutorId(tutor.getTutorId())
               .bio(tutor.getBio()) // Tutor has its own bio field which might override or complement Profile bio
               .averageRating(tutor.getAverageRating())
               .ratingCount(tutor.getRatingCount())
               .expertiseAreas(tutor.getExpertiseAreasString());
    }

    return builder.build();
  }
}
