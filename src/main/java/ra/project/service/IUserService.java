package ra.project.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.UserRequest;
import ra.project.model.entity.Roles;
import ra.project.model.entity.Users;


import java.util.List;
public interface IUserService {
   List<Roles> getAllUsers();
    Users getUserById(Long id);
    Users getUserByUserName(String username);
    Users updateUserStatus(Long id) throws CustomException;
    Page<Users> getUsersWithPaginationAndSorting(Pageable pageable, String search);
    boolean changePassword(String oldPassword, String newPassword, String confirmPassword);
    Users updateUser(UserRequest userRequest);
}
