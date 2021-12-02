package me.manaki.deepup.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.manaki.deepup.entity.Role;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.repository.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserDetailsImpl implements OAuth2User, UserDetails {

    private String username;

    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    private String accountType;

    @Setter
    private String nick;

    private final Collection<? extends GrantedAuthority> authorities;

    @Setter
    @Getter
    private Map<String, Object> attributes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getName() {
        return this.fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNick() {
        return this.nick == null ? this.username : this.nick;
    }

    // Static

    public static CustomUserDetailsImpl createCustomUser(User user, Map<String, Object> attributes, Role role, String defaultAvatar) {
        return new CustomUserDetailsImpl(
                user.getUsername(),
                user.getHashPassword(),
                user.getEmail(),
                user.getFullName(),
                user.getAvatar() == null ? defaultAvatar : user.getAvatar(),
                user.getAccountType(),
                user.getNick(),
                List.of(new SimpleGrantedAuthority(role.getRoleName())),
                attributes
        );
    }

    public static CustomUserDetailsImpl createCustomUser(User user, Role role, String defaultAvatar) {
        return createCustomUser(user, new HashMap<>(), role, defaultAvatar);
    }
}
