package me.manaki.deepup.service;

import me.manaki.deepup.dto.detail.CommentDetail;
import me.manaki.deepup.dto.request.CommentCreateRequest;
import me.manaki.deepup.entity.Comment;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> getComment(Integer id);

    List<CommentDetail> getGreatComments(Integer postId, CustomUserDetailsImpl user);

    boolean createComment(CommentCreateRequest request, String username, Integer postId);

    boolean setVote(Integer commentId, String username, Boolean isUpvote);

}
