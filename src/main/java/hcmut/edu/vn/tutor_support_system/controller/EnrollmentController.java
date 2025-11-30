package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.ApiResponse;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentDto;
import hcmut.edu.vn.tutor_support_system.dto.EnrollmentRequest;
import hcmut.edu.vn.tutor_support_system.entity.EnrollmentStatus;
import hcmut.edu.vn.tutor_support_system.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll a student", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Enrollment created",
                    content = @Content(schema = @Schema(implementation = EnrollmentDto.class)))
    })
    public ResponseEntity<ApiResponse<EnrollmentDto>> enroll(@Valid @RequestBody EnrollmentRequest request) {
        EnrollmentDto dto = enrollmentService.enrollStudent(request);
        return ResponseEntity.ok(ApiResponse.success("Enrollment created", dto));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments for a student")
    public ResponseEntity<ApiResponse<List<EnrollmentDto>>> list(@PathVariable Long studentId) {
        List<EnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Enrollments fetched", enrollments));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update enrollment status")
    public ResponseEntity<ApiResponse<EnrollmentDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        EnrollmentDto dto = enrollmentService.updateEnrollmentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Status updated", dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel enrollment")
    public ResponseEntity<ApiResponse<Void>> cancel(@PathVariable Long id) {
        enrollmentService.cancelEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment cancelled", null));
    }
}
