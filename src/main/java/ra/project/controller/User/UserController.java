package ra.project.controller.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.project.model.dto.req.UserRequest;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.Users;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.ICategoriesService;
import ra.project.service.IProductsService;
import ra.project.service.IUserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController{
    private final ICategoriesService categoriesService;
    private final IProductsService productsService;
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getUser()
    {
        Users currentUser = userService.getCurrentUser();
//        return ResponseEntity.ok(currentUser);
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(userDetails.getUsers());

    }

    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody UserRequest userRequest) {
        boolean result = userService.changePassword(userRequest.getOldPassword(), userRequest.getNewPassword(), userRequest.getConfirmNewPassword());
        if (result) {
            return ResponseEntity.ok("Đổi mật khẩu thành công !!");
        } else {
            return ResponseEntity.badRequest().body("Thay đổi mật khẩu thất bại");
        }
    }

    @PutMapping("/update-user")
    public ResponseEntity<DataResponse> updateUser(@Valid @ModelAttribute UserRequest userRequest) {
        return new ResponseEntity<>(new DataResponse(userService.updateUser(userRequest), HttpStatus.OK), HttpStatus.OK);
    }


}
