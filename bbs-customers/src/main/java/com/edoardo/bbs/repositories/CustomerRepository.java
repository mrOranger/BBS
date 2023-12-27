package com.edoardo.bbs.repositories;

import com.edoardo.bbs.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    List<Customer> findByFirstNameAndLastName (String firstName, String lastName);
    List<Customer> findByBirthDate (LocalDate birthDate);

}
