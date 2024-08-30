package ra.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.CategoryRequest;
import ra.project.model.entity.Categories;
import ra.project.model.entity.Products;
import ra.project.repository.ICategoriesRepository;
import ra.project.service.ICategoriesService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriesServiceImpl implements ICategoriesService {
    @Autowired
    private ICategoriesRepository categoriesRepository;

    @Override
    public List<Categories> getCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories getCategoriesById(Long id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là : " + id));
    }

    @Override
    public Categories insertCategories(CategoryRequest categories) throws CustomException {
        if (categoriesRepository.existsByName(categories.getCategoryName())) {
            throw new CustomException("Tên danh mục đã tồn tại", HttpStatus.CONFLICT);
        }
        Categories category = Categories.builder()
                .name(categories.getCategoryName())
                .description(categories.getDescription())
                .status(true)
                .build();
        return categoriesRepository.save(category);
    }

    @Override
    public Categories updateCategories(Long id, CategoryRequest categories) {
//        if (categories == null) {
//            throw new CustomException(" ", HttpStatus.BAD_REQUEST);
//        }
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là: " + id));
        category.setName(categories.getCategoryName());
        category.setDescription(categories.getDescription());
        category.setStatus(true);
        return categoriesRepository.save(category);
    }

    @Override
    public void deleteCategories(Long id) {
        categoriesRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là: " + id));
        categoriesRepository.deleteById(id);
    }


    @Override
    public Page<Categories> getCategoriesWithPaginationAndSorting(Pageable pageable, String search) {
        Page<Categories> categories;
        if(search.isEmpty()){
            categories = categoriesRepository.findAll(pageable);
        }else {
            categories = categoriesRepository.findAllByNameContains(search,pageable);
        }
        return categories;
    }
}
