package ra.project.service;

import ra.project.exception.CustomException;
import ra.project.model.dto.req.WishListRequest;
import ra.project.model.dto.resp.WishListResponse;

import java.util.List;

public interface IWishListService {
    WishListResponse addWishList(WishListRequest wishListRequest) throws CustomException;
    List<WishListResponse> getAllWishList() throws CustomException;
    void deleteWishList(Long id) throws CustomException;
}
