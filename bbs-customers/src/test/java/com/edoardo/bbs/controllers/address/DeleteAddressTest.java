package com.edoardo.bbs.controllers.address;


import com.edoardo.bbs.controllers.api.v1.AddressController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.ResourceNotFoundException;
import com.edoardo.bbs.services.AddressService;
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

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AddressController.class) @AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class DeleteAddressTest {

    @MockBean
    private final AddressService addressService;

    private final ObjectMapper mapper;
    private AddressDTO addressDTO;
    private final MockMvc mockMvc;
    private final Faker faker;

    @Autowired
    public DeleteAddressTest (ObjectMapper objectMapper, MockMvc mockMvc, AddressService addressService) {
        this.addressService = addressService;
        this.mapper = objectMapper;
        this.faker = new Faker();
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void init () {
        this.addressDTO = AddressDTO.builder()
                .state(this.faker.address().state())
                .city(this.faker.address().city())
                .country(this.faker.address().country())
                .street(this.faker.address().streetName())
                .streetNumber(Integer.parseInt(this.faker.address().streetAddressNumber()))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test @SneakyThrows
    public void testDeleteNotExistingAddressReturnsNotFound () {
        final String taxCode = this.faker.code().isbn10();
        final String address = Integer.toString(this.faker.number().randomDigit());
        final String errorMessage = "Address " + address + " not found.";
        when(this.addressService.deleteAddress(taxCode, address))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/addresses/address/{id}/customer/{taxCode}", address, taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test @SneakyThrows
    public void testDeleteAddressOfNotExistingCustomerReturnsNotFound () {
        final String taxCode = this.faker.code().isbn10();
        final String address = Integer.toString(this.faker.number().randomDigit());
        final String errorMessage = "Customer " + taxCode + " not found.";
        when(this.addressService.deleteAddress(taxCode, address))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/addresses/address/{id}/customer/{taxCode}", address, taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test @SneakyThrows
    public void testDeleteAddressReturnsOk () {
        final String taxCode = this.faker.code().isbn10();
        final String address = Integer.toString(this.faker.number().randomDigit());
        when(this.addressService.deleteAddress(taxCode, address))
                .thenReturn(this.addressDTO);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/addresses/address/{id}/customer/{taxCode}", address, taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country", CoreMatchers.is(this.addressDTO.getCountry())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state", CoreMatchers.is(this.addressDTO.getState())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city", CoreMatchers.is(this.addressDTO.getCity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is(this.addressDTO.getStreet())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetNumber", CoreMatchers.is(this.addressDTO.getStreetNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postalCode", CoreMatchers.is(this.addressDTO.getPostalCode())))
                .andDo(MockMvcResultHandlers.print());
    }
}
