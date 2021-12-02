package me.manaki.deepup.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.manaki.deepup.constant.AccountType;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.repository.RoleRepository;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.security.oauth.factory.OAuth2UserInfoFactory;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j // logger (log)
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final Environment environment;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var oauth2user = super.loadUser(userRequest);
        log.info(oauth2user.toString());
        
        return processAuthenticateUser(userRequest, oauth2user);
    }

    private OAuth2User processAuthenticateUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        var registerId = userRequest.getClientRegistration().getRegistrationId();
        var oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registerId, oAuth2User.getAttributes());

        assert oauth2UserInfo != null;
        if (!StringUtils.hasText(oauth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationException("No email mother fucker");
        }

        var oUser = userRepository.findByUsername(oauth2UserInfo.getId());
        User user = oUser.orElseGet(() -> registerUser(registerId, oauth2UserInfo));

        var role = roleRepository.findById(user.getRoleId());
        if (role.isEmpty()) throw new NullPointerException("No role found!");

        var defaultAvt = environment.getProperty("avatar.default");

        return CustomUserDetailsImpl.createCustomUser(user, oAuth2User.getAttributes(), role.get(), defaultAvt);
    }

    private User registerUser(String registerId, OAuth2UserInfo userInfo) {
        var user = User.builder()
                .username(userInfo.getId())
                .hashPassword(passwordEncoder.encode("default"))
                .accountType(AccountType.parse(registerId).name())
                .roleId(0)
                .email(userInfo.getEmail())
                .fullName(userInfo.getName())
                .avatar(userInfo.getAvatarUrl())
                .build();

        userRepository.save(user);

        return user;
    }
}
