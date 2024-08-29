package ra.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.Categories;
import ra.project.service.ICategoriesService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoriesController {
    @Autowired
    private ICategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllCategories() {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategories(), HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable Long id) {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategoriesById(id), HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse> insertCategory(@RequestBody Categories categories) {
        return new ResponseEntity<>(new DataResponse(categoriesService.insertCategories(categories), HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateCategory(@PathVariable Long id, @RequestBody Categories categories) {
        return new ResponseEntity<>(new DataResponse(categoriesService.updateCategories(id, categories), HttpStatus.OK),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategories(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công danh mục có mã là : "+id,HttpStatus.NO_CONTENT),HttpStatus.NO_CONTENT);
    }

    @GetMapping("/searchByName/{name}")
    public ResponseEntity<DataResponse> searchByName(@PathVariable String name) {
        return new ResponseEntity<>(new DataResponse(categoriesService.getCategoriesByName(name), HttpStatus.OK),HttpStatus.OK);
    }
}
