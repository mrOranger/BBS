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
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindByIdTest {

    private final Faker faker;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private final CustomerServiceImpl customerService;

    public FindByIdTest () {
        this.faker = new Faker();
    }

    @Test
    public void CustomerService_findById_returnsNoneCustomer () {
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

        when(this.customerRepository.findById(customerDTO.getTaxCode())).thenReturn(Optional.ofNullable(newCustomer));
        final CustomerDTO customer = this.customerService.getById(ustomerDTO.getTaxCode());

        Assertions.assertThat(customer).isNull();
    }

    @Test
    public void CustomerService_findAll_returnsOneCustomer () {
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

        when(this.customerRepository.findById(customerDTO.getTaxCode())).thenReturn(Optional.ofNullable(newCustomer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
        this.customerService.createCustomer(customerDTO);

        final CustomerDTO customer = this.customerService.getById(ustomerDTO.getTaxCode());

        Assertions.assertThat(customer).isNotNull();
    }
}
