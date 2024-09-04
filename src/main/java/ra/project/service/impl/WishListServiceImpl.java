package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.project.advice.SuccessException;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.WishListRequest;
import ra.project.model.dto.resp.WishListResponse;
import ra.project.model.entity.Products;
import ra.project.model.entity.WishList;
import ra.project.repository.IProductsRepository;
import ra.project.repository.IWishListRepository;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IUserService;
import ra.project.service.IWishListService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements IWishListService {
    private final IWishListRepository wishListRepository;
    private final IProductsRepository productsRepository;
    @Override
    public WishListResponse addWishList(WishListRequest wishListRequest) throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Products product = productsRepository.findById(wishListRequest.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Sản phẩm này không tồn tại"));
        if(wishListRepository.existsByProductAndUser(product, userDetails.getUsers())) {
            throw new CustomException("Sản phẩm yêu thích đã tồn tại", HttpStatus.NOT_FOUND);
        }
        WishList wishList = WishList.builder()
                .user(userDetails.getUsers())
                .product(product)
                .build();

        wishList = wishListRepository.save(wishList);


        WishListResponse response = new WishListResponse();
        response.setId(wishList.getId());
        response.setWishlistProName(product.getName());
        response.setProductId(product.getId());
        response.setUserId(userDetails.getUsers().getId());
        return response;

    }

    @Override
    public List<WishListResponse> getAllWishList() throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<WishList> wishList = wishListRepository.findAllByUserId(userDetails.getUsers().getId());
        if (wishList.isEmpty()) {
            throw new CustomException("Không có sản phẩm nào trong danh sách yêu thích", HttpStatus.NOT_FOUND);
        }
        List<WishListResponse> responseList = wishList.stream()
                .map(wish -> { WishListResponse response = new WishListResponse();
                    response.setId(wish.getId());
                    response.setProductId(wish.getProduct().getId());
                    response.setWishlistProName(wish.getProduct().getName());
                    response.setUserId(userDetails.getUsers().getId());
                    return response;}).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public void deleteWishList(Long id) throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WishList wishList = wishListRepository.findByIdAndUser(id, userDetails.getUsers())
                .orElseThrow(()-> new CustomException("không tồn tại mã sản phẩm yêu thích này", HttpStatus.NOT_FOUND));
        if (wishList.getUser().getId().equals(userDetails.getUsers().getId())) {
            wishListRepository.delete(wishList);
            throw new SuccessException("Đã xóa thành công sản phẩm yêu thích");
        } else {
            throw new CustomException("Không tồn tại sản phẩm yêu thích của bạn", HttpStatus.NOT_FOUND);
        }
    }
}
