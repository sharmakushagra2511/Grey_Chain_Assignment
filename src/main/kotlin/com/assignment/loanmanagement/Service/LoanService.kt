package com.assignment.loanmanagement.Service

import com.assignment.loanmanagement.DTO.LoanRequest
import com.assignment.loanmanagement.Models.Loan
import com.assignment.loanmanagement.Repository.LoanRepository
import com.assignment.loanmanagement.errors.BadRequestException
import com.assignment.loanmanagement.errors.NotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.transaction.Transactional

@Service
class LoanService(
        private val loanRepository: LoanRepository,
        private val customerService: CustomerService,
        private val lenderService: LenderService,
) {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    fun validAmount(amount: Double): Boolean {
        if (amount <= 0) {
            return false
        } else {
            return true
        }
    }

    @Transactional
    fun addLoan(loanRequest: LoanRequest) {
        log.info { "Received Request to add a loan" }
        val customer = customerService.findById(loanRequest.customerId)
                .orElseThrow { NotFoundException("Customer with id ${loanRequest.customerId} not found") }
        val lender = lenderService.findById(loanRequest.lenderId)
                .orElseThrow { NotFoundException("Lender with id ${loanRequest.lenderId} not found") }
        if (!validAmount(loanRequest.amount)) {
            throw BadRequestException("Amount: ${loanRequest.amount} is invalid")
        }
        if (loanRequest.remainingAmount > loanRequest.amount) {
            throw BadRequestException("Remaining Amount cannot be more than Amount")
        }
        val paymentDate = LocalDate.parse(loanRequest.paymentDate.toString(), dateFormatter)
        if(paymentDate.isAfter(LocalDate.now()))
        {
            throw BadRequestException("Payment Date cannot be ahead of current date")
        }
        val dueDate = LocalDate.parse(loanRequest.dueDate.toString(), dateFormatter)
        if(dueDate.isBefore(paymentDate))
        {
            throw BadRequestException("Due date cannot be behind payment date")
        }

        if (paymentDate.isAfter(dueDate)) {
            throw BadRequestException("Payment Date can't be ahead of Due date")
        }
        if (loanRequest.interestPerDay < 0) {
            throw BadRequestException("Interest Rate cannot be less than 0")
        }
        if (loanRequest.penaltyPerDay < 0) {
            throw BadRequestException("Penalty percentage cannot be less than 0")
        }

        val loan = Loan(
                customer = customer,
                lender = lender,
                amount = loanRequest.amount,
                remainingAmount = loanRequest.remainingAmount,
                paymentDate = paymentDate,
                interestRatePerDay = loanRequest.interestPerDay,
                dueDate = dueDate,
                penaltyPerDay = loanRequest.penaltyPerDay,
                cancelStatus = false
        )
        loanRepository.save(loan)
        log.info { "Loan added successfully" }
    }

    fun getAllLoans(): List<Loan> {
        log.info { "Received request to get all loans" }
        val loans = loanRepository.findAll()
        if (loans.isEmpty()) {
            throw NotFoundException("No loans found")
        }
        log.info { "Found the list of loans" }
        for (loan in loans) {
            if (loan.dueDate < LocalDate.now()) {
                log.warn { "The Due date for payment has surpassed for loan with id: ${loan.id}" }
            }
        }
        return loans
    }

    fun getLoanById(loanId: Long): Loan {
        log.info { "Received request to get loan by loanId" }
        return loanRepository.findById(loanId).orElseThrow { NotFoundException("Loan with id $loanId not found ") }
    }

    fun getLoanByCustomerId(customerId: Long): List<Loan> {
        log.info { "Received request to get loans for customerId $customerId" }
        val customer = customerService.findById(customerId).orElseThrow {
            NotFoundException("Customer with id $customerId not found")
        }
        val loans = loanRepository.findByCustomerId(customerId)
        if (loans.isEmpty()) {
            throw NotFoundException("Loans for customer with id: $customerId not found")
        }
        for (loan in loans) {
            if (loan.dueDate < LocalDate.now()) {
                log.warn { "The Due date for payment has surpassed for loan with id: ${loan.id}" }
            }
        }
        return loans
    }

    fun getLoanByLenderId(lenderId: Long): List<Loan> {
        log.info { "Received request to get loans for lender with id: $lenderId" }
        val lender = lenderService.findById(lenderId).orElseThrow {
            NotFoundException("Lender with id: $lenderId not found")
        }
        val loans = loanRepository.findByLenderId(lenderId)
        if (loans.isEmpty()) {
            throw NotFoundException("Loans for lender with $lender not found")
        }
        for (loan in loans) {
            if (loan.dueDate < LocalDate.now()) {
                log.warn { "The Due date for payment has surpassed for loan with id: ${loan.id}" }
            }
        }
        return loans
    }

    private fun getInterest(loan: Loan): Double {
        val totalDays = ChronoUnit.DAYS.between(loan.paymentDate, LocalDate.now())
        val interest =
                (loan.interestRatePerDay / 100) * totalDays.toDouble() * loan.remainingAmount
        return if (interest < 0) 0.0 else interest
    }

    private fun getPenalty(loan: Loan): Double {
        val totalDays = ChronoUnit.DAYS.between(loan.dueDate, LocalDate.now())
        val penalty = (loan.penaltyPerDay / 100.0) * totalDays * loan.remainingAmount.toDouble()
        return if (penalty < 0.0) 0.0 else penalty
    }

    fun getAggregateLoansByLender(lenderId: Long): Map<String, Double> {
        log.info { "Received request to get aggregate loans for lender with id: $lenderId" }

        val lender = lenderService.findById(lenderId).orElseThrow {
            NotFoundException("Lender with id: $lenderId not found")
        }

        val loans = loanRepository.findByLenderId(lenderId)
        if (loans.isEmpty()) {
            throw NotFoundException("Loans for lender with id: $lenderId not found")
        }
        for (loan in loans) {
            if (loan.dueDate < LocalDate.now()) {
                log.warn { "The Due date for payment has surpassed for loan with id: ${loan.id}" }
            }
        }

        var totalLendedAmount = 0.0
        var totalRemainingAmount = 0.0
        var totalInterest = 0.0
        var totalPenalty = 0.0

        for (loan in loans) {
            totalLendedAmount += loan.amount
            totalRemainingAmount += loan.remainingAmount
            totalInterest += getInterest(loan)
            totalPenalty += getPenalty(loan)
        }

        return mapOf(
                "totalLendedAmount" to totalLendedAmount,
                "totalRemainingAmount" to totalRemainingAmount,
                "totalInterestEarned" to totalInterest,
                "totalPenaltyEarned" to totalPenalty
        )
    }

    fun getAggregateLoansByCustomerId(customerId: Long): Map<String, Double> {
        log.info { "Received request to get aggregate loans for customer with id: $customerId" }

        val customer = customerService.findById(customerId).orElseThrow {
            NotFoundException("Customer with id: $customerId not found")
        }

        val loans = loanRepository.findByCustomerId(customerId)

        for (loan in loans) {
            if (loan.dueDate < LocalDate.now()) {
                log.warn { "The Due date for payment has surpassed for loan with id: ${loan.id}" }
            }
        }

        if (loans.isEmpty()) {
            throw NotFoundException("Loans for customer with id: $customerId not found")
        }

        var totalBorrowedAmount = 0.0
        var totalRemainingAmount = 0.0
        var totalInterest = 0.0
        var totalPenalty = 0.0

        for (loan in loans) {
            totalBorrowedAmount += loan.amount
            totalRemainingAmount += loan.remainingAmount
            totalInterest += getInterest(loan)
            totalPenalty += getPenalty(loan)
        }

        return mapOf(
                "totalBorrowedAmount" to totalBorrowedAmount,
                "totalRemainingAmount" to totalRemainingAmount,
                "totalInterestIssued" to totalInterest,
                "totalPenaltyIssued" to totalPenalty
        )
    }

    fun getAggregateLoansByInterest(interestRate: Double): Map<String, Double> {
        log.info { "Received request to get aggregate loans by interest rate: $interestRate" }

        val loans = loanRepository.findByInterestRatePerDay(interestRate)
        if (loans.isEmpty()) {
            throw NotFoundException("No loans found with interest rate: $interestRate")
        }

        var totalAmount = 0.0
        var totalRemainingAmount = 0.0
        var totalInterest = 0.0
        var totalPenalty = 0.0

        for (loan in loans) {
            totalAmount += loan.amount
            totalRemainingAmount += loan.remainingAmount
            totalInterest += getInterest(loan)
            totalPenalty += getPenalty(loan)
        }

        return mapOf(
                "totalAmount" to totalAmount,
                "totalRemainingAmount" to totalRemainingAmount,
                "totalInterest" to totalInterest,
                "totalPenalty" to totalPenalty
        )
    }


}