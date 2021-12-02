package me.manaki.deepup.repository;

import me.manaki.deepup.entity.UserPostMark;
import me.manaki.deepup.entity.id.UserPostMarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostMarkRepository extends JpaRepository<UserPostMark, UserPostMarkId> {

}
