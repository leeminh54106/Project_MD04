package ra.project.service;

import ra.project.exception.CustomException;
import ra.project.model.dto.req.AddressRequest;
import ra.project.model.dto.resp.AddressResponse;
import ra.project.model.entity.Address;

import java.util.List;

public interface IAddressService {
    Address addNewAddress(AddressRequest address);
    List<AddressResponse> getUserAddresses() throws CustomException;
    AddressResponse getAddressById(Long id);
    void deleteAddressById(Long id) throws CustomException;
}
