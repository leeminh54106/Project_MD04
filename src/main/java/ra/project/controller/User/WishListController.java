package ra.project.controller.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.WishListRequest;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.dto.resp.WishListResponse;
import ra.project.service.IWishListService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class WishListController {
    private final IWishListService wishListService;

    @PostMapping("/wish-list")
    public ResponseEntity<WishListResponse> addWishList(@RequestBody WishListRequest wishListRequest) throws CustomException {
        WishListResponse wishListResponse = wishListService.addWishList(wishListRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListResponse);
    }

    @GetMapping("/wish-list")
    public ResponseEntity<List<WishListResponse>> getWishList() throws CustomException {
        List<WishListResponse> wishListResponses = wishListService.getAllWishList();
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponses);
    }
    @DeleteMapping("/wish-list/{id}")
    public ResponseEntity<DataResponse> deleteWishList(@PathVariable Long id) throws CustomException {
        wishListService.deleteWishList(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
