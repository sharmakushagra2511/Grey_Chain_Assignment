package com.assignment.loanmanagement.Service



import com.assignment.loanmanagement.DTO.LenderRequest
import com.assignment.loanmanagement.Models.Lender
import com.assignment.loanmanagement.Repository.LenderRepository
import com.assignment.loanmanagement.errors.AlreadyExistException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class LenderService(private val lenderRepository: LenderRepository
) {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    fun addLender(lenderRequest: LenderRequest) {
        log.info { "Received Request to add new lender" }
        val existingLender = lenderRepository.findByEmail(lenderRequest.email)
        if (existingLender != null) {
            throw AlreadyExistException("Lender with ${lenderRequest.email} already exists")
        } else {
            val newLender = Lender(
                    name = lenderRequest.name,
                    email = lenderRequest.email
            )
            lenderRepository.save(newLender)
            log.info { "Added new lender: ${lenderRequest.name} )" }
        }
    }

    fun findById(lenderId : Long) : Optional<Lender>
    {
        log.info { "Received request to find lender with lenderId" }
        return lenderRepository.findById(lenderId)
    }
}