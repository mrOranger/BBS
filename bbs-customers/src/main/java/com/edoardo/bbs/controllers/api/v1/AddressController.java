package com.edoardo.bbs.controllers.api.v1;


import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.services.AddressService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1") @Log
public class AddressController {
    public AddressService addressService;

    @Autowired
    public AddressController (AddressService addressService) {
        this.addressService = addressService;
    }

    @PatchMapping(value = "/addresses/customer/{customer}", produces = "application/json") @SneakyThrows
    public ResponseEntity<AddressDTO> addAddressToCustomer (
            @PathVariable String customer,
            @Valid @RequestBody AddressDTO address
    ) {
        return ResponseEntity.ok(this.addressService.addAddress(customer, address));
    }

    @PutMapping(value = "/addresses/address/{address}/customer/{customer}", produces = "application/json") @SneakyThrows
    public ResponseEntity<AddressDTO> updateAddressCustomer (
            @PathVariable String address,
            @PathVariable String customer,
            @Valid @RequestBody AddressDTO addressDTO
    ) {
        return ResponseEntity.ok(this.addressService.updateAddress(customer, address, addressDTO));
    }
}
