package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.constants.RoleName;

import ra.project.model.entity.Roles;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByRoleName(RoleName roleName) ;
}
