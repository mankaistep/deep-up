package me.manaki.deepup.service.impl;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.detail.CommentDetail;
import me.manaki.deepup.dto.request.CommentCreateRequest;
import me.manaki.deepup.entity.*;
import me.manaki.deepup.entity.id.UserCommentVoteId;
import me.manaki.deepup.repository.CommentRepository;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.repository.UserCommentVoteRepository;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.CommentService;
import me.manaki.deepup.utils.Utils;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserCommentVoteRepository userCommentVoteRepository;

    @Override
    public Optional<Comment> getComment(Integer id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<CommentDetail> getGreatComments(Integer postId, CustomUserDetailsImpl user) {
        var greatComments = commentRepository.getGreatComments(postId);

        return greatComments.stream().map(cmt -> getCommentDetail(cmt.getId(), user)).collect(Collectors.toList());
    }

    @Override
    public boolean createComment(CommentCreateRequest request, String username, Integer postId) {
        if (StringUtils.isBlank(request.getContent())) return false;
        Integer parentId = request.getParentId() == -1 ? null : request.getParentId();
        var cmt = new Comment(request.getContent(), Instant.now(), username, parentId, postId);

        commentRepository.save(cmt);

        return true;
    }

    @Override
    public boolean setVote(Integer commentId, String username, Boolean isUpvote) {
        var id = new UserCommentVoteId(username, commentId);

        var oucv = userCommentVoteRepository.findById(id);
        UserCommentVote ucv;
        if (oucv.isPresent()) {
            ucv = oucv.get();
            if (ucv.getIsUpvote() == isUpvote)  {
                userCommentVoteRepository.delete(ucv);
                return true;
            }
            else ucv.setIsUpvote(isUpvote);
        }
        else ucv = new UserCommentVote(id, isUpvote);

        userCommentVoteRepository.save(ucv);

        return true;
    }

    private CommentDetail getCommentDetail(Integer commentId, CustomUserDetailsImpl currentUser) {
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("Can't find comment id " + commentId));
        var user = userRepository.findById(comment.getUsername()).orElseThrow(() -> new NullPointerException("Cant find user id " + comment.getUsername()));

        var date = Utils.format(comment.getCreationDate());

        List<CommentDetail> childDetails = new ArrayList<>();
        var childComments = commentRepository.findByParentId(commentId);
        if (childComments.size() > 0) {
            childDetails = childComments.stream().map(cmt -> getCommentDetail(cmt.getId(), currentUser)).collect(Collectors.toList());
        }

        var vote = userCommentVoteRepository.countUpvotes(commentId) - userCommentVoteRepository.countDownvotes(commentId);
        var isUpvoted = false;
        var isDownvoted = false;

        if (currentUser != null) {
            isUpvoted = userCommentVoteRepository.isUpvoted(currentUser.getUsername(), commentId).isPresent();
            isDownvoted = userCommentVoteRepository.isDownvoted(currentUser.getUsername(), commentId).isPresent();
        }

        return CommentDetail
                .builder()
                .id(commentId)
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .creationDate(date)
                .content(comment.getContent())
                .votes(vote)
                .childs(childDetails)
                .downVoted(isDownvoted)
                .upVoted(isUpvoted)
                .build();
    }

}
