package com.assignment.loanmanagement.DTO

data class CustomerDetailsResponse(
        val customerId : Long,
        val name : String,
        val email : String?
)
