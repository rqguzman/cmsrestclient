package com.guzmanr.cmsrestclient.controlers;

import com.guzmanr.cmsrestclient.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class PagesController {

    @Autowired
    private RestTemplate rest;

    @GetMapping
    public String home(Model model){

        Page page = rest.getForObject("http://localhost:8080/pages/home", Page.class);
        model.addAttribute("page", page);
        return "page";
    }
}
