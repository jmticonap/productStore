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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private Environment environment;

    @GetMapping("/hostdata")
    public Map<String,String> hostData(HttpServletRequest request){
        Map<String, String> result = new HashMap<>();
        try {
            result.put("HostAddress", InetAddress.getLocalHost().getHostAddress());
            result.put("HostName", InetAddress.getLocalHost().getHostName());
            result.put("HostCanonicalHostName", InetAddress.getLocalHost().getCanonicalHostName());
            result.put("Host", request.getHeader("host"));
            result.put("PathInfo",request.getRequestURI());
            result.put("Protocol", request.getScheme());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Product getProduct(@PathVariable String productId){
        return productService.getProduct(productId);
    }

    @GetMapping(value = "/{productId}/image", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public void serveImage(HttpServletResponse response, @PathVariable String productId ) throws IOException{
        ImageProduct image = productService.getImageContent( productId );
        InputStream in = new ByteArrayInputStream( image.getContentData().getData() );
        response.setContentType( image.getContentType() );
        IOUtils.copy(in, response.getOutputStream());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Product save( @ModelAttribute("product") ProductRequest product) throws IOException {
        return productService.save( product );
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
