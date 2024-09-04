package ra.project.controller.User;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project.exception.CustomException;
import ra.project.model.dto.req.AddressRequest;
import ra.project.model.dto.resp.AddressResponse;
import ra.project.model.dto.resp.DataResponse;
import ra.project.service.IAddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/account/address")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<DataResponse> addAddress(@Valid @RequestBody AddressRequest address) {
        return new ResponseEntity<>(new DataResponse(addressService.addNewAddress(address), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses() throws CustomException {
        List<AddressResponse> addressResponse = addressService.getUserAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id)  {
        AddressResponse addressResponse = addressService.getAddressById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteAddress(@PathVariable Long id) throws CustomException {
        addressService.deleteAddressById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
