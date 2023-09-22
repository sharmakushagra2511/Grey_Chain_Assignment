package com.assignment.loanmanagement.DTO

import java.time.LocalDate

data class LoanDetailsResponse(
        val loanId: Long,
        val customerId: Long,
        val lenderId: Long,
        val amount: Double,
        val remainingAmount: Double,
        val paymentDate: LocalDate,
        val interestPerDay: Double,
        val dueDate: LocalDate,
        val penaltyPerDay: Double,
        val cancelStatus: Boolean
)
