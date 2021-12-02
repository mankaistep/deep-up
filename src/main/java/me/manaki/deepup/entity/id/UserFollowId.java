package me.manaki.deepup.entity.id;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserFollowId implements Serializable {

    private static final long serialVersionUID = 652807940786336926L;

    @Column(name = "follower_username", nullable = false, length = 100)
    private String followerUsername;
    @Column(name = "followed_username", nullable = false, length = 100)
    private String followedUsername;

    public UserFollowId() {
    }

    public UserFollowId(String followerUsername, String followedUsername) {
        this.followerUsername = followerUsername;
        this.followedUsername = followedUsername;
    }

    public String getFollowedUsername() {
        return followedUsername;
    }

    public void setFollowedUsername(String followedUsername) {
        this.followedUsername = followedUsername;
    }

    public String getFollowerUsername() {
        return followerUsername;
    }

    public void setFollowerUsername(String followerUsername) {
        this.followerUsername = followerUsername;
    }

    @Override
    public int hashCode() {
        return Objects.hash(followedUsername, followerUsername);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserFollowId entity = (UserFollowId) o;
        return Objects.equals(this.followedUsername, entity.followedUsername) &&
                Objects.equals(this.followerUsername, entity.followerUsername);
    }
}