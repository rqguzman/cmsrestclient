package com.guzmanr.cmsrestclient.controlers;

import com.guzmanr.cmsrestclient.models.PageRepository;
import com.guzmanr.cmsrestclient.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository repository;

    @Autowired
    private RestTemplate rest;

    @GetMapping
    public String index(Model model) {

        ResponseEntity<List<Page>> responseEntity = rest.exchange("http://localhost:8080/pages/all", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Page>>() {});
        List<Page> pages = responseEntity.getBody();

        model.addAttribute("pages", pages);

        return "admin/pages/index";
    }
}
