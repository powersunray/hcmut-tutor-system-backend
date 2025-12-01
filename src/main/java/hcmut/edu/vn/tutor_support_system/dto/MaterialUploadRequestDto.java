package hcmut.edu.vn.tutor_support_system.dto;

import hcmut.edu.vn.tutor_support_system.entity.MaterialSourceType;
import hcmut.edu.vn.tutor_support_system.entity.MaterialVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialUploadRequestDto {

  @NotBlank(message = "Session ID is required") private String sessionId;

  @NotBlank(message = "Material name is required") private String name;

  @NotNull(message = "Source type is required") private MaterialSourceType sourceType;

  @NotBlank(message = "Content URL is required") private String contentUrl;

  @NotNull(message = "Visibility is required") private MaterialVisibility visibility;

  @NotBlank(message = "Uploader user ID is required") private String uploadedByUserId;
}
