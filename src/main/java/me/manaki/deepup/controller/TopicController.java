package me.manaki.deepup.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.PostService;
import me.manaki.deepup.service.TopicService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;
    private final PostService postService;
    private final Environment environment;

    @GetMapping("/{topicUrl}")
    public String toTopic(Model model,
                          @PathVariable String topicUrl,
                          @AuthenticationPrincipal CustomUserDetailsImpl user) throws NotFoundException {
        var otopic = topicService.getByUrl(topicUrl);
        if (otopic.isEmpty()) throw new NotFoundException("Can't found topic with url " + topicUrl);

        var topic = otopic.get();

        model.addAttribute("currentPage", 1);
        model.addAttribute("prePage", 1);
        model.addAttribute("nextPage", 2);
        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("topic", topic);
        model.addAttribute("topicImage", environment.getProperty("storage.topic-images") + "/" + topic.getImage());
        model.addAttribute("topicQuote", topic.getQuote());

        // posts
        var top3 = postService.getTop3Posts(topic.getId());
        var posts = postService.getPagedPosts(topic.getId(), 1);

        model.addAttribute("top1", top3.get(0));
        model.addAttribute("top2", top3.get(1));
        model.addAttribute("top3", top3.get(2));
        model.addAttribute("posts", posts);

        return "topic";
    }

    @GetMapping("/{topicUrl}/trang-{page}")
    public String toPagedTopic(Model model,
                               @PathVariable String topicUrl,
                               @PathVariable int page,
                               @AuthenticationPrincipal CustomUserDetailsImpl user) throws NotFoundException {
        if (page == 1) return "redirect:/topic/" + topicUrl;

        var otopic = topicService.getByUrl(topicUrl);
        if (otopic.isEmpty()) throw new NotFoundException("Can't found topic with url " + topicUrl);

        var topic = otopic.get();

        var posts = postService.getPagedPosts(topic.getId(), page);
        if (posts.size() == 0) {
            return "redirect:/topic/" + topicUrl +  "/trang-" + (page - 1);
        }

        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("topic", topic);
        model.addAttribute("topicImage", environment.getProperty("storage.topic-images") + "/" + topic.getImage());
        model.addAttribute("topicQuote", topic.getQuote());

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("prePage", page - 1);
        model.addAttribute("nextPage", page + 1);

        return "posts-paged";
    }


}
