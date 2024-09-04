package ra.project.controller.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ShoppingCartRequest;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.ShoppingCart;
import ra.project.service.IShoppingCartService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/user/cart")
@RequiredArgsConstructor
public class CartController {
    private final IShoppingCartService shoppingCartService;

    //hiển thị cart
    @GetMapping("/list")
    public ResponseEntity<?> getCart(){
        return ResponseEntity.ok().body(shoppingCartService.findAllByUserId());
    }

    //thêm
    @PostMapping("/add")
    public ResponseEntity<?> addShoppingCart(@RequestBody ShoppingCartRequest cart) throws CustomException {
        return ResponseEntity.created(URI.create("/api/v1/user/cart")).body(shoppingCartService.addNewCart(cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> removeFromCart(@PathVariable Long id){
        shoppingCartService.removeProductToCart(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa sản phẩm có id là : "+id, HttpStatus.OK), HttpStatus.OK);
    }


    @DeleteMapping("/deleteAllProduct")
    public ResponseEntity<DataResponse> deleteAllProduct(){
        shoppingCartService.removeAllProductToCart();
        return new ResponseEntity<>(new DataResponse("Đã xóa hết giỏ hàng", HttpStatus.OK), HttpStatus.OK);
    }


}
