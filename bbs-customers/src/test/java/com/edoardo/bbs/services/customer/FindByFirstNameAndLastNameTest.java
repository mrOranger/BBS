package com.edoardo.bbs.services.customer;


import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.mapper.CustomerMapper;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindByFirstNameAndLastNameTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;


    private Faker faker;
    private int maxRandomElements;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
        this.maxRandomElements = (int)((Math.random() * 10) + 1);
    }

    @Test
    public void findByBirthDateReturnsEmptySet () {
        final String firstName = this.faker.name().firstName();
        final String lastName = this.faker.name().lastName();

        final List<CustomerDTO> customers = this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName);

        Assertions.assertThat(customers).isNotNull();
        Assertions.assertThat(customers.isEmpty()).isTrue();
    }

    @Test
    public void findByBirthDateReturnsOneCustomer () {
        final String firstName = this.faker.name().firstName();
        final String lastName = this.faker.name().lastName();
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .build();

        when(this.customerMapper.convertToDTO(newCustomer)).thenReturn(this.mapToDto(newCustomer));
        when(this.customerRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(List.of(newCustomer));
        final List<CustomerDTO> customers = this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName);

        Assertions.assertThat(customers).isNotNull();
        Assertions.assertThat(customers.isEmpty()).isFalse();
        Assertions.assertThat(customers.size()).isEqualTo(1);
        Assertions.assertThat(customers.get(0).getTaxCode()).isEqualTo(newCustomer.getTaxCode());
    }

    @Test
    public void findByBirthDateReturnsManyCustomers () {
        final String firstName = this.faker.name().firstName();
        final String lastName = this.faker.name().lastName();
        final List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < this.maxRandomElements; i++) {
            final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                    .firstName(firstName)
                    .lastName(lastName)
                    .birthDate(this.faker.date().birthday())
                    .email(this.faker.internet().emailAddress())
                    .emailVerifiedAt(this.faker.date().birthday())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .build();
            when(this.customerMapper.convertToDTO(newCustomer)).thenReturn(this.mapToDto(newCustomer));
            when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(newCustomer);
            this.customerRepository.save(newCustomer);

            customers.add(newCustomer);
        }


        when(this.customerRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(customers);
        final List<CustomerDTO> savedCustomers = this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName);

        Assertions.assertThat(savedCustomers).isNotNull();
        Assertions.assertThat(savedCustomers.isEmpty()).isFalse();
        Assertions.assertThat(savedCustomers.size()).isEqualTo(this.maxRandomElements);
    }

    private CustomerDTO mapToDto(Customer customer) {
        if (customer != null) {
            final Set<AddressDTO> addresses = new HashSet<>();
            if (customer.getAddresses() != null) {
                customer.getAddresses().forEach((entity) -> {
                    final AddressDTO address = AddressDTO.builder()
                            .country(entity.getCountry())
                            .state(entity.getState())
                            .city(entity.getCity())
                            .street(entity.getStreet())
                            .streetNumber(entity.getStreetNumber())
                            .postalCode(entity.getPostalCode())
                            .build();
                    addresses.add(address);
                });
            }
            return CustomerDTO.builder()
                    .taxCode(customer.getTaxCode())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .birthDate(customer.getBirthDate())
                    .email(customer.getEmail())
                    .emailVerifiedAt(customer.getEmailVerifiedAt())
                    .password(customer.getPassword())
                    .idCard(customer.getIdCard())
                    .addresses(addresses)
                    .build();
        }
        return null;
    }
}
