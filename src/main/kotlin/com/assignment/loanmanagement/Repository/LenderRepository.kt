package com.assignment.loanmanagement.Repository

import com.assignment.loanmanagement.Models.Lender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LenderRepository : JpaRepository<Lender, Long> {
    fun findByEmail (email : String) : Lender?
}