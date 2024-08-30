package ra.project.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.CategoryRequest;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.Categories;
import ra.project.service.ICategoriesService;

@RestController
@RequestMapping("/api/v1/admin/products/categories")
public class CategoriesController {
    @Autowired
    private ICategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllCategories() {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategories(), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategoriesById(id), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse> insertCategory(@Valid @RequestBody CategoryRequest categories) throws CustomException {
        return new ResponseEntity<>(new DataResponse(categoriesService.insertCategories(categories), HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateCategory( @PathVariable Long id,@Valid @RequestBody CategoryRequest categories) {
        return new ResponseEntity<>(new DataResponse(categoriesService.updateCategories(id, categories), HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategories(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công danh mục có mã là : " + id, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<DataResponse> searchByName(@PageableDefault(page = 0,
            size = 3,
            sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable,
                                                     @RequestParam(value = "search", defaultValue = "") String search) {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategoriesWithPaginationAndSorting(pageable, search), HttpStatus.OK), HttpStatus.OK);
    }

}
