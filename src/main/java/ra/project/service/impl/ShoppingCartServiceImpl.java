package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ShoppingCartRequest;
import ra.project.model.entity.Products;
import ra.project.model.entity.ShoppingCart;
import ra.project.repository.IProductsRepository;
import ra.project.repository.IShoppingCartRepository;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IShoppingCartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements IShoppingCartService {
    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductsRepository productsRepository;

    @Override
    public List<ShoppingCart> findAllByUserId() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return shoppingCartRepository.findAllByUserId(userDetails.getUsers().getId());
    }

    @Override
    public ShoppingCart addNewCart(ShoppingCartRequest cart) throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ShoppingCart> optionalShoppingCart =
                shoppingCartRepository.findByUserIdAndProductId(
                        userDetails.getUsers().getId(),
                        cart.getProductId()
                );
        if (optionalShoppingCart.isPresent())
        {
            // đã tồn tại và thay đổi số lượng
            ShoppingCart shoppingCart = optionalShoppingCart.get();
            if (shoppingCart.getProduct().getQuantity() > shoppingCart.getQuantity() + cart.getQuantity())
            {
                shoppingCart.setQuantity(shoppingCart.getQuantity() + cart.getQuantity());
                return shoppingCartRepository.save(shoppingCart);
            }
            else
            {
                throw new CustomException("product not enough quantity", HttpStatus.BAD_REQUEST);
            }

        }
        else
        {
            // chưa tồn tại thì tạo đối tượng ShoppingCart và thêm mới vào database
            Products product = productsRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new CustomException("product not found", HttpStatus.NOT_FOUND));
            if (product.getQuantity() > cart.getQuantity())
            {
                ShoppingCart shoppingCart = ShoppingCart.builder()
                        .user(userDetails.getUsers())
                        .product(product)
                        .quantity(cart.getQuantity())
                        .build();
                return shoppingCartRepository.save(shoppingCart);
            }
            else
            {
                throw new CustomException("product not enough quantity", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @Override
    public void removeProductToCart(Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       ShoppingCart shoppingCart = shoppingCartRepository.findByIdAndUser(id, userDetails.getUsers())
               .orElseThrow(() -> new NoSuchElementException("giỏ hàng trống!"));
       shoppingCartRepository.delete(shoppingCart);

    }

    @Override
    public void removeAllProductToCart() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userDetails.getUsers().getId());
        shoppingCartRepository.deleteAll(shoppingCarts);
    }
}
