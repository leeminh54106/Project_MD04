package ra.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Products;

public interface IProductsRepository extends JpaRepository<Products, Long> {
    Page<Products> findProductsByNameContainsIgnoreCase(String name, Pageable pageable);
    boolean existsByName(String productName);
}
