package com.guzmanr.cmsrestclient;

import com.guzmanr.cmsrestclient.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private RestTemplate rest;

    @ModelAttribute
    public void sharedData(Model model) {

        ResponseEntity<List<Page>> responseEntity = rest.exchange("http://localhost:8080/pages/all", HttpMethod.GET,
                                                null, new ParameterizedTypeReference<List<Page>>() {});
        List<Page> pages = responseEntity.getBody();

        model.addAttribute("cpages", pages);
    }
}
