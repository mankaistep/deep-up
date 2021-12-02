package me.manaki.deepup.service;

import me.manaki.deepup.dto.request.RegisterRequest;
import org.springframework.ui.Model;

public interface AuthenticationService {

    boolean registerUser(Model model, RegisterRequest request);

}
