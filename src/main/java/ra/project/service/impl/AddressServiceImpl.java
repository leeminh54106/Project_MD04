package ra.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.project.advice.SuccessException;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.AddressRequest;
import ra.project.model.dto.resp.AddressResponse;
import ra.project.model.entity.Address;
import ra.project.repository.IAddressRepository;
import ra.project.security.principle.MyUserDetails;
import ra.project.service.IAddressService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {
    private final IAddressRepository addressRepository;


    @Override
    public Address addNewAddress(AddressRequest address) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (address.getPhone() != null) {
            if (addressRepository.existsByPhone(address.getPhone())) {
                throw new NoSuchElementException("Số điện thoại đã tồn tại");
            }
        }
        Address newAddress = Address.builder()
                .users(userDetails.getUsers())
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
        return addressRepository.save(newAddress);
    }

    @Override
    public List<AddressResponse> getUserAddresses() throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Address> addresses = addressRepository.findByUsers(userDetails.getUsers());
        if (addresses.isEmpty()) {
            throw new CustomException("Không có địa chỉ nào của người dùng", HttpStatus.NOT_FOUND);
        }

        List<AddressResponse> responseList = addresses.stream()
                .map( addr -> {
                    AddressResponse addressResponse = new AddressResponse();
                    addressResponse.setId(addr.getId());
                    addressResponse.setFullAddress(addr.getFullAddress());
                    addressResponse.setPhone(addr.getPhone());
                    addressResponse.setReceiveName(addr.getReceiveName());
                    return addressResponse;
                }).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findByIdAndUsers(id, userDetails.getUsers())
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại địa ch có id: "+id));
        return AddressResponse.builder()
                .id(address.getId())
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
    }

    @Override
    public void deleteAddressById(Long id) throws CustomException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = addressRepository.findByIdAndUsers(id, userDetails.getUsers()).orElseThrow(()-> new CustomException("Không tồn tại địa chỉ này", HttpStatus.NOT_FOUND));
        if(address.getUsers().getId().equals(userDetails.getUsers().getId())) {
            addressRepository.delete(address);
            throw new SuccessException("Đã xóa thành công địa chỉ");
        }
        else {
            throw new CustomException("Không tồn tại địa chỉ của bạn", HttpStatus.NOT_FOUND);
        }
    }
}
