package me.manaki.deepup.controller;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.request.RegisterRequest;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.AuthenticationService;
import me.manaki.deepup.service.PostService;
import me.manaki.deepup.service.TopicService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BaseController {

    private final Environment environment;

    private final PostService postService;
    private final TopicService topicService;
    private final AuthenticationService authenticationService;

    @GetMapping("/")
    public String toHomePage(Model model, @AuthenticationPrincipal CustomUserDetailsImpl user) {
        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("topicImage", environment.getProperty("mainpage.image"));
        model.addAttribute("topicQuote", environment.getProperty("mainpage.quote"));

        var posts = postService.getTop10Posts();
        model.addAttribute("top1", posts.get(0));
        model.addAttribute("top2", posts.get(1));
        model.addAttribute("top3", posts.get(2));
        model.addAttribute("posts", posts.subList(3, 9));

        return "index";
    }

    @GetMapping("/login")
    public String toLoginPage(@AuthenticationPrincipal CustomUserDetailsImpl user) {
        return "login";
    }

    @GetMapping("/register")
    public String toRegisterPage(Principal user, Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(Model model,
                             @Valid @ModelAttribute RegisterRequest request,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!authenticationService.registerUser(model, request)) {
            return "register";
        }
        return "redirect:/login";
    }

}
