package com.azuresample.springbootwithspa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {
    @CrossOrigin
    @PreAuthorize("hasAnyRole('UI_READ,UI_WRITE')")
    @RequestMapping(value="/api/showData", method= RequestMethod.GET)
    public String showData(){
        return"success";
    }

    @CrossOrigin
    @PreAuthorize("hasRole('UI_WRITE')")
    @RequestMapping(value="/api/writeData", method= RequestMethod.GET)
    public Authentication writeData(){
        return  SecurityContextHolder.getContext().getAuthentication();
    }


}
