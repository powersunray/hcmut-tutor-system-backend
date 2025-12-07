package hcmut.edu.vn.tutor_support_system.mapper;

import hcmut.edu.vn.tutor_support_system.dto.AvailabilityDto;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.dto.EvaluationDto;
import hcmut.edu.vn.tutor_support_system.dto.FeedbackDto;
import hcmut.edu.vn.tutor_support_system.dto.MaterialDto;
import hcmut.edu.vn.tutor_support_system.dto.MeetingNoteDto;
import hcmut.edu.vn.tutor_support_system.dto.StaffDto;
import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.dto.UserDto;
import hcmut.edu.vn.tutor_support_system.dto.CreateUserRequest;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.dto.TutorProfileDto;
import hcmut.edu.vn.tutor_support_system.dto.UserProfileDto;
import hcmut.edu.vn.tutor_support_system.dto.*;
import hcmut.edu.vn.tutor_support_system.entity.Availability;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.Evaluation;
import hcmut.edu.vn.tutor_support_system.entity.Feedback;
import hcmut.edu.vn.tutor_support_system.entity.Material;
import hcmut.edu.vn.tutor_support_system.entity.MeetingNote;
import hcmut.edu.vn.tutor_support_system.entity.Staff;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.User;

import hcmut.edu.vn.tutor_support_system.dto.SessionResponseDto;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public final class DtoMapper {
  private DtoMapper() {}

  private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

  public static AvailabilityDto toAvailabilityDto(Availability a) {
    return AvailabilityDto.builder()
        .uuid(a.getUuid() != null ? a.getUuid().toString() : null) // Real database UUID
        .availabilityId(a.getAvailabilityId()) // Business ID (e.g., "slot-1")
        .dayOfWeek(a.getDayOfWeek().name())
        .startTime(a.getStartTime().format(TIME))
        .endTime(a.getEndTime().format(TIME))
        .mode(a.getMode())
        .locationOrLink(a.getLocationOrLink())
        .capacity(a.getCapacity())
        .published(a.isPublished())
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

  public static UserDto toUserDto(User u) {
    if (u == null) return null;
    return UserDto.builder()
        .id(u.getId())
        .firstName(u.getFirstName())
        .lastName(u.getLastName())
        .email(u.getEmail())
        .role(u.getRole())
        .createdAt(u.getCreatedAt())
        .updatedAt(u.getUpdatedAt())
        .build();
}

    public static User toUserEntity(CreateUserRequest req) {
        if (req == null) return null;
        User u = new User();
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setEmail(req.getEmail());
        u.setRole(req.getRole());
        return u;
    }
  public static UserProfileDto toUserProfileDto(User user) {
    UserProfileDto.UserProfileDtoBuilder builder = UserProfileDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .role(user.getRole())
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
               .expertiseAreas(tutor.getExpertiseAreas());
    }

    return builder.build();
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
        .studentUuid(session.getStudent() != null ? session.getStudent().getId().toString() : null)
        .title(session.getTitle())
        .studentName(studentName)
        .studentEmail(session.getStudent() != null ? session.getStudent().getEmail() : null)
        .startTime(session.getStartTime())
        .endTime(session.getEndTime())
        .mode(session.getMode())
        .locationOrLink(session.getLocationOrLink())
        .status(session.getStatus())
        .build();
  }

  public static FeedbackDto toFeedbackDto(Feedback feedback) {
    return FeedbackDto.builder()
        .feedbackId(feedback.getFeedbackId())
        .sessionId(feedback.getSession() != null ? feedback.getSession().getSessionId() : null)
        .studentName(
            feedback.getSession() != null && feedback.getSession().getStudent() != null
                ? feedback.getSession().getStudent().getFirstName()
                    + " "
                    + feedback.getSession().getStudent().getLastName()
                : null)
        .tutorName(
            feedback.getSession() != null && feedback.getSession().getTutor() != null
                ? feedback.getSession().getTutor().getFirstName()
                    + " "
                    + feedback.getSession().getTutor().getLastName()
                : null)
        .rating(feedback.getRating())
        .comment(feedback.getComment())
        .createdAt(feedback.getCreatedAt())
        .updatedAt(feedback.getUpdatedAt())
        .build();
  }

  public static EvaluationDto toEvaluationDto(Evaluation evaluation) {
    return EvaluationDto.builder()
        .evaluationId(evaluation.getEvaluationId())
        .sessionId(evaluation.getSession() != null ? evaluation.getSession().getSessionId() : null)
        .evaluatorName(
            evaluation.getEvaluator() != null
                ? evaluation.getEvaluator().getFirstName()
                    + " "
                    + evaluation.getEvaluator().getLastName()
                : null)
        .evaluatorEmail(
            evaluation.getEvaluator() != null ? evaluation.getEvaluator().getEmail() : null)
        .tutorName(
            evaluation.getSession() != null && evaluation.getSession().getTutor() != null
                ? evaluation.getSession().getTutor().getFirstName()
                    + " "
                    + evaluation.getSession().getTutor().getLastName()
                : null)
        .content(evaluation.getContent())
        .createdAt(evaluation.getCreatedAt())
        .updatedAt(evaluation.getUpdatedAt())
        .build();
  }

  public static MaterialDto toMaterialDto(Material material) {
    return MaterialDto.builder()
        .materialId(material.getMaterialId())
        .sessionId(material.getSession() != null ? material.getSession().getSessionId() : null)
        .name(material.getName())
        .sourceType(material.getSourceType())
        .contentUrl(material.getContentUrl())
        .visibility(material.getVisibility())
        .uploadedByName(
            material.getUploadedBy() != null
                ? material.getUploadedBy().getFirstName()
                    + " "
                    + material.getUploadedBy().getLastName()
                : null)
        .uploadedByEmail(
            material.getUploadedBy() != null ? material.getUploadedBy().getEmail() : null)
        .createdAt(material.getCreatedAt())
        .updatedAt(material.getUpdatedAt())
        .build();
  }

  public static MeetingNoteDto toMeetingNoteDto(MeetingNote meetingNote) {
    return MeetingNoteDto.builder()
        .noteId(meetingNote.getNoteId())
        .sessionId(
            meetingNote.getSession() != null ? meetingNote.getSession().getSessionId() : null)
        .createdByName(
            meetingNote.getCreatedBy() != null
                ? meetingNote.getCreatedBy().getFirstName()
                    + " "
                    + meetingNote.getCreatedBy().getLastName()
                : null)
        .createdByEmail(
            meetingNote.getCreatedBy() != null ? meetingNote.getCreatedBy().getEmail() : null)
        .content(meetingNote.getContent())
        .createdAt(meetingNote.getCreatedAt())
        .updatedAt(meetingNote.getUpdatedAt())
        .build();
  }
}
