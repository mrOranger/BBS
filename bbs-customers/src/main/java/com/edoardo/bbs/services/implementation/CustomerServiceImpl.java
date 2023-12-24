package com.edoardo.bbs.services.implementation;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service @Transactional(readOnly = true)
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
        final List<Customer> customersList = this.customerRepository.findByFirstNameAndLastName(firstName, lastName);

        return customersList.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<CustomerDTO> getCustomersByBirthDate(Date birthDate) {
        final List<Customer> customersList = this.customerRepository.findByBirthDate(birthDate);

        return customersList.stream().map(this::mapToDto).toList();
    }

    @Override
    public CustomerDTO getCustomerByTaxCode(String taxCode) {
        final Optional<Customer> customer = this.customerRepository.findById(taxCode);

        return this.mapToDto(customer.orElseThrow(() -> new IllegalArgumentException("Not a valid Tax Code.")));
    }

    @Override @Transactional
    public void createCustomer(CustomerDTO customer) {

    }

    @Override @Transactional
    public void updateCustomer(CustomerDTO customer) {

    }

    @Override @Transactional
    public void deleteCustomer(CustomerDTO customer) {

    }

    private CustomerDTO mapToDto(Customer customer) {
        return CustomerDTO.builder()
                .taxCode(customer.getTaxCode())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .birthDate(customer.getBirthDate())
                .email(customer.getEmail())
                .emailVerifiedAt(customer.getEmailVerifiedAt())
                .password(customer.getPassword())
                .idCard(customer.getIdCard())
                .build();
    }
}
