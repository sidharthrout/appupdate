package com.raisex.workreport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loadForm(Model model) {
        model.addAttribute("user", new com.raisex.workreport.model.User());
        return "login";
    }
}
