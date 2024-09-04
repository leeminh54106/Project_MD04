package ra.project.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.Users;
import ra.project.service.IUserService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    //vào trang admin
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body("Chào mừng bạn đến với trang quản trị!");
    }

    //danh sách quyền
    @GetMapping("/userAdmin")
    public ResponseEntity<DataResponse> getAllUserAdmin() {
        return new ResponseEntity<>(new DataResponse(userService.getAllUsers(), HttpStatus.OK), HttpStatus.OK);
    }

    //tìm kiếm user,hiển thị user
    @GetMapping("/searchByName")
    public ResponseEntity<?> searchUserByName(@PageableDefault(page = 0,
            size = 3,
            sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable,
                                              @RequestParam(value = "search", defaultValue = "") String search) {
        return new ResponseEntity<>(new DataResponse(userService.getUsersWithPaginationAndSorting(pageable, search)
                .getContent(), HttpStatus.OK), HttpStatus.OK);
    }

    //khóa user
    @PutMapping("/user/{id}")
    public ResponseEntity<Users> changeUserStatus(@PathVariable Long id) throws CustomException {

        Users changeUserStatus = userService.updateUserStatus(id);
        return new ResponseEntity<>(changeUserStatus, HttpStatus.OK);
    }



}