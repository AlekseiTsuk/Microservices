package web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public String getMyData(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roles", userDetails.getAuthorities());
        return "profile";
    }
}
