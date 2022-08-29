package com.jmtp.productStore.controller;

import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.responce.ProductResponse;
import com.jmtp.productStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("/saludo")
    public Map<Long, Double> saludo(){
        Map<Long, Double> result = new HashMap<>();
        result.put(1L,1.0);
        result.put(2L,10.4);
        result.put(3L,14.5);
        return result;
    }

    @GetMapping
    public List<Product> getAll(){
        return productService.getAll();
    }

    @PostMapping
    public Product save(@RequestBody Product product){
        return productService.save( product );
    }

    @DeleteMapping(path = "/{productId}")
    public ProductResponse<Boolean> delete(@PathVariable(name = "productId") Long id){
        return new ProductResponse<Boolean>(
                productService.delete(id)
        );
    }

    @PostMapping(path = "/purchase")
    public ProductResponse<Boolean> purchase(@RequestBody Map<Long, Double> cart){
        return new ProductResponse<Boolean>(
                productService.purchase(cart)
        );
    }

}
