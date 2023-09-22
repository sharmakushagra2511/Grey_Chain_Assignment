package com.assignment.loanmanagement.Models

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "loans")
class Loan(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "customer_id", nullable = false)
        val customer: Customer,

        @ManyToOne
        @JoinColumn(name = "lender_id", nullable = false)
        val lender: Lender,

        @Column(name = "amount") var amount: Double,

        @Column(name = "remaining_amount") var remainingAmount: Double,

        @Column(name = "payment_date") val paymentDate: LocalDate,

        @Column(name = "interest_rate_per_day") var interestRatePerDay: Double,

        @Column(name = "due_date") var dueDate: LocalDate,

        @Column(name = "penalty_per_day") var penaltyPerDay: Double,

        @Column(name = "cancel_status") var cancelStatus: Boolean = false
)