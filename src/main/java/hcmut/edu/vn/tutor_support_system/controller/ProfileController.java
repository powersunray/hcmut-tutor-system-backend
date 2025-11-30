package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.UserProfileDto;
import hcmut.edu.vn.tutor_support_system.entity.Profile;
import hcmut.edu.vn.tutor_support_system.entity.Student;
import hcmut.edu.vn.tutor_support_system.entity.Tutor;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.StudentRepository;
import hcmut.edu.vn.tutor_support_system.repository.TutorRepository;
import hcmut.edu.vn.tutor_support_system.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    // Get profile by user ID
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok(DtoMapper.toUserProfileDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable UUID id,
            @RequestBody UserProfileDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        // Update or create Profile
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setProfileId("PROF-" + UUID.randomUUID());
            user.setProfile(profile);
        }

        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setCampus(dto.getCampus());
        profile.setAddress(dto.getAddress());
        profile.setGender(dto.getGender());

        // Student-specific fields
        if (user instanceof Student student) {
            student.setFaculty(dto.getFaculty());
            student.setMajor(dto.getMajor());
            studentRepository.save(student);

        // Tutor-specific fields
        } else if (user instanceof Tutor tutor) {
            tutor.setBio(dto.getBio());
            tutor.setExpertiseAreas(dto.getExpertiseAreas());
            tutorRepository.save(tutor);

        } else {
            userRepository.save(user);
        }

        return ResponseEntity.ok(DtoMapper.toUserProfileDto(user));
    }

    @PostMapping("/tutor/{tutorId}/credentials")
    public ResponseEntity<String> uploadCredentials(
            @PathVariable String tutorId,
            @RequestParam("file") MultipartFile file) {

        Tutor tutor = tutorRepository.findByTutorId(tutorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found: " + tutorId));

        String filename = file.getOriginalFilename();

        // TODO: save file to storage (filesystem, cloud, etc.) and link to tutor profile
        // Example: tutorRepository.saveCredentials(tutor, fileContent);

        return ResponseEntity.ok("Credentials uploaded: " + filename);
    }
}
