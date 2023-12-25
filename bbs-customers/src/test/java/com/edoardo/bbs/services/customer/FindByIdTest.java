package com.edoardo.bbs.services.customer;


import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindByIdTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;


    private Faker faker;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
    }

    @Test
    public void CustomerService_findById_returnsNoCustomer () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();

        final CustomerDTO customer = this.customerService.getCustomerByTaxCode(newCustomer.getTaxCode());

        Assertions.assertThat(customer).isNull();
    }

    @Test
    public void CustomerService_findById_returnsOneCustomer () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
        this.customerRepository.save(newCustomer);

        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.of(newCustomer));
        final CustomerDTO customer = this.customerService.getCustomerByTaxCode(newCustomer.getTaxCode());

        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer.getTaxCode()).isEqualTo(newCustomer.getTaxCode());
    }
}
