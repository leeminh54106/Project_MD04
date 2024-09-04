package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Products;
import ra.project.model.entity.ShoppingCart;
import ra.project.model.entity.Users;

import java.util.List;
import java.util.Optional;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findAllByUserId(Long id);
    Optional<ShoppingCart> findByUserIdAndProductId(Long userId,Long productId);
    Optional<ShoppingCart> findByIdAndUser(Long id, Users users);
    List<ShoppingCart> findByProduct(Products products);
    List<ShoppingCart> findAllByUser(Users user);
}
