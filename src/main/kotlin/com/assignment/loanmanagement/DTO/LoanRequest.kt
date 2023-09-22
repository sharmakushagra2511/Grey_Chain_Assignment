package com.assignment.loanmanagement.DTO

import java.time.LocalDate

data class LoanRequest(
        val customerId: Long,
        val lenderId: Long,
        val amount: Double,
        val remainingAmount: Double,
        val paymentDate: LocalDate,
        val interestPerDay: Double,
        val dueDate: LocalDate,
        val penaltyPerDay: Double,
        val cancel: Boolean? = false
)
