package me.manaki.deepup.service;

import lombok.RequiredArgsConstructor;
import me.manaki.deepup.entity.Role;

import java.util.Optional;


public interface RoleService {

    Optional<Role> getRole(Integer id);

}
