package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.AddressMapper;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

public class AddressServiceImpl implements AddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, CustomerRepository customerRepository, AddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }


    @Override
    public AddressDTO addAddress(String taxCode, AddressDTO address) throws MaximumAddressNumberException, ResourceNotFoundException {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            final Customer customerEntity = customer.get();
            if (customerEntity.getAddresses().size() < 2) {
                Address addressEntity = this.addressMapper.convertToEntity(address);
                addressEntity.setCustomer(customerEntity);
                return this.addressMapper.convertToDTO(this.addressRepository.save(addressEntity));
            }
            throw new MaximumAddressNumberException("Customer " + taxCode + ", maximum addresses reached.");
        }
        throw new ResourceNotFoundException("Customer " + taxCode + " not found.");
    }

    @Override
    public AddressDTO updateAddress(String taxCode, String addressId, AddressDTO address) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public AddressDTO deleteAddress(String taxCode, String addressId) throws ResourceNotFoundException {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            final Customer customerEntity = customer.get();
            System.out.println(customerEntity.getAddresses());
            Optional<Address> addressToDelete = customerEntity.getAddresses().stream()
                    .filter((address -> {
                        System.out.println(address.getId().toString());
                        return address.getId().toString().equals(addressId);
                    }))
                    .findFirst();
            if (addressToDelete.isPresent()) {
                this.addressRepository.delete(addressToDelete.get());
                return this.addressMapper.convertToDTO(addressToDelete.get());
            }
            throw new ResourceNotFoundException("Address " + addressId + " not found.");
        }
        throw new ResourceNotFoundException("Customer " + taxCode + " not found.");
    }
}
