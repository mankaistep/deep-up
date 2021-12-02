package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.dto.request.PasswordChangeRequest;
import me.manaki.deepup.dto.request.ProfileEditRequest;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.entity.UserFollow;
import me.manaki.deepup.entity.UserVote;
import me.manaki.deepup.entity.id.UserFollowId;
import me.manaki.deepup.entity.id.UserVoteId;
import me.manaki.deepup.repository.UserFollowRepository;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.repository.UserVoteRepository;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import me.manaki.deepup.service.ProfileService;
import me.manaki.deepup.service.StorageService;
import me.manaki.deepup.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StorageService storageService;
    private final UserRepository userRepository;
    private final UserVoteRepository userVoteRepository;
    private final UserFollowRepository userFollowRepository;
    private final ProfileService profileService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUser(String nick) {
        return Optional.of(userRepository.findByNick(nick).orElseGet(() -> userRepository.findById(nick).get()));
    }

    @Override
    public Boolean isFollowedBy(String follower, String followed) {
        return userFollowRepository.isFollowedBy(follower, followed).isPresent();
    }

    @Override
    public void doFollow(String follower, String followed) {
        if (follower.equals(followed)) return;

        var id = new UserFollowId(follower, followed);
        if (isFollowedBy(follower, followed)) {
            var uf = userFollowRepository.findById(id).orElseThrow(() -> new NullPointerException("What the fuck?"));
            userFollowRepository.delete(uf);
        }
        else {
            var uf = new UserFollow(id);
            userFollowRepository.save(uf);
        }
    }

    @Override
    public boolean updateUser(Model model, CustomUserDetailsImpl user, ProfileEditRequest request, HttpServletRequest httpServletRequest) {
        var username = user.getUsername();
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail()) && userRepository.checkEmailExists(request.getEmail()).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return false;
        }
        if (StringUtils.hasText(request.getNick()) && !request.getNick().equals(user.getNick()) && userRepository.checkNickExists(request.getNick()).isPresent()) {
            model.addAttribute("error", "Định danh đã tồn tại!");
            return false;
        }

        var profile = profileService.getProfile(username);

        String avatar;
        if (!request.getAvatar().isEmpty()) {
            avatar = storageService.saveAvatar(username, request.getAvatar(), httpServletRequest);
        } else avatar = user.getAvatar();

        String wallpaper;
        if (!request.getWallpaper().isEmpty()) {
            wallpaper = storageService.saveWallpaper(username, request.getWallpaper());
        } else wallpaper = profile.getWallpaper();

        userRepository.updateProfile(username,
                request.getFullName(),
                request.getEmail(),
                avatar,
                request.getNick(),
                wallpaper,
                request.getFacebook(),
                request.getGithub());

        // Update user
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setAvatar(avatar);
        user.setNick(request.getNick());

        return true;
    }

    @Override
    public boolean changePassword(Model model, CustomUserDetailsImpl userDetails, PasswordChangeRequest request) {
        var oldPw = request.getOldPassword();
        var ouser = userRepository.findById(userDetails.getUsername());
        if (ouser.isEmpty()) {
            model.addAttribute("error", "Đéo biết lỗi gì");
            return false;
        }

        // Check password
        var user = ouser.get();
        if (!passwordEncoder.matches(oldPw, user.getHashPassword())) {
            model.addAttribute("error", "? Mật khẩu mình cũng nhập sai thì chịu rồi bố trẻ");
            return false;
        }

        // Encode and save
        var newpw = request.getPassword();
        var hashPw = passwordEncoder.encode(newpw);

        userRepository.changePassword(user.getUsername(), hashPw);

        return true;
    }

    @Override
    public Optional<UserVote> getVote(String username, Integer postId) {
        var id = new UserVoteId(username, postId);
        return userVoteRepository.findById(id);
    }

}
