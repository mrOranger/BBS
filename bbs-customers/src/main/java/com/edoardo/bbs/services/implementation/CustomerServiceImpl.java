package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl (CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerResponse getAllCustomers(Pageable pageable) {
        final Page<Customer> customers = this.customerRepository.findAll(pageable);
        final List<Customer> customersList = customers.getContent();
        final List<CustomerDTO> customerDTOList = customersList.stream().map(this::mapToDto).toList();

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
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<CustomerDTO> getCustomersByBirthDate(Date birthDate) {
        return this.customerRepository.findByBirthDate(birthDate).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public CustomerDTO getCustomerByTaxCode(String taxCode) {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);
        return customer.map(this::mapToDto).orElse(null);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
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

        return this.mapToDto(this.customerRepository.save(customerEntity));
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        final Optional<Customer> existingCustomer = this.customerRepository.findById(customer.getTaxCode());
        if (existingCustomer.isPresent()) {
            return this.createCustomer(customer);
        }
        return null;
    }

    @Override
    public void deleteCustomer(CustomerDTO customer) {

    }

    private CustomerDTO mapToDto(Customer customer) {
        if (customer != null) {
            final Set<AddressDTO> addresses = new HashSet<>();
            customer.getAddresses().forEach((entity) -> {
                final AddressDTO address = AddressDTO.builder()
                        .country(entity.getCountry())
                        .state(entity.getState())
                        .city(entity.getCity())
                        .street(entity.getStreet())
                        .streetNumber(entity.getStreetNumber())
                        .postalCode(entity.getPostalCode())
                        .build();
                addresses.add(address);
            });
            return CustomerDTO.builder()
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
        }
        return null;
    }
}
