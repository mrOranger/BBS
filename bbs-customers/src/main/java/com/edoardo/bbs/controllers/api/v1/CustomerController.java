package com.edoardo.bbs.controllers.api.v1;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.services.CustomerService;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/v1") @Log
public class CustomerController {

    @Autowired
    public CustomerService customerService;

    @GetMapping(value = "/customers", produces = "application/json")
    public ResponseEntity<CustomerResponse> getAllCustomers (
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return ResponseEntity.ok(this.customerService.getAllCustomers(PageRequest.of(pageNo, pageSize)));
    }

    @GetMapping(value = "/customers/firstName/{firstName}/lastName/{lastName}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByFirstNameAndLastName (
            @PathVariable String firstName,
            @PathVariable String lastName
    ) {
        return ResponseEntity.ok(this.customerService.getCustomersByFirstNameAndLastName(firstName, lastName));
    }

    @GetMapping(value = "/customers/birthDate/{birthDate}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByBirthDate (
            @PathVariable LocalDate birthDate
    ) {
        return ResponseEntity.ok(this.customerService.getCustomersByBirthDate(birthDate));
    }

    @GetMapping(value = "/customers/{taxCode}") @SneakyThrows
    public ResponseEntity<CustomerDTO> getCustomerByTaxCode (@PathVariable String taxCode) {
        return ResponseEntity.ok(this.customerService.getCustomerByTaxCode(taxCode));
    }

    @PostMapping(value = "/customers", produces = "application/json") @SneakyThrows
    public ResponseEntity<Void> postCustomer (
            @Valid @RequestBody CustomerDTO customer, UriComponentsBuilder uriBuilder
    ) {
        final CustomerDTO savedCustomer = this.customerService.createCustomer(customer);
        final URI customerURI = uriBuilder.path("/customers/{id}").buildAndExpand(savedCustomer.getTaxCode()).toUri();
        return ResponseEntity.created(customerURI).build();
    }

    @PutMapping(value = "/customers/{taxCode}", produces = "application/json") @SneakyThrows
    public ResponseEntity<CustomerDTO> putCustomer (
            @PathVariable String taxCode,
            @Valid @RequestBody CustomerDTO updatedCustomer
    ) {
        return ResponseEntity.ok(this.customerService.updateCustomer(taxCode, updatedCustomer));
    }

    @DeleteMapping(value = "/customers/{taxCode}") @SneakyThrows
    public ResponseEntity<CustomerDTO> deleteCustomer ( @PathVariable String taxCode ) {
        return ResponseEntity.ok(this.customerService.deleteCustomer(taxCode));
    }
}
