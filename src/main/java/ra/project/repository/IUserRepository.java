package ra.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Users;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users,Long> {
//    Optional<Users> findByEmail(String email);
    Optional<Users> findByUsername(String username);
    Page<Users> findByUsernameContaining(String username, Pageable pageable);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
