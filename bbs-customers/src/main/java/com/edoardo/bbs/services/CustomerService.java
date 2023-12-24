package com.edoardo.bbs.services;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers (Pageable pageable);
    Optional<CustomerDTO> getCustomerByTaxCode (String taxCode);
    List<CustomerDTO> getCustomersByFirstNameAndLastName (String firstName, String lastName);
    List<CustomerDTO> getCustomersByBirthDate (Date birthDate);
    void createCustomer (CustomerDTO customer);
    void updateCustomer (CustomerDTO customer);
    void deleteCustomer (CustomerDTO customer);
}
