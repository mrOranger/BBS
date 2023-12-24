package com.edoardo.bbs.services;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomerService {
    CustomerResponse getAllCustomers (Pageable pageable);
    List<CustomerDTO> getCustomersByFirstNameAndLastName (String firstName, String lastName);
    List<CustomerDTO> getCustomersByBirthDate (Date birthDate);

    CustomerDTO getCustomerByTaxCode (String taxCode);

    CustomerDTO createCustomer (CustomerDTO customer);
    CustomerDTO updateCustomer (CustomerDTO customer);
    void deleteCustomer (CustomerDTO customer);
}
