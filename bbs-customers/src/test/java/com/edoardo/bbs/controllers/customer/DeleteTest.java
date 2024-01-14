package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CustomerController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DeleteTest {
    @MockBean
    private CustomerService customerService;

    private CustomerDTO customer;
    private ObjectMapper mapper;
    private final MockMvc mockMvc;
    private final String baseUrl;

    @Autowired
    public DeleteTest (MockMvc mockMvc, CustomerService customerService, ObjectMapper mapper) {
        this.customerService = customerService;
        this.baseUrl = "/api/v1/customers/";
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @BeforeEach
    public void init () {
        Faker faker = new Faker();
        Set<AddressDTO> addresses = new HashSet<>();
        final AddressDTO newAddress = AddressDTO.builder().country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .street(faker.address().streetName())
                .streetNumber(Integer.parseInt(faker.address().streetAddressNumber()))
                .postalCode(faker.address().zipCode())
                .build();
        addresses.add(newAddress);
        this.customer = CustomerDTO.builder().taxCode(faker.code().isbn10())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .emailVerifiedAt(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .password(faker.internet().password())
                .idCard(faker.file().fileName())
                .addresses(addresses)
                .build();
    }

    @Test @SneakyThrows
    public void testDeleteCustomerReturnsNotFoundException () {
        when(this.customerService.deleteCustomer(this.customer.getTaxCode()))
                .thenThrow(new ResourceNotFoundException("Not found."));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Not found.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testDeleteCustomerReturnsOkResponse () {
        when(this.customerService.deleteCustomer(this.customer.getTaxCode()))
                .thenReturn(this.customer);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete(this.baseUrl + customer.getTaxCode())
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
