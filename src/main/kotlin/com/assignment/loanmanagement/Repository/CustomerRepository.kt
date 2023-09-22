package com.assignment.loanmanagement.Repository

import com.assignment.loanmanagement.Models.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {
 fun findByEmail(email : String) : Customer?
}