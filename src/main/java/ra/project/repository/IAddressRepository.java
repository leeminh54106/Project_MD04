package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Address;
import ra.project.model.entity.Users;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUsers(Users users);
    Optional<Address> findByIdAndUsers(Long id, Users users);
    boolean existsByPhone(String phone);
}