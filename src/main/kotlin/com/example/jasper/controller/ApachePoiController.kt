package com.example.jasper.controller

import com.example.jasper.repository.EmployeeRepository
import net.sf.jasperreports.engine.export.ooxml.XlsxWorkbookHelper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApachePoiController(
    private val employeeRepository: EmployeeRepository
) {

    @GetMapping("/poi")
    fun poiGenerateXlsx() {
        //val workBook = XlsxWorkbookHelper()
    }
}