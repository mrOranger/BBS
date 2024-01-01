package com.edoardo.bbs.services;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;

public interface AddressService {
    AddressDTO addAddress (String taxCode, AddressDTO address) throws MaximumAddressNumberException;
    AddressDTO updateAddress (String taxCode, AddressDTO address) throws ResourceNotFoundException;
    AddressDTO deleteAddress (String taxCode, AddressDTO address) throws ResourceNotFoundException;
}
