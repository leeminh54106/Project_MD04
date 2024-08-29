package ra.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ProductRequest;
import ra.project.model.entity.Categories;
import ra.project.model.entity.Products;
import ra.project.repository.IProductsRepository;
import ra.project.service.IFileService;
import ra.project.service.IProductsService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductsServiceImpl implements IProductsService {
    @Autowired
    private IFileService uploadFile;
    @Autowired
    private IProductsRepository productsRepository;
    @Autowired
    private CategoriesServiceImpl categoriesService;

    @Override
    public List<Products> getProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Products getProductById(Long id) {
        return productsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));
    }

    @Override
    public Products insertProduct(ProductRequest productRequest) throws CustomException {
        if (productsRepository.existsByName(productRequest.getName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.CONFLICT);
        }
        Products product = Products.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .image(uploadFile.uploadLocal(productRequest.getImage()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Categories categories = categoriesService.getCategoriesById(productRequest.getCategoryId());
        product.setCategories(categories);
        return productsRepository.save(product);
    }

    @Override
    public Products updateProduct(Long id, Products product) {
        productsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));
        product.setId(id);
       return productsRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));
        productsRepository.deleteById(id);
    }

    @Override
    public Page<Products> getProductsWithPaginationAndSorting(Pageable pageable, String search) {
        Page<Products> productsPage;
        if(search.isEmpty()){
            productsPage = productsRepository.findAll(pageable);
        }else {
            productsPage = productsRepository.findProductsByNameContainingIgnoreCase(search, pageable);
        }
        return productsPage;
    }
}
