package me.manaki.deepup.service.impl;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.entity.Role;
import me.manaki.deepup.repository.RoleRepository;
import me.manaki.deepup.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> getRole(Integer id) {
        return roleRepository.findById(id);
    }
}
