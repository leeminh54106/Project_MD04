package ra.project.service;

import ra.project.exception.CustomException;
import ra.project.model.dto.req.ShoppingCartRequest;
import ra.project.model.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    List<ShoppingCart> findAllByUserId();
    ShoppingCart addNewCart(ShoppingCartRequest cart) throws CustomException;
    void removeProductToCart(Long id);
    void removeAllProductToCart();
}
