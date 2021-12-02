package me.manaki.deepup.controller;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.ProfileService;
import me.manaki.deepup.service.TopicService;
import me.manaki.deepup.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class OtherProfileController {

    private final ProfileService profileService;
    private final TopicService topicService;
    private final UserService userService;

    @GetMapping("/{nick}")
    public String redirectProfile(@PathVariable String nick) {
        return "redirect:" + nick + "/uploaded/trang-1";
    }

    @GetMapping("/{nick}/uploaded/trang-{page}")
    public String toUserProfile(Model model,
                                @PathVariable String nick,
                                @PathVariable int page,
                                @AuthenticationPrincipal CustomUserDetailsImpl user) {

        var targetUser = userService.getUser(nick).orElseThrow(() -> new NullPointerException("Can't find user nick " + nick));
        if (isTheSameUser(targetUser, user)) return "redirect:/profile";

        var isFollowed = user != null && userService.isFollowedBy(user.getUsername(), targetUser.getUsername());

        model.addAttribute("isFollowed", isFollowed);
        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(targetUser.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        var posts = profileService.getUploadedPosts(targetUser.getUsername(), page);
        if (posts.size() == 0 && page != 1) {
            return "redirect:../uploaded/trang-1";
        }

        model.addAttribute("posts", posts);

        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", Math.min(1, page));
        model.addAttribute("nextPage", page + 1);

        return "user-uploaded";
    }

    @GetMapping("/{nick}/marked/trang-{page}")
    public String toUserMarked(Model model,
                                @PathVariable String nick,
                                @PathVariable int page,
                                @AuthenticationPrincipal CustomUserDetailsImpl user) {

        var targetUser = userService.getUser(nick).orElseThrow(() -> new NullPointerException("Can't find user nick " + nick));

        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfile(targetUser.getUsername()));
        model.addAttribute("topics", topicService.getAllTopic());

        var posts = profileService.getMarkedPosts(targetUser.getUsername(), page);
        if (posts.size() == 0 && page != 1) {
            return "redirect:../marked/trang-1";
        }

        model.addAttribute("posts", posts);

        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", Math.min(1, page));
        model.addAttribute("nextPage", page + 1);

        return "user-marked";
    }



    @GetMapping("/{nick}/follow-check")
    public String followCheck(@PathVariable String nick,
                               @AuthenticationPrincipal CustomUserDetailsImpl user) {

        if (user == null)  return "redirect:" + nick;

        var targetUser = userService.getUser(nick).orElseThrow(() -> new NullPointerException("Can't find user nick " + nick));

        userService.doFollow(user.getUsername(), targetUser.getUsername());

        return "redirect:/user/" + nick;
    }

    private boolean isTheSameUser(User targetUser, CustomUserDetailsImpl userDetails) {
        return userDetails != null && userDetails.getUsername().equals(targetUser.getUsername());
    }

}
