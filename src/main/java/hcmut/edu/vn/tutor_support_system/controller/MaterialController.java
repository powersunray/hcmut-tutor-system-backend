package hcmut.edu.vn.tutor_support_system.controller;

import hcmut.edu.vn.tutor_support_system.dto.MaterialDto;
import hcmut.edu.vn.tutor_support_system.dto.MaterialUploadRequestDto;
import hcmut.edu.vn.tutor_support_system.entity.MaterialVisibility;
import hcmut.edu.vn.tutor_support_system.service.MaterialService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

  private final MaterialService materialService;

  @GetMapping("/{materialId}")
  public ResponseEntity<MaterialDto> getMaterialById(@PathVariable String materialId) {
    MaterialDto material = materialService.getMaterialById(materialId);
    return ResponseEntity.ok(material);
  }

  @GetMapping("/session/{sessionId}")
  public ResponseEntity<List<MaterialDto>> getMaterialsBySessionId(@PathVariable String sessionId) {
    List<MaterialDto> materials = materialService.getMaterialsBySessionId(sessionId);
    return ResponseEntity.ok(materials);
  }

  @GetMapping
  public ResponseEntity<List<MaterialDto>> getMaterials(
      @RequestParam(required = false) MaterialVisibility visibility) {
    List<MaterialDto> materials;

    if (visibility != null) {
      materials = materialService.getMaterialsByVisibility(visibility);
    } else {
      materials = materialService.getAllMaterials();
    }

    return ResponseEntity.ok(materials);
  }

  @PostMapping
  public ResponseEntity<MaterialDto> uploadMaterial(
      @Valid @RequestBody MaterialUploadRequestDto request) {
    MaterialDto material = materialService.uploadMaterial(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(material);
  }

  @PutMapping("/{materialId}")
  public ResponseEntity<MaterialDto> updateMaterial(
      @PathVariable String materialId, @Valid @RequestBody MaterialUploadRequestDto request) {
    MaterialDto material = materialService.updateMaterial(materialId, request);
    return ResponseEntity.ok(material);
  }

  @DeleteMapping("/{materialId}")
  public ResponseEntity<Void> deleteMaterial(@PathVariable String materialId) {
    materialService.deleteMaterial(materialId);
    return ResponseEntity.noContent().build();
  }
}
