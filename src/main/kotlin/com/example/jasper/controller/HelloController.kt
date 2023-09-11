package com.example.jasper.controller

import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileNotFoundException

@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        val lines = ResourceUtils.getFile("classpath:reports/file.txt").readLines()
        File("some.txt").writeText("asdfas")
        ResourceUtils.getFile("classpath:reports/file.txt").appendText("appended 2")
        println(lines)
        return "hello"
    }
}