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
import ra.project.model.dto.req.ProductRequest;
import ra.project.model.dto.resp.DataResponse;
import ra.project.model.entity.Categories;
import ra.project.service.IProductsService;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductsController {
    @Autowired
    private IProductsService productsService;

    @GetMapping
    private ResponseEntity<DataResponse> getAllProducts() {
        return new ResponseEntity<>(new DataResponse(productsService.getProducts(), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<DataResponse> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(new DataResponse(productsService.getProductById(id), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<DataResponse> insertProduct(@Valid @ModelAttribute ProductRequest productRequest) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.insertProduct(productRequest), HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Long id,@Valid @ModelAttribute ProductRequest productRequest) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.updateProduct(id, productRequest), HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteProduct(@PathVariable Long id) {
        productsService.deleteProduct(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công sản phẩm có mã: " + id, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<?> searchProductByName(@PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @RequestParam(value = "search", defaultValue = "") String search) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.getProductsWithPaginationAndSorting(pageable, search).getContent(), HttpStatus.OK), HttpStatus.OK);
    }
}
