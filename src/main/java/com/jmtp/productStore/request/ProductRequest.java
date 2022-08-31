package com.jmtp.productStore.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private String id=null;
    private String name=null;
    private Double price=null;
    private MultipartFile image=null;
    private Double stock=null;
    private String category=null;

}
