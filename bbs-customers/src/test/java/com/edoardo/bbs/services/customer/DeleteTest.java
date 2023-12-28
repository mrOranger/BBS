package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerMapper customerMapper;

    private Faker faker;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
    }

    @Test
    public void deleteCustomerThrowsException () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.customerService.deleteCustomer(newCustomer.getTaxCode()));
    }

    @Test
    public void deleteCustomerReturnsCustomer () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.of(newCustomer));

        Assertions.assertAll(() -> this.customerService.deleteCustomer(newCustomer.getTaxCode()));
    }
}
