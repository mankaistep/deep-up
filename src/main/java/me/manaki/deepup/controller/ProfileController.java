package me.manaki.deepup.controller;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.request.PasswordChangeRequest;
import me.manaki.deepup.dto.request.ProfileEditRequest;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.ProfileService;
import me.manaki.deepup.service.TopicService;
import me.manaki.deepup.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final TopicService topicService;
    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping("")
    public String redirectProfile() {
        return "redirect:/profile/edit-profile/thong-tin";
    }


    @GetMapping("/edit-profile/thong-tin")
    public String toProfilePage(Model model, @AuthenticationPrincipal CustomUserDetailsImpl user) {

        var profile = profileService.getProfile(user.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("profile", profile);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("profileEditRequest",
                ProfileEditRequest.builder()
                        .fullName(profile.getFullName())
                        .email(profile.getEmail())
                        .avatar(null)
                        .wallpaper(null)
                        .nick(profile.getNick())
                        .facebook(profile.getFacebook())
                        .github(profile.getGithub())
                        .build());

        return "profile-profile";
    }

    @GetMapping("/password/doi-mat-khau")
    public String toPasswordPage(Model model, @AuthenticationPrincipal CustomUserDetailsImpl user) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("passwordChangeRequest", new PasswordChangeRequest());

        return "profile-password";
    }

    @GetMapping("/marked/trang-{page}")
    public String toMarkedPage(Model model,
                               @AuthenticationPrincipal CustomUserDetailsImpl user,
                               @PathVariable int page) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        var posts = profileService.getMarkedPosts(user.getUsername(), page);
        if (posts.size() == 0 && page != 1) {
            return "redirect:/profile/marked/trang-1";
        }

        model.addAttribute("posts", posts);

        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", Math.min(1, page));
        model.addAttribute("nextPage", page + 1);

        return "profile-marked";
    }

    @GetMapping("/uploaded/trang-{page}")
    public String toUploadedPage(Model model,
                               @AuthenticationPrincipal CustomUserDetailsImpl user,
                               @PathVariable int page) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        var posts = profileService.getUploadedPosts(user.getUsername(), page);
        if (posts.size() == 0 && page != 1) {
            return "redirect:/profile/uploaded/trang-1";
        }

        model.addAttribute("posts", posts);

        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", Math.min(1, page));
        model.addAttribute("nextPage", page + 1);

        return "profile-uploaded";
    }

    @GetMapping("/following/trang-{page}")
    public String toFollowingPage(Model model,
                                 @AuthenticationPrincipal CustomUserDetailsImpl user,
                                 @PathVariable int page) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        var followings = profileService.getFollowings(user.getUsername(), page);
        if (followings.size() == 0 && page != 1) {
            return "redirect:/profile/following/trang-1";
        }

        model.addAttribute("followings", followings);
        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", Math.min(1, page));
        model.addAttribute("nextPage", page + 1);

        return "profile-following";
    }

    @PostMapping("")
    public String editProfile(Model model,
                              @AuthenticationPrincipal CustomUserDetailsImpl user,
                              @Valid @ModelAttribute ProfileEditRequest profileEditRequest,
                              BindingResult bindingResult,
                              HttpServletRequest httpServletRequest,
                              RedirectAttributes redirectAttributes) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        if (bindingResult.hasErrors()) {
            return "profile-profile";
        }

        if (!userService.updateUser(model, user, profileEditRequest, httpServletRequest)) {
            return "profile-profile";
        }

        redirectAttributes.addAttribute("successful", true);

        return "redirect:/profile";
    }

    @PostMapping("/password")
    public String changePassword(Model model,
                                 @AuthenticationPrincipal CustomUserDetailsImpl user,
                                 @Valid @ModelAttribute PasswordChangeRequest passwordChangeRequest,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(user.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        if (bindingResult.hasErrors()) {
            model.addAttribute("successful", false);
            return "profile-password";
        }

        if (!userService.changePassword(model, user, passwordChangeRequest)) {
            model.addAttribute("successful", false);
            return "profile-password";
        }

        redirectAttributes.addAttribute("successful", true);

        return "redirect:/profile/password";
    }

}
