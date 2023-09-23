package com.example.jasper.config

import com.example.jasper.entity.Employee
import com.example.jasper.entity.Post
import com.example.jasper.repository.EmployeeRepository
import com.example.jasper.repository.PostRepository
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperReport
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import kotlin.random.Random

@Configuration
class AppConfig {

    private val LOG: Logger = LoggerFactory.getLogger(AppConfig::class.java)

    @Bean
    fun compiledReport(): JasperReport {
        return  JasperCompileManager.compileReport(ClassPathResource("reports/report2.jrxml").inputStream)
    }
    @Bean
    fun runner(
        employeeRepository: EmployeeRepository,
        postRepository: PostRepository
    ): CommandLineRunner {

        return CommandLineRunner {


            val upperBound = 100
            val range = 1..upperBound
            LOG.info("Saving $upperBound employees...")
            for (i in range) {
                employeeRepository.save(Employee(Random.nextLong(), "firstName", "lastName", "email@email.com", "propasdfasdf","propasdfasdf","propasdfasdf","propasdfasdf","propasdfasdf","propasdfasdf",))
            }
            LOG.info("Employees saved")
            postRepository.save(Post(Random.nextLong(), "post 1", "post 1 text"))
            postRepository.save(Post(Random.nextLong(), "post 2", "post 2 text"))
            postRepository.save(Post(Random.nextLong(), "post 3", "post 3 text"))
            postRepository.save(Post(Random.nextLong(), "post 4", "post 4 text"))
            postRepository.save(Post(Random.nextLong(), "post 5", "post 5 text"))

        }
    }
}