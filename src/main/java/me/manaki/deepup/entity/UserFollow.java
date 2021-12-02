package me.manaki.deepup.entity;

import me.manaki.deepup.entity.id.UserFollowId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Table(name = "tbl_user_follow")
@Entity
public class UserFollow {

    @EmbeddedId
    private UserFollowId id;

    @Column(name = "follow_date")
    private Instant followDate;

    public UserFollow() {
    }

    public UserFollow(UserFollowId id) {
        this.id = id;
    }

    public Instant getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Instant followDate) {
        this.followDate = followDate;
    }

    public UserFollowId getId() {
        return id;
    }

    public void setId(UserFollowId id) {
        this.id = id;
    }
}