package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.MaterialDto;
import hcmut.edu.vn.tutor_support_system.dto.MaterialUploadRequestDto;
import hcmut.edu.vn.tutor_support_system.entity.Material;
import hcmut.edu.vn.tutor_support_system.entity.MaterialVisibility;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.exception.MaterialUploadException;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.MaterialRepository;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MaterialService {

  private final MaterialRepository materialRepository;
  private final SessionRepository sessionRepository;

  @Transactional(readOnly = true)
  public MaterialDto getMaterialById(String materialId) {
    ValidationUtil.validateNotEmpty(materialId, "Material ID");

    Material material =
        materialRepository
            .findByMaterialId(materialId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Material not found with id: " + materialId));
    return DtoMapper.toMaterialDto(material);
  }

  @Transactional(readOnly = true)
  public List<MaterialDto> getMaterialsBySessionId(String sessionId) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    List<Material> materials = materialRepository.findBySession(session);
    return materials.stream().map(DtoMapper::toMaterialDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MaterialDto> getMaterialsByVisibility(MaterialVisibility visibility) {
    ValidationUtil.validateNotNull(visibility, "Visibility");

    List<Material> materials = materialRepository.findByVisibility(visibility);
    return materials.stream().map(DtoMapper::toMaterialDto).collect(Collectors.toList());
  }

  @Transactional
  public MaterialDto uploadMaterial(MaterialUploadRequestDto request) {
    ValidationUtil.validateNotNull(request, "Material upload request");
    ValidationUtil.validateNotEmpty(request.getSessionId(), "Session ID");
    ValidationUtil.validateNotEmpty(request.getName(), "Material name");
    ValidationUtil.validateNotEmpty(request.getContentUrl(), "Content URL");

    Session session =
        sessionRepository
            .findBySessionId(request.getSessionId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Session not found with id: " + request.getSessionId()));

    if (request.getContentUrl() == null || request.getContentUrl().trim().isEmpty()) {
      throw new MaterialUploadException("Material content URL cannot be empty");
    }

    Material material = new Material();
    material.setMaterialId("MAT-" + UUID.randomUUID().toString());
    material.setSession(session);
    material.setName(request.getName());
    material.setSourceType(request.getSourceType());
    material.setContentUrl(request.getContentUrl());
    material.setVisibility(request.getVisibility());

    User uploader = new User();
    uploader.setId(UUID.fromString(request.getUploadedByUserId()));
    material.setUploadedBy(uploader);

    Material savedMaterial = materialRepository.save(material);
    return DtoMapper.toMaterialDto(savedMaterial);
  }

  @Transactional
  public MaterialDto updateMaterial(String materialId, MaterialUploadRequestDto request) {
    ValidationUtil.validateNotEmpty(materialId, "Material ID");
    ValidationUtil.validateNotNull(request, "Material update request");

    Material material =
        materialRepository
            .findByMaterialId(materialId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Material not found with id: " + materialId));

    if (request.getName() != null && !request.getName().trim().isEmpty()) {
      material.setName(request.getName());
    }
    if (request.getSourceType() != null) {
      material.setSourceType(request.getSourceType());
    }
    if (request.getContentUrl() != null && !request.getContentUrl().trim().isEmpty()) {
      material.setContentUrl(request.getContentUrl());
    }
    if (request.getVisibility() != null) {
      material.setVisibility(request.getVisibility());
    }

    Material updatedMaterial = materialRepository.save(material);
    return DtoMapper.toMaterialDto(updatedMaterial);
  }

  @Transactional
  public void deleteMaterial(String materialId) {
    ValidationUtil.validateNotEmpty(materialId, "Material ID");

    Material material =
        materialRepository
            .findByMaterialId(materialId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Material not found with id: " + materialId));

    materialRepository.delete(material);
  }

  @Transactional(readOnly = true)
  public List<MaterialDto> getAllMaterials() {
    List<Material> materials = materialRepository.findAll();
    return materials.stream().map(DtoMapper::toMaterialDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Long getMaterialCountByUserId(UUID userId) {
    ValidationUtil.validateNotNull(userId, "User ID");

    return materialRepository.countByUploadedById(userId);
  }
}
