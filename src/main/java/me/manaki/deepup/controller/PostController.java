package me.manaki.deepup.controller;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.request.CommentCreateRequest;
import me.manaki.deepup.dto.request.PostChangeRequest;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.CommentService;
import me.manaki.deepup.service.PostService;
import me.manaki.deepup.service.TopicService;
import me.manaki.deepup.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final TopicService topicService;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    private final Environment environment;

    @GetMapping("/create-post")
    public String toPostCreatePage(Model model,
                                   @AuthenticationPrincipal CustomUserDetailsImpl user) {

        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("postChangeRequest", new PostChangeRequest());

        return "create-post";
    }

    @PostMapping("/create-post")
    public String createPost(Model model,
                             @AuthenticationPrincipal CustomUserDetailsImpl user,
                             @Valid @ModelAttribute PostChangeRequest postChangeRequest,
                             BindingResult bindingResult) {

        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());

        if (bindingResult.hasErrors()) {
            return "create-post";
        }

        var id = postService.changePost(user, postChangeRequest);

        if (id == -1) {
            return "create-post";
        }

        return "redirect:/post/" + id + "/show?create=true";
    }

    @GetMapping("/post/{postId}/show")
    public String toPost(Model model,
                         HttpSession session,
                         @PathVariable Integer postId,
                         @AuthenticationPrincipal CustomUserDetailsImpl user,
                         @RequestParam(value="create", required = false) Boolean create) {

        var postDetail = postService.getPostDetail(postId, user);
        if (postDetail.getHidden() && (user == null || !user.getUsername().equals(postDetail.getUsername()))) {
            return "redirect:/";
        }

        // Check views
        postService.viewCheck(session, postDetail);

        boolean upVote = false;
        boolean downVote = false;

        // Vote check
        if (user != null) {
            var ouv = userService.getVote(user.getUsername(), postId);
            if (ouv.isPresent()) {
                var uv = ouv.get();
                if (uv.getIsUpvote()) upVote = true;
                else downVote = true;
            }
        }
        model.addAttribute("upVote", upVote);
        model.addAttribute("downVote", downVote);
        model.addAttribute("user", user);
        model.addAttribute("username", user == null ? null : user.getUsername());
        model.addAttribute("topicImage", environment.getProperty("mainpage.image"));
        model.addAttribute("topicQuote", environment.getProperty("mainpage.quote"));
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("post", postDetail);
        model.addAttribute("logged", user != null);

        if (create != null) model.addAttribute("create", true);

        var comments = commentService.getGreatComments(postId, user);
        model.addAttribute("comments", comments);

        return "post";
    }

    @GetMapping("/post/{postId}/edit")
    public String editPost(Model model,
                           @PathVariable Integer postId,
                           @AuthenticationPrincipal CustomUserDetailsImpl user) {
        var postDetail = postService.getPostDetail(postId, user);
        if (user == null || !postDetail.getUsername().equals(user.getUsername())) return "redirect:/post/" + postId + "/show";

        var post = postService.getPost(postId).orElseThrow(() -> new NullPointerException("Can't find post id " + postId));
        var request = PostChangeRequest.builder()
                .id(postId)
                .topicId(post.getTopicId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .build();

        model.addAttribute("user", user);
        model.addAttribute("topics", topicService.getAllTopic());
        model.addAttribute("postChangeRequest", request);
        model.addAttribute("edit", true);

        return "create-post";
    }

    @PostMapping("/post/{postId}/comment-upload")
    public String uploadComment(@Valid @ModelAttribute CommentCreateRequest commentCreateRequest,
                                @PathVariable Integer postId,
                                @AuthenticationPrincipal CustomUserDetailsImpl user) {

        if (!commentService.createComment(commentCreateRequest, user.getUsername(), postId)) return "post";

        return "redirect:/post/" + postId + "/show";
    }

    @ResponseBody
    @PostMapping("/post/{postId}/mark")
    public boolean markPost(@PathVariable Integer postId,
                           @AuthenticationPrincipal CustomUserDetailsImpl user) {

        if (user != null) {
            postService.setMark(postId, user.getUsername());
        }

        return true;
    }

    @GetMapping("/post/{postId}/hide")
    public String hidePost(@PathVariable Integer postId,
                           @AuthenticationPrincipal CustomUserDetailsImpl user) {

        var postDetail = postService.getPostDetail(postId, user);
        if (user == null || !postDetail.getUsername().equals(user.getUsername())) return "redirect:/post/" + postId + "/show";

        postService.hide(postId);

        return "redirect:/post/" + postId + "/show?create=true";
    }

    @GetMapping("/post/{postId}/showHidden")
    public String showHiddenPost(@PathVariable Integer postId,
                                 @AuthenticationPrincipal CustomUserDetailsImpl user) {

        var postDetail = postService.getPostDetail(postId, user);
        if (user == null || !postDetail.getUsername().equals(user.getUsername())) return "redirect:/post/" + postId + "/show";

        postService.showHidden(postId);

        return "redirect:/post/" + postId + "/show?create=true";
    }

}
