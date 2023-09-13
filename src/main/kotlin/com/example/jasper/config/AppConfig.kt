package com.example.jasper.config

import com.example.jasper.entity.Employee
import com.example.jasper.entity.Post
import com.example.jasper.repository.EmployeeRepository
import com.example.jasper.repository.PostRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.random.Random

@Configuration
class AppConfig {

    private val LOG: Logger = LoggerFactory.getLogger(AppConfig::class.java)

    @Bean
    fun runner(
        employeeRepository: EmployeeRepository,
        postRepository: PostRepository
    ): CommandLineRunner {

        return CommandLineRunner {
            val newEmployee = employeeRepository.findById(12)
            if(newEmployee.isEmpty) {
                LOG.info("Saving new employee")
                employeeRepository.save(Employee(Random.nextLong(), "firstName", "lastName", "email@email.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firswqrtName", "lastName", "emertil@email.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firswertName", "laerstName", "emaertil@email.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firstertName", "lastName", "emaierl@email.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firstewrtName", "lastertName", "email@email.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firsetName", "lastName", "email@eertmail.com"))
                employeeRepository.save(Employee(Random.nextLong(), "firstertName", "lastNertame", "email@ertemail.com"))
                postRepository.save(Post(Random.nextLong(), "post 1", "post 1 text"))
                postRepository.save(Post(Random.nextLong(), "post 2", "post 2 text"))
                postRepository.save(Post(Random.nextLong(), "post 3", "post 3 text"))
                postRepository.save(Post(Random.nextLong(), "post 4", "post 4 text"))
                postRepository.save(Post(Random.nextLong(), "post 5", "post 5 text"))

            }
        }
    }
}