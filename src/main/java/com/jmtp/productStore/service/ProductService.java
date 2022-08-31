package com.jmtp.productStore.service;

import com.jmtp.productStore.model.ImageProduct;
import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.repository.ProductRepository;
import com.jmtp.productStore.request.ProductRequest;
import lombok.AllArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public Product save(ProductRequest prod_request) throws IOException {
        Product product = Product.builder()
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
        product = productRepository.save(product);
        return product;
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

}
