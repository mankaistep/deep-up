package me.manaki.deepup.service;

import me.manaki.deepup.dto.detail.PostDetail;
import me.manaki.deepup.dto.detail.ProfileDetail;
import me.manaki.deepup.dto.request.ProfileEditRequest;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;

import java.util.List;
import java.util.Optional;

public interface ProfileService {

    ProfileDetail getProfile(String username);

    boolean editProfile(CustomUserDetailsImpl user, ProfileEditRequest request);

    int countPosts(String username);

    int countVotes(String username);

    List<PostDetail> getMarkedPosts(String username, Integer page);

    List<PostDetail> getUploadedPosts(String username, Integer page);

    List<ProfileDetail> getFollowings(String username, Integer page);

}
