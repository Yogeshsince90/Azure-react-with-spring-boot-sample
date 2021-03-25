package com.azuresample.springbootwithspa.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {
    @CrossOrigin
    @PreAuthorize("hasAuthority('UI_READ')")
    @RequestMapping(value="/api/showData", method= RequestMethod.GET)
    public String showData(){


        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @CrossOrigin
    @PreAuthorize("hasAuthority('UI_WRITE')")
    @RequestMapping(value="/api/writeData", method= RequestMethod.GET)
    public String writeData(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @CrossOrigin
    @PreAuthorize("hasAnyAuthority('UI_READ,UI_WRITE')")
    @RequestMapping(value="/api/viewCommonData", method= RequestMethod.GET)
    public String viewCommonData(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }


}
