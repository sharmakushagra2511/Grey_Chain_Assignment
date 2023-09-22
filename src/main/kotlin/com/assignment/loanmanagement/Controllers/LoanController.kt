package com.assignment.loanmanagement.Controllers

import com.assignment.loanmanagement.DTO.LoanRequest
import com.assignment.loanmanagement.DTO.Response
import com.assignment.loanmanagement.Models.Loan
import com.assignment.loanmanagement.Service.LoanService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanController(
        private val loanService: LoanService
) {
    @PostMapping("/add")
    fun addLoan(@RequestBody loanRequest: LoanRequest): ResponseEntity<Response<String>> {
        loanService.addLoan(loanRequest)
        return ResponseEntity.ok(Response("Loan added successfully"))
    }

    @GetMapping
    fun getAllLoans(): ResponseEntity<Response<List<Loan>>> {
        val loans = loanService.getAllLoans()
        return ResponseEntity.ok(Response("All the loans are", data = loans))
    }

    @GetMapping("/loanId/{loanId}")
    fun getLoanById(@PathVariable loanId: Long): ResponseEntity<Response<Loan>> {
        val loan = loanService.getLoanById(loanId = loanId)
        return ResponseEntity.ok(Response("Loan with id $loanId :", data = loan))
    }

    @GetMapping("/customerId/{customerId}")
    fun getLoansByCustomerId(@PathVariable customerId: Long): ResponseEntity<Response<List<Loan>>> {
        val loans = loanService.getLoanByCustomerId(customerId)
        return ResponseEntity.ok(Response("Loans for customer with id: $customerId :", data = loans))
    }

    @GetMapping("/lenderId/{lenderId}")
    fun getLoansByLenderId(@PathVariable lenderId: Long): ResponseEntity<Response<List<Loan>>> {
        val loans = loanService.getLoanByLenderId(lenderId)
        return ResponseEntity.ok(Response("Loans for lender with id $lenderId :", data = loans))
    }

    @GetMapping("/aggregate/lenderId/{lenderId}")
    fun getAggregateLoansByLenderId(@PathVariable lenderId: Long): ResponseEntity<Response<Map<String, Double>>> {
        val aggregateLoans = loanService.getAggregateLoansByLender(lenderId)
        return ResponseEntity.ok(Response(
                "Aggregate Loans for lender with id $lenderId :", data = aggregateLoans))
    }

    @GetMapping("/aggregate/customerId/{customerId}")
    fun getAggregateLoansByCustomerId(@PathVariable customerId: Long): ResponseEntity<Response<Map<String, Double>>> {
        val aggregateLoans = loanService.getAggregateLoansByCustomerId(customerId)
        return ResponseEntity.ok(Response(
                "Aggregate loans for customer with id $customerId : ", data = aggregateLoans
        ))
    }

    @GetMapping("/aggregate/interestRate/{interestRate}")
    fun getAggregateLoansByInterestRate(@PathVariable interestRate: Double):
            ResponseEntity<Response<Map<String, Double>>> {
        val aggregateLoans = loanService.getAggregateLoansByInterest(interestRate)
        return ResponseEntity.ok(Response(
                "Aggregate Loans with Interest rate $interestRate% :", data = aggregateLoans
        ))
    }

}