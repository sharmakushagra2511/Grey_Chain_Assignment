package com.assignment.loanmanagement.DTO

class Response<T>(
        val message: String,
        val data: T? = null,
        error: Boolean? = false
)
