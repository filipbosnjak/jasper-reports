package com.example.jasper.controller

import com.example.jasper.entity.Employee
import com.example.jasper.entity.Post
import com.example.jasper.repository.EmployeeRepository
import com.example.jasper.repository.PostRepository
import jakarta.servlet.http.HttpServletResponse
import net.sf.jasperreports.engine.*
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.OutputStream
import javax.sql.DataSource


@RestController
class JasperController(
    private val dataSource: DataSource,
    private val employeeRepository: EmployeeRepository,
    private val postRepository: PostRepository,
    private val response: HttpServletResponse
) {

    private val LOG = LoggerFactory.getLogger(JasperController::class.java)

    @GetMapping("/generatePdf")
    fun generatePdfReport(): String {

        // Compiled jasper report
        val report: JasperReport = JasperCompileManager.compileReport("C:\\Users\\Filip\\Desktop\\Projects\\JasperReports\\jasper\\src\\main\\resources\\reports\\report2.jrxml")

        val params: Map<String, String> = mapOf(
            "param1" to "Param 1",
            "param2" to "Param 2"
        )


        val ds = JRBeanCollectionDataSource(employeeRepository.findAll())

        // Here we are filling report with params and data from datasource
        val jasperPrint: JasperPrint = JasperFillManager.fillReport(report, params, ds)


        // Pdf exporter
        val pdfExporter: JRPdfExporter = JRPdfExporter()

        // Set input
        pdfExporter.setExporterInput(SimpleExporterInput(jasperPrint))

        // Set PDF output
        pdfExporter.exporterOutput = SimpleOutputStreamExporterOutput("simple_out_report.pdf")

        // Export pdf
        pdfExporter.exportReport()

        /*
        * Alternative way of exporting

        FileOutputStream("file_out_report.pdf").use {
            it.write(JasperExportManager.exportReportToPdf(jasperPrint))
        }

        * */

        LOG.info("PDF Report compiled ok")
        return "PDF Report generated OK"
    }

    @GetMapping("/generateXlsx")
    fun generateXlsxReport(): String {

        // Compiled jasper report
        val report: JasperReport = JasperCompileManager.compileReport("C:\\Users\\Filip\\Desktop\\Projects\\JasperReports\\jasper\\src\\main\\resources\\reports\\report2.jrxml")

        val params: Map<String, String> = mapOf(
            "param1" to "Param 1",
            "param2" to "Param 2"
        )


        val ds = JRBeanCollectionDataSource(employeeRepository.findAll())

        // Here we are filling report with params and data from datasource
        val jasperPrint: JasperPrint = JasperFillManager.fillReport(report, params, ds)


        // Xlsx export
        val xlsxExporter: JRXlsxExporter = JRXlsxExporter()

        // Set input
        xlsxExporter.setExporterInput(SimpleExporterInput(jasperPrint))

        // Set XLSX output -> it can be simple file name or response.outputStream
        xlsxExporter.exporterOutput = SimpleOutputStreamExporterOutput("simple_out_report.xlsx")

        xlsxExporter.exportReport()

        /*
        * Alternative way of exporting

        FileOutputStream("file_out_report.xlsx").use {
            it.write(JasperExportManager.exportReportToPdf(jasperPrint))
        }

        * */

        LOG.info("XLSX Report compiled ok")
        return "XLSX Report generated OK"
    }

    @GetMapping("/getXlsxReport", produces = ["application/octet-stream", "application/json"] )
    fun getXlsxReport(): String {

        // Compiled jasper report
        val report: JasperReport = JasperCompileManager.compileReport("C:\\Users\\Filip\\Desktop\\Projects\\JasperReports\\jasper\\src\\main\\resources\\reports\\report2.jrxml")

        val params: Map<String, String> = mapOf(
            "param1" to "Param 1",
            "param2" to "Param 2"
        )

        val ds = JRBeanCollectionDataSource(employeeRepository.findAll())

        // Here we are filling report with params and data from datasource
        val jasperPrint: JasperPrint = JasperFillManager.fillReport(report, params, ds)

        // Xlsx export
        val xlsxExporter: JRXlsxExporter = JRXlsxExporter()

        // Set input
        xlsxExporter.setExporterInput(SimpleExporterInput(jasperPrint))

        // Set XLSX output
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx")
        response.contentType = "application/octet-stream"
        xlsxExporter.exporterOutput = SimpleOutputStreamExporterOutput(response.outputStream)


        // Export xlsx
        xlsxExporter.exportReport()

        LOG.info("XLSX Report compiled ok and sent with HttpServletResponse")

        return "Report compiled ok"
    }

/*    @GetMapping("/getXlsxReport", produces = ["application/octet-stream", "application/json"])
    fun getXlsxReport(): ResponseEntity<Resource> {
        // Compiled jasper report
        val report: JasperReport = JasperCompileManager.compileReport("C:\\Users\\Filip\\Desktop\\Projects\\JasperReports\\jasper\\src\\main\\resources\\reports\\report2.jrxml")

        val params: Map<String, String> = mapOf(
            "param1" to "Param 1",
            "param2" to "Param 2"
        )

        val ds = JRBeanCollectionDataSource(employeeRepository.findAll())

        // Here we are filling report with params and data from datasource
        val jasperPrint: JasperPrint = JasperFillManager.fillReport(report, params, ds)

        // Xlsx export
        val xlsxExporter: JRXlsxExporter = JRXlsxExporter()

        // Set input
        xlsxExporter.setExporterInput(SimpleExporterInput(jasperPrint))

        // Set XLSX output
        xlsxExporter.exporterOutput = SimpleOutputStreamExporterOutput("simple_out_report.xlsx")

        return ResponseEntity.ok(ClassPathResource("application.properties"))
    }*/

    fun outputStreamToByteArray(out: OutputStream): ByteArray {
        val baos = ByteArrayOutputStream()
        baos.writeTo(out)
        return baos.toByteArray()
    }
}