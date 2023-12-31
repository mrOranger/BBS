package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
