package com.assignment.loanmanagement.Controllers

import com.assignment.loanmanagement.DTO.LenderRequest
import com.assignment.loanmanagement.DTO.Response
import com.assignment.loanmanagement.Service.LenderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lender")
class LenderController(
        val lenderService: LenderService
) {
    @PostMapping("/add")
    fun addLender(@RequestBody lenderRequest: LenderRequest): ResponseEntity<Response<String>> {
        lenderService.addLender(lenderRequest)
        return ResponseEntity.ok(Response(message = "Lender Added Successfully"))
    }
}