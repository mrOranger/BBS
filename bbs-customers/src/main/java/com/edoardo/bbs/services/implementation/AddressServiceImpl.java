package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.AddressMapper;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }


    @Override
    public AddressDTO addAddress(String taxCode, AddressDTO address) throws MaximumAddressNumberException, ResourceNotFoundException {
        return null;
    }

    @Override
    public AddressDTO updateAddress(String taxCode, String addressId, AddressDTO address) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public AddressDTO deleteAddress(String taxCode, String addressId) throws ResourceNotFoundException {
        return null;
    }
}
