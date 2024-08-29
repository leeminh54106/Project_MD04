package ra.project.service;

import ra.project.model.entity.Categories;

import java.util.List;

public interface ICategoriesService {
    List<Categories> getCategories();
    Categories getCategoriesById(Long id);
    Categories insertCategories(Categories categories);
    Categories updateCategories(Long id,Categories categories);
    void deleteCategories(Long id);
    List<Categories> getCategoriesByName(String name);
}
