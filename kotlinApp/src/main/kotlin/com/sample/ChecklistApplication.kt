package com.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChecklistApplication

fun main(args: Array<String>) {
	runApplication<ChecklistApplication>(*args)
}
