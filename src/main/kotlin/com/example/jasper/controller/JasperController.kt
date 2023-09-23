package com.example.jasper.controller

import com.example.jasper.config.JasperUtils
import com.example.jasper.repository.EmployeeRepository
import jakarta.servlet.http.HttpServletResponse
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.file.Paths


@RestController
class JasperController(
    private val employeeRepository: EmployeeRepository,
    private val response: HttpServletResponse
) {

    private val LOG = LoggerFactory.getLogger(JasperController::class.java)

    @GetMapping("/generatePdf")
    fun generatePdfReport(): String {

        // Compiled jasper report
        val report: JasperReport =
            JasperCompileManager.compileReport(ClassPathResource("reports/report4.jrxml").inputStream)

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
        val report: JasperReport =
            JasperCompileManager.compileReport(ClassPathResource("reports/report2.jrxml").inputStream)

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

    @GetMapping("/getXlsxReport", produces = ["application/octet-stream", "application/json"])
    fun getXlsxReport(
        @RequestParam count: Int?
    ): ResponseEntity<ByteArray> {

        val ds = JRBeanCollectionDataSource(tableRowDummyData())

        val params: Map<String, String> = mapOf(
            "exportLayout" to " Consolidated work time",
            "timePeriod" to " 01.08.2023 - 08.08.2023",
            "employees" to " Botond Vasarhelyi",
            "dateFormat" to " dd.MM.yyyy",
            "durationFormat" to " Decimal time with comma, e.g.: \"8,25\"",
            "rounding" to " No rounding (0 / 1 / 2 … 59min)",
            "aggregation" to " employee: yes project: yes day: yes",
            "includedTypesOfTime" to " work time: yes normal time: no special condition: no rest: no absence: no",
        )


        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")

        val byteArrayOutputStream = ByteArrayOutputStream()

        // Here we are filling report with params and data from datasource
        val jasperPrint1: JasperPrint = JasperFillManager.fillReport(JasperUtils.compiledReport, params, ds)

        // Xlsx exportER
        val xlsxExporter1: JRXlsxExporter = JRXlsxExporter()

        // Set input i.e. the compiled template with data (params and ds)
        xlsxExporter1.setExporterInput(SimpleExporterInput(jasperPrint1))

        // Set export output -
        xlsxExporter1.exporterOutput = SimpleOutputStreamExporterOutput(byteArrayOutputStream)

        // Export xlsx
        xlsxExporter1.exportReport()

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(byteArrayOutputStream.toByteArray())
    }

    @GetMapping("/download")
    fun downloadExcelFile(): ResponseEntity<Resource> {
        try {
            val resource: Resource = UrlResource(
                Paths.get("table.xlsx").toUri()
            )
            return if (resource.exists() && resource.isReadable) {
                val headers = HttpHeaders()
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${resource.filename}")
                ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (ex: IOException) {
            // Handle exceptions, e.g., file not found, access denied, etc.
            return ResponseEntity.status(500).build()
        }
    }
}

data class TableRow(
    val year: Int,
    val month: Int,
    val day: Int,
    val weekday: String,
    val startDate: String,
    val endDate: String,
    val company: String,
    val employeeId: String,
    val employeeFirstName: String,
    val employeeLastName: String,
    val employeeRole: String,
    val supervisorFirstName: String,
    val supervisorLastName: String,
    val costCenter: String,
    val projectName: String,
    val projectId: String,
    val status: String,
    val category: String,
    val typeOfRecord: String,
    val duration: String,
    val attendanceType: String,
    val durationAttendance: String,
    val absenceType: String,
    val absenceCode: String,
    val durationAbsence: String
)

fun tableRowDummyData(): List<TableRow> {
    return listOf(
        TableRow(
            year = 2023,
            month = 9,
            day = 20,
            weekday = "Monday",
            startDate = "2023-09-20",
            endDate = "2023-09-20",
            company = "Example Company",
            employeeId = "12345",
            employeeFirstName = "John",
            employeeLastName = "Doe",
            employeeRole = "Employee",
            supervisorFirstName = "Jane",
            supervisorLastName = "Smith",
            costCenter = "Cost Center 1",
            projectName = "Project A",
            projectId = "P123",
            status = "Active",
            category = "Category A",
            typeOfRecord = "Type 1",
            duration = "8 hours",
            attendanceType = "In-Person",
            durationAttendance = "8 hours",
            absenceType = "None",
            absenceCode = "",
            durationAbsence = ""
        ),
        TableRow(
            year = 2023,
            month = 9,
            day = 21,
            weekday = "Tuesday",
            startDate = "2023-09-21",
            endDate = "2023-09-21",
            company = "Example Company",
            employeeId = "54321",
            employeeFirstName = "Alice",
            employeeLastName = "Smith",
            employeeRole = "Manager",
            supervisorFirstName = "Bob",
            supervisorLastName = "Johnson",
            costCenter = "Cost Center 2",
            projectName = "Project B",
            projectId = "P456",
            status = "Active",
            category = "Category B",
            typeOfRecord = "Type 2",
            duration = "7.5 hours",
            attendanceType = "Remote",
            durationAttendance = "7.5 hours",
            absenceType = "Vacation",
            absenceCode = "VAC",
            durationAbsence = "8 hours"
        ),
        // Add more TableRow objects here for a total of 5
        TableRow(
            year = 2023,
            month = 9,
            day = 22,
            weekday = "Wednesday",
            startDate = "2023-09-22",
            endDate = "2023-09-22",
            company = "Example Company",
            employeeId = "67890",
            employeeFirstName = "Emily",
            employeeLastName = "Brown",
            employeeRole = "Employee",
            supervisorFirstName = "David",
            supervisorLastName = "Wilson",
            costCenter = "Cost Center 3",
            projectName = "Project C",
            projectId = "P789",
            status = "Active",
            category = "Category C",
            typeOfRecord = "Type 3",
            duration = "9 hours",
            attendanceType = "In-Person",
            durationAttendance = "9 hours",
            absenceType = "None",
            absenceCode = "",
            durationAbsence = ""
        ),
        TableRow(
            year = 2023,
            month = 9,
            day = 23,
            weekday = "Thursday",
            startDate = "2023-09-23",
            endDate = "2023-09-23",
            company = "Example Company",
            employeeId = "98765",
            employeeFirstName = "Sarah",
            employeeLastName = "Johnson",
            employeeRole = "Manager",
            supervisorFirstName = "Michael",
            supervisorLastName = "Davis",
            costCenter = "Cost Center 4",
            projectName = "Project D",
            projectId = "P999",
            status = "Active",
            category = "Category D",
            typeOfRecord = "Type 4",
            duration = "6.5 hours",
            attendanceType = "Remote",
            durationAttendance = "6.5 hours",
            absenceType = "Sick Leave",
            absenceCode = "SL",
            durationAbsence = "8 hours"
        ),
        TableRow(
            year = 2023,
            month = 9,
            day = 24,
            weekday = "Friday",
            startDate = "2023-09-24",
            endDate = "2023-09-24",
            company = "Example Company",
            employeeId = "13579",
            employeeFirstName = "Robert",
            employeeLastName = "Anderson",
            employeeRole = "Employee",
            supervisorFirstName = "Laura",
            supervisorLastName = "Moore",
            costCenter = "Cost Center 5",
            projectName = "Project E",
            projectId = "P246",
            status = "Active",
            category = "Category E",
            typeOfRecord = "Type 5",
            duration = "7 hours",
            attendanceType = "In-Person",
            durationAttendance = "7 hours",
            absenceType = "None",
            absenceCode = "",
            durationAbsence = ""
        )
    )
}