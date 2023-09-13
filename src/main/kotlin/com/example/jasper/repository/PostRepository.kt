package com.example.jasper.repository

import com.example.jasper.entity.Employee
import com.example.jasper.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}