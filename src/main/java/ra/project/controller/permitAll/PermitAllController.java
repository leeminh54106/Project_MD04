package ra.project.controller.permitAll;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.resp.DataResponse;
import ra.project.service.ICategoriesService;
import ra.project.service.IProductsService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PermitAllController {
    private final ICategoriesService categoriesService;
    private final IProductsService productsService;

    //show danh mục
    @GetMapping("/category/categorySale")
    public ResponseEntity<DataResponse> categoriesForSale(@PageableDefault(page = 0,
            size = 3,sort = "id",direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(new DataResponse(categoriesService.listCategoriesForSale(pageable), HttpStatus.OK), HttpStatus.OK);
    }

    //pro theo id
    @GetMapping("/products/{id}")
    public ResponseEntity<DataResponse> getProductById(@PathVariable Long id){
        return new ResponseEntity<>(new DataResponse(productsService.getProductById(id), HttpStatus.OK), HttpStatus.OK);
    }

    //sản phẩm theo danh mục
    @GetMapping("/products/categories/{id}")
    public ResponseEntity<DataResponse> getProductsByCategoryId(@PathVariable Long id){
        return new ResponseEntity<>(new DataResponse(productsService.findProductsByCategoryId(id),HttpStatus.OK), HttpStatus.OK);
    }

    //sản phẩm mới
    @GetMapping("/products/new-products")
    public ResponseEntity<?> getNewProducts() {
        return new ResponseEntity<>(new DataResponse(productsService.getTopNewProducts(), HttpStatus.OK), HttpStatus.OK);
    }

    //danh mục được bán
    @GetMapping("/categories")
    public ResponseEntity<DataResponse> getCategoriesForSale(@PageableDefault(page = 0,
    size = 5,sort = "id",direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(new DataResponse(categoriesService.listCategoriesForSale(pageable), HttpStatus.OK), HttpStatus.OK);
    }

    //sản phẩm được bán
    @GetMapping("/products")
    public ResponseEntity<DataResponse> getProductsForSale(@PageableDefault(page = 0,
            size = 5,sort = "id",direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(new DataResponse(productsService.listProductsForSale(pageable), HttpStatus.OK), HttpStatus.OK);
    }

    //search theo name or des
    @GetMapping("/products/search")
    public ResponseEntity<DataResponse> searchProduct(@PageableDefault(page = 0, size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "search",defaultValue = "") String search) throws CustomException {
        return new ResponseEntity<>(new DataResponse(productsService.getProductsWithPaginationAndSort(pageable,search),HttpStatus.OK),HttpStatus.OK);
    }
}
