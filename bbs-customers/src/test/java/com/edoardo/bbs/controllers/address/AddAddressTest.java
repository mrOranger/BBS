package com.edoardo.bbs.controllers.address;


import com.edoardo.bbs.controllers.api.v1.AddressController;
import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.exceptions.MaximumAddressNumberException;
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
public class AddAddressTest {
    @MockBean
    private final AddressService addressService;

    private final ObjectMapper mapper;
    private AddressDTO addressDTO;
    private final MockMvc mockMvc;
    private final Faker faker;

    @Autowired
    public AddAddressTest (ObjectMapper objectMapper, MockMvc mockMvc, AddressService addressService) {
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
                .streetNumber(this.faker.number().numberBetween(1, 100))
                .postalCode(this.faker.address().zipCode())
                .build();
    }

    @Test @SneakyThrows
    public void testAddAddressToNotExistingCustomerReturnsNotFound () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Customer " + taxCode + " not found.";
        when(this.addressService.addAddress(taxCode, this.addressDTO))
                .thenThrow(new ResourceNotFoundException(errorMessage));


        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithAlreadyThreeAddressReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Maximum number of addresses.";
        when(this.addressService.addAddress(taxCode, this.addressDTO))
                .thenThrow(new MaximumAddressNumberException(errorMessage));


        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutCountryReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address country must be not empty.";
        this.addressDTO.setCountry(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidCountryReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address country must be at most 100 character long.";
        this.addressDTO.setCountry(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutStateReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address state must be not empty.";
        this.addressDTO.setState(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidStateReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address state must be at most 100 character long.";
        this.addressDTO.setState(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutCityReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address city must be not empty.";
        this.addressDTO.setCity(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidCityReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address city must be at most 100 character long.";
        this.addressDTO.setCity(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutStreetReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address street must be not empty.";
        this.addressDTO.setStreet(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidStreetReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address street must be at most 100 character long.";
        this.addressDTO.setStreet(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutStreetNumberReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address street number must be not empty.";
        this.addressDTO.setStreetNumber(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidStreetNumberReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address street number must be greater than or equal to 1.";
        this.addressDTO.setStreetNumber(-1);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithoutPostalCodeReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address postal code must be not empty.";
        this.addressDTO.setPostalCode(null);
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerWithInvalidPostalCodeNumberReturnsBadRequest () {
        final String taxCode = this.faker.code().isbn10();
        final String errorMessage = "Address postal code must be at most 50 character long.";
        this.addressDTO.setPostalCode(this.faker.lorem().sentence(200));
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
                .content(this.mapper.writeValueAsString(this.addressDTO))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test @SneakyThrows
    public void testAddAddressToCustomerReturnsOkay () {
        final String taxCode = this.faker.code().isbn10();
        AddressDTO savedAddress = this.addressDTO;
        savedAddress.setId(this.faker.number().randomDigit());
        when(this.addressService.addAddress(taxCode, this.addressDTO)).thenReturn(savedAddress);

        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/addresses/customer/{taxCode}", taxCode)
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
