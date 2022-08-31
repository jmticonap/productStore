package com.jmtp.productStore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/")
@Controller
public class baseController {

    @GetMapping
    public ModelAndView rootPath(){
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }

}
