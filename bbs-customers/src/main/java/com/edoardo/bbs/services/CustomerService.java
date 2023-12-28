package com.edoardo.bbs.services;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.exceptions.ResourceConflictException;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    CustomerResponse getAllCustomers (Pageable pageable);
    List<CustomerDTO> getCustomersByFirstNameAndLastName (String firstName, String lastName);
    List<CustomerDTO> getCustomersByBirthDate (LocalDate birthDate);

    CustomerDTO getCustomerByTaxCode (String taxCode) throws ResourceNotFoundException;

    CustomerDTO createCustomer (CustomerDTO customer) throws ResourceConflictException;
    CustomerDTO updateCustomer (String taxCode, CustomerDTO customer) throws ResourceNotFoundException;
    CustomerDTO deleteCustomer (CustomerDTO customer);
}
