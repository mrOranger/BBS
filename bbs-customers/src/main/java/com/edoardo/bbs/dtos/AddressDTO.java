package com.edoardo.bbs.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AddressDTO {
    private Integer id;

    @NotNull(message = "Address country must be not empty.")
    @Size(min = 1, max = 100, message = "Address country must be at most 100 character long.")
    private String country;

    @NotNull(message = "Address state must be not empty.")
    @Size(min = 1, max = 100, message = "Address state must be at most 100 character long.")
    private String state;

    @NotNull(message = "Address city must be not empty.")
    @Size(min = 1, max = 100, message = "Address city must be at most 100 character long.")
    private String city;

    @NotNull(message = "Address street must be not empty.")
    @Size(min = 1, max = 100, message = "Address street must be at most 100 character long.")
    private String street;

    @NotNull(message = "Address street number must be not empty.")
    @Min(value = 1, message = "Address street number must be greater than or equal to 1.")
    private Integer streetNumber;

    @NotNull(message = "Address postal code must be not empty.")
    @Size(min = 1, max = 50, message = "Address postal code must be at most 50 character long.")
    private String postalCode;

    private CustomerDTO customer;
}
