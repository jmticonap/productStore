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
    public List<ProductResponse<Map<String, Object>>> getAll(HttpServletRequest request){
        List<ProductResponse<Map<String, Object>>> result = new ArrayList<>();
        List<Product> list_products = productService.getAll();
        list_products.stream()
                .forEach(prod -> {
                    result.add( makeProductResponsable(prod, request) );
                });
        return result;
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProductResponse<Map<String, Object>> getProduct(@PathVariable String productId, HttpServletRequest request){
        //Por uso de Mongo Atlas se debe crear el URI de la imagen a la salida
        Product product = productService.getProduct(productId);

        return makeProductResponsable(product, request);
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

    /***
     *
     * @param product
     * @param request
     * @return :the full URI to get the image resorce
     */
    private String makeImagePath( Product product, HttpServletRequest request ){
        //https://jmtpproductstore.herokuapp.com/api/v1/product/630ecd17ffe5ad336fae0d9c/image
        return String.format(
                "%s://%s/api/v1/product/%s/image",
                request.getScheme(),
                request.getHeader("host"),
                product.getId() );
    }

    /***
     *
     * @param product
     * @param request
     * @return
     */
    private ProductResponse<Map<String,Object>> makeProductResponsable(Product product, HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        result.put("id", product.getId());
        result.put("name", product.getName());
        result.put("price", product.getPrice());
        result.put("imagePath", makeImagePath(product, request));
        result.put("stock", product.getStock());
        result.put("category", product.getCategory());

        return new ProductResponse<Map<String,Object>>( result );
    }

}
