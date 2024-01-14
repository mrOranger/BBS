package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.mapper.CustomerMapper;
import com.edoardo.bbs.repositories.CustomerRepository;
import com.edoardo.bbs.services.implementation.CustomerServiceImpl;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private Address address;
    private Customer customer;

    @BeforeEach
    public void init () {
        final Faker faker = new Faker();
        this.customer = Customer.builder().taxCode(faker.code().isbn10())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(faker.internet().emailAddress())
                .emailVerifiedAt(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(faker.internet().password())
                .idCard(faker.file().toString())
                .addresses(new HashSet<>())
                .build();

        this.address = Address.builder().country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .street(faker.address().city())
                .streetNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                .postalCode(faker.address().zipCode())
                .build();
    }

    @Test @SneakyThrows
    public void updateCustomerWithoutAddressThrowsException () {
        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> this.customerService.updateCustomer(customer.getTaxCode(), this.mapToDto(customer)));
    }

    @Test @SneakyThrows
    public void updateCustomerWithoutAddressReturnsCustomer () {
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(this.customer);
        when(this.customerMapper.convertToDTO(this.customer)).thenReturn(this.mapToDto(this.customer));
        when(this.customerMapper.convertToEntity(Mockito.any(CustomerDTO.class))).thenReturn(this.customer);
        when(this.customerRepository.findById(this.customer.getTaxCode())).thenReturn(Optional.of(this.customer));

        final CustomerDTO updatedCustomer = this.customerService.updateCustomer(customer.getTaxCode(), this.mapToDto(customer));

        assertAll(
                () -> assertThat(updatedCustomer).isNotNull(),
                () -> assertThat(updatedCustomer.getTaxCode()).isEqualTo(customer.getTaxCode()),
                () -> assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName()),
                () -> assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName()),
                () -> assertThat(updatedCustomer.getBirthDate()).isEqualTo(customer.getBirthDate()),
                () -> assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail()),
                () -> assertThat(updatedCustomer.getEmailVerifiedAt()).isEqualTo(customer.getEmailVerifiedAt()),
                () -> assertThat(updatedCustomer.getPassword()).isEqualTo(customer.getPassword()),
                () -> assertThat(updatedCustomer.getIdCard()).isEqualTo(customer.getIdCard()),
                () -> assertThat(customer.getAddresses().size()).isEqualTo(0)
        );
    }

    @Test @SneakyThrows
    public void updateCustomerWithAddressReturnsCustomer () {
        this.customer.getAddresses().add(this.address);
        when(this.customerMapper.convertToDTO(customer)).thenReturn(this.mapToDto(customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(customer);
        when(this.customerRepository.findById(customer.getTaxCode())).thenReturn(Optional.of(customer));
        when(this.customerMapper.convertToEntity(Mockito.any(CustomerDTO.class))).thenReturn(this.customer);

        final CustomerDTO updatedCustomer = this.customerService.updateCustomer(customer.getTaxCode(), this.mapToDto(customer));

        assertAll(
                () -> assertThat(updatedCustomer).isNotNull(),
                () -> assertThat(updatedCustomer.getTaxCode()).isEqualTo(customer.getTaxCode()),
                () -> assertThat(updatedCustomer.getFirstName()).isEqualTo(customer.getFirstName()),
                () -> assertThat(updatedCustomer.getLastName()).isEqualTo(customer.getLastName()),
                () -> assertThat(updatedCustomer.getBirthDate()).isEqualTo(customer.getBirthDate()),
                () -> assertThat(updatedCustomer.getEmail()).isEqualTo(customer.getEmail()),
                () -> assertThat(updatedCustomer.getEmailVerifiedAt()).isEqualTo(customer.getEmailVerifiedAt()),
                () -> assertThat(updatedCustomer.getPassword()).isEqualTo(customer.getPassword()),
                () -> assertThat(updatedCustomer.getIdCard()).isEqualTo(customer.getIdCard()),
                () -> assertThat(customer.getAddresses().size()).isEqualTo(1)
        );
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
