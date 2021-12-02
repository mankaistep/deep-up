package me.manaki.deepup.controller;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.CommentService;
import me.manaki.deepup.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/vote")
@RequiredArgsConstructor
public class VoteController {

    private final PostService postService;
    private final CommentService commentService;

    @ResponseBody
    @PostMapping("/upvote")
    public boolean upVote(@RequestParam Integer postId,
                       @RequestParam String username) {

        return postService.setVote(postId, username, true);
    }

    @ResponseBody
    @PostMapping("/downvote")
    public boolean downVote(@RequestParam Integer postId,
                       @RequestParam String username) {

        return postService.setVote(postId, username, false);
    }

    @ResponseBody
    @PostMapping("/comment/upvote")
    public boolean upVoteComment(@RequestParam Integer commentId, @AuthenticationPrincipal CustomUserDetailsImpl user) {
        return commentService.setVote(commentId, user.getUsername(), true);
    }

    @ResponseBody
    @PostMapping("/comment/downvote")
    public boolean downVoteComment(@RequestParam Integer commentId, @AuthenticationPrincipal CustomUserDetailsImpl user) {
        return commentService.setVote(commentId, user.getUsername(), false);
    }

}
