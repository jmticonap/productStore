package com.jmtp.productStore.service;

import com.jmtp.productStore.model.Product;
import com.jmtp.productStore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Page<Product> getAllByPage(int page, int quantity){
        Pageable pageRqt = PageRequest.of(page, quantity);
        return productRepository.findAll(pageRqt);
    }

    public Product save(Product product){
        product = productRepository.saveAndFlush(product);
        return product;
    }

    public Boolean delete(Long id){
        try{
            productRepository.deleteById(id);
        }catch (Exception err){
            return false;
        }

        return true;
    }

    public Boolean purchase(Map<Long, Double> cart){
        List<Product> products =  productRepository.findAllById(cart.keySet());
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
