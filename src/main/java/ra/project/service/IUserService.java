package ra.project.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project.model.entity.Users;


import java.util.List;
public interface IUserService {
   List<Users> getAllUsers();
    Users getUserById(Long id);
    Users getUserByUserName(String username);
    Users updateUserStatus(Long id, Boolean status);
    Page<Users> getUsersWithPaginationAndSorting(Pageable pageable, String search);
}
