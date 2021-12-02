package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.constant.AccountType;
import me.manaki.deepup.dto.request.RegisterRequest;
import me.manaki.deepup.entity.User;
import me.manaki.deepup.repository.UserRepository;
import me.manaki.deepup.service.AuthenticationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean registerUser(Model model, RegisterRequest request) {
        if (userRepository.existsById(request.getUsername())) {
            model.addAttribute("error", "Tài khoản đã tồn tại!");
            return false;
        }
        if (StringUtils.hasText(request.getEmail()) && userRepository.checkEmailExists(request.getEmail()).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return false;
        }

        User user = User.builder()
                .username(request.getUsername())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roleId(0)
                .accountType(AccountType.DEFAULT.name())
                .build();

        userRepository.save(user);

        return true;
    }

}
