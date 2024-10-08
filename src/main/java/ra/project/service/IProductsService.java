package ra.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ProductRequest;
import ra.project.model.entity.Categories;
import ra.project.model.entity.Products;

import java.util.List;

public interface IProductsService {
    List<Products> getProducts();

    Products getProductById(Long id);

    Products insertProduct(ProductRequest products) throws CustomException;

    Products updateProduct(Long id, ProductRequest productRequest) throws CustomException;

    void deleteProduct(Long id) throws CustomException;

    Page<Products> getProductsWithPaginationAndSorting(Pageable pageable, String search) throws CustomException;

    List<Products> getTopNewProducts();

    List<Products> findProductsByCategoryId(Long id);
    Page<Products> getProductsWithPaginationAndSort(Pageable pageable, String search) throws CustomException;
    Page<Products> listProductsForSale(Pageable pageable);
}
