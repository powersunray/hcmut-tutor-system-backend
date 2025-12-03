package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.UserRole;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    // Basic user info
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    
    // Student-specific fields (nullable)
    private String studentId;
    private String faculty;
    private String major;
    
    // Tutor-specific fields (nullable)
    private String tutorId;
    private String bio;
    private List<String> expertiseAreas;
    private Double averageRating;
    private Integer ratingCount;
    
    // Profile fields
    private String phoneNumber;
    private String campus;
    private String address;
    private String gender;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}