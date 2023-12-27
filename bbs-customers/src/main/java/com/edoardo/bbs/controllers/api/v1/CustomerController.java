package com.edoardo.bbs.controllers.api.v1;

import com.edoardo.bbs.dtos.CustomerDTO;
import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.services.CustomerService;
import jakarta.websocket.server.PathParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
