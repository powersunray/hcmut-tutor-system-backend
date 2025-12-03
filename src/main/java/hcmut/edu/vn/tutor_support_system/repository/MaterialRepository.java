package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Material;
import hcmut.edu.vn.tutor_support_system.entity.MaterialVisibility;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {

  Optional<Material> findByMaterialId(String materialId);

  List<Material> findBySession(Session session);

  List<Material> findByUploadedBy(User uploadedBy);

  List<Material> findByVisibility(MaterialVisibility visibility);

  @Query("SELECT m FROM Material m WHERE m.session.id = :sessionId ORDER BY m.createdAt DESC")
  List<Material> findBySessionIdOrderByCreatedAtDesc(@Param("sessionId") UUID sessionId);

  @Query(
      "SELECT m FROM Material m WHERE m.uploadedBy.id = :userId AND m.visibility = :visibility"
          + " ORDER BY m.createdAt DESC")
  List<Material> findByUploadedByIdAndVisibility(
      @Param("userId") UUID userId, @Param("visibility") MaterialVisibility visibility);

  @Query("SELECT COUNT(m) FROM Material m WHERE m.uploadedBy.id = :userId")
  Long countByUploadedById(@Param("userId") UUID userId);
}
