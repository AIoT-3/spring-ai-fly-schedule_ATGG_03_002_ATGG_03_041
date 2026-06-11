package com.nhnacademy.flyschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search-flight")
    public String searchFlight() {
        return "search-flight";
    }

    @GetMapping("/search-llm")
    public String searchLlm() {
        return "search-llm";
    }


}
