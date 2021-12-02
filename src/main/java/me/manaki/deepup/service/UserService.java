package me.manaki.deepup.service;

import me.manaki.deepup.dto.request.PasswordChangeRequest;
import me.manaki.deepup.dto.request.ProfileEditRequest;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.entity.UserVote;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {

    Optional<User> getUser(String nick);

    Boolean isFollowedBy(String follower, String followed);

    void doFollow(String follower, String followed);

    boolean updateUser(Model model, CustomUserDetailsImpl user, ProfileEditRequest request, HttpServletRequest httpServletRequest);
    boolean changePassword(Model model, CustomUserDetailsImpl user, PasswordChangeRequest request);

    Optional<UserVote> getVote(String username, Integer postId);

}
