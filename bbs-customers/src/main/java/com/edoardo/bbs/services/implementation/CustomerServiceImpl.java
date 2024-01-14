package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
import com.edoardo.bbs.exceptions.ResourceConflictException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.AddressMapper;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.AddressRepository;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.CustomerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;
    private final CustomerMapper customerModelMapper;
    private final AddressMapper addressMapper;

    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            CustomerMapper customerModelMapper,
            AddressMapper addressMapper
    ) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.customerModelMapper = customerModelMapper;
    }

    @Override
    public CustomerResponse getAllCustomers(Pageable pageable) {
        final Page<Customer> customers = this.customerRepository.findAll(pageable);
        final List<Customer> customersList = customers.getContent();
        final List<CustomerDTO> customerDTOList = customersList.stream().map(this.customerModelMapper::convertToDTO).toList();

        return CustomerResponse.builder()
                .content(customerDTOList)
                .pageNo(customers.getNumber())
                .pageSize(customers.getSize())
                .totalElements(customers.getTotalElements())
                .totalPages(customers.getTotalPages())
                .last(customers.isLast())
                .build();
    }

    @Override
    public List<CustomerDTO> getCustomersByFirstNameAndLastName(String firstName, String lastName) {
        return this.customerRepository.findByFirstNameAndLastName(firstName, lastName).stream()
                .map(this.customerModelMapper::convertToDTO)
                .toList();
    }

    @Override
    public List<CustomerDTO> getCustomersByBirthDate(LocalDate birthDate) {
        return this.customerRepository.findByBirthDate(birthDate).stream()
                .map(this.customerModelMapper::convertToDTO)
                .toList();
    }

    @Override @SneakyThrows
    public CustomerDTO getCustomerByTaxCode(String taxCode) {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            return customer.map(this.customerModelMapper::convertToDTO).get();
        }
        throw new ResourceNotFoundException("Customer " + taxCode + " not found.");
    }

    @Override @SneakyThrows
    public CustomerDTO createCustomer(CustomerDTO customer) {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(customer.getTaxCode());
        if (existingCustomer.isEmpty()) {
            final Customer customerEntity = this.customerModelMapper.convertToEntity(customer);
            customerEntity.getAddresses().forEach(address -> address.setCustomer(customerEntity));
            return this.customerModelMapper.convertToDTO(this.customerRepository.save(customerEntity));
        }
        throw new ResourceConflictException("Conflict.");
    }

    @Override @SneakyThrows
    public CustomerDTO updateCustomer(String taxCode, CustomerDTO customer) {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(taxCode);
        if (existingCustomer.isPresent()) {
            customer.setTaxCode(taxCode);
            final Customer customerEntity = this.customerModelMapper.convertToEntity(customer);
            customerEntity.getAddresses().forEach(address -> address.setCustomer(customerEntity));
            return this.customerModelMapper.convertToDTO(this.customerRepository.save(customerEntity));
        }
        throw new ResourceNotFoundException("Not found.");
    }

    @Override @SneakyThrows
    public CustomerDTO deleteCustomer(String taxCode) {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(taxCode);
        if (existingCustomer.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        this.customerRepository.delete(existingCustomer.get());
        return this.customerModelMapper.convertToDTO(existingCustomer.get());
    }
}
