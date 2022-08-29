package com.jmtp.productStore.repository;

import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository repository){
        return args -> {
            Product prod1 = Product.builder()
                    .name("Producto de prueba 01")
                    .price(10.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-A")
                    .stock(10.0)
                    .build();
            Product prod2 = Product.builder()
                    .name("Producto de prueba 02")
                    .price(10.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-A")
                    .stock(15.0)
                    .build();
            Product prod3 = Product.builder()
                    .name("Producto de prueba 03")
                    .price(20.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-A")
                    .stock(5.0)
                    .build();

            Product prod4 = Product.builder()
                    .name("Producto de prueba 04")
                    .price(10.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-B")
                    .stock(10.0)
                    .build();
            Product prod5 = Product.builder()
                    .name("Producto de prueba 05")
                    .price(10.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-B")
                    .stock(15.0)
                    .build();
            Product prod6 = Product.builder()
                    .name("Producto de prueba 06")
                    .price(20.0)
                    .imagePath("https://picsum.photos/500")
                    .category("Categoria-B")
                    .stock(5.0)
                    .build();
            List<Product> products = new ArrayList<>();
            products.add(prod1);
            products.add(prod2);
            products.add(prod3);
            products.add(prod4);
            products.add(prod5);
            products.add(prod6);
            repository.saveAll( products );
        };
    }

}
