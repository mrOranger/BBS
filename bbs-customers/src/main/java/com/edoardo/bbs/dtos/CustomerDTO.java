package com.edoardo.bbs.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data @Builder
public class CustomerDTO {
    private String taxCode;
    private String firstName;
    private String lastName;
    private Date birthDate;

    private String email;
    private Date emailVerifiedAt;
    private String password;
    private String idCard;

    private Set<AddressDTO> addresses = new HashSet<>();
}
