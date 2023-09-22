package com.assignment.loanmanagement.errors

data class ErrorMessage(
    val statusCode: Int,
    val message: String?,
    val error: Boolean = true
)
