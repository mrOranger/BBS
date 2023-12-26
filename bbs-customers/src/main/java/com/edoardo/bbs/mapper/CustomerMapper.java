package com.edoardo.bbs.mapper;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public final ModelMapper modelMapper;

    @Autowired
    public CustomerMapper (ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Customer convertToEntity (CustomerDTO customerDTO) {
        return this.modelMapper.map(customerDTO, Customer.class);
    }

    public CustomerDTO convertToDTO (Customer customer) {
        return this.modelMapper.map(customer, CustomerDTO.class);
    }
}
