package com.azuresample.springbootwithspa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UIController {

    @RequestMapping(value="/")
    public String loadUI(){
        return  "forward:/index.html";
    }
}
