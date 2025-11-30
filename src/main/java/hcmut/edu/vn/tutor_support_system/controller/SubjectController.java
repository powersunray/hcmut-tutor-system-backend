package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.ApiResponse;
import hcmut.edu.vn.tutor_support_system.dto.SubjectDto;
import hcmut.edu.vn.tutor_support_system.service.SubjectService;
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
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create subject", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Subject created",
                    content = @Content(schema = @Schema(implementation = SubjectDto.class)))
    })
    public ResponseEntity<ApiResponse<SubjectDto>> create(@Valid @RequestBody SubjectDto dto) {
        SubjectDto created = subjectService.createSubject(dto);
        return ResponseEntity.ok(ApiResponse.success("Subject created", created));
    }

    @GetMapping
    @Operation(summary = "List subjects")
    public ResponseEntity<ApiResponse<List<SubjectDto>>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer credits) {
        List<SubjectDto> subjects = subjectService.getSubjectsByFilter(name, credits);
        return ResponseEntity.ok(ApiResponse.success("Subjects fetched", subjects));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject")
    public ResponseEntity<ApiResponse<SubjectDto>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Subject fetched", subjectService.getSubject(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject")
    public ResponseEntity<ApiResponse<SubjectDto>> update(@PathVariable Long id, @Valid @RequestBody SubjectDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Subject updated", subjectService.updateSubject(id, dto)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success("Subject deleted", null));
    }
}
