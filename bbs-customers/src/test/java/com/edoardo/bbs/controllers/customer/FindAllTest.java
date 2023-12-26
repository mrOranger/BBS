package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CustomerController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FindAllTest {

    private Faker faker;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper customerMapper;

    private List<CustomerDTO> customers;
    private Set<AddressDTO> addresses;

    @BeforeEach
    public void init () {
        this.faker = new Faker();
        for (int i = 0; i < 10; i++) {
            this.customers = new ArrayList<>();
            this.addresses = new HashSet<>();
            final AddressDTO newAddress = AddressDTO.builder().country(this.faker.address().country())
                    .state(this.faker.address().state())
                    .city(this.faker.address().city())
                    .street(this.faker.address().streetName())
                    .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                    .postalCode(this.faker.address().zipCode())
                    .build();
            this.addresses.add(newAddress);
            final CustomerDTO newCustomer = CustomerDTO.builder().taxCode(this.faker.code().isbn10())
                    .firstName(this.faker.name().firstName())
                    .lastName(this.faker.name().lastName())
                    .email(this.faker.internet().emailAddress())
                    .birthDate(this.faker.date().birthday())
                    .emailVerifiedAt(this.faker.date().birthday())
                    .password(this.faker.internet().password())
                    .idCard(this.faker.file().toString())
                    .addresses(this.addresses)
                    .build();
            this.customers.add(newCustomer);
        }
    }

    @Test
    public void testGetAllCustomersReturnsEmptyCollection () throws Exception {
        CustomerResponse response = CustomerResponse.builder().pageSize(0)
                .last(true)
                .totalPages(0)
                .content(new ArrayList<>())
                .build();
        when(this.customerService.getAllCustomers(Pageable.ofSize(10))).thenReturn(response);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(response.getContent().size())));
    }

    @Test
    public void testGetAllCustomersReturnsManyCustomers () throws Exception {
        CustomerResponse response = CustomerResponse.builder().pageSize(10)
                .last(true)
                .totalPages(1)
                .content(this.customers)
                .build();
        when(this.customerService.getAllCustomers(Pageable.ofSize(10))).thenReturn(response);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo", "1")
                .param("pageSize", "10"));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(response.getContent().size())));
    }
}
