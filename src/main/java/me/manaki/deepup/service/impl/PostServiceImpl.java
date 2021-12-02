package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.detail.PostDetail;
import me.manaki.deepup.dto.request.PostChangeRequest;
import me.manaki.deepup.entity.Post;
import me.manaki.deepup.entity.UserPostMark;
import me.manaki.deepup.entity.UserVote;
import me.manaki.deepup.entity.id.UserPostMarkId;
import me.manaki.deepup.entity.id.UserVoteId;
import me.manaki.deepup.repository.*;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.PostService;
import me.manaki.deepup.service.StorageService;
import me.manaki.deepup.utils.Utils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final long VIEW_COOLDOWN = 10000;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserVoteRepository userVoteRepository;
    private final UserPostMarkRepository userPostMarkRepository;

    private final StorageService storageService;

    private final Environment environment;

    private final Map<String, Long> lastViews = new HashMap<>();

    @Override
    public Optional<Post> getPost(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public Integer changePost(CustomUserDetailsImpl user, PostChangeRequest request) {
        var id = request.getId();
        Post post;
        // Edit
        if (id != null) {
            post = postRepository.findById(id).orElseThrow(() -> new NullPointerException("Can't find post id " + id));

            post.setTitle(Utils.notNullOr(request.getTitle(), post.getTitle()));
            post.setSubtitle(Utils.notNullOr(request.getSubtitle(), post.getSubtitle()));
            post.setContent(Utils.notNullOr(request.getContent(), post.getContent()));
            post.setEditDate(Instant.now());
            post.setHidden(false);
        }
        else {
            // Create
            post = new Post(
                    request.getTitle(),
                    request.getSubtitle(),
                    request.getContent(),
                    environment.getProperty("post.image.default"),
                    0,
                    request.getTopicId(),
                    user.getUsername(),
                    Instant.now(),
                    null,
                    false
            );
        }

        var mf = request.getImage();
        if (!mf.isEmpty()) {
            var img = storageService.savePostImage(post.getId() + "", mf);
            post.setImage(img);
        }

        postRepository.save(post);

        return post.getId();
    }

    @Override
    public PostDetail getPostDetail(Integer postId, CustomUserDetailsImpl viewer) {
        var post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Can't find post id " + postId));
        var username = post.getUsername();
        var user = userRepository.findById(username).orElseThrow(() -> new NullPointerException("Can't find user id " + username));
        var topic = topicRepository.findById(post.getTopicId()).orElseThrow(() -> new NullPointerException("Can't find topic id " + post.getTopicId()));

        var vote = userVoteRepository.countUpvotes(post.getId()) - userVoteRepository.countDownvotes(post.getId());

        var mark = false;
        if (viewer != null) {
            mark = userPostMarkRepository.findById(new UserPostMarkId(viewer.getUsername(), postId)).isPresent();
        }

        var editDate = Utils.format(post.getEditDate());

        return  PostDetail
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .subtitle(post.getSubtitle())
                .content(post.getContent())
                .image(post.getImage())
                .views(post.getViews())
                .votes(vote)
                .creationDate(Utils.format(post.getCreationDate()))
                .username(username)
                .userFullname(user.getFullName())
                .avatar(user.getAvatar())
                .topic(topic.getName())
                .marked(mark)
                .nick(user.getNick() == null ? username : user.getNick())
                .editDate(editDate)
                .hidden(post.getHidden())
                .build();
    }

    @Override
    public boolean setVote(Integer postId, String username, Boolean isUpvote) {
        var id = new UserVoteId(username, postId);
        var ouv = userVoteRepository.findById(id);
        UserVote uv;
        if (ouv.isPresent()) {
            uv = ouv.get();
            if (uv.getIsUpvote() == isUpvote)  {
                userVoteRepository.delete(uv);
                return true;
            }
            else uv.setIsUpvote(isUpvote);
        }
        else uv = new UserVote(id, isUpvote);

        userVoteRepository.save(uv);

        return true;
    }

    @Override
    public boolean viewCheck(HttpSession session, PostDetail postDetail) {
        var id = session.getId();
        if (lastViews.containsKey(id) && lastViews.get(id) > System.currentTimeMillis()) return false;
        lastViews.put(id, System.currentTimeMillis() + VIEW_COOLDOWN);

        postDetail.setViews(postDetail.getViews() + 1);

        var postId = postDetail.getId();
        var newViews = postDetail.getViews();

        postRepository.updateViews(postId, newViews);

        return true;
    }

    @Override
    public List<PostDetail> getTop10Posts() {
        var top10 = postRepository.getTop10Posts();
        while (top10.size() < 10) {
            top10.add(0);
        }
        return top10.stream().map(id -> getPostDetail(id, null)).collect(Collectors.toList());
    }

    @Override
    public List<PostDetail> getTop3Posts(Integer topicId) {
        var top3 = postRepository.getTop3Posts(topicId);
        while (top3.size() < 3) {
            top3.add(0);
        }
        var a = top3.stream().map(id -> getPostDetail(id, null)).collect(Collectors.toList());
        return top3.stream().map(id -> getPostDetail(id, null)).collect(Collectors.toList());
    }

    @Override
    public List<PostDetail> getPagedPosts(Integer topicId, Integer page) {
        return postRepository.getPagedPosts(topicId, page).stream().map(id -> getPostDetail(id, null)).collect(Collectors.toList());
    }

    @Override
    public boolean setMark(Integer postId, String username) {
        var id = new UserPostMarkId(username, postId);
        var oupm = userPostMarkRepository.findById(id);
        UserPostMark upm;

        if (oupm.isEmpty()) {
            upm = new UserPostMark(id, Instant.now());
            userPostMarkRepository.save(upm);
        }
        else {
            upm = oupm.get();
            userPostMarkRepository.delete(upm);
        }

        return true;
    }

    @Override
    public boolean hide(Integer postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Can't find post id " + postId));
        post.setHidden(true);
        postRepository.save(post);

        return true;
    }

    @Override
    public boolean showHidden(Integer postId) {
        var post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Can't find post id " + postId));
        post.setHidden(false);
        postRepository.save(post);

        return true;
    }

}
