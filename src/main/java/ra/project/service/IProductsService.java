package ra.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ProductRequest;
import ra.project.model.entity.Products;

import java.util.List;

public interface IProductsService {
    List<Products> getProducts();
    Products getProductById(Long id);
    Products insertProduct(ProductRequest products) throws CustomException;
    Products updateProduct(Long id,Products product);
    void deleteProduct(Long id);
    Page<Products> getProductsWithPaginationAndSorting(Pageable pageable,String search);
}
