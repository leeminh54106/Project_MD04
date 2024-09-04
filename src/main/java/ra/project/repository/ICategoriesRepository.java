package ra.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.project.model.entity.Categories;


public interface ICategoriesRepository extends JpaRepository<Categories, Long> {

    //List<Categories> findCategoriesByNameContains(String name);
    Page<Categories> findAllByNameContains(String name, Pageable pageable);

    boolean existsByName(String name);

    boolean existsById(Long id);

    Page<Categories> findCategoriesByStatusTrue(Pageable pageable);

}
