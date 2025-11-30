package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.Staff;
import hcmut.edu.vn.tutor_support_system.entity.StaffRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

  @Query("SELECT s FROM Staff s LEFT JOIN FETCH s.profile WHERE s.id = :id")
  Optional<Staff> findByIdWithProfile(@Param("id") UUID id);

  @Query("SELECT s FROM Staff s LEFT JOIN FETCH s.profile")
  List<Staff> findAllWithProfile();

  @Query("SELECT s FROM Staff s LEFT JOIN FETCH s.profile WHERE s.staffId = :staffId")
  Optional<Staff> findByStaffId(@Param("staffId") String staffId);

  @Query("SELECT s FROM Staff s " + "LEFT JOIN FETCH s.profile " + "WHERE s.staffRole = :staffRole")
  List<Staff> findByStaffRole(@Param("staffRole") StaffRole staffRole);

  @Query(
      "SELECT s FROM Staff s " + "LEFT JOIN FETCH s.profile " + "WHERE s.department = :department")
  List<Staff> findByDepartment(@Param("department") String department);

  @Query("SELECT s FROM Staff s LEFT JOIN FETCH s.profile WHERE s.email = :email")
  Optional<Staff> findByEmail(@Param("email") String email);

  @Query(
      "SELECT s FROM Staff s "
          + "LEFT JOIN FETCH s.profile "
          + "WHERE s.staffRole = :staffRole AND s.department = :department")
  List<Staff> findByStaffRoleAndDepartment(
      @Param("staffRole") StaffRole staffRole, @Param("department") String department);

  @Query("SELECT COUNT(s) FROM Staff s WHERE s.staffRole = :staffRole")
  Long countByStaffRole(@Param("staffRole") StaffRole staffRole);
}
