package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZoneId;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllTest {

    private final Faker faker;
    private final int maxRandomElements;
    private final Pageable pageable;

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    public FindAllTest() {
        this.faker = new Faker();
        this.pageable = Pageable.ofSize(10);
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void findAllCustomersReturnsEmptySet () {
        final Page<Customer> customers = Mockito.mock(Page.class);

        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        final CustomerResponse customerResponse = this.customerService.getAllCustomers(this.pageable);

        Assertions.assertThat(customerResponse).isNotNull();
    }

    @Test
    public void findAllCustomersReturnsOneCustomer () {
        final Page<Customer> customers = Mockito.mock(Page.class);
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
        this.customerRepository.save(newCustomer);


        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        final CustomerResponse customerResponse = this.customerService.getAllCustomers(this.pageable);

        Assertions.assertThat(customerResponse).isNotNull();
    }

    @Test
    public void findAllCustomersReturnsManyCustomers () {
        final Page<Customer> customers = Mockito.mock(Page.class);
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(this.faker.name().firstName())
                    .lastName(this.faker.name().lastName())
                    .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();
            when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
            this.customerRepository.save(newCustomer);
        }

        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        final CustomerResponse customerResponse = this.customerService.getAllCustomers(this.pageable);

        Assertions.assertThat(customerResponse).isNotNull();
    }
}
