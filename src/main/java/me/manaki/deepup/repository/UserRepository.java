package me.manaki.deepup.repository;

import me.manaki.deepup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query
    Optional<User> findByNick(String nick);

    @Query
    Optional<User> findByUsername(String username);

    @Query("select u.email from User u where u.email = :email")
    Optional<String> checkEmailExists(String email);

    @Query("select u.nick from User u where u.nick = :nick")
    Optional<String> checkNickExists(String nick);

    @Modifying
    @Transactional
    @Query("update User u set u.fullName = :fullName, u.email = :email, u.avatar = :avatar, u.nick = :nick, u.wallpaper = :wallpaper, u.facebook = :facebook, u.github = :github where u.username = :username")
    void updateProfile(String username, String fullName, String email, String avatar, String nick, String wallpaper, String facebook, String github);

    @Modifying
    @Transactional
    @Query("update User u set u.hashPassword = :hashPassword where u.username = :username")
    void changePassword(String username, String hashPassword);

    @Query("select count(uv) from User u, UserVote uv, Post p where u.username = :username and u.username = p.username and p.id = uv.id.postId and uv.isUpvote = true")
    Integer countVotes(String username);

    @Query("select count(p) from User u, Post p where u.username = :username and u.username = p.username")
    Integer countPosts(String username);

    @Query("select sum(p.views) from User u, Post p where u.username = :username and u.username = p.username")
    Integer countViews(String username);

    @Query(nativeQuery = true, value = "select pm.post_id\n" +
            "from tbl_user_post_mark as pm\n" +
            "where pm.username = :username\n" +
            "order by pm.mark_date desc\n" +
            "offset 5 * (:page - 1) rows\n" +
            "fetch next 5 rows only")
    List<Integer> getMarkedPosts(String username, Integer page);

    @Query(nativeQuery = true, value = "select p.post_id\n" +
            "from tbl_post as p\n" +
            "where p.post_username = :username\n" +
            "order by p.post_creation_date desc\n" +
            "offset 5 * (:page - 1) rows\n" +
            "fetch next 5 rows only")
    List<Integer> getUploadedPosts(String username, Integer page);

    @Query(nativeQuery = true, value = "select uf.followed_username\n" +
            "from tbl_user_follow as uf\n" +
            "where uf.follower_username = :username\n" +
            "order by uf.follow_date desc\n" +
            "offset 5 * (:page - 1) rows\n" +
            "fetch next 5 rows only")
    List<String> getFollowings(String username, Integer page);

}
