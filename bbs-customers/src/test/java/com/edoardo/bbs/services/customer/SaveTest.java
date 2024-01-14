package com.edoardo.bbs.services.customer;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.entities.Address;
import com.edoardo.bbs.entities.Customer;
import com.edoardo.bbs.exceptions.ResourceConflictException;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SaveTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private Faker faker;
    private Address address;
    private Customer customer;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
        this.customer = Customer.builder().taxCode(this.faker.code().isbn10())
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(this.faker.internet().emailAddress())
                .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(this.faker.internet().password())
                .idCard(this.faker.file().toString())
                .addresses(new HashSet<>())
                .build();
        this.address = Address.builder().country(this.faker.address().country())
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .street(this.faker.address().city())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test @SneakyThrows
    public void saveCustomerWithoutAddress () {
        when(this.customerMapper.convertToEntity(Mockito.any(CustomerDTO.class))).thenReturn(this.customer);
        when(this.customerMapper.convertToDTO(this.customer)).thenReturn(this.mapToDto(this.customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(this.customer);

        final CustomerDTO customer = this.customerService.createCustomer(this.mapToDto(this.customer));

        assertAll(
                () -> assertThat(customer).isNotNull(),
                () -> assertThat(customer.getTaxCode()).isEqualTo(this.customer.getTaxCode()),
                () -> assertThat(customer.getFirstName()).isEqualTo(this.customer.getFirstName()),
                () -> assertThat(customer.getLastName()).isEqualTo(this.customer.getLastName()),
                () -> assertThat(customer.getBirthDate()).isEqualTo(this.customer.getBirthDate()),
                () -> assertThat(customer.getEmail()).isEqualTo(this.customer.getEmail()),
                () -> assertThat(customer.getEmailVerifiedAt()).isEqualTo(this.customer.getEmailVerifiedAt()),
                () -> assertThat(customer.getPassword()).isEqualTo(this.customer.getPassword()),
                () -> assertThat(customer.getIdCard()).isEqualTo(this.customer.getIdCard()),
                () -> assertThat(customer.getAddresses().size()).isEqualTo(0)
        );
    }

    @Test @SneakyThrows
    public void saveCustomerWithAddress () {
        this.customer.setAddresses(Set.of(this.address));
        when(this.customerMapper.convertToEntity(Mockito.any(CustomerDTO.class))).thenReturn(this.customer);
        when(this.customerMapper.convertToDTO(this.customer)).thenReturn(this.mapToDto(this.customer));
        when(this.customerRepository.save(Mockito.any(Customer.class))).thenReturn(this.customer);

        final CustomerDTO customer = this.customerService.createCustomer(this.mapToDto(this.customer));

        assertAll(
                () -> assertThat(customer).isNotNull(),
                () -> assertThat(customer.getTaxCode()).isEqualTo(this.customer.getTaxCode()),
                () -> assertThat(customer.getFirstName()).isEqualTo(this.customer.getFirstName()),
                () -> assertThat(customer.getLastName()).isEqualTo(this.customer.getLastName()),
                () -> assertThat(customer.getBirthDate()).isEqualTo(this.customer.getBirthDate()),
                () -> assertThat(customer.getEmail()).isEqualTo(this.customer.getEmail()),
                () -> assertThat(customer.getEmailVerifiedAt()).isEqualTo(this.customer.getEmailVerifiedAt()),
                () -> assertThat(customer.getPassword()).isEqualTo(this.customer.getPassword()),
                () -> assertThat(customer.getIdCard()).isEqualTo(this.customer.getIdCard()),
                () -> assertThat(customer.getAddresses().size()).isEqualTo(1)
        );
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
