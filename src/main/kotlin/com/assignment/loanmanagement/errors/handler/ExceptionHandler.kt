package com.assignment.loanmanagement.errors.handler

import com.assignment.loanmanagement.DTO.Response
import com.assignment.loanmanagement.errors.AlreadyExistException
import com.assignment.loanmanagement.errors.BadRequestException
import com.assignment.loanmanagement.errors.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException, request: WebRequest):
            ResponseEntity<Response<Nothing>> {
        val data = Response<Nothing>(message = ex.message ?: "", error = true)
        return ResponseEntity(data, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AlreadyExistException::class)
    fun handleAlreadyExistException(ex: AlreadyExistException, request: WebRequest):
            ResponseEntity<Response<Nothing>> {
        val data = Response<Nothing>(message = ex.message ?: "", error = true)
        return ResponseEntity(data, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException, request: WebRequest):
            ResponseEntity<Response<Nothing>> {
        val data = Response<Nothing>(message = ex.message ?: "", error = true)
        return ResponseEntity(data, HttpStatus.NOT_FOUND)
    }

}
