package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.AddressMapper;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.AddressService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    @Autowired
    public AddressServiceImpl(
            AddressRepository addressRepository,
            CustomerRepository customerRepository,
            CustomerMapper customerMapper,
            AddressMapper addressMapper
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerMapper = customerMapper;
        this.addressMapper = addressMapper;
    }

    @Override @SneakyThrows
    public AddressDTO addAddress(String taxCode, AddressDTO address)  {
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

    @Override @SneakyThrows
    public AddressDTO updateAddress(String taxCode, String addressId, AddressDTO address) {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            final Optional<Address> foundAddress = this.addressRepository.findById(addressId);
            if (foundAddress.isPresent()) {
                address.setId(Integer.parseInt(addressId));
                address.setCustomer(this.customerMapper.convertToDTO(customer.get()));
                return this.addressMapper.convertToDTO(
                        this.addressRepository.save(this.addressMapper.convertToEntity(address))
                );
            }
            throw new ResourceNotFoundException("Address " + addressId + " not found.");
        }
        throw new ResourceNotFoundException("Customer " + taxCode + " not found.");
    }

    @Override @SneakyThrows
    public AddressDTO deleteAddress(String taxCode, String addressId) {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            final Optional<Address> address = this.addressRepository.findById(addressId);
            if (address.isPresent()) {
                this.addressRepository.delete(address.get());
                return this.addressMapper.convertToDTO(address.get());
            }
            throw new ResourceNotFoundException("Address " + addressId + " not found.");
        }
        throw new ResourceNotFoundException("Customer " + taxCode + " not found.");
    }
}
