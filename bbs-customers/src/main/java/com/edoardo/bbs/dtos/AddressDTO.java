package com.edoardo.bbs.dtos;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AddressDTO {
    private Integer id;
    private String country;
    private String state;
    private String city;
    private String street;
    private Integer streetNumber;
    private String postalCode;

    private CustomerDTO customer;
}
