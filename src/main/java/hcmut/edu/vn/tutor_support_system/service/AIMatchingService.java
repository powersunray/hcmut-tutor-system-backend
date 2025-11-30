package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.entity.Enrollment;
import hcmut.edu.vn.tutor_support_system.entity.SupportNeed;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AIMatchingService {

  private final double expertiseMatchWeight = 50.0;
  private final double enrollmentMatchWeight = 5.0;
  private final double supportNeedWeight = 10.0;
  private final double ratingWeight = 5.0;
  private final double experienceWeight = 0.5;
  private final double campusMatchWeight = 15.0;

  public List<Tutor> recommendTutors(Student student, List<Tutor> candidates, String subjectId) {
    return candidates.stream()
        .sorted(Comparator.comparingDouble((Tutor t) -> calculateScore(student, t, subjectId)).reversed())
        .collect(Collectors.toList());
  }

  private double calculateScore(Student student, Tutor tutor, String subjectId) {
    double score = 0.0;

    // 1. Expertise Match (Primary factor)
    List<String> expertise = tutor.getExpertiseAreas();
    if (expertise != null) {
      if (subjectId != null && expertise.contains(subjectId)) {
        score += expertiseMatchWeight; // High boost for requested subject
      }

      // Bonus for matching other enrollments
      if (student.getEnrollments() != null) {
        for (Enrollment enrollment : student.getEnrollments()) {
          if (enrollment.getSubject() != null && expertise.contains(enrollment.getSubject().getCode())) {
            score += enrollmentMatchWeight;
          } else if (enrollment.getCourseCode() != null && expertise.contains(enrollment.getCourseCode())) {
            score += enrollmentMatchWeight;
          }
        }
      }

      // Bonus for matching support needs (Description-based matching)
      if (student.getSupportNeeds() != null) {
        for (SupportNeed need : student.getSupportNeeds()) {
          if (need.getDescription() != null) {
            for (String area : expertise) {
              if (need.getDescription().toLowerCase().contains(area.toLowerCase())) {
                score += supportNeedWeight;
              }
            }
          }
        }
      }
    }

    // 2. Rating (Quality factor)
    score += tutor.getAverageRating() * ratingWeight; // 0-5 stars -> 0-25 points

    // 3. Experience (Quantity factor)
    score += Math.min(tutor.getRatingCount(), 20) * experienceWeight; // Up to 10 points for experience

    // 4. Campus Match (Logistics factor)
    if (student.getProfile() != null && tutor.getProfile() != null) {
      if (student.getProfile().getCampus() != null &&
          student.getProfile().getCampus().equalsIgnoreCase(tutor.getProfile().getCampus())) {
        score += campusMatchWeight;
      }
    }

    return score;
  }
}

