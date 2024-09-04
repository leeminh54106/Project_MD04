package ra.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Products;
import ra.project.model.entity.ShoppingCart;
import ra.project.model.entity.Users;
import ra.project.model.entity.WishList;

import java.util.List;
import java.util.Optional;

public interface IWishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUserId(Long id);
    Optional<WishList> findByIdAndUser(Long id, Users users);
    List<WishList> findByProduct(Products products);
    boolean existsByProductAndUser(Products products, Users users);
}
