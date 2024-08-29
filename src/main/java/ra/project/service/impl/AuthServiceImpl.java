package ra.project.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project.constants.RoleName;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.FormLogin;
import ra.project.model.dto.req.FormRegister;
import ra.project.model.dto.resp.JwtResponse;
import ra.project.model.entity.Roles;
import ra.project.model.entity.Users;
import ra.project.repository.IRoleRepository;
import ra.project.repository.IUserRepository;
import ra.project.security.jwt.JwtProvider;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IAuthService;

import javax.management.relation.Role;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    private final AuthenticationManager manager;
    private final JwtProvider jwtProvider;

    @Override
    public void register(FormRegister formRegister) throws CustomException {
        if (userRepository.existsByUsername(formRegister.getUsername())) {
            throw new CustomException("Tên đăng nhập đã tồn tại", HttpStatus.CONFLICT);
        }
        Set<Roles> roles = new HashSet<>();
        roles.add(findByRoleName(RoleName.ROLE_USER));
        Users user = Users.builder()
                .username(formRegister.getUsername())
                .fullName(formRegister.getFullName())
                .email(formRegister.getEmail())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .phone(formRegister.getPhone())
                .roles(roles)
                .status(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .isDeleted(false)
                .build();
        userRepository.save(user);
    }

    public Roles findByRoleName(RoleName roleName) throws CustomException {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new CustomException("Không tìm thấy quyền", HttpStatus.NOT_FOUND));
    }

    @Override
    public JwtResponse login(FormLogin formLogin) throws CustomException {
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException("Tên người dùng hoặc mật khẩu không đúng!", HttpStatus.BAD_REQUEST);
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        if (!userDetails.getUsers().getStatus()) {
            throw new CustomException("Tài khoản của bạn đã bị khóa!", HttpStatus.BAD_REQUEST);
        }
        return JwtResponse.builder()
                .accessToken(jwtProvider.generateToken(userDetails.getUsername()))
                .username(userDetails.getUsername())
                .fullName(userDetails.getUsers().getFullName())
                .email(userDetails.getUsers().getEmail())
                .address(userDetails.getUsers().getAddress())
                .avatar(userDetails.getUsers().getAvatar())
                .createdAt(userDetails.getUsers().getCreatedAt())
                .updatedAt(userDetails.getUsers().getUpdatedAt())
                .isDeleted(userDetails.getUsers().getIsDeleted())
                .phone(userDetails.getUsers().getPhone())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .status(userDetails.getUsers().getStatus())
                .build();

    }


}
