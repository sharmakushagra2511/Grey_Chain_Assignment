package com.assignment.loanmanagement.Controllers

import com.assignment.loanmanagement.DTO.CustomerRequest
import com.assignment.loanmanagement.DTO.Response
import com.assignment.loanmanagement.Service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer")
class CustomerController
(
        val customerService: CustomerService
) {
    @PostMapping("/add")
    fun addCustomer(@RequestBody customerRequest: CustomerRequest):
            ResponseEntity<Response<String>> {
        customerService.addCustomer(customerRequest)
        return ResponseEntity.ok(Response(message = "Customer added successfully"))

    }
}