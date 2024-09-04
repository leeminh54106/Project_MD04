package ra.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.project.model.entity.Products;

import java.util.List;

public interface IProductsRepository extends JpaRepository<Products, Long> {
    Page<Products> findProductsByNameContainsIgnoreCase(String name, Pageable pageable);

    @Query("select p from Products p where p.name like %:keyword% or p.description like %:keyword%")
    Page<Products>findProductsByNameAndDescriptionContainingIgnoreCase(@Param("keyword") String search, Pageable pageable);
    boolean existsByName(String productName);
    List<Products> findProductsByCategoriesId(Long id);

//    @Query(value = "SELECT * FROM products ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Products> findTop5ByOrderByIdDesc();
    Page<Products> findProductsByStatusTrue(Pageable pageable);
    boolean existsByCategoriesId(Long id);
}
