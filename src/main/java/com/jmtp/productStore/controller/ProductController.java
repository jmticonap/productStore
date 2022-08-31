package com.jmtp.productStore.controller;

import com.jmtp.productStore.model.ImageProduct;
import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.request.ProductRequest;
import com.jmtp.productStore.responce.ProductResponse;
import com.jmtp.productStore.service.ProductService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ProductResponse<Map<String, Object>>> getAll(HttpServletRequest request){
        List<ProductResponse<Map<String, Object>>> result = new ArrayList<>();
        List<Product> list_products = productService.getAll();
        list_products.stream()
                .forEach(prod -> {
                    result.add( productService.makeProductResponsable(prod, request) );
                });
        return result;
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProductResponse<Map<String, Object>> getProduct(@PathVariable String productId, HttpServletRequest request){
        //Por uso de Mongo Atlas se debe crear el URI de la imagen a la salida
        Product product = productService.getProduct(productId);

        return productService.makeProductResponsable(product, request);
    }

    @GetMapping(value = "/image/{productId}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public void serveImage(HttpServletResponse response, @PathVariable String productId ) throws IOException{
        ImageProduct image = productService.getImageContent( productId );
        InputStream in = new ByteArrayInputStream( image.getContentData().getData() );
        response.setContentType( image.getContentType() );
        IOUtils.copy(in, response.getOutputStream());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Product save(
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) Double price,
            @RequestParam(name = "image", required = false) MultipartFile image,
            @RequestParam(name = "stock", required = false) Double stock,
            @RequestParam(name = "category", required = false) String category ) throws IOException {
        return productService.save( ProductRequest.builder()
                        .id(id)
                        .name(name)
                        .price(price)
                        .image(image)
                        .stock(stock)
                        .category(category)
                .build() );
    }

    @DeleteMapping(path = "/{productId}")
    public ProductResponse<Boolean> delete(@PathVariable(name = "productId") String id){
        return new ProductResponse<Boolean>(
                productService.delete(id)
        );
    }

    @PostMapping(path = "/purchase")
    public ProductResponse<Boolean> purchase(@RequestBody Map<String, Double> cart){
        return new ProductResponse<Boolean>(
                productService.purchase(cart)
        );
    }

}
