package com.guzmanr.cmsrestclient.controlers;

import com.guzmanr.cmsrestclient.models.PageRepository;
import com.guzmanr.cmsrestclient.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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

    @GetMapping("/add")
    public String add( Page thePage) {

        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult theResult, RedirectAttributes theAttributes, Model theModel) {

        if (theResult.hasErrors()) {
            return "admin/pages/add";
        }

        theAttributes.addFlashAttribute("message", "Page added");
        theAttributes.addFlashAttribute("alertClass", "alert-success");

        String theSlug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = repository.findBySlug(theSlug);

        if (slugExists != null) {
            theAttributes.addFlashAttribute("message", "Slug exists, please choose another one.");
            theAttributes.addFlashAttribute("alertClass", "alert-danger");
            theAttributes.addFlashAttribute("page", page);

        } else {
            page.setSlug(theSlug);
            page.setSorting(100);

            rest.postForObject("http://localhost:8080/admin/pages/add", page, Page.class);
        }

        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model theModel) {

        Page thePage = rest.getForObject("http://localhost:8080/admin/pages/edit/{id}", Page.class, id);

        theModel.addAttribute("page", thePage);

        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult theResult, RedirectAttributes theAttributes, Model theModel) {

        Page pageCurrent = repository.getOne(page.getId());

        if (theResult.hasErrors()) {
            theModel.addAttribute("pageTitle", pageCurrent.getTitle());
            return "admin/pages/edit";
        }

        theAttributes.addFlashAttribute("message", "Page edited");
        theAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");

		Page slugExists = repository.findBySlugAndIdNot(slug, page.getId());

        if (slugExists != null) {
            theAttributes.addFlashAttribute("message", "Slug exists, please choose another one");
            theAttributes.addFlashAttribute("alertClass", "alert-danger");
            theAttributes.addFlashAttribute("page", page);

        } else {
            page.setSlug(slug);

            rest.put("http://localhost:8080/admin/pages/edit", page);
        }

        return "redirect:/admin/pages/edit/" + page.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes theAttributes) {

        rest.delete("http://localhost:8080/admin/pages/delete/{id}", id);

        theAttributes.addFlashAttribute("message", "Page deleted");
        theAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }
}
