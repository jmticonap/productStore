package com.jmtp.productStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product")
public class Product {

    @Id
    private String id;
    private String name;
    private Double price;
    @Transient
    private String imagePath;
    private ImageProduct image;
    private Double stock;
    private String category;

}
