package com.example.jasper.config

import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperReport
import org.springframework.core.io.ClassPathResource

class JasperUtils {


    companion object {

        val compiledReport: JasperReport = compileReport()
        private fun compileReport(): JasperReport {
            return JasperCompileManager.compileReport(ClassPathResource("reports/table.jrxml").inputStream)
        }
    }
}