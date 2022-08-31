package com.jmtp.productStore.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private String name;
    private Double price;
    private MultipartFile image;
    private Double stock;
    private String category;

}
