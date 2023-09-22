package com.assignment.loanmanagement.Repository

import com.assignment.loanmanagement.Models.Loan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface LoanRepository : JpaRepository<Loan, Long> {
    fun findByCustomerId(customerId : Long) : List<Loan>
    fun findByLenderId(lenderId : Long) : List<Loan>
    fun findByInterestRatePerDay(interestRate : Double) : List<Loan>
}