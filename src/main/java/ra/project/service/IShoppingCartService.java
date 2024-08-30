package ra.project.service;

import ra.project.model.dto.req.ShoppingCartRequest;
import ra.project.model.entity.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    List<ShoppingCart> findAllByUserId();
    ShoppingCart addNewCart(ShoppingCartRequest cart);
}
