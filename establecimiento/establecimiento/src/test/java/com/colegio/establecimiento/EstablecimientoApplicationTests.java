package com.colegio.establecimiento;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=none")
class EstablecimientoApplicationTests {

	@Test
	void contextLoads() {
	}

}
