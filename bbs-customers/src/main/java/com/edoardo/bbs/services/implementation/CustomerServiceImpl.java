package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceConflictException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerModelMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerModelMapper) {
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

    @Override
    public CustomerDTO getCustomerByTaxCode(String taxCode) throws ResourceNotFoundException {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        if (customer.isPresent()) {
            return customer.map(this.customerModelMapper::convertToDTO).get();
        }
        throw new ResourceNotFoundException();
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) throws ResourceConflictException {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(customer.getTaxCode());
        if (existingCustomer.isEmpty()) {
            final Set<Address> addresses = new HashSet<>();
            customer.getAddresses().forEach((entity) -> {
                final Address address = Address.builder()
                        .country(entity.getCountry())
                        .state(entity.getState())
                        .city(entity.getCity())
                        .street(entity.getStreet())
                        .streetNumber(entity.getStreetNumber())
                        .postalCode(entity.getPostalCode())
                        .build();
                addresses.add(address);
            });
            final Customer customerEntity = Customer.builder()
                    .taxCode(customer.getTaxCode())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .birthDate(customer.getBirthDate())
                    .email(customer.getEmail())
                    .emailVerifiedAt(customer.getEmailVerifiedAt())
                    .password(customer.getPassword())
                    .idCard(customer.getIdCard())
                    .addresses(addresses)
                    .build();

            return this.customerModelMapper.convertToDTO(this.customerRepository.save(customerEntity));
        }
        throw new ResourceConflictException("Conflict.");
    }

    @Override
    public CustomerDTO updateCustomer(String taxCode, CustomerDTO customer) throws ResourceNotFoundException {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(taxCode);
        if (existingCustomer.isPresent()) {
            final Set<Address> addresses = new HashSet<>();
            customer.getAddresses().forEach((entity) -> {
                final Address address = Address.builder()
                        .country(entity.getCountry())
                        .state(entity.getState())
                        .city(entity.getCity())
                        .street(entity.getStreet())
                        .streetNumber(entity.getStreetNumber())
                        .postalCode(entity.getPostalCode())
                        .build();
                addresses.add(address);
            });
            final Customer customerEntity = Customer.builder()
                    .taxCode(customer.getTaxCode())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .birthDate(customer.getBirthDate())
                    .email(customer.getEmail())
                    .emailVerifiedAt(customer.getEmailVerifiedAt())
                    .password(customer.getPassword())
                    .idCard(customer.getIdCard())
                    .addresses(addresses)
                    .build();

            return this.customerModelMapper.convertToDTO(this.customerRepository.save(customerEntity));
        }
        throw new ResourceNotFoundException("Not found.");
    }

    @Override
    public CustomerDTO deleteCustomer(String taxCode) throws ResourceNotFoundException {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(taxCode);
        if (existingCustomer.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        this.customerRepository.delete(existingCustomer.get());
        return this.customerModelMapper.convertToDTO(existingCustomer.get());
    }
}
