package com.jmtp.productStore.repository;

import com.jmtp.productStore.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
