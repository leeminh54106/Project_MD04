package ra.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.CategoryRequest;
import ra.project.model.entity.Categories;
import ra.project.model.entity.Products;

import java.util.List;

public interface ICategoriesService {
    List<Categories> getCategories();
    Categories getCategoriesById(Long id);
    Categories insertCategories(CategoryRequest categories) throws CustomException;
    Categories updateCategories(Long id,CategoryRequest categories) ;
    void deleteCategories(Long id);
    Page<Categories> getCategoriesWithPaginationAndSorting(Pageable pageable,String search);
    Page<Categories> listCategoriesForSale(Pageable pageable);
}
