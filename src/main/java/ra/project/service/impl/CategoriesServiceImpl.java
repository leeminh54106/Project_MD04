package ra.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project.model.entity.Categories;
import ra.project.repository.ICategoriesRepository;
import ra.project.service.ICategoriesService;

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
        return categoriesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là : " + id));
    }

    @Override
    public Categories insertCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    @Override
    public Categories updateCategories(Long id, Categories categories) {
        categoriesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là: " + id));
        categories.setId(id);
        return categoriesRepository.save(categories);
    }

    @Override
    public void deleteCategories(Long id) {
        categoriesRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy danh mục có mã là: " + id));
        categoriesRepository.deleteById(id);
    }

    @Override
    public List<Categories> getCategoriesByName(String name) {
        return categoriesRepository.findCategoriesByNameContains(name);
    }
}
