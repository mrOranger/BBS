package com.edoardo.bbs.services;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;

public interface AddressService {
    AddressDTO addAddress (String taxCode, AddressDTO address) throws MaximumAddressNumberException, ResourceNotFoundException;
    AddressDTO updateAddress (String taxCode, String addressId, AddressDTO address) throws ResourceNotFoundException;
    AddressDTO deleteAddress (String taxCode, String addressId) throws ResourceNotFoundException;
}
