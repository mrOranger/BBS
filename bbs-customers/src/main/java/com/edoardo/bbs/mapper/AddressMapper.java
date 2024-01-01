package com.edoardo.bbs.mapper;

import com.edoardo.bbs.dtos.AddressDTO;
import com.edoardo.bbs.entities.Address;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public final ModelMapper modelMapper;

    @Autowired
    public AddressMapper (ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Address convertToEntity (AddressDTO address) {
        return this.modelMapper.map(address, Address.class);
    }

    public AddressDTO convertToDTO (Address address) {
        return this.modelMapper.map(address, AddressDTO.class);
    }
}
