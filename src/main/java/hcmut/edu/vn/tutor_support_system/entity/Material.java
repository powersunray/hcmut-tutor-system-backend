package hcmut.edu.vn.tutor_support_system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    private String id;
    private Session session;

    private String title;
    private String description;

    private MaterialSourceType sourceType;     // LOCAL_UPLOAD or LIBRARY_RESOURCE
    private String urlOrPath;                  // file path or library link

    private MaterialVisibility visibility;     // PRIVATE / SHARED_WITH_STUDENT
    private LocalDateTime uploadedAt;
}

