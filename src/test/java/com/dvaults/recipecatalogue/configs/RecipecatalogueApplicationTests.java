package com.dvaults.recipecatalogue.configs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RecipecatalogueApplicationTests {

	@Test
	void contextLoads() {
	}

}
