package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.repository.RoleRepository;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.security.user.CustomUserDetailsImpl;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final Environment environment;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, NullPointerException {
        var user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not registered yet!"));
        var orole = roleRepository.findById(user.getRoleId());
        if (orole.isEmpty()) throw new NullPointerException("Can't find role id" + user.getRoleId());

        var defaultAvt = environment.getProperty("avatar.default");

        var ud = CustomUserDetailsImpl.createCustomUser(user, orole.get(), defaultAvt);;

        return ud;
    }

}
