package com.assignment.loanmanagement.DTO

data class LenderDetailsResponse(
        val lenderId: Long,
        val name: String,
        val email: String?
)
