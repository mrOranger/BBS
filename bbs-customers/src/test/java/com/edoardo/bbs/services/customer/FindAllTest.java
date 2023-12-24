package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.repositories.CustomerRepository;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllTest {

    private final Faker faker;
    private final int maxRandomElements;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private final CustomerServiceImpl customerService;

    public FindAllTest() {
        this.faker = new Faker();
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void CustomerService_findAll_returnsEmptyCustomerList () {
        final Page<Customer> customers = Mockito.mock(Page.class);

        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);

        final CustomerResponse customerResponse = this.customerService.getAllCustomers();

        Assertions.assertThat(customerResponse).isNotNull();
    }

    @Test
    public void CustomerService_findAll_returnsOneCustomerList () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();

        final CustomerDTO customerDTO = CustomerDTO.builder().taxCode(this.faker.code().isbn10())
                .firstName(newCustomer.getFirstName())
                .lastName(newCustomer.getLastName())
                .birthDate(newCustomer.getBirthDate())
                .email(newCustomer.getEmail())
                .emailVerifiedAt(newCustomer.getEmailVerifiedAt())
                .password(newCustomer.getPassword())
                .idCard(newCustomer.getIdCard())
                .build();

        final Page<Customer> customers = Mockito.mock(Page.class);
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        this.customerService.createCustomer(customerDTO);

        final CustomerResponse customerResponse = this.customerService.getAllCustomers();

        Assertions.assertThat(customerResponse).isNotNull();
    }

    @Test
    public void CustomerService_findAll_returnsManyCustomersList () {
        final Page<Customer> customers = Mockito.mock(Page.class);
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(this.faker.name().firstName())
                    .lastName(this.faker.name().lastName())
                    .birthDate(this.faker.date().birthday())
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();

            final CustomerDTO customerDTO = CustomerDTO.builder().taxCode(this.faker.code().isbn10())
                    .firstName(newCustomer.getFirstName())
                    .lastName(newCustomer.getLastName())
                    .birthDate(newCustomer.getBirthDate())
                    .email(newCustomer.getEmail())
                    .emailVerifiedAt(newCustomer.getEmailVerifiedAt())
                    .password(newCustomer.getPassword())
                    .idCard(newCustomer.getIdCard())
                    .build();

            when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
            this.customerService.createCustomer(customerDTO);
        }

        when(this.customerRepository.findAll(Mockito.any(Pageable.class))).thenReturn(customers);
        final CustomerResponse customerResponse = this.customerService.getAllCustomers();

        Assertions.assertThat(customerResponse).isNotNull();
    }
}
