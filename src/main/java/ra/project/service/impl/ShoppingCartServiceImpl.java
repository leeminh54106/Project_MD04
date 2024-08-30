package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.project.model.dto.req.ShoppingCartRequest;
import ra.project.model.entity.ShoppingCart;
import ra.project.service.IShoppingCartService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {
    @Override
    public List<ShoppingCart> findAllByUserId() {
        return List.of();
    }

    @Override
    public ShoppingCart addNewCart(ShoppingCartRequest cart) {
        return null;
    }
}
