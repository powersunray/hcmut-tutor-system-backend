package hcmut.edu.vn.tutor_support_system.repository;

import hcmut.edu.vn.tutor_support_system.entity.User;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,UUID> {

    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<User> findByProfileId(UUID profileId);
    
    List<User> findByRole(String role);

}
