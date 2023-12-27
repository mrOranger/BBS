package com.edoardo.bbs.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data @Builder
public class CustomerDTO {
    private String taxCode;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String email;
    private LocalDate emailVerifiedAt;
    private String password;
    private String idCard;

    private Set<AddressDTO> addresses;
}
