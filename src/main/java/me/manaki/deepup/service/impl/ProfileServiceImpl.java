package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.detail.PostDetail;
import me.manaki.deepup.dto.detail.ProfileDetail;
import me.manaki.deepup.dto.request.ProfileEditRequest;
import me.manaki.deepup.repository.UserFollowRepository;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.PostService;
import me.manaki.deepup.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;

    private final PostService postService;

    @Override
    public ProfileDetail getProfile(String username) {
        var user = userRepository.findById(username).orElseThrow(() -> new NullPointerException("No user id " + username));

        var followers = userFollowRepository.countFollowers(username);
        var followings = userFollowRepository.countFollowings(username);

        return ProfileDetail.builder()
                .username(user.getUsername())
                .nick(user.getNick() == null ? user.getUsername() : user.getNick())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar() == null ? "default.png" : user.getAvatar())
                .wallpaper(user.getWallpaper() == null ? "default.jpg" : user.getWallpaper())
                .facebook(user.getFacebook())
                .github(user.getGithub())
                .votes(userRepository.countVotes(username))
                .views(userRepository.countViews(username))
                .posts(userRepository.countPosts(username))
                .followers(followers)
                .followings(followings)
                .build();
    }

    @Override
    public boolean editProfile(CustomUserDetailsImpl user, ProfileEditRequest request) {
        return false;
    }

    @Override
    public int countPosts(String username) {
        return userRepository.countPosts(username);
    }

    @Override
    public int countVotes(String username) {
        return userRepository.countVotes(username);
    }

    @Override
    public List<PostDetail> getMarkedPosts(String username, Integer page) {
        return userRepository.getMarkedPosts(username, page).stream().map(id -> postService.getPostDetail(id, null)).collect(Collectors.toList());
    }

    @Override
    public List<PostDetail> getUploadedPosts(String username, Integer page) {
        return userRepository.getUploadedPosts(username, page).stream().map(id -> postService.getPostDetail(id, null)).collect(Collectors.toList());
    }

    @Override
    public List<ProfileDetail> getFollowings(String username, Integer page) {
        return userRepository.getFollowings(username, page).stream().map(this::getProfile).collect(Collectors.toList());
    }

}
