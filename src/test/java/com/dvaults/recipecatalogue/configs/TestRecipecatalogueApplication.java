package com.dvaults.recipecatalogue.configs;

import com.dvaults.recipecatalogue.RecipecatalogueApplication;
import org.springframework.boot.SpringApplication;

public class TestRecipecatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.from(RecipecatalogueApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
