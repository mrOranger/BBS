package com.edoardo.bbs.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data @Builder
public class CustomerDTO {
    private String taxCode;

    @NotEmpty(message = "Customer first name must not be empty.")
    private String firstName;

    @NotEmpty(message = "Customer last name must not be empty.")
    private String lastName;

    private LocalDate birthDate;

    @NotEmpty(message = "Customer email must not be empty.")
    private String email;
    private LocalDate emailVerifiedAt;

    @NotEmpty(message = "Customer password must not be empty.")
    private String password;

    @NotEmpty(message = "Customer id card must not be empty.")
    private String idCard;

    private Set<AddressDTO> addresses;
}
