package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findAllByUserId(Long id);
    Optional<ShoppingCart> findByUserIdAndProductId(Long userId,Long productId);
}
