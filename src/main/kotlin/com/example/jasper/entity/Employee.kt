package com.example.jasper.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
class Employee(
    @Id
    val id: Long,

    @Column
    val firstName: String,

    @Column
    val lastName: String,

    @Column
    val email: String,
    
    @Column
    val prop1: String,

    @Column
    val prop2: String,

    @Column
    val prop3: String,

    @Column
    val prop4: String,

    @Column
    val prop5: String,

    @Column
    val prop6: String,
) {
}