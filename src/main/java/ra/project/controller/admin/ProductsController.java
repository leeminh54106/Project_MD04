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

    //hiển thị sản phẩm
    @GetMapping
    private ResponseEntity<DataResponse> getAllProducts() {
        return new ResponseEntity<>(new DataResponse(productsService.getProducts(), HttpStatus.OK), HttpStatus.OK);
    }

    //sản phẩm theo id
    @GetMapping("/{id}")
    private ResponseEntity<DataResponse> getProductById(@PathVariable Long id) {
        return new ResponseEntity<>(new DataResponse(productsService.getProductById(id), HttpStatus.OK), HttpStatus.OK);
    }

    //thêm
    @PostMapping
    private ResponseEntity<DataResponse> insertProduct(@Valid @ModelAttribute ProductRequest productRequest) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.insertProduct(productRequest), HttpStatus.OK), HttpStatus.OK);
    }

    //sửa
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateProduct(@PathVariable Long id,@Valid @ModelAttribute ProductRequest productRequest) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.updateProduct(id, productRequest), HttpStatus.OK), HttpStatus.OK);
    }

    //xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteProduct(@PathVariable Long id) throws CustomException {
        productsService.deleteProduct(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công sản phẩm có mã: " + id, HttpStatus.OK), HttpStatus.OK);
    }

    //tìm kiếm,show, phân trang
    @GetMapping("/searchByName")
    public ResponseEntity<?> searchProductByName(@PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                 @RequestParam(value = "search", defaultValue = "") String search) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.getProductsWithPaginationAndSorting(pageable, search).getContent(), HttpStatus.OK), HttpStatus.OK);
    }



}
