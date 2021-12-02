package me.manaki.deepup.service;

import me.manaki.deepup.dto.detail.PostDetail;
import me.manaki.deepup.dto.request.PostChangeRequest;
import me.manaki.deepup.entity.Post;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> getPost(Integer id);

    Integer changePost(CustomUserDetailsImpl user, PostChangeRequest request);

    PostDetail getPostDetail(Integer postId, CustomUserDetailsImpl viewer);

    boolean setVote(Integer postId, String username, Boolean isUpvote);

    boolean viewCheck(HttpSession session, PostDetail postDetail);

    List<PostDetail> getTop10Posts();

    List<PostDetail> getTop3Posts(Integer topicId);

    List<PostDetail> getPagedPosts(Integer topicId, Integer page);

    boolean setMark(Integer postId, String username);

    boolean hide(Integer postId);

    boolean showHidden(Integer postId);

}
