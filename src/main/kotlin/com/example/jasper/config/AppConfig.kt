package com.example.jasper.config

import com.example.jasper.entity.Employee
import com.example.jasper.repository.EmployeeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    private val LOG: Logger = LoggerFactory.getLogger(AppConfig::class.java)

    @Bean
    fun runner(
        employeeRepository: EmployeeRepository
    ): CommandLineRunner {

        return CommandLineRunner {
            val newEmployee = employeeRepository.findById(12)
            if(newEmployee.isEmpty) {
                LOG.info("Saving new employee")
                employeeRepository.save(Employee(12, "firstName", "lastName", "email@email.com"))
                employeeRepository.save(Employee(1224, "firswqrtName", "lastName", "emertil@email.com"))
                employeeRepository.save(Employee(123245, "firswertName", "laerstName", "emaertil@email.com"))
                employeeRepository.save(Employee(13452, "firstertName", "lastName", "emaierl@email.com"))
                employeeRepository.save(Employee(13452342, "firstewrtName", "lastertName", "email@email.com"))
                employeeRepository.save(Employee(123452, "firsetName", "lastName", "email@eertmail.com"))
                employeeRepository.save(Employee(1345562, "firstertName", "lastNertame", "email@ertemail.com"))
            }
        }
    }
}