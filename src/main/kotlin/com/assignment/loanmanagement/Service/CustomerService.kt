package com.assignment.loanmanagement.Service


import com.assignment.loanmanagement.DTO.CustomerRequest
import com.assignment.loanmanagement.Models.Customer
import com.assignment.loanmanagement.Repository.CustomerRepository
import com.assignment.loanmanagement.errors.AlreadyExistException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(
        private val customerRepository: CustomerRepository
) {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    fun addCustomer(customerRequest: CustomerRequest) {
        log.info { "Received Request to add new customer" }
        val existingCustomer = customerRepository.findByEmail(customerRequest.email)
        if (existingCustomer != null) {
            throw AlreadyExistException("Customer with ${customerRequest.email} already exists")
        } else {
            val newCustomer = Customer(
                    name = customerRequest.name,
                    email = customerRequest.email
            )
            customerRepository.save(newCustomer)
            log.info { "Added new customer: ${customerRequest.name} )" }
        }
    }

    fun findById(customerId : Long) : Optional<Customer>
    {
        log.info { "Received request to find a customer by customerId" }
        return  customerRepository.findById(customerId)
    }
}