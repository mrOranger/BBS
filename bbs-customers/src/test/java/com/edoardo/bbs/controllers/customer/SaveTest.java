package com.edoardo.bbs.controllers.customer;

import com.edoardo.bbs.controllers.api.v1.CustomerController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.exceptions.ResourceConflictException;
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
public class SaveTest {

    private final MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private CustomerDTO customer;

    private ObjectMapper mapper;

    @Autowired
    public SaveTest (MockMvc mockMvc, CustomerService customerService, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.customerService = customerService;
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
                .idCard(faker.file().toString())
                .addresses(addresses)
                .build();
    }

    @Test
    public void testSaveCustomerReturnsConflictException () throws Exception {
        when(this.customerService.createCustomer(this.customer)).thenThrow(new ResourceConflictException("Conflict."));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Conflict.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testSaveCustomerReturnsOkayResponse () throws Exception {
        when(this.customerService.createCustomer(this.customer)).thenReturn(this.customer);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers")
                .content(this.mapper.writeValueAsString(this.customer))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Customer created successfully.")))
                .andDo(MockMvcResultHandlers.print());
    }
}
