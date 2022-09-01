package com.jmtp.productStore.service;

import com.jmtp.productStore.model.ImageProduct;
import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.repository.ProductRepository;
import com.jmtp.productStore.request.ProductRequest;
import com.jmtp.productStore.responce.ProductResponse;
import lombok.AllArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getProduct(String id){
        return productRepository.findById(id).get();
    }

    public ImageProduct getImageContent(String id){
        Product product = productRepository.findById(id).get();
        return product.getImage();
    }

    public Page<Product> getAllByPage(int page, int quantity){
        Pageable pageRqt = PageRequest.of(page, quantity);
        return productRepository.findAll(pageRqt);
    }

    /***
     *
     * @param prod_request
     * @return Saved product[Product]
     * @throws IOException
     */
    public Product save(ProductRequest prod_request) throws IOException {

        Product product;
        if( prod_request.getId() == null | prod_request.getId().isEmpty() ) {
            //New Product
            product = Product.builder()
                    .name(prod_request.getName())
                    .price(prod_request.getPrice())
                    .stock(prod_request.getStock())
                    .category(prod_request.getCategory())
                    .image(new ImageProduct(
                            prod_request.getImage().getContentType(),
                            new Binary(
                                    BsonBinarySubType.BINARY,
                                    prod_request.getImage().getBytes())))
                    .build();
        }else{
            //Update prooduct
            product = productRepository.findById(prod_request.getId()).get();
            if(prod_request.getName() != null & !prod_request.getName().isEmpty()){
                product.setName(prod_request.getName());
            }
            if(prod_request.getCategory() != null & !prod_request.getCategory().isEmpty()){
                product.setCategory(prod_request.getCategory());
            }
            if(prod_request.getPrice() != null){
                product.setPrice(prod_request.getPrice());
            }
            if(prod_request.getStock() != null){
                product.setStock(prod_request.getStock());
            }
            if(prod_request.getImage() != null){
                product.setImage(new ImageProduct(
                        prod_request.getImage().getContentType(),
                        new Binary(
                                BsonBinarySubType.BINARY,
                                prod_request.getImage().getBytes())));
            }
        }
        return productRepository.save(product);
    }

    public Boolean delete(String id){
        try{
            productRepository.deleteById(id);
        }catch (Exception err){
            return false;
        }

        return true;
    }

    public Boolean purchase(Map<String, Double> cart){
        List<Product> products = (List<Product>)productRepository.findAllById( cart.keySet() );
        try{
            products
                    .stream()
                    .forEach(product -> {
                        Double new_stock = product.getStock() - cart.get(product.getId());
                        if( new_stock < 0 ){
                            throw new IllegalStateException("One or more items have a quatity grater than the current stock.");
                        }
                        product.setStock( new_stock );
                    });
            productRepository.saveAll( products );
        }catch(Exception err){
            return false;
        }

        return true;
    }






    /***
     *
     * @param product
     * @param request
     * @return :the full URI to get the image resorce
     */
    public static String makeImagePath( Product product, HttpServletRequest request ){
        //https://jmtpproductstore.herokuapp.com/api/v1/product/630ecd17ffe5ad336fae0d9c/image
        return String.format(
                "%s://%s/api/v1/product/image/%s",
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
    public static ProductResponse<Map<String,Object>> makeProductResponsable(Product product, HttpServletRequest request){
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
