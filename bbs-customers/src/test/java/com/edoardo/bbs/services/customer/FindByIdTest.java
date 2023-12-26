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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindByIdTest {

    @Mock
    private CustomerMapper customerMapper;
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
    public void findByIdReturnsNoneCustomer () {
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
    public void findByIdReturnsOneCustomer () {
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

        when(this.customerMapper.convertToDTO(newCustomer)).thenReturn(this.mapToDto(newCustomer));
        when(this.customerRepository.findById(newCustomer.getTaxCode())).thenReturn(Optional.of(newCustomer));
        final CustomerDTO customer = this.customerService.getCustomerByTaxCode(newCustomer.getTaxCode());

        Assertions.assertThat(customer).isNotNull();
        Assertions.assertThat(customer.getTaxCode()).isEqualTo(newCustomer.getTaxCode());
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
