package com.edoardo.bbs.dtos;

import com.edoardo.bbs.validation.rules.BirthDateValid;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerDTO implements Serializable {

    private String taxCode;

    @NotNull(message = "Customer first name must not be null.")
    @Size(min = 1, max = 50, message = "Customer first name length must be between 1 and 50 chars.")
    private String firstName;

    @NotNull(message = "Customer last name must not be null.")
    @Size(min = 1, max = 50, message = "Customer last name length must be between 1 and 50 chars.")
    private String lastName;

    @NotNull(message = "Customer last name must not be null.")
    @BirthDateValid
    private LocalDate birthDate;

    @NotNull(message = "Customer email must not be null.")
    @Size(min = 1, max = 50, message = "Customer email length must be between 1 and 50 chars.")
    @Email(message = "Customer email must be valid.")
    private String email;

    private LocalDate emailVerifiedAt;

    @NotNull(message = "Customer password must not be null.")
    @Size(min = 1, max = 50, message = "Customer password length must be between 1 and 50 chars.")
    private String password;

    @NotNull(message = "Customer id card must not be null.")
    @Size(min = 1, message = "Customer id card length must not be empty.")
    private String idCard;

    @NotEmpty(message = "Customer addresses cannot be empty.")
    @Size(min = 1, max = 3, message = "Customer addresses must be at least 1 and at most 3.")
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Set<AddressDTO> addresses;
}
