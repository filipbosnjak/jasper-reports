package com.example.jasper

import java.io.File

fun main(args: Array<String>) {
    println(File("src/main/resources/reports/file.txt").readLines())
}