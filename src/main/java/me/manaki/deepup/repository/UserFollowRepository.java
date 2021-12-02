package me.manaki.deepup.repository;

import me.manaki.deepup.entity.UserFollow;
import me.manaki.deepup.entity.id.UserFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {

    @Query("select count(uf) from UserFollow uf where uf.id.followedUsername = :username")
    Integer countFollowers(String username);

    @Query("select count(uf) from UserFollow uf where uf.id.followerUsername= :username")
    Integer countFollowings(String username);

    @Query("select uf from UserFollow uf where uf.id.followerUsername = :follower and uf.id.followedUsername = :followed")
    Optional<UserFollow> isFollowedBy(String follower, String followed);

}
