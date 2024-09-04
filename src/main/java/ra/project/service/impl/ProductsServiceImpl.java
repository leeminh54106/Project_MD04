package ra.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.ProductRequest;
import ra.project.model.dto.resp.WishListResponse;
import ra.project.model.entity.*;
import ra.project.repository.*;
import ra.project.service.ICategoriesService;
import ra.project.service.IFileService;
import ra.project.service.IProductsService;
import ra.project.service.IShoppingCartService;

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
    private ICategoriesService categoriesService;
    @Autowired
    private ICategoriesRepository categoriesRepository;
    @Autowired
    private IShoppingCartRepository shoppingCartRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    @Autowired
    private IWishListRepository wishListRepository;
    @Override
    public List<Products> getProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Products getProductById(Long id) {
        return productsRepository
                .findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));
    }

    @Override
    public Products insertProduct(ProductRequest productRequest) throws CustomException {
        if (productsRepository.existsByName(productRequest.getName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.CONFLICT);
        }
        Products product = Products.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .image(uploadFile.uploadLocal(productRequest.getImage()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .status(true)
                .build();
        Categories categories = categoriesService.getCategoriesById(productRequest.getCategoryId());
        product.setCategories(categories);
        return productsRepository.save(product);
    }

    @Override
    public Products updateProduct(Long id, ProductRequest productRequest) throws CustomException {
        productsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));
        if(productsRepository.existsByName(productRequest.getName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.CONFLICT);
        }
        Products product = Products.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .image(uploadFile.uploadLocal(productRequest.getImage()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Categories categories = categoriesService.getCategoriesById(productRequest.getCategoryId());
        product.setCategories(categories);
        product.setId(id);
       return productsRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws CustomException {
      Products product =  productsRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm có mã là : " +id));

        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByProduct(product);
        if (!shoppingCarts.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong giỏ hàng", HttpStatus.BAD_REQUEST);
        }

        List<OrderDetails> orderDetails = orderDetailRepository.findByProduct(product);
        if (!orderDetails.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong đơn hàng", HttpStatus.BAD_REQUEST);
        }

        List<WishList> wishList = wishListRepository.findByProduct(product);
        if(!wishList.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong sản phẩm yêu thích", HttpStatus.BAD_REQUEST);
        }
        productsRepository.deleteById(id);
    }

    @Override
    public Page<Products> getProductsWithPaginationAndSorting(Pageable pageable, String search) throws CustomException {
        Page<Products> productsPage;
        if(search==null || search.isEmpty()){
            productsPage = productsRepository.findAll(pageable);
        }else {
            productsPage = productsRepository.findProductsByNameContainsIgnoreCase(search, pageable);
            if(productsPage.isEmpty()){
                throw new CustomException("Không tìm thấy sản phẩm có tên là : " +search,HttpStatus.NOT_FOUND);
            }
        }
        return productsPage;
    }

    @Override
    public List<Products> getTopNewProducts() {
        return productsRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public List<Products> findProductsByCategoryId(Long id) {
        if(!categoriesRepository.existsById(id)){
            throw new NoSuchElementException("Không tìm thấy danh mục có Id: "+id);
        }

        List<Products> products = productsRepository.findProductsByCategoriesId(id);

        if (products.isEmpty()){
            throw new NoSuchElementException("Danh mục trống!");
        }
        return products;
    }

    @Override
    public Page<Products> getProductsWithPaginationAndSort(Pageable pageable, String search) throws CustomException {
        Page<Products> listproducts;
        if(search==null || search.isEmpty()){
            listproducts = productsRepository.findAll(pageable);
        }else {
            listproducts = productsRepository.findProductsByNameAndDescriptionContainingIgnoreCase(search,pageable);
            if(listproducts.isEmpty()){
                throw new CustomException("Không tìm thấy sản phẩm hoặc mô tả có tên là : " +search,HttpStatus.NOT_FOUND);
            }
        }

           return listproducts;
    }

    @Override
    public Page<Products> listProductsForSale(Pageable pageable) {
        return productsRepository.findProductsByStatusTrue(pageable);
    }
}
