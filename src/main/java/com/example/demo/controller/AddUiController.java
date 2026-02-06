package com.example.demo.controller;

import com.example.demo.service.AddService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddUiController {

    private final AddService addService;

    public AddUiController(AddService addService) {
        this.addService = addService;
    }

    @GetMapping("/add-ui")
    public String showForm() {
        return "add"; // Thymeleaf template name
    }

    @PostMapping("/add-ui")
    public String submitForm(@RequestParam Integer a, @RequestParam Integer b, Model model) {
        try {
            int result = addService.add(a, b);
            model.addAttribute("result", String.valueOf(result));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("a", a);
        model.addAttribute("b", b);
        return "add";
    }
}