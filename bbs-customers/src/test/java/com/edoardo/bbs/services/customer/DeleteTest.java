package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Customer;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteTest {

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
    public void CustomerRepository_delete_returnsNull () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.of(newCustomer));
        Assertions.assertAll(() -> this.customerService.deleteCustomer(this.mapToDto(newCustomer)));
    }

    @Test
    public void CustomerRepository_delete_returnsCustomer () {
        final Customer newCustomer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.empty());
        Assertions.assertAll(() -> this.customerService.deleteCustomer(this.mapToDto(newCustomer)));
    }

    private CustomerDTO mapToDto(Customer customer) {
        if (customer != null) {
            final Set<AddressDTO> addresses = new HashSet<>();
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
