package com.edoardo.bbs.factories;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;
import java.util.HashSet;

public class CustomerFactory implements Factory<Customer, CustomerDTO> {

    private final Faker faker;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerFactory () {
        this.faker = new Faker();
    }

    @Override
    public Customer create() {
        return Customer.builder().taxCode(faker.code().isbn10())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .emailVerifiedAt(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(faker.internet().password())
                .idCard(faker.file().toString())
                .addresses(new HashSet<>())
                .build();
    }

    @Override
    public CustomerDTO createDTO () {
        return this.customerMapper.convertToDTO(this.create());
    }
}
