package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.ApiResponse;
import hcmut.edu.vn.tutor_support_system.dto.SupportNeedDto;
import hcmut.edu.vn.tutor_support_system.entity.SupportPriority;
import hcmut.edu.vn.tutor_support_system.service.SupportNeedService;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/support-needs")
@RequiredArgsConstructor
public class SupportNeedController {

    private final SupportNeedService supportNeedService;

    @PostMapping
    @Operation(summary = "Declare support need", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Support need created",
                    content = @Content(schema = @Schema(implementation = SupportNeedDto.class)))
    })
    public ResponseEntity<ApiResponse<SupportNeedDto>> declare(@Valid @RequestBody SupportNeedDto dto) {
        SupportNeedDto saved = supportNeedService.declareSupportNeed(dto);
        return ResponseEntity.ok(ApiResponse.success("Support need created", saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update support need")
    public ResponseEntity<ApiResponse<SupportNeedDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody SupportNeedDto dto) {
        SupportNeedDto updated = supportNeedService.updateSupportNeed(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Support need updated", updated));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "List support needs")
    public ResponseEntity<ApiResponse<List<SupportNeedDto>>> list(@PathVariable Long studentId,
                                                                  @RequestParam(required = false) SupportPriority priority) {
        List<SupportNeedDto> needs = supportNeedService.getSupportNeedsByStudent(studentId, priority);
        return ResponseEntity.ok(ApiResponse.success("Support needs fetched", needs));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete support need")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        supportNeedService.deleteSupportNeed(id);
        return ResponseEntity.ok(ApiResponse.success("Support need deleted", null));
    }

    @DeleteMapping("/archive")
    @Operation(summary = "Archive old support needs")
    public ResponseEntity<ApiResponse<Void>> archive(@RequestParam LocalDate threshold) {
        supportNeedService.archiveOldNeeds(threshold);
        return ResponseEntity.ok(ApiResponse.success("Support needs archived", null));
    }
}
