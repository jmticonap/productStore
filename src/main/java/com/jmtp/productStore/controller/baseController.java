package com.jmtp.productStore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RestController
@AllArgsConstructor
public class baseController {

    private ResourceLoader resourceLoader;

    @GetMapping
    public ModelAndView rootPath(){
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }

    @GetMapping("/static-db")
    public List<Map<String, Object>> static_db(HttpServletRequest request) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/product-db.json");
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Map<String,Object>>> typeRef = new TypeReference<List<Map<String,Object>>>() {};

        List<Map<String,Object>> result = (List<Map<String, Object>>) mapper
                .readValue( resource.getInputStream(), typeRef);
        result.stream().forEach(product -> {
            product.put("image_path", String.format("%s://%s%s",
                    request.getScheme(),
                    request.getHeader("host"),
                    product.get("image_path")));
        });
        return result;
    }

}
