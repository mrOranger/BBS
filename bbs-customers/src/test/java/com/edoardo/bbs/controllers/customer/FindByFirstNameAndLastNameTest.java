package com.edoardo.bbs.controllers.customer;


import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.services.CustomerService;
import com.github.javafaker.Faker;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CustomerController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FindByFirstNameAndLastNameTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private List<CustomerDTO> customers;
    private Faker faker;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
        for (int i = 0; i < 10; i++) {
            this.customers = new ArrayList<>();
            Set<AddressDTO> addresses = new HashSet<>();
            final AddressDTO newAddress = AddressDTO.builder().country(faker.address().country())
                    .state(faker.address().state())
                    .city(faker.address().city())
                    .street(faker.address().streetName())
                    .streetNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                    .postalCode(faker.address().zipCode())
                    .build();
            addresses.add(newAddress);
            final CustomerDTO newCustomer = CustomerDTO.builder().taxCode(faker.code().isbn10())
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .birthDate(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .emailVerifiedAt(this.faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .password(faker.internet().password())
                    .idCard(faker.file().toString())
                    .addresses(addresses)
                    .build();
            this.customers.add(newCustomer);
        }
    }

    @Test
    public void testGetAllCustomersByFirstNameAndLastNameReturnsEmptyResponse () throws Exception {
        final String firstName = this.faker.name().firstName();
        final String lastName = this.faker.name().lastName();
        when(this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName)).thenReturn(new ArrayList<>());


        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/firstName/{firstName}/lastName/{lastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(0)));
    }

    @Test
    public void testGetAllCustomersByFirstNameAndLastNameReturnsManyCustomers () throws Exception {
        final String firstName = this.faker.name().firstName();
        final String lastName = this.faker.name().lastName();
        when(this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName)).thenReturn(this.customers);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/firstName/{firstName}/lastName/{lastName}", firstName, lastName)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(this.customers.size())));
    }
}
