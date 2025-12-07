package hcmut.edu.vn.tutor_support_system.dto;
import hcmut.edu.vn.tutor_support_system.entity.SupportType;
import lombok.Data;

@Data
public class SupportNeedRequest {
    private SupportType supportType;
    private String description;
}