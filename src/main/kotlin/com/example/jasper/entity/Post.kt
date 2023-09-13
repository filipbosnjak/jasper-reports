package com.example.jasper.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
class Post(
    @Id
    val id: Long,

    @Column
    val title: String,

    @Column
    val text: String,
) {
}