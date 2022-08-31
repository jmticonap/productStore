package com.jmtp.productStore;

import com.jmtp.productStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {ProductRepository.class})
public class ProductStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductStoreApplication.class, args);
	}

}
