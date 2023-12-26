package com.edoardo.bbs.controllers.api.v1;

import com.edoardo.bbs.dtos.CustomerResponse;
import com.edoardo.bbs.services.CustomerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
