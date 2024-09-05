package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project.constants.RoleName;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.UserRequest;
import ra.project.model.entity.Roles;
import ra.project.model.entity.Users;
import ra.project.repository.IUserRepository;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IUserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadFile uploadFile;

    @Override
    public Users getCurrentUser() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsers();
    }

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

    @Override
    public boolean changePassword(String oldPassword, String newPassword, String confirmPassword) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!passwordEncoder.matches(oldPassword, userDetails.getUsers().getPassword())) {
            throw new NoSuchElementException("Mật khẩu cũ của bạn không đúng, mời bạn nhập lại !");
        }
        if (oldPassword.equals(confirmPassword)) {
            throw new NoSuchElementException("Mật khẩu mới không được trùng với mật khẩu cũ!");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new NoSuchElementException("Nhập lại mật khẩu không chính xác !");
        }
        if(oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()){
            throw new NoSuchElementException("Mật khẩu không được để trống");
        }

        userDetails.getUsers().setPassword(passwordEncoder.encode(newPassword));
        userDetails.getUsers().setUpdatedAt(new Date());
        userRepository.save(userDetails.getUsers());
        return true;
    }

    @Override
    public Users updateUser(UserRequest userRequest) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRequest.getFullName() != null) {
            userDetails.getUsers().setFullName(userRequest.getFullName());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new NoSuchElementException("email đã tồn tại");
            }
            userDetails.getUsers().setEmail(userRequest.getEmail());
        }

        if (userRequest.getPhone() != null) {
            if (userRepository.existsByPhone(userRequest.getPhone())) {
                throw new NoSuchElementException("Số điện thoại đã tồn tại");
            }
            userDetails.getUsers().setPhone(userRequest.getPhone());
        }

        if (userRequest.getAddress() != null) {
            userDetails.getUsers().setAddress(userRequest.getAddress());
        }

        if (userRequest.getAvatar() != null) {
            userDetails.getUsers().setAvatar(uploadFile.uploadLocal(userRequest.getAvatar()));
        }

        userDetails.getUsers().setUpdatedAt(new Date());

        return userRepository.save(userDetails.getUsers());
    }
}
