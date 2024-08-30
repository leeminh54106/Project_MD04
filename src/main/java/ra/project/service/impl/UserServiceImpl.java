package ra.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project.constants.RoleName;
import ra.project.exception.CustomException;
import ra.project.model.entity.Roles;
import ra.project.model.entity.Users;
import ra.project.repository.IUserRepository;
import ra.project.service.IUserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<Roles> getAllUsers() {
       List<Users> users = userRepository.findAll();
    List <Roles> roles = users.stream()
            .flatMap(item -> item.getRoles().stream())
            .distinct()
            .collect(Collectors.toList());
            return roles;
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng có mã là: " + id));
    }

    @Override
    public Users getUserByUserName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng có tên là : " + username));
    }

    @Override
    public Users updateUserStatus(Long id) throws CustomException {
       Users user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy người dùng có mã là: " + id));

        if ( user.getRoles().stream().anyMatch(roles -> roles.getRoleName().equals(RoleName.ROLE_ADMIN))){
            throw new CustomException("admin không thể thay đổi trạng thái!", HttpStatus.FORBIDDEN);
        }
        user.setStatus(!user.getStatus());
        return userRepository.save(user);
    }

    @Override
    public Page<Users> getUsersWithPaginationAndSorting(Pageable pageable, String search) {
       Page<Users> userPage;
       if(search.isEmpty()){
           userPage = userRepository.findAll(pageable);
       }else {
           userPage = userRepository.findByUsernameContaining(search,pageable);
           if (userPage.isEmpty()){
               throw new NoSuchElementException("Không tìm thấy tên người dùng!");
           }
       }
       return userPage;
    }
}
