package com.assignment.loanmanagement.Models

import com.assignment.loanmanagement.DTO.CustomerDetailsResponse
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "customers")
class Customer(
        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        var id: Long? = null,

        @Column var name: String = "",

        @Column var email: String? = null
)

