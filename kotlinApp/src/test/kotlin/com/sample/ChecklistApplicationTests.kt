package com.sample

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class ChecklistApplicationTests {

	@Test
	fun contextLoads() {
	}

}
