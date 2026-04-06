package com.meraproject.jobmicro;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"eureka.client.enabled=false"})
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
